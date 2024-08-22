package com.saki.service;

import com.saki.exception.UserException;
import com.saki.model.User;

public interface UserService {
    User findUserById(Long userId) throws UserException;
    User findUserProfileByJwt(String jwt) throws UserException;
}
