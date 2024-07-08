package com.example.crud.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.crud.services.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

  @Autowired
  private UserService userService;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    if (userService == null) {
      return true;
    }

    return !userService.existsByUsername(value);
  }

}
