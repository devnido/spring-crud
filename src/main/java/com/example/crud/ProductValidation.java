package com.example.crud;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.crud.entities.Product;

@Component
public class ProductValidation implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Product.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {

    Product product = (Product) target;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", null, "NotEmpty.product.name");

    if (product.getDescription() == null | product.getDescription().isEmpty()) {
      errors.rejectValue("description", null, "NotBlank.product.description");
    }

    if (product.getPrice() == null) {
      errors.rejectValue("price", null, "NotNull.product.price");
    } else if (product.getPrice() < 500) {
      errors.rejectValue("price", null, "Min.product.price");
    }

  }

}
