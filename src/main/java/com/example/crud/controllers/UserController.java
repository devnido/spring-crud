package com.example.crud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud.entities.User;
import com.example.crud.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public List<User> findAll() {
    return userService.findAll();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<?> save(@Valid @RequestBody User user, BindingResult result) {

    if (result.hasFieldErrors()) {
      return validation(result);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
  }

  @PostMapping("register")
  public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {

    if (result.hasFieldErrors()) {
      return validation(result);
    }

    user.setAdmin(false);
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
  }

  private ResponseEntity<?> validation(BindingResult result) {

    Map<String, String> errors = new HashMap<>();

    result.getFieldErrors().forEach(err -> {
      errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
    });

    return ResponseEntity.badRequest().body(errors);
  }

}
