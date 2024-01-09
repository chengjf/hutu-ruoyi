package tech.hutu.admin.web.security;


import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import tech.hutu.common.web.model.LoginUser;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author chengjf
 * @date 2024-01-09
 * @since 2024-01-09
 */
@Primary
@Component
public class TokenAuthenticationManager implements ReactiveAuthenticationManager {

    private final Tokenizer tokenizer;

    public TokenAuthenticationManager(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication.getCredentials())
                .filter(Objects::nonNull)
                .flatMap((Function<Object, Mono<DecodedJWT>>) credential -> tokenizer.verify((String) credential))
                .switchIfEmpty(Mono.error(new AccessDeniedException("token is not valid")))
                .flatMap((Function<DecodedJWT, Mono<UsernamePasswordAuthenticationToken>>) decodedJWT -> {
                    String username = decodedJWT.getClaim("principal").asString();
                    String role = decodedJWT.getClaim("role").asString();
                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
                    //
                    LoginUser loginUser = new LoginUser();
                    loginUser.setUsername(username);
                    return Mono.just(new UsernamePasswordAuthenticationToken(loginUser, null, authorities));
                });
    }

}