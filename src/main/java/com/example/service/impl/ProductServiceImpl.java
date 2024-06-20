package com.example.service.impl;

import com.cloudinary.Cloudinary;
import com.example.dto.ProductDto;
import com.example.entity.Image;
import com.example.entity.Product;
import com.example.exception.BadRequestException;
import com.example.mapper.ProductMapper;
import com.example.repository.ProductRepository;
import com.example.service.FileUploadService;
import com.example.service.ProductService;
import com.example.util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.common.MessageConstant.FIELD_INVALID;
import static com.example.common.MessageConstant.VALUE_EXISTED;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private FileUploadService fileUpload;
  @Autowired
  private Cloudinary cloudinary;

  @Override
  public void addProduct(ProductDto productDto, MultipartFile[] files) {
    if (AppUtil.containsSpecialCharacters(productDto.getNameProduct())) {
      throw new BadRequestException("Name is invalid");
    }
    boolean existCategory = productRepository.findProductByNameProduct(productDto.getNameProduct()) != null;
    if (existCategory) {
      throw new BadRequestException(VALUE_EXISTED);
    }
    if (productDto.getPrice() < 0) {
      throw new BadRequestException(FIELD_INVALID);
    }
    try {
      Product product = ProductMapper.toCreateEntity(productDto);
      List<Image> imageUrlList  = fileUpload.uploadFiles(files,product);
      product.setImages(imageUrlList);
      productRepository.save(product);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
