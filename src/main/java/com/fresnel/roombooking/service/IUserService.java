package com.fresnel.roombooking.service;

import com.fresnel.roombooking.model.User;

import java.util.List;



public interface IUserService {
    User registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);

    User getUserById(Long id);
}
