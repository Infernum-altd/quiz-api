package com.quiz.service;

import com.quiz.dao.UserDao;
import com.quiz.dto.UserDto;
import com.quiz.entities.User;
import com.quiz.exceptions.EmailExistException;
import com.quiz.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

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

    public User findProfileInfoByUserId(int id) {
        return userDao.findProfileInfoByUserId(id);
    }

    public List<User> findFriendByUserId(int id) {
        return userDao.findFriendByUserId(id);
    }


    public boolean updateUser(User user) {
        return userDao.updateUser(user);
    }

    public boolean updatePasswordById(int id, String newPassword) {
        return userDao.updatePasswordById(id, newPassword);
    }

    public int getUserIdByEmail(String email){
        return userDao.getUserIdByEmail(email);
    }

    public List<User> findAdminsUsers() {
        return userDao.findAdminsUsers();
    }

    public UserDto createAdminUsers(User adminsUser, String role) {
        User adminsUserdb = userDao.findByEmail(adminsUser.getEmail());
        if (adminsUserdb != null) {
            throw new EmailExistException("Moderator with this email already exist");
        }
        adminsUser.setPassword(passwordEncoder.encode(adminsUser.getPassword()));
        userDao.createAdminUsers(adminsUser, role);
        return new UserDto(adminsUser);
    }

    public void deleteUserById(int id) { userDao.deleteUserById(id); }

}
