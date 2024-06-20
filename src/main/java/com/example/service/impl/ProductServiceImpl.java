package com.example.service.impl;

import com.example.dto.ProductDto;
import com.example.entity.Product;
import com.example.exception.BadRequestException;
import com.example.mapper.ProductMapper;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;
import com.example.util.AppUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
  @Autowired
  private ProductRepository productRepository;
  @Override
  public void addProduct(ProductDto productDto) {
    if (AppUtil.containsSpecialCharacters(productDto.getNameProduct())) {
      throw new BadRequestException("Name is invalid");
    }
    Product product = ProductMapper.toCreateEntity(productDto);
    productRepository.save(product);
  }
}
