package com.example.mapper;

import com.example.dto.ImageDto;
import com.example.dto.NewsDto;
import com.example.entity.Image;
import com.example.entity.News;

public class ImageMapper {
  public static ImageDto toDto(Image image) {
    ImageDto imageDto = new ImageDto();
    imageDto.setUrlImg(image.getUrlImg());


    return imageDto;
  }

  public static Image toEntity(ImageDto imageDto) {
    Image image = new Image();
    image.setUrlImg(imageDto.getUrlImg());
    return image;
  }
}
