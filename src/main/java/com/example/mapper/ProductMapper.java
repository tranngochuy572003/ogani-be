package com.example.mapper;

import com.example.dto.CategoryDto;
import com.example.dto.ProductDto;
import com.example.entity.Category;
import com.example.entity.Product;

import java.util.ArrayList;
import java.util.List;

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

  public static List<ProductDto> toListDto(List<Product> products) {
    List<ProductDto> productDtoList  = new ArrayList<>();
    for(Product product :products){
      productDtoList.add(ProductMapper.toDto(product));
    }
    return productDtoList;
  }
}
