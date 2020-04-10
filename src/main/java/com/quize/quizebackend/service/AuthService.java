package com.quize.quizebackend.service;

import com.quize.quizebackend.dao.UserDao;
import com.quize.quizebackend.dto.UserDto;
import com.quize.quizebackend.entities.User;
import com.quize.quizebackend.exceptions.EmailExistException;
import com.quize.quizebackend.exceptions.NotFoundException;
import com.quize.quizebackend.exceptions.PasswordException;
import com.quize.quizebackend.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDao userDao;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto register(User user) {
        User userdb =userDao.findByEmail(user.getEmail());
        if(userdb != null){
            throw new EmailExistException("User with this email already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.insert(user);
        return new UserDto(user);
    }

    public String login(User user) {
        User userdb = userDao.findByEmail(user.getEmail());
        if (userdb == null) {
            throw new NotFoundException("user", "email", user.getEmail());
        }
        if(!passwordEncoder.matches(user.getPassword(), userdb.getPassword())){
            throw new PasswordException();
        }
        return tokenProvider.createToken(userdb.getEmail());
    }
}
