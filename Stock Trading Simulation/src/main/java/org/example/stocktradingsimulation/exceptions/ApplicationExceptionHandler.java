package org.example.stocktradingsimulation.exceptions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.stocktradingsimulation.dto.outbound.user.ErrorResponse;
import org.example.stocktradingsimulation.enums.HTTPStatusCodesEnum;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(
            final ApplicationException exception, final HttpServletRequest request
    ) {
        var guid = UUID.randomUUID().toString();
        log.error(
                String.format("Error GUID=%s; error message: %s", guid, exception.getMessage()),
                exception
        );
        var response = new ErrorResponse(
                guid,
                exception.getErrorCode(),
                exception.getMessage(),
                exception.getHttpStatus().value(),
                exception.getHttpStatus().name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, exception.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknownException(
            final Exception exception, final HttpServletRequest request
    ) {
        var guid = UUID.randomUUID().toString();
        log.error(
                String.format("Error GUID=%s; error message: %s", guid, exception.getMessage()),
                exception
        );
        var response = new ErrorResponse(
                guid,
                HTTPStatusCodesEnum.INTERNAL_SERVER_ERROR.getHttpStatusCode(),
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<?> handleValidationException(HandlerMethodValidationException ex, final HttpServletRequest request) {
        var guid = UUID.randomUUID().toString();
        log.error(
                String.format("Error GUID=%s; error message: %s", guid, ex.getMessage()),
                ex
        );

        List<String> errors =  ex.getAllValidationResults().stream()
                                 .flatMap(result -> result.getResolvableErrors().stream())
                                 .map(this::extractErrorMessage)
                                 .collect(Collectors.toList());

        var response = new ErrorResponse(
                UUID.randomUUID().toString(),
                "VALIDATION_FAILURE",
                "Validation failure",
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );
        response.setErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String extractErrorMessage(Object error) {
        if (error instanceof DefaultMessageSourceResolvable) {
            DefaultMessageSourceResolvable resolvable = (DefaultMessageSourceResolvable) error;
            String field = Arrays.stream(resolvable.getCodes())
                                 .filter(code -> code.contains("."))
                                 .map(code -> code.substring(code.lastIndexOf(".") + 1))
                                 .findFirst()
                                 .orElse("Field not specified");

            String message = resolvable.getDefaultMessage();
            return field + ": " + message;
        }
        return "Unknown error";
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(
            BadCredentialsException exception, HttpServletRequest request) {
        var guid = UUID.randomUUID().toString();
        log.error(
                String.format("Error GUID=%s; error message: %s", guid, exception.getMessage()),
                exception
        );
        var response = new ErrorResponse(
                guid,
                "BAD_CREDENTIALS",
                "The username or password is incorrect",
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> handleLockedException(
            LockedException exception, HttpServletRequest request) {
        var guid = UUID.randomUUID().toString();
        log.error(
                String.format("Error GUID=%s; error message: %s", guid, exception.getMessage()),
                exception
        );
        var response = new ErrorResponse(
                guid,
                "ACCOUNT_LOCKED",
                "The account is locked",
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(
            UsernameNotFoundException exception, HttpServletRequest request) {
        var guid = UUID.randomUUID().toString();
        log.error(
                String.format("Error GUID=%s; error message: %s", guid, exception.getMessage()),
                exception
        );
        var response = new ErrorResponse(
                guid,
                "USERNAME_NOT_FOUND",
                "The specified username was not found",
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(
            AccessDeniedException exception, HttpServletRequest request) {
        var guid = UUID.randomUUID().toString();
        log.error(
                String.format("Error GUID=%s; error message: %s", guid, exception.getMessage()),
                exception
        );
        var response = new ErrorResponse(
                guid,
                "ACCESS_DENIED",
                "You are not authorized to access this resource",
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handleSignatureException(
            SignatureException exception, HttpServletRequest request) {
        var guid = UUID.randomUUID().toString();
        log.error(
                String.format("Error GUID=%s; error message: %s", guid, exception.getMessage()),
                exception
        );
        var response = new ErrorResponse(
                guid,
                "INVALID_SIGNATURE",
                "The JWT signature is invalid",
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(
            ExpiredJwtException exception, HttpServletRequest request) {
        var guid = UUID.randomUUID().toString();
        log.error(
                String.format("Error GUID=%s; error message: %s", guid, exception.getMessage()),
                exception
        );
        var response = new ErrorResponse(
                guid,
                "JWT_EXPIRED",
                "The JWT token has expired",
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        ) {
        };
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }


}
