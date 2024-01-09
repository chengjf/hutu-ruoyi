package tech.hutu.common.web.exception;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.BizException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DataBufferFactory;

import java.net.URI;

/**
 * @author chengjf
 * @date 2024-01-09
 * @since 2024-01-09
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        // 默认配置
        serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);


        DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
        SingleResponse response = SingleResponse.buildFailure("400", "未知问题");

        if (throwable instanceof AccessDeniedException) {
            AccessDeniedException e = (AccessDeniedException) throwable;
            response.setErrCode("400");
            response.setErrMessage(e.getMessage());
            serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        } else if (throwable instanceof HttpRequestMethodNotSupportedException) {
            URI uri = serverWebExchange.getRequest().getURI();
            HttpRequestMethodNotSupportedException e = (HttpRequestMethodNotSupportedException) throwable;
            logger.error("请求地址'{}',不支持'{}'请求", uri, e.getMethod());
            response.setErrCode("400");
            response.setErrMessage(e.getMessage());
            serverWebExchange.getResponse().setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
        } else if (throwable instanceof BizException) {
            logger.error("发生业务异常", throwable);
            // 业务异常
            BizException e = (BizException) throwable;
            response.setErrCode(e.getErrCode());
            response.setErrMessage(e.getMessage());
            serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        } else if (throwable instanceof MissingPathVariableException) {
            logger.error("发生路径参数异常", throwable);
            // 发生业务异常
            MissingPathVariableException e = (MissingPathVariableException) throwable;
            response.setErrCode("400");
            response.setErrMessage(e.getMessage());
            serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        } else if (throwable instanceof MethodArgumentTypeMismatchException) {
            logger.error("发生参数类型匹配异常", throwable);
            // 发生参数类型匹配异常
            MethodArgumentTypeMismatchException e = (MethodArgumentTypeMismatchException) throwable;
            response.setErrCode("400");
            response.setErrMessage(e.getMessage());
            serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        } else if (throwable instanceof MethodArgumentNotValidException) {
            logger.error("发生参数不合法异常", throwable);
            // 发生参数不合法异常
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) throwable;
            response.setErrCode("400");
            response.setErrMessage(e.getMessage());
            serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        } else if (throwable instanceof BindException) {
            logger.error("发生绑定异常", throwable);
            // 发生绑定异常
            BindException e = (BindException) throwable;
            response.setErrCode("400");
            response.setErrMessage(e.getMessage());
            serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        } else if (throwable instanceof RuntimeException) {

        } else if (throwable instanceof Exception) {

        }


        String s = null;
        try {
            s = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {

        }
        DataBuffer dataBuffer = bufferFactory.wrap(s.getBytes());
        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}
