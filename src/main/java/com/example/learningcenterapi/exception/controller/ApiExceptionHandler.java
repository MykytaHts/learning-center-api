package com.example.learningcenterapi.exception.controller;

import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.exception.UserAccessRestrictedException;
import com.example.learningcenterapi.exception.dto.ApiExceptionDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.*;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@Slf4j
@Order(HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiExceptionHandler {

    /**
     * Exception handler for {@link NoHandlerFoundException}
     * @param ex exception to handle
     * @param httpServletRequest current request
     * @return returns not found response if the url starts with api otherwise redirect to page not found
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handlerNotFound(NoHandlerFoundException ex, HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getRequestURI().startsWith("/api"))
            return new ResponseEntity<>("Not Found", NOT_FOUND);
        else {
            return "redirect:/#/page-not-found";
        }
    }

    /**
     * Exception handler for {@link ConcurrencyFailureException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link ConcurrencyFailureException}
     */
    @ExceptionHandler(ConcurrencyFailureException.class)
    @ResponseStatus(CONFLICT)
    @ResponseBody
    public ApiExceptionDTO processConcurrencyError(ConcurrencyFailureException ex) {
        log.error("ConcurrencyFailureException:", ex);
        return exceptionToApiError(ex).code(CONFLICT.value());
    }

    /**
     * Exception handler for {@link ConcurrencyFailureException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link ConcurrencyFailureException}
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    @ResponseBody
    public ApiExceptionDTO processBadCredentialsException(BadCredentialsException ex) {
        log.error("BadCredentialsException:", ex);
        return exceptionToApiError(ex).code(UNAUTHORIZED.value());
    }

    /**
     * Exception handler for {@link MethodArgumentNotValidException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link MethodArgumentNotValidException}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public List<ApiExceptionDTO> processValidationError(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException:", ex);

        List<ApiExceptionDTO> errorMessages = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(e -> {
            ApiExceptionDTO apiException = exceptionToApiError(ex).code(BAD_REQUEST.value());
            apiException.setMessage(e.getDefaultMessage());
            errorMessages.add(apiException);
        });

        return errorMessages;
    }

    /**
     * Exception handler for {@link IllegalArgumentException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link IllegalArgumentException}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public ApiExceptionDTO processIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException:", ex);
        return exceptionToApiError(ex).code(BAD_REQUEST.value());
    }

    /**
     * Exception handler for Access restricted exceptions
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link AccessDeniedException}
     */
    @ExceptionHandler({AccessDeniedException.class,
            UserAccessRestrictedException.class,
            AuthenticationCredentialsNotFoundException.class})
    @ResponseStatus(FORBIDDEN)
    @ResponseBody
    public ApiExceptionDTO processAccessDeniedException(RuntimeException ex) {
        log.error("AccessDeniedException:", ex);
        return exceptionToApiError(ex).code(FORBIDDEN.value());
    }

    /**
     * Exception handler for {@link HttpRequestMethodNotSupportedException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link HttpRequestMethodNotSupportedException}
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ApiExceptionDTO processMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("AccessDeniedException:", ex);
        return exceptionToApiError(ex).code(METHOD_NOT_ALLOWED.value());
    }

    /**
     * Exception handler for {@link ConstraintViolationException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link ConstraintViolationException}
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public ApiExceptionDTO processConstraintViolationException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException:", ex);

        ApiExceptionDTO apiException = exceptionToApiError(ex).code(BAD_REQUEST.value());
        apiException.setMessage(ex.getConstraintViolations().toString());

        return apiException;
    }

    /**
     * Exception handler for {@link SystemException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link SystemException}
     */
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ApiExceptionDTO> handleCustomException(SystemException ex) {
        log.error(ex.getMessage(), ex);
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(ex.getHttpStatus());
        ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO();
        apiExceptionDTO.setMessage(ex.getMessage());
        apiExceptionDTO.setCode(ex.getHttpStatus().value());
        return builder.body(apiExceptionDTO);
    }

    /**
     * Exception handler for {@link HttpMessageConversionException} and {@link MethodArgumentTypeMismatchException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link HttpMessageConversionException} and {@link MethodArgumentTypeMismatchException}
     */
    @ExceptionHandler({HttpMessageConversionException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ApiExceptionDTO> handleEmptyPostBody(NestedRuntimeException ex) {
        ApiExceptionDTO apiException = exceptionToApiError(ex).code(BAD_REQUEST.value());
        if (ex.getMostSpecificCause() instanceof SystemException){
            apiException.setMessage(ex.getMostSpecificCause().getMessage());
        }

        return ResponseEntity.status(BAD_REQUEST).body(apiException);
    }

    /**
     * Exception handler for {@link HttpMessageNotReadableException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link HttpMessageNotReadableException}
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ApiExceptionDTO> handleHttpBodyNotReadable(HttpMessageNotReadableException ex) {
        ApiExceptionDTO apiException = exceptionToApiError(ex).code(BAD_REQUEST.value());
        apiException.setMessage("An exception arise while trying to parse request body");
        apiException.setProperties(Map.of("error", ex.getLocalizedMessage()));
        return ResponseEntity.status(BAD_REQUEST).body(apiException);
    }

    /**
     * Util method to convert any exception type to user-friendly {@link ApiExceptionDTO}
     * @param ex exception to convert
     * @return user-friendly {@link ApiExceptionDTO}
     */
    private ApiExceptionDTO exceptionToApiError(Exception ex) {
        ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO();
        apiExceptionDTO.setMessage(ex.getMessage());
        apiExceptionDTO.setStackTrace(ExceptionUtils.getStackTrace(ex));
        return apiExceptionDTO;
    }
}
