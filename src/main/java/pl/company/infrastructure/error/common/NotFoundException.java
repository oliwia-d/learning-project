package pl.company.infrastructure.error.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4309105685685625442L;

    public NotFoundException(String message) {
        super(message);
    }
}
