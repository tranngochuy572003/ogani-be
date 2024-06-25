package com.example.service;

import com.example.entity.Image;
import com.example.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploadService {
    List<Image> uploadFiles(MultipartFile [] files, Product product) throws IOException;
    void deleteImagesByUrls(List<String> imageUrls);
}
