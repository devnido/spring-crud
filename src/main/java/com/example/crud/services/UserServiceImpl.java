package com.example.crud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crud.entities.Role;
import com.example.crud.entities.User;
import com.example.crud.repositories.RoleRepository;
import com.example.crud.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  @Override
  public List<User> findAll() {
    return (List<User>) userRepository.findAll();
  }

  @Transactional
  @Override
  public User save(User user) {

    Optional<Role> roleUser = roleRepository.findByName("ROLE_USER");

    List<Role> roles = new ArrayList<>();

    roleUser.ifPresent(roles::add);

    if (user.isAdmin()) {
      Optional<Role> roleAdmin = roleRepository.findByName("ROLE_ADMIN");

      roleAdmin.ifPresent(roles::add);
    }

    user.setRoles(roles);

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

}
