package com.app.global.interceptor;

import com.app.domain.member.constant.Role;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.service.TokenManager;
import com.app.global.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final TokenManager tokenManager;
    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

        String authorizationHeader = request.getHeader("Authorization");
        String accessToken = authorizationHeader.split(" ")[1];

        Claims tokenClaims = jwtUtils.getTokenClaims(accessToken);
        String role = (String) tokenClaims.get("role");

        if (!Role.ADMIN.equals(Role.valueOf(role))) {
            throw new AuthenticationException(ErrorCode.FORBIDDEN_ADMIN);
        }

        return true;
    }
}
