package com.example.ogani.service;

import com.example.repository.ImageRepository;
import com.example.service.impl.ImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

class ImageServiceTest {
    @InjectMocks
    ImageServiceImpl imageServiceImpl;

    @Mock
    ImageRepository imageRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);}
    @Test
    void testDeleteByUrlImg(){
        imageServiceImpl.deleteByUrlImg(anyString());
        verify(imageRepository).deleteByUrlImg(anyString());

    }
}
