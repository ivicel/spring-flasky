package info.ivicel.springflasky.web.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.servlet.ModelAndView;


public class CustomBasicErrorController extends BasicErrorController {

    public CustomBasicErrorController(ErrorAttributes errorAttributes,
            ErrorProperties errorProperties) {
        super(errorAttributes, errorProperties);
    }

    public CustomBasicErrorController(ErrorAttributes errorAttributes,
            ErrorProperties errorProperties,
            List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorProperties, errorViewResolvers);
    }

    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        return super.errorHtml(request, response);
    }
}
