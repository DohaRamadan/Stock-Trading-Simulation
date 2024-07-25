package org.example.stocktradingsimulation.exceptions.user;

import org.example.stocktradingsimulation.enums.HTTPStatusCodesEnum;
import org.example.stocktradingsimulation.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserDoesNotExistException extends ApplicationException {
    public UserDoesNotExistException() {
        super(HTTPStatusCodesEnum.NOT_FOUND.getHttpStatusCode(), "This User Does Not Exist", HttpStatus.NOT_FOUND);
    }

    public UserDoesNotExistException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}