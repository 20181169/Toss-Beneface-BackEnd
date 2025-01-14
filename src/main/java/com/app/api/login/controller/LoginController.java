package com.app.api.login.controller;

import com.app.api.login.dto.JoinDto;
import com.app.api.login.dto.LoginDto;
import com.app.api.login.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "authentication", description = "로그인/로그아웃/토큰재발급 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @Tag(name = "authentication")
    @Operation(summary = "회원가입 API", description = "회원가입 API")
    @PostMapping("/join")
    public ResponseEntity<JoinDto.Response> join(@RequestBody JoinDto.Request joinRequestDto,
                                                        HttpServletRequest httpServletRequest) {
        log.info("회원가입 요청 수신: {}", joinRequestDto);

        JoinDto.Response joinResponseDto = loginService.join(joinRequestDto);

        return ResponseEntity.ok(joinResponseDto);
    }

    @Tag(name = "authentication")
    @Operation(summary = "로그인 API", description = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<LoginDto.Response> login(@RequestBody LoginDto.Request loginRequestDto,
                                                        HttpServletRequest httpServletRequest) {

//        String authorizationHeader = httpServletRequest.getHeader("Authorization");
//        AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);
//        String accessToken = authorizationHeader.split(" ")[1];
        LoginDto.Response jwtTokenResponseDto = loginService.login(loginRequestDto);
        return ResponseEntity.ok(jwtTokenResponseDto);
    }
}
