package com.example.ogani.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.entity.Image;
import com.example.entity.Product;
import com.example.exception.BadRequestException;
import com.example.service.impl.FileUploadServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FileUploadServiceTest {
    @InjectMocks
    FileUploadServiceImpl fileUploadService;
    private MultipartFile[] multipartFiles;
    private Product product;
    @Mock
    private Cloudinary cloudinary;
    @Mock
    private Uploader uploader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        multipartFiles = new MultipartFile[]{
                new MockMultipartFile("file1", "file1.txt", "text/plain", "some content".getBytes()),
        };
        product = new Product("name", true, 100L, "description", "information", 100L, null, null, null);


    }

    @Test
    void testUploadFiles() throws IOException {
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", "url1");
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(Object.class), any(Map.class))).thenReturn(uploadResult);
        List<Image> images = fileUploadService.uploadFiles(multipartFiles, product);
        Assertions.assertEquals("url1", images.get(0).getUrlImg());

    }
    @Test
    void testUploadFileInvalidThenThrowBadRequest() throws IOException {
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(Object.class), any(Map.class))).thenThrow(IOException.class);
        Assertions.assertThrows(BadRequestException.class, () -> fileUploadService.uploadFiles(multipartFiles, product));

    }
    @Test
    void testDeleteImagesByUrlsThenSuccess() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String url = "http://res.cloudinary.com/du3r55gby/image/upload/v1721881222/4d1fee28-6872-4b58-a986-4b11898e3967.jpg";
        List<String> images = new ArrayList<>(List.of(url));

        FileUploadServiceImpl fileUploadInstance = new FileUploadServiceImpl(cloudinary);
        Method method = FileUploadServiceImpl.class.getDeclaredMethod("extractPublicId", String.class);
        method.setAccessible(true);
        method.invoke(fileUploadInstance, url);

        when(cloudinary.uploader()).thenReturn(uploader);
        fileUploadService.deleteImagesByUrls(images);
        verify(cloudinary).uploader();

    }

    @Test
    void testDeleteImagesByUrlInvalidThenThrowBadRequest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String url = "imageInvalid";
        List<String> images = new ArrayList<>(List.of(url));

        FileUploadServiceImpl fileUploadInstance = new FileUploadServiceImpl(cloudinary);
        Method method = FileUploadServiceImpl.class.getDeclaredMethod("extractPublicId", String.class);
        method.setAccessible(true);
        method.invoke(fileUploadInstance, url);
        Assertions.assertThrows(BadRequestException.class, () -> fileUploadService.deleteImagesByUrls(images));
    }

    @Test
    void testDeleteWhenProductIdInvalidThenThrowBadRequest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        String url = "http://res.cloudinary.com/du3r55gby/image/upload/v1721881222/4d1fee28-6872-4b58-a986-4b11898e3967.jpg";
        List<String> images = new ArrayList<>(List.of(url));

        FileUploadServiceImpl fileUploadInstance = new FileUploadServiceImpl(cloudinary);
        Method method = FileUploadServiceImpl.class.getDeclaredMethod("extractPublicId", String.class);
        method.setAccessible(true);
        method.invoke(fileUploadInstance, url);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(anyString(), anyMap())).thenThrow(IOException.class);
        Assertions.assertThrows(BadRequestException.class, () -> fileUploadService.deleteImagesByUrls(images));

    }


}

