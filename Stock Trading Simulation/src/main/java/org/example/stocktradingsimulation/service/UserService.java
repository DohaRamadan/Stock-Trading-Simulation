package org.example.stocktradingsimulation.service;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import org.example.stocktradingsimulation.dto.inbound.user.UserLoginRequest;
import org.example.stocktradingsimulation.dto.inbound.user.UserRegisterRequest;
import org.example.stocktradingsimulation.dto.outbound.user.UserLoginResponse;
import org.example.stocktradingsimulation.dto.outbound.user.UserRegisterResponse;
import org.example.stocktradingsimulation.exceptions.user.UserDoesNotExistException;
import org.example.stocktradingsimulation.exceptions.user.UserIsAlreadyRegisteredException;
import org.example.stocktradingsimulation.models.User;
import org.example.stocktradingsimulation.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserRegisterResponse registerUser(UserRegisterRequest input){
        User user = userRepository.findByEmail(input.getEmail());
        if(user != null){
            throw new UserIsAlreadyRegisteredException();
        }
        user = new User();
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        userRepository.save(user);
        return new UserRegisterResponse();
    }
    public UserLoginResponse login(UserLoginRequest input) {
        User userEntity = userRepository.findByEmail(input.getEmail());
        if(userEntity == null){
            throw new UserDoesNotExistException();
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(userEntity);

        UserLoginResponse loginResponse = new UserLoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        return loginResponse;
    }

    public ResponseEntity<?> signout(HttpServletRequest request) {
        String jwtToken = extractJwtToken(request);
        jwtService.invalidateToken(jwtToken);

        return ResponseEntity.ok("Sign out successful");
    }

    private String extractJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }


}
