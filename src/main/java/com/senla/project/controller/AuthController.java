package com.senla.project.controller;

import com.senla.project.dto.request.LoginRequest;
import com.senla.project.dto.request.RegisterRequest;
import com.senla.project.exception.ConflictException;
import com.senla.project.repository.RoleRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.security.JwtService;
import com.senla.project.service.impl.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  private final UserDetailsServiceImpl userDetailsService;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;


  @PostMapping("/register")
  public String register(@Valid @RequestBody RegisterRequest registerRequest) {
    if (userRepository.existsByUsername(registerRequest.getUsername())) {
      throw new ConflictException("This username is already taken.");
    }

    if (userRepository.existsByEmail(registerRequest.getEmail())) {
      throw new ConflictException("This email is already taken.");
    }

    if (!roleRepository.existsByName(registerRequest.getRole())) {
      throw new ConflictException("This role doesn't exist.");
    }

    return userDetailsService.registerNewUser(registerRequest);
  }

  @PostMapping("/login")
  public String login(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    if (authentication.isAuthenticated()) {
      return jwtService.generateToken(loginRequest.getUsername());
    } else {
      throw new UsernameNotFoundException("Invalid user request: username not found!");
    }
  }
}