package com.example.crud.entities;

import com.example.crud.validation.IsExistsDb;
import com.example.crud.validation.IsRequired;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @IsRequired
  // @NotEmpty(message = "{NotEmpty.product.name}")
  @Size(min = 3, max = 50, message = "{Size.product.name}")
  private String name;

  @Min(500)
  @NotNull(message = "{NotNull.product.price}")
  private Double price;

  @IsRequired
  private String description;

  @IsExistsDb
  private Long sku;

  public Product() {
  }

  public Product(Long id, String name, String description, Double price, Long sku) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.sku = sku;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Long getSku() {
    return sku;
  }

  public void setSku(Long sku) {
    this.sku = sku;
  }

  @Override
  public String toString() {
    return "Product [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", sku=" + sku
        + "]";
  }

  // Getters and Setters

}
