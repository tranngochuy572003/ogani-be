package com.example.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.entity.Image;
import com.example.entity.Product;
import com.example.exception.BadRequestException;
import com.example.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;

import static com.example.common.AppConstant.PUBLIC_ID_PATTERN;
import static com.example.common.MessageConstant.FIELD_INVALID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    private final Cloudinary cloudinary;

    @Override
    public List<Image> uploadFiles(MultipartFile[] files, Product product) throws IOException {
        List<Image> uploadedImages = new ArrayList<>();

        try {
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
        }catch (Exception e){
            throw new BadRequestException(FIELD_INVALID);
        }

        return uploadedImages;
    }
    public void deleteImagesByUrls(List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            try {
                String publicId = extractPublicId(imageUrl);
                if (publicId != null) {
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    log.info("Successfully deleted image with public ID: " + publicId);
                } else {
                    log.warn("Could not extract public ID from URL: " + imageUrl);
                }
            } catch (IOException e) {
                log.error("Error deleting image from Cloudinary", e);
            }
        }
    }

    private String extractPublicId(String imageUrl) {
        Matcher matcher = PUBLIC_ID_PATTERN.matcher(imageUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


}
