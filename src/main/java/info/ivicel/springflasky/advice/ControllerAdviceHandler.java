package info.ivicel.springflasky.advice;

import static info.ivicel.springflasky.core.common.Const.DEFAULT_404_PAGE;

import info.ivicel.springflasky.exception.PageNotFoundException;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerAdviceHandler {

    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String pageNotFound(PageNotFoundException e) {
        return DEFAULT_404_PAGE;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String volationError(ConstraintViolationException e) {
        return "error/400";
    }
}
