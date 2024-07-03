package com.example.crud.services;

import java.util.List;
import java.util.Optional;

import com.example.crud.entities.Product;

public interface ProductService {

  List<Product> findAll();

  Optional<Product> findById(Long id);

  Product save(Product product);

  Optional<Product> update(Long id, Product product);

  void delete(Product product);

  void deleteById(Long id);

  boolean existsBySku(Long sku);

}
