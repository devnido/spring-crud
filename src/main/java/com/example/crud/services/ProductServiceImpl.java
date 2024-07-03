package com.example.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crud.entities.Product;
import com.example.crud.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Transactional(readOnly = true)
  @Override
  public List<Product> findAll() {

    return (List<Product>) productRepository.findAll();

  }

  @Transactional(readOnly = true)
  @Override
  public Optional<Product> findById(Long id) {

    return productRepository.findById(id);
  }

  @Transactional
  @Override
  public Product save(Product product) {

    return productRepository.save(product);
  }

  @Override
  public Optional<Product> update(Long id, Product product) {

    Optional<Product> productOptional = productRepository.findById(id);

    if (productOptional.isPresent()) {

      Product updatedProduct = productOptional.get();

      updatedProduct.setName(product.getName());
      updatedProduct.setDescription(product.getDescription());
      updatedProduct.setPrice(product.getPrice());

      return Optional.of(productRepository.save(updatedProduct));
    }

    return Optional.empty();
  }

  @Transactional
  @Override
  public void delete(Product product) {

    productRepository.delete(product);
  }

  @Override
  public void deleteById(Long id) {

    productRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsBySku(Long sku) {

    return productRepository.existsBySku(sku);
  }

}
