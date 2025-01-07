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
import java.util.regex.Pattern;

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
        Category category = categoryService.findCategoryByName(productDto.getCategory());
        checkProductDtoValid(productDto, category);
        boolean existProduct = productRepository.findProductByNameProduct(productDto.getNameProduct()) != null;
        if (existProduct) {
            throw new BadRequestException(VALUE_EXISTED);
        }

        Product product = ProductMapper.toCreateEntity(productDto);
        product.setCategory(category);

        try {
            List<Image> imageUrlList = fileUpload.uploadFiles(files, product);
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
        if (optionalProduct.isEmpty()) {
            throw new NotFoundException(VALUE_NO_EXIST);
        }
        Product product = optionalProduct.get();
        Category category = categoryService.findCategoryByName(productDto.getCategory());

        checkProductDtoValid(productDto, category);
        boolean existProduct = productRepository.findProductByNameProduct(productDto.getNameProduct()) != null;
        if (!product.getNameProduct().equals(productDto.getNameProduct()) && existProduct) {
            throw new BadRequestException(VALUE_EXISTED);
        }

        Product productSaved = ProductMapper.toUpdateEntity(product, productDto);
        productSaved.setCategory(category);
        try {
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

    @Override
    public ProductDto getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NotFoundException(VALUE_NO_EXIST);
        }
        return ProductMapper.toDto(product.get());

    }

    @Override
    public Product findProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NotFoundException(VALUE_NO_EXIST);
        }
        return product.get();

    }

    @Override
    public Product findProductByName(String name) {
        Product product = productRepository.findProductByNameProduct(name);
        if (product == null) {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
        return product;
    }

    @Override
    public ProductDto getProductByName(String name) {
        Product product = productRepository.findProductByNameProduct(name);
        if (product == null) {
            throw new NotFoundException(VALUE_NO_EXIST);
        }
        return ProductMapper.toDto(product);

    }

    @Override
    public List<ProductDto> getProductsByCreatedDate(LocalDate localDate) {
        return ProductMapper.toListDto(productRepository.findByCreatedDateBetween(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay()));
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

    @Override
    public List<ProductDto> getProductByPrice(String price) {
        long priceValue;
        try {
            priceValue = Long.parseLong(price);
        } catch (NumberFormatException e) {
            throw new BadRequestException(FIELD_INVALID);
        }
        Long priceLowest = 10L;
        List<Product> productList = productRepository.findByPriceBetween(priceLowest, priceValue);
        return ProductMapper.toListDto(productList);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    private void checkProductDtoValid(ProductDto productDto, Category category) {
        if (AppUtil.containsSpecialCharacters(productDto.getCategory())
                || AppUtil.containsSpecialCharacters(productDto.getNameProduct())) {
            throw new BadRequestException(FIELD_INVALID);
        }

        if (!Pattern.compile("\\d+").matcher(productDto.getInventory()).matches()
                || !Pattern.compile("\\d+").matcher(productDto.getPrice()).matches()) {
            throw new BadRequestException(FIELD_INVALID);
        }
        long price = Long.parseLong(productDto.getPrice());
        long inventory = Long.parseLong(productDto.getInventory());
        if (price <= 0 || inventory < 0) {
            throw new BadRequestException(FIELD_INVALID);
        }
        if (category == null) {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
        if (!category.isActive()) {
            throw new BadRequestException(ITEM_UNACTIVED);
        }
    }
}
