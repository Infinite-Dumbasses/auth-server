package com.infinite.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Autowired
    private final Environment env;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // auth server의 session creation policy가 stateless일 이유 없을 듯
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        // 추후 InMemory 말고 DB에 옮기는 방안 고려 (Redis가 효율적일까?)
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository(){
        // 이건 웬만하면 건들 일이 없어 보임...
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService());
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        // 이건 InMemory 한다해서 문제 없을 듯 ㅇㅇ..
        return new InMemoryClientRegistrationRepository(
                this.googleClientRegistration(),
                this.naverClientRegistration(),
                this.kakaoClientRegistration()
        );
    }

    private ClientRegistration googleClientRegistration() {
        Set<String> scope = new HashSet<>(Arrays.asList(Objects.requireNonNull(env.getProperty("GOOGLE_SCOPE")).split(",")));
        return ClientRegistration.withRegistrationId("google")
                .clientId(env.getProperty("GOOGLE_CLIENT_ID"))
                .clientSecret(env.getProperty("GOOGLE_CLIENT_SECRET"))
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri(env.getProperty("GOOGLE_AUTHORIZATION_URI"))
                .redirectUri(env.getProperty("GOOGLE_REDIRECT_URI"))
                .tokenUri(env.getProperty("GOOGLE_TOKEN_URI"))
                .userInfoUri(env.getProperty("GOOGLE_USER_INFO_URI"))
                .userNameAttributeName(env.getProperty("GOOGLE_USER_NAME_ATTRIBUTE_NAME"))
                .scope(scope)
                .clientName("google")
                .build();
    }

    private ClientRegistration naverClientRegistration() {
        return ClientRegistration.withRegistrationId("naver")
                .clientId(env.getProperty("NAVER_CLIENT_ID"))
                .clientSecret(env.getProperty("NAVER_CLIENT_SECRET"))
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri(env.getProperty("NAVER_AUTHORIZATION_URI"))
                .redirectUri(env.getProperty("NAVER_REDIRECT_URI"))
                .tokenUri(env.getProperty("NAVER_TOKEN_URI"))
                .userInfoUri(env.getProperty("NAVER_USER_INFO_URI"))
                .userNameAttributeName(env.getProperty("NAVER_USER_NAME_ATTRIBUTE_NAME"))
                .clientName("naver")
                .build();
    }

    private ClientRegistration kakaoClientRegistration() {
        return ClientRegistration.withRegistrationId("kakao")
                .clientId(env.getProperty("KAKAO_CLIENT_ID"))
                .clientSecret(env.getProperty("KAKAO_CLIENT_SECRET"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri(env.getProperty("KAKAO_AUTHORIZATION_URI"))
                .redirectUri(env.getProperty("KAKAO_REDIRECT_URI"))
                .tokenUri(env.getProperty("KAKAO_TOKEN_URI"))
                .userInfoUri(env.getProperty("KAKAO_USER_INFO_URI"))
                .userNameAttributeName(env.getProperty("KAKAO_USER_NAME_ATTRIBUTE_NAME"))
                .clientName("kakao")
                .build();
    }

}
