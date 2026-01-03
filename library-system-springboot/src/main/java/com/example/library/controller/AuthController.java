package com.example.library.controller;

import com.example.library.dto.ApiResp;
import com.example.library.dto.AuthDtos;
import com.example.library.entity.User;
import com.example.library.security.JwtUtil;
import com.example.library.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ApiResp<String> register(@Valid @RequestBody AuthDtos.RegisterReq req) {
        userService.register(req.getUsername(), req.getPassword());
        return ApiResp.okMsg("注册成功", "ok");
    }

    @PostMapping("/login")
    public ApiResp<AuthDtos.LoginResp> login(@Valid @RequestBody AuthDtos.LoginReq req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        String role = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        String token = jwtUtil.generateToken(req.getUsername(), role);
        return ApiResp.ok(new AuthDtos.LoginResp(token, role));
    }
}
