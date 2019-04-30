package info.ivicel.springflasky.web.controller;

import static info.ivicel.springflasky.util.CommonUtil.renderEmailTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import info.ivicel.springflasky.exception.AccountExistsException;
import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.model.dto.LoginDTO;
import info.ivicel.springflasky.web.model.dto.RegisterDTO;
import info.ivicel.springflasky.web.service.UserService;
import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@PropertySource("classpath:/application.yml")
@RequestMapping("/auth")
public class AuthController {

    private MailProperties mailProperties;
    private UserService userService;

    @Value("${flasky.securityKey}")
    private String securityKey;

    @Value("${flasky.serverHost:}")
    private String serverHost;

    @Autowired
    public AuthController(UserService userService, MailProperties mailProperties) {
        this.userService = userService;
        this.mailProperties = mailProperties;
    }

    /**
     * login
     * @param model
     * @return
     */
    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new LoginDTO());
        }
        return "auth/login";
    }

    /**
     * register
     * @param model
     * @return
     */
    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("registerDto")) {
            model.addAttribute("registerDto", new RegisterDTO());
        }

        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Validated RegisterDTO dto, BindingResult result, RedirectAttributes ra,
            HttpServletRequest request) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("registerDto", dto);
            ra.addFlashAttribute("org.springframework.validation.BindingResult.registerDto", result);
            return "redirect:/auth/register";
        }
        try {
            userService.register(dto);
        } catch (AccountExistsException e) {
            log.debug("account already exists, {}", e.getMessage());
            ra.addFlashAttribute("registerDto", dto);
            ra.addFlashAttribute("msg", "Account already exist.");
            ra.addFlashAttribute("classappend", "alert-warning");
            return "redirect:/auth/register";
        }

        ra.addFlashAttribute("msg",
                "An email has sent to your, before you can access this site you need to confirm your account.");
        ra.addFlashAttribute("classappend", "alert-info");

        sendConfirmed(dto.getUsername(), dto.getEmail(), request);

        return "redirect:/auth/login";
    }

    @GetMapping("/unconfirm")
    @PreAuthorize("@webAuth.loginRequired(authentication) and not @webAuth.isConfirmed(authentication)")
    public String unconfirm() {

        return "auth/unconfirm";
    }

    @GetMapping("/confirm")
    public String confirmAccount(Authentication auth, HttpSession session, @RequestParam("token") String token) {
        String username = null;
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(securityKey)).build().verify(token);
            username = jwt.getClaim("user").asString();

            Optional<User> user = userService.findByUsernameAndNotConfrim(username);
            if (!user.isPresent()) {
                return "auth/already_confirm";
            }
            user.get().setConfirmed(true);
            userService.confirm(user.get());
        } catch (Exception e) {
            log.warn("confirm token verify error: {}", e.getMessage());
            if (auth != null) {
                if (auth.getName().equals(username)) {
                    return "redirect:/auth/unconfirm";
                } else {
                    return "redirect:/";
                }
            }

            return "redirect:/auth/login";
        }

        return "auth/confirm_success";
    }

    @GetMapping("/resend-confirmation")
    @PreAuthorize("@webAuth.loginRequired(authentication) and not @webAuth.isConfirmed(authentication)")
    public String resendConfirmation(Authentication auth, HttpServletRequest request, HttpServletResponse response) {
        User user = (User) auth.getPrincipal();
        sendConfirmed(user.getUsername(), user.getEmail(), request);

        return "auth/resend_confirm";
    }

    private void sendConfirmed(String username, String email, HttpServletRequest request) {
        new Thread(() -> {
            String token = JWT.create()
                    // 30 mins
                    .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                    // random hash, let our token looks random
                    .withClaim("hash", UUID.randomUUID().toString().replaceAll("-", ""))
                    .withClaim("user", username)
                    .sign(Algorithm.HMAC256(securityKey));

            String confirmUrl = buildConfirmUrl(request, token);
            try {
                sendMail(username, email, confirmUrl);
            } catch (SendMailException e) {
                log.error("Can not send email to {}: {}", email, e.getMessage());
            }
        }).run();
    }

    private void sendMail(String username, String email, String confirmUrl) throws SendMailException {
            Map<String, Object> attrs = new HashMap<>();
            attrs.put("username", username);
            attrs.put("confirmUrl", confirmUrl);

            OhMyEmail.subject("Account Comfirm")
                    .from(mailProperties.getUsername())
                    .to(email)
                    .text(renderEmailTemplate("templates/auth/email/confirm.txt", attrs))
                    .html(renderEmailTemplate("templates/auth/email/confirm.html", attrs))
                    .send();
    }

    private String buildConfirmUrl(HttpServletRequest request, String token) {
        if (serverHost != null && serverHost.trim().length() > 0) {
            return serverHost.trim();
        }

        URI uri = URI.create(request.getRequestURL().toString());
        StringBuilder builder = new StringBuilder(uri.getScheme() + "://" + uri.getHost());
        int port = uri.getPort();
        if (port != -1 && port != 80 && port != 443) {
            builder.append(":");
            builder.append(port);
        }
        if (request.getContextPath() != null) {
            builder.append(request.getContextPath());
        }
        builder.append("/auth/confirm?token=");
        builder.append(token);

        return builder.toString();
    }
}
