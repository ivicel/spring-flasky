package info.ivicel.springflasky.handler;

import info.ivicel.springflasky.web.model.dto.LoginDto;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private ITemplateEngine templateEngine;

    @Autowired
    public LoginFailureHandler(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        LoginDto user = new LoginDto();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setRememberMe(request.getParameter("rememberMe") != null);
        ctx.setVariable("user", user);
        ctx.setVariable("msg", "username or password error.");
        ctx.setVariable("classappend", "alert-warning");
        templateEngine.process("auth/login", ctx, response.getWriter());
    }
}
