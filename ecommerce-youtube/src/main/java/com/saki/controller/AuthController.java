package com.saki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saki.config.JwtProvider;
import com.saki.exception.UserException;
import com.saki.model.Cart;
import com.saki.model.User;
import com.saki.repository.UserRepository;
import com.saki.request.LoginRequest;
import com.saki.response.AuthResponse;
import com.saki.service.CartService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserRepository userRepository, JwtProvider jwtProvider,
                          PasswordEncoder passwordEncoder, CartService cartService,
                          AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        // Check if the user already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserException("Email is already used with another account");
        }

        // Encode the password and create the new user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Create a cart for the new user
        Cart cart = cartService.createCart(savedUser);

        // Authenticate the new user (using their plain password)
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), user.getPassword());
        authentication = authenticationManager.authenticate(authentication);

        // Generate JWT token
        String token = jwtProvider.generateToken(authentication);

        // Prepare response
        AuthResponse authResponse = new AuthResponse(token, "Signup Success");
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            authentication = authenticationManager.authenticate(authentication);

            // Set authentication in context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String token = jwtProvider.generateToken(authentication);

            // Prepare response
            AuthResponse authResponse = new AuthResponse(token, "Signin Success");
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            // Handle authentication failure
            return new ResponseEntity<>(new AuthResponse(null, "Invalid credentials"), HttpStatus.UNAUTHORIZED);
        }
    }
}
