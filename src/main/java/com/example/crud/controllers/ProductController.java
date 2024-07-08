package com.example.crud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud.ProductValidation;
import com.example.crud.entities.Product;
import com.example.crud.services.ProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  private ProductValidation productValidation;

  @GetMapping()
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  public List<Product> findAll() {

    return productService.findAll();
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  public ResponseEntity<?> findById(@PathVariable Long id) {
    Optional<Product> product = productService.findById(id);
    if (product.isPresent()) {
      return ResponseEntity.ok(product.orElseThrow());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping()
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result) {

    // productValidation.validate(product, result);

    if (result.hasFieldErrors()) {
      return validation(result);
    }

    Product newProduct = productService.save(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
  }

  @PutMapping("/{id}") // BindingResult result tiene que ir después de @RequestBody en la segunda
                       // posición
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> update(@Valid @RequestBody Product product, BindingResult result, @PathVariable Long id) {

    if (result.hasFieldErrors()) {
      return validation(result);
    }

    Optional<Product> productOptional = productService.update(id, product);

    if (productOptional.isPresent()) {

      Product updatedProduct = productOptional.get();

      updatedProduct.setName(product.getName());
      updatedProduct.setDescription(product.getDescription());
      updatedProduct.setPrice(product.getPrice());

      productService.save(updatedProduct);

      return ResponseEntity.ok(updatedProduct);

    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> delete(@PathVariable Long id) {

    Optional<Product> product = productService.findById(id);

    if (product.isPresent()) {
      productService.deleteById(id);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  private ResponseEntity<?> validation(BindingResult result) {

    Map<String, String> errors = new HashMap<>();

    result.getFieldErrors().forEach(err -> {
      errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
    });

    return ResponseEntity.badRequest().body(errors);
  }

}
