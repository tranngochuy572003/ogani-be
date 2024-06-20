package com.example.mapper;

import com.example.dto.ProductDto;
import com.example.entity.Product;

public class ProductMapper {
  public static ProductDto toDto(Product product) {
    ProductDto productDto = new ProductDto();
    productDto.setActive(product.isActive());
    productDto.setNameProduct(product.getNameProduct());
    productDto.setInventory(product.getInventory());
    productDto.setPrice(product.getPrice());
    productDto.setDescription(product.getDescription());
    productDto.setInformation(product.getInformation());

    return productDto;
  }

  public static Product toCreateEntity(ProductDto productDto) {
    Product product = new Product();
    product.setActive(productDto.isActive());
    product.setNameProduct(productDto.getNameProduct());
    product.setInventory(productDto.getInventory());
    product.setPrice(productDto.getPrice());
    product.setDescription(productDto.getDescription());
    product.setInformation(productDto.getInformation());

    return product;
  }

  public static Product toUpdateEntity(Product product , ProductDto productDto) {
    product.setActive(productDto.isActive());
    product.setNameProduct(productDto.getNameProduct());
    product.setInventory(productDto.getInventory());
    product.setPrice(productDto.getPrice());
    product.setDescription(productDto.getDescription());
    product.setInformation(productDto.getInformation());

    return product;
  }
}
