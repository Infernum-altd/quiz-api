package com.quize.quizebackend.service;

import com.quize.quizebackend.dao.UserDao;
import com.quize.quizebackend.dto.UserDto;
import com.quize.quizebackend.entities.User;
import com.quize.quizebackend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public User findByEmail(String email) {
        User userdb = userDao.findByEmail(email);
        if (userdb == null) {
            throw new NotFoundException("user", "email", email);
        }
        return userdb;
    }

    public User findById(int id) {
        return userDao.findById(id);
    }
}
