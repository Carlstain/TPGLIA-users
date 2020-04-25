package series.users.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason = "Authentication required")
public class RequestUnauthorizedException extends RuntimeException {}
