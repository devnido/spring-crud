package com.example.crud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.crud.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

  boolean existsBySku(Long sku);

}
