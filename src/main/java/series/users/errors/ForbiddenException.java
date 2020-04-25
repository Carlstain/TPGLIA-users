package series.users.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException{
    public ForbiddenException() {
        super("This action is not allowed");
    }
    public ForbiddenException(String message){
        super(message);
    }
}
