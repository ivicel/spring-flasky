package info.ivicel.springflasky.advice;

import info.ivicel.springflasky.core.common.Const;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@Slf4j
@Component("accessDeniedHandler")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private TemplateEngine templateEngine;

    @Autowired
    public CustomAccessDeniedHandler(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.debug("access to page denied: " + request.getRequestURL().toString());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("text/html");
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        templateEngine.process(Const.DEFAULT_403_PAGE, ctx, response.getWriter());
    }
}
