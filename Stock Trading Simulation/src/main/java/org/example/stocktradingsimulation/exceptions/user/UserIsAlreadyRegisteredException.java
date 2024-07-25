package org.example.stocktradingsimulation.exceptions.user;

import org.example.stocktradingsimulation.enums.HTTPStatusCodesEnum;
import org.example.stocktradingsimulation.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserIsAlreadyRegisteredException extends ApplicationException {
    public UserIsAlreadyRegisteredException() {
        super(HTTPStatusCodesEnum.CONFLICT.getHttpStatusCode(), "This User Is Already Registered", HttpStatus.CONFLICT);
    }

    public UserIsAlreadyRegisteredException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
