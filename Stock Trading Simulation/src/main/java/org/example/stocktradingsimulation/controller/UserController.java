package org.example.stocktradingsimulation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.stocktradingsimulation.dto.inbound.user.UserLoginRequest;
import org.example.stocktradingsimulation.dto.inbound.user.UserRegisterRequest;
import org.example.stocktradingsimulation.dto.outbound.user.UserLoginResponse;
import org.example.stocktradingsimulation.dto.outbound.user.UserRegisterResponse;
import org.example.stocktradingsimulation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.NotNull;


@RequestMapping("/auth")
@RestController
public class UserController {
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody @NotNull UserRegisterRequest registerUserDto) {
        return ResponseEntity.ok(userService.registerUser(registerUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> authenticate(@Valid @RequestBody @NotNull UserLoginRequest loginUserDto) {
        return ResponseEntity.ok(userService.login(loginUserDto));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(HttpServletRequest request) {
        return ResponseEntity.ok(userService.signout(request));
    }
}
