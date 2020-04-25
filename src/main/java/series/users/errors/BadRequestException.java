package series.users.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{
    public BadRequestException() {
        super("The request parameters are malformed");
    }
    public BadRequestException(String message) {
        super(message);
    }
}
