package com.example.service.impl;

import com.example.dto.ProductDto;
import com.example.entity.Category;
import com.example.entity.Image;
import com.example.entity.Product;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.mapper.ProductMapper;
import com.example.repository.ProductRepository;
import com.example.service.CategoryService;
import com.example.service.FileUploadService;
import com.example.service.ImageService;
import com.example.service.ProductService;
import com.example.util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.common.MessageConstant.*;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FileUploadService fileUpload;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ImageService imageService;


    @Override
    public void addProduct(ProductDto productDto, MultipartFile[] files) {
        if (AppUtil.containsSpecialCharacters(productDto.getNameProduct())) {
            throw new BadRequestException(FIELD_INVALID);
        }
        boolean existProduct = productRepository.findProductByNameProduct(productDto.getNameProduct()) != null;
        if (existProduct) {
            throw new BadRequestException(VALUE_EXISTED);
        }
        if (productDto.getPrice() < 0) {
            throw new BadRequestException(FIELD_INVALID);
        }
        try {
            if (AppUtil.containsSpecialCharacters(productDto.getCategory())) {
                throw new BadRequestException(FIELD_INVALID);
            }
            Category category= categoryService.findCategoryByName(productDto.getCategory());
            if(category==null){
                throw new BadRequestException(VALUE_NO_EXIST);
            }
            Product product = ProductMapper.toCreateEntity(productDto);
            product.setCategory(category);
            List<Image> imageUrlList  = fileUpload.uploadFiles(files, product);
            product.setImages(imageUrlList);
            productRepository.save(product);
        } catch (IOException e) {
            throw new BadRequestException(FILE_UPLOAD_ERROR);
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
            boolean existProduct = productRepository.findProductByNameProduct(productDto.getNameProduct()) != null;
            if (!product.getNameProduct().equals(productDto.getNameProduct()) && existProduct) {
                throw new BadRequestException(VALUE_EXISTED);
            }
            if (productDto.getPrice() < 0 || productDto.getInventory() < 0) {
                throw new BadRequestException(FIELD_INVALID);
            }
            try {
                if (AppUtil.containsSpecialCharacters(productDto.getCategory())) {
                    throw new BadRequestException(FIELD_INVALID);
                }
                Category category = categoryService.findCategoryByName(productDto.getCategory());
                if(category==null){
                    throw new BadRequestException(VALUE_NO_EXIST);
                }
                Product productSaved = ProductMapper.toUpdateEntity(product, productDto);
                productSaved.setCategory(category);
                if (files != null && files.length > 0) {
                    List<Image> imageUrlList = fileUpload.uploadFiles(files, productSaved);
                    productSaved.setImages(imageUrlList);
                }
                productRepository.save(productSaved);
            } catch (IOException e) {
                throw new BadRequestException(FILE_UPLOAD_ERROR);
            }
            List<String> listToRemove = productDto.getImageList();
            if (listToRemove != null) {
                fileUpload.deleteImagesByUrls(listToRemove);
                for (String url : listToRemove) {
                    imageService.deleteByUrlImg(url);
                }
            }
        }
        else {
            throw new NotFoundException(VALUE_NO_EXIST);
        }
    }

    @Override
    public ProductDto getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            return ProductMapper.toDto(product.get());
        }
        else {
            throw new NotFoundException(VALUE_NO_EXIST);
        }
    }

    @Override
    public Product findProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            return product.get();
        }
        else {
            throw new NotFoundException(VALUE_NO_EXIST);
        }    }

    @Override
    public ProductDto getProductByName(String name) {
        if (AppUtil.containsSpecialCharacters(name)) {
            throw new BadRequestException(FIELD_INVALID);
        }
        Product product = productRepository.findProductByNameProduct(name);
        if (product != null) {
            return ProductMapper.toDto(product);
        } else {
            throw new NotFoundException(VALUE_NO_EXIST);
        }
    }

    @Override
    public List<ProductDto> getProductsByCreatedDate(LocalDate localDate) {
        return ProductMapper.toListDto(productRepository.findByCreatedDateBetween(localDate.atStartOfDay(),localDate.plusDays(1).atStartOfDay()));
    }

    @Override
    public void deleteById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new NotFoundException(VALUE_NO_EXIST);
        }

    }
}
