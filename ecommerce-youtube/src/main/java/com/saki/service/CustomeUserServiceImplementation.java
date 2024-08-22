package com.saki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.saki.model.User;
import com.saki.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomeUserServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by email
        User user = userRepository.findByEmail(username)
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();
        // Add authorities if necessary, e.g., roles or permissions
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}