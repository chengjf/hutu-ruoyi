package tech.hutu.admin.web.controller;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.BizException;
import com.alibaba.cola.exception.ExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import tech.hutu.admin.web.security.TokenAuthenticationManager;
import tech.hutu.admin.web.security.Tokenizer;
import tech.hutu.admin.web.vo.AuthReq;
import tech.hutu.admin.web.vo.AuthResp;
import tech.hutu.common.web.controller.BaseController;
import tech.hutu.ruoyi.dubbo.api.DemoService;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author chengjf
 * @date 2024-01-09
 * @since 2024-01-09
 */

@RestController
@RequiredArgsConstructor
public class TestController extends BaseController {

    @DubboReference
    private DemoService demoService;

    private final MapReactiveUserDetailsService userDetailsService;

    private final Tokenizer tokenizer;

    private final UserDetailsRepositoryReactiveAuthenticationManager authenticationManager;

    @GetMapping("/sayHello/{name}")
    public Mono<SingleResponse<String>> sayHello(@PathVariable("name") String name) {
        String x = "3";
        return Mono.just(SingleResponse.of(demoService.sayHello(name)));
    }

    @GetMapping("/api/sayHello/{name}")
    public Mono<SingleResponse<String>> sayApiHello(@PathVariable("name") String name) {
        String x = "3";
        return Mono.just(SingleResponse.of(demoService.sayHello(name)));
    }

    @PostMapping("/login")
    public Mono<SingleResponse<AuthResp>> login(@RequestBody @Validated AuthReq req) {
        String username = req.getUsername();
        Mono<UserDetails> byUsername = userDetailsService.findByUsername(username);
        return byUsername.flatMap(user -> {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, req.getPassword());
            return authenticationManager.authenticate(authentication).map(auth -> {
                String token = tokenizer.tokenize(user.getUsername());
                return AuthResp.builder().token(token).username(user.getUsername()).build();
            }).map(SingleResponse::of);

        });
    }

    @GetMapping("profile")
    public Mono<SingleResponse<User>> getProfile(Authentication authentication) {
        logger.info("getProfile {}", getUsername());
        return Mono.justOrEmpty(authentication).filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(ExceptionFactory.sysException("未登录")))
                .map(auth -> (String) auth.getPrincipal())
                .flatMap((Function<String, Mono<UserDetails>>) userDetailsService::findByUsername).map(user -> {
                    String username = user.getUsername();
                    return new User(username, "123456", user.getAuthorities());
                }).switchIfEmpty(Mono.error(ExceptionFactory.sysException("用户不存在"))).map(SingleResponse::of);
    }

}
