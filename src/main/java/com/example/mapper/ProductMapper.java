package com.example.mapper;

import com.example.dto.ProductDto;
import com.example.entity.Product;

public class ProductMapper {
  public static ProductDto toDto(Product product) {
    ProductDto productDto = new ProductDto();
    productDto.setActive(product.isActive());
    productDto.setNameProduct(product.getNameProduct());
    productDto.setAvailability(product.getAvailability());
    productDto.setInventory(product.getInventory());
    productDto.setPrice(product.getPrice());
    productDto.setDescription(product.getDescription());
    productDto.setShipping(product.getShipping());
    productDto.setInformation(product.getInformation());

    return productDto;
  }

  public static Product toEntity(ProductDto productDto) {
    Product product = new Product();
    product.setActive(productDto.isActive());
    product.setNameProduct(productDto.getNameProduct());
    product.setAvailability(productDto.getAvailability());
    product.setInventory(productDto.getInventory());
    product.setPrice(productDto.getPrice());
    product.setDescription(productDto.getDescription());
    product.setShipping(productDto.getShipping());
    product.setInformation(productDto.getInformation());

    return product;
  }
}
