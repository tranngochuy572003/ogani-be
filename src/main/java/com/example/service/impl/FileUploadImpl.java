package com.example.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.entity.Image;
import com.example.entity.Product;
import com.example.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadImpl implements FileUploadService {

    private final Cloudinary cloudinary;
    @Override
    public List<Image> uploadFiles(MultipartFile[] files, Product product) throws IOException {
        List<Image> uploadedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("public_id", UUID.randomUUID().toString())
            );
            String imageUrl = (String) uploadResult.get("url");
            Image image = new Image();
            image.setUrlImg(imageUrl);
            image.setProducts(product);
            uploadedImages.add(image);
        }

        return uploadedImages;
    }
}
