package com.example.crud.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.crud.services.ProductService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsExistsDbValidation implements ConstraintValidator<IsExistsDb, Long> {

  @Autowired
  private ProductService productService;

  @Override
  public boolean isValid(Long value, ConstraintValidatorContext context) {

    if (productService == null) {
      return true;
    }

    return value != null && !productService.existsBySku(value);
  }

}
