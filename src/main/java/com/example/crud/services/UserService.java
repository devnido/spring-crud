package com.example.crud.services;

import java.util.List;

import com.example.crud.entities.User;

public interface UserService {
  List<User> findAll();

  User save(User user);

  boolean existsByUsername(String username);

}
