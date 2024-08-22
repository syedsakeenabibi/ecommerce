package com.saki.service;

import com.saki.config.JwtProvider;
import com.saki.exception.UserException;
import com.saki.model.User;
import com.saki.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserById(Long userId) throws UserException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with ID: " + userId));
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found with email: " + email));
    }
}
