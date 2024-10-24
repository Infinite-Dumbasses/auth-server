package com.infinite.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${JWT_ACCESS_SECRET}")
    private final String accessTokenSecretKey;
    @Value("${JWT_REFRESH_SECRET}")
    private final String refreshTokenSecretKey;
    @Value("${JWT_ACCESS_EXPIRES")
    private final long accessTokenExpiresInSeconds;
    @Value("${JWT_REFRESH_EXPIRES")
    private final long refreshTokenExpiresInSeconds;

    private final ObjectMapper objectMapper;

}
