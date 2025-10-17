package com.cjlabs.web.exception;

import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.web.threadlocal.FmkContextUtil;
import com.cjlabs.web.threadlocal.FmkResult;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler<T> {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<FmkResult<T>> handleValidationException(ValidationException ex) {
        log.info("GlobalExceptionHandler|handleValidationException|msgType={}|msgKey={}|ex={}", ex.getMsgType(), ex.getMsgKey(), ex.getMessage(), ex);
        String msgType = ex.getMsgType();
        String msgKey = ex.getMsgKey();

        FmkResult<T> result = FmkResult.error(ex.getHttpCode(), msgType, msgKey);

        Optional<FmkTraceId> traceIdOptional = FmkContextUtil.getTraceId();
        traceIdOptional.ifPresent(result::setTraceId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<FmkResult<T>> handleBusinessException(BusinessException ex) {
        log.info("GlobalExceptionHandler|handleBusinessException|msgType={}|msgKey={}|ex={}", ex.getMsgType(), ex.getMsgKey(), ex.getMessage(), ex);
        String msgType = ex.getMsgType();
        String msgKey = ex.getMsgKey();

        FmkResult<T> result = FmkResult.error(ex.getHttpCode(), msgType, msgKey);

        Optional<FmkTraceId> traceIdOptional = FmkContextUtil.getTraceId();
        traceIdOptional.ifPresent(result::setTraceId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 可选：处理其他异常（如兜底异常）
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<FmkResult<T>> handleSystemException(SystemException ex) {
        log.info("GlobalExceptionHandler|handleSystemException|msgType={}|msgKey={}|ex={}", ex.getMsgType(), ex.getMsgKey(), ex.getMessage(), ex);
        String msgType = ex.getMsgType();
        String msgKey = ex.getMsgKey();

        FmkResult<T> result = FmkResult.error(ex.getHttpCode(), msgType, msgKey);

        Optional<FmkTraceId> traceIdOptional = FmkContextUtil.getTraceId();
        traceIdOptional.ifPresent(result::setTraceId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 可选：处理其他异常（如兜底异常）
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FmkResult<T>> handleException(Exception ex) {
        log.info("GlobalExceptionHandler|handleException|ex={}", ex.getMessage(), ex);

        FmkResult<T> result = FmkResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SYSTEM_ERROR", "UNKNOWN_ERROR");

        Optional<FmkTraceId> traceIdOptional = FmkContextUtil.getTraceId();
        traceIdOptional.ifPresent(result::setTraceId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}