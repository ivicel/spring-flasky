package info.ivicel.springflasky.handler;

import info.ivicel.springflasky.exception.PageNotFoundException;
import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.service.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * after login successfully, check is account confirmed or else
 */
@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private UserService userService;

    @Autowired
    public LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        User user;
        try {
            user = (User) authentication.getPrincipal();
        } catch (Exception e) {
            log.error("user not login", e);
            throw new PageNotFoundException();
        }

        if (!user.isConfirmed()) {
            response.sendRedirect(request.getContextPath() + "/auth/unconfirm");
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }

        // update user's last seen time
        userService.updateLastSeen(user);
    }
}
