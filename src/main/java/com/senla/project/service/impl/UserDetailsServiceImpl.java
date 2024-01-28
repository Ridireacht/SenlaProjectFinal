package com.senla.project.service.impl;

import com.senla.project.entity.User;
import com.senla.project.repository.UserRepository;
import com.senla.project.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);

    return user.map(UserDetailsImpl::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
  }

  @Transactional
  public String addUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return "User added successfully. Now log in.";
  }

}

