package com.example.service.impl;

import com.example.dto.ProductDto;
import com.example.entity.Product;
import com.example.exception.BadRequestException;
import com.example.mapper.ProductMapper;
import com.example.repository.ProductRepository;
import com.example.service.FileUploadService;
import com.example.service.ProductService;
import com.example.util.AppUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Data
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private FileUploadService fileUpload;
  @Override
  public void addProduct(ProductDto productDto, MultipartFile multipartFile, Model model) throws IOException {
    if (AppUtil.containsSpecialCharacters(productDto.getNameProduct())) {
      throw new BadRequestException("Name is invalid");
    }

    String imageURL = fileUpload.uploadFile(multipartFile);
    model.addAttribute("imageURL",imageURL);
    Product product = ProductMapper.toCreateEntity(productDto);
    productRepository.save(product);
  }
}
