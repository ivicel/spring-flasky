package info.ivicel.springflasky.web.controller;

import static info.ivicel.springflasky.util.CommonUtil.renderEmailTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import info.ivicel.springflasky.core.MailSender;
import info.ivicel.springflasky.exception.AccountExistsException;
import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.model.dto.LoginDTO;
import info.ivicel.springflasky.web.model.dto.PasswordDTO;
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
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@Validated
public class AuthController {

    private MailProperties mailProperties;
    private UserService userService;

    @Value("${flasky.securityKey}")
    private String securityKey;

    @Value("${flasky.serverHost:}")
    private String serverHost;

    private HttpServletRequest request;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, MailProperties mailProperties,
            HttpServletRequest request, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.mailProperties = mailProperties;
        this.request = request;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * login
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
                "An email has sent to your, before you can access this site you need to save your account.");
        ra.addFlashAttribute("classappend", "alert-info");

        sendConfirmed(dto.getUsername(), dto.getEmail(), this::sendAccountConfirmedMail);

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
            userService.save(user.get());
        } catch (Exception e) {
            log.warn("save token verify error: {}", e.getMessage());
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
        sendConfirmed(user.getUsername(), user.getEmail(), this::sendAccountConfirmedMail);

        return "auth/resend_confirm";
    }

    @GetMapping("/reset-password")
    public String resetPassword(Model model) {
        return "auth/reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Email @RequestParam("email") String email, RedirectAttributes ra) {
        Optional<User> user = userService.findByEmail(email);
        if (!user.isPresent()) {
            ra.addFlashAttribute("email", email);
            ra.addFlashAttribute("msg", "email not exists.");
            ra.addFlashAttribute("classappend", "alert-warning");
            return "redirect:reset-password";
        }

        sendConfirmed(user.get().getUsername(), email, this::sendRestPasswordMail);
        return "auth/reset_password_mail_sent";
    }

    @GetMapping("/reset-new-password")
    public String resetNewPassword(@RequestParam("token") String token, Model model,
            HttpServletResponse response) {
        if (!model.containsAttribute("passwordDto")) {
            model.addAttribute("passwordDto", new PasswordDTO());
        }

        try {
            JWT.require(Algorithm.HMAC256(securityKey)).build().verify(token);
            model.addAttribute("token", token);
            return "auth/reset_new_password";
        } catch (JWTVerificationException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "auth/token_expired";
        }
    }

    @PostMapping("/reset-new-password")
    public String resetNewPassword(@RequestParam("token") String token,
            @Validated PasswordDTO password, BindingResult result,
            RedirectAttributes ra,
            HttpServletResponse response) {
        if (result.hasErrors()) {
            ra.addAttribute("token", token);
            ra.addFlashAttribute("passwordDto", password);
            ra.addFlashAttribute("org.springframework.validation.BindingResult.passwordDto", result);
            return "redirect:reset-new-password";
        }

        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(securityKey)).build().verify(token);
            String username = jwt.getClaim("user").asString();
            Optional<User> user = userService.findByUsername(username);
            user.ifPresent((u) -> {
                u.setPasswordHash(passwordEncoder.encode(password.getPassword()));
                userService.save(u);
            });

            ra.addFlashAttribute("msg", "your password has been reset successful.");
            ra.addFlashAttribute("classappend", "alert-info");

            return "redirect:login";
        } catch (Exception e) {
            log.warn("bad request, token error [{}]: {}", token, e.getMessage());
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return "auth/token_expired";
    }

    @GetMapping("/change-password")
    @PreAuthorize("@webAuth.loginRequired(authentication)")
    public String changePassword(Model model) {
        if (!model.containsAttribute("passwordDto")) {
            model.addAttribute("passwordDto", new PasswordDTO());
        }
        return "auth/reset_new_password";
    }

    @PostMapping("/change-password")
    @PreAuthorize("@webAuth.loginRequired(authentication)")
    public String changePassword(Authentication auth, RedirectAttributes ra,
            @Validated PasswordDTO password, BindingResult result) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.passwordDto", result);
            ra.addFlashAttribute("passwordDto", password);
        } else {
            String passwordHash = passwordEncoder.encode(password.getPassword());
            int count = userService.updatePasswordByUsername(auth.getName(), passwordHash);
            if (count > 0) {
                ra.addFlashAttribute("msg", "change password success.");
                ra.addFlashAttribute("classappend", "alert-info");
            }
        }

        return "redirect:change-password";
    }

    // todo: to send mail asynchronous, add this task into a task queue
    private void sendConfirmed(String username, String email, Function<String, MailSender> func) {
        String token = JWT.create()
                // 30 mins
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                // random hash, let our token looks random
                .withClaim("hash", UUID.randomUUID().toString().replaceAll("-", ""))
                .withClaim("user", username)
                .sign(Algorithm.HMAC256(securityKey));

        func.apply(token).send(username, email);
    }

    private MailSender sendAccountConfirmedMail(String token) {
        String url = buildUrl("/auth/save", token);
        return (String username, String email) -> {
            Map<String, Object> attrs = new HashMap<>();
            attrs.put("username", username);
            attrs.put("callbackUrl", url);

            try {
                OhMyEmail.subject("Account Comfirm")
                        .from(mailProperties.getUsername())
                        .to(email)
                        .text(renderEmailTemplate("templates/auth/email/confirm.txt", attrs))
                        .html(renderEmailTemplate("templates/auth/email/confirm.html", attrs))
                        .send();
            } catch (SendMailException e) {
                log.error("Can not send email to {}: {}", email, e.getMessage());
                throw new RuntimeException(e);
            }
        };
    }

    private MailSender sendRestPasswordMail(String token) {
        String url = buildUrl("/auth/reset-new-password", token);
        return (String username, String email) -> {
            Map<String, Object> attrs = new HashMap<>();
            attrs.put("username", username);
            attrs.put("callbackUrl", url);

            try {
                OhMyEmail.subject("Rest your password")
                        .from(mailProperties.getUsername())
                        .to(email)
                        .text(renderEmailTemplate("templates/auth/email/reset_password.txt", attrs))
                        .html(renderEmailTemplate("templates/auth/email/reset_password.html", attrs))
                        .send();
            } catch (SendMailException e) {
                log.error("Can not send email to {}: {}", email, e.getMessage());
                e.printStackTrace();
            }
        };
    }

    private String buildUrl(String prefix, String token) {
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

        builder.append(prefix).append("?token=").append(token);

        return builder.toString();
    }
}
