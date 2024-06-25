package com.example.service.impl;

import com.example.repository.ImageRepository;
import com.example.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Autowired
    private  ImageRepository imageRepository;
    @Override
    public void deleteByUrlImg(String url) {
        imageRepository.deleteByUrlImg(url);
    }
}
