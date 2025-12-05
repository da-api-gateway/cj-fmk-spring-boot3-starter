package com.cjlabs.web.exception;

import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.domain.exception.BaseException;
import com.cjlabs.domain.exception.Error200Exception;
import com.cjlabs.domain.exception.Error400Exception;
import com.cjlabs.domain.exception.Error500Exception;
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

    @ExceptionHandler(Error400Exception.class)
    public ResponseEntity<FmkResult<T>> handleValidationException(Error400Exception ex) {
        log.info("处理验证异常|类型={}|键={}|消息={}", ex.getErrorType(), ex.getErrorKey(), ex.getMessage(), ex);
        return createErrorResponse(ex);
    }

    @ExceptionHandler(Error200Exception.class)
    public ResponseEntity<FmkResult<T>> handleBusinessException(Error200Exception ex) {
        log.info("处理业务异常|类型={}|键={}|消息={}", ex.getErrorType(), ex.getErrorKey(), ex.getMessage(), ex);
        return createErrorResponse(ex);
    }

    @ExceptionHandler(Error500Exception.class)
    public ResponseEntity<FmkResult<T>> handleSystemException(Error500Exception ex) {
        log.error("处理系统异常|类型={}|键={}|消息={}", ex.getErrorType(), ex.getErrorKey(), ex.getMessage(), ex);
        return createErrorResponse(ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<FmkResult<T>> handleException(Exception ex) {
        log.error("处理未知异常|消息={}", ex.getMessage(), ex);
        
        FmkResult<T> result = FmkResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "SYSTEM_ERROR", "UNKNOWN_ERROR");
        
        Optional<FmkTraceId> traceIdOptional = FmkContextUtil.getTraceId();
        traceIdOptional.ifPresent(result::setTraceId);
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    /**
     * 创建统一的错误响应
     * 
     * @param ex 基础异常
     * @return 响应实体
     */
    private ResponseEntity<FmkResult<T>> createErrorResponse(BaseException ex) {
        FmkResult<T> result = FmkResult.error(ex.getHttpCode(), ex.getErrorType(), ex.getErrorKey());
        
        Optional<FmkTraceId> traceIdOptional = FmkContextUtil.getTraceId();
        traceIdOptional.ifPresent(result::setTraceId);
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}