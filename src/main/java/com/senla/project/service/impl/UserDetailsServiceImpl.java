package com.senla.project.service.impl;

import com.senla.project.dto.request.RegisterRequest;
import com.senla.project.entity.Rating;
import com.senla.project.entity.Role;
import com.senla.project.entity.User;
import com.senla.project.repository.RatingRepository;
import com.senla.project.repository.RoleRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private RatingRepository ratingRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);

    return user.map(UserDetailsImpl::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
  }

  @Transactional
  public String registerUser(RegisterRequest registerRequest) {
    Role role = roleRepository.findByName(registerRequest.getRole());

    User user = new User();
    user.setUsername(registerRequest.getUsername());
    user.setEmail(registerRequest.getEmail());
    user.setAddress(registerRequest.getAddress());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    user.setRole(role);

    User savedUser = userRepository.save(user);

    Rating rating = new Rating();
    rating.setUser(savedUser);
    ratingRepository.save(rating);

    return "New user registered successfully. Now log in, using /auth/login.";
  }

  public String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }

}

