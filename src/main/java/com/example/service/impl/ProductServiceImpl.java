package com.example.service.impl;

import com.example.dto.ProductDto;
import com.example.entity.Image;
import com.example.entity.Product;
import com.example.exception.BadRequestException;
import com.example.mapper.ProductMapper;
import com.example.repository.CategoryRepository;
import com.example.repository.ImageRepository;
import com.example.repository.ProductRepository;
import com.example.service.FileUploadService;
import com.example.service.ProductService;
import com.example.util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
    private CategoryRepository categoryRepository;
    @Autowired
    private ImageRepository imageRepository;

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
            product.setCategory(categoryRepository.findByName(productDto.getCategory()));
            List<Image> imageUrlList  = fileUpload.uploadFiles(files, product);
            product.setImages(imageUrlList);
            productRepository.save(product);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return ProductMapper.toListDto(productList);
    }

    @Transactional
    @Override
    public void updateProduct(String id, ProductDto productDto, MultipartFile[] files) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (AppUtil.containsSpecialCharacters(productDto.getNameProduct())) {
                throw new BadRequestException(FIELD_INVALID);
            }
            boolean existCategory = productRepository.findProductByNameProduct(productDto.getNameProduct()) != null;
            if (!product.getNameProduct().equals(productDto.getNameProduct()) && existCategory) {
                throw new BadRequestException(VALUE_EXISTED);
            }
            if (productDto.getPrice() < 0 || productDto.getInventory() < 0) {
                throw new BadRequestException(FIELD_INVALID);
            }
            try {
                Product productSaved = ProductMapper.toUpdateEntity(product, productDto);
                productSaved.setCategory(categoryRepository.findByName(productDto.getCategory()));
                if (files != null && files.length > 0) {
                    List<Image> imageUrlList = fileUpload.uploadFiles(files, productSaved);
                    productSaved.setImages(imageUrlList);
                }
                productRepository.save(productSaved);
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
            List<String> listToRemove = productDto.getImageList();
            if (listToRemove != null) {
                fileUpload.deleteImagesByUrls(listToRemove);
                for (String url : listToRemove) {
                    imageRepository.deleteByUrlImg(url);
                }
            }


        } else {
            throw new BadRequestException(FIELD_INVALID);
        }
    }
}
