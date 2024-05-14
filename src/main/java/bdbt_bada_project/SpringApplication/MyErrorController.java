package bdbt_bada_project.SpringApplication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
@ControllerAdvice
public class MyErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "errors/403";
            }
            else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "errors/404";
            }
            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "errors/500";
            }
            else if (statusCode == HttpStatus.GATEWAY_TIMEOUT.value()) {
                return "errors/504";
            }
            else {
                return "errors/other";
            }
        }
        return "errors/other";
    }
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // HTTP 409 Conflict
    public String handleDuplicateKeyException(DuplicateKeyException e, Model model) {
        model.addAttribute("errorMessage", "Konto o podanym adresie już istnieje.");
        return "errors/account_exist_error";
    }
}