package com.cjlabs.web.exception;

import com.xodo.fmk.core.FmkResult;
import com.xodo.fmk.jdk.basetype.type.FmkTraceId;
import com.xodo.fmk.web.FmkContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler<T> {
    @Autowired
    private ExceptionService exceptionService;

    @ExceptionHandler(DcxjDbMsgKeyException.class)
    public ResponseEntity<FmkResult<T>> handleDcxjException(DcxjDbMsgKeyException ex) {
        log.info("GlobalExceptionHandler|handleDcxjException|msgType={}|msgKey={}|ex={}", ex.getMsgType(), ex.getMsgKey(), ex.getMessage(), ex);
        String msgType = ex.getMsgType();
        String msgKey = ex.getMsgKey();

        String errorMsg = exceptionService.errorMsg(msgType, msgKey);

        FmkResult<T> result = FmkResult.error(errorMsg);

        Optional<FmkTraceId> traceIdOptional = FmkContextUtil.getTraceId();
        traceIdOptional.ifPresent(result::setTraceId);

        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DcxjCommonException.class)
    public ResponseEntity<FmkResult<T>> handleDcxjCommonException(DcxjCommonException ex) {
        log.info("GlobalExceptionHandler|handleDcxjCommonException|msgType={}|msgKey={}|ex={}", ex.getZhMsg(), ex.getEnMsg(), ex.getMessage(), ex);
        String zhMsg = ex.getZhMsg();
        String enMsg = ex.getEnMsg();

        String errorMsg = exceptionService.commonErrorMsg(zhMsg, enMsg);

        FmkResult<T> result = FmkResult.error(errorMsg);

        Optional<FmkTraceId> traceIdOptional = FmkContextUtil.getTraceId();
        traceIdOptional.ifPresent(result::setTraceId);

        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 可选：处理其他异常（如兜底异常）
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FmkResult<T>> handleGenericException(Exception ex) {
        log.info("GlobalExceptionHandler|handleGenericException|ex={}", ex.getMessage(), ex);

        String errorMsg = exceptionService.errorMsg(null, null);

        // 检查 ex 是否为 ErrorResponse 类型
        HttpStatus statusCode;
        // if (ex instanceof ErrorResponse) {
        //     statusCode = ((ErrorResponse) ex).getStatusCode();
        // } else {
        // 默认状态码为 500（Internal Server Error）
        statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        // }

        FmkResult<T> result = FmkResult.error(statusCode.value(), errorMsg);

        Optional<FmkTraceId> traceIdOptional = FmkContextUtil.getTraceId();
        traceIdOptional.ifPresent(result::setTraceId);

        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}