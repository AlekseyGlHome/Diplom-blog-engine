package com.skillbox.engine.exception;

import com.skillbox.engine.api.response.CommentErrorResponse;
import com.skillbox.engine.api.response.ErrorResponse;
import com.skillbox.engine.api.response.UploadImageErrorResponse;
import com.skillbox.engine.model.DTO.ErrorComment;
import com.skillbox.engine.model.DTO.ErrorUploadImage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundEx(NotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({LoadImageExceprion.class})
    protected ResponseEntity<Object> handleUploadImageEx(LoadImageExceprion ex, WebRequest request) {
        UploadImageErrorResponse errorResponse = new UploadImageErrorResponse(false, new ErrorUploadImage(ex.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CommentException.class})
    protected ResponseEntity<Object> handleAddCommentEx(CommentException ex, WebRequest request) {
        CommentErrorResponse errorResponse = new CommentErrorResponse(false, new ErrorComment(ex.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }
}
