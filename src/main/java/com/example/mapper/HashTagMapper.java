package com.example.mapper;

import com.example.dto.HashTagDto;
import com.example.dto.ImageDto;
import com.example.entity.HashTag;
import com.example.entity.Image;

public class HashTagMapper {
  public static HashTagDto toDto(HashTag hashTag) {
    HashTagDto hashTagDto = new HashTagDto();
    hashTagDto.setHashTagName(hashTag.getHashTagName());
    return hashTagDto;
  }

  public static HashTag toEntity(HashTagDto hashTagDto) {
    HashTag hashTag = new HashTag();
    hashTag.setHashTagName(hashTagDto.getHashTagName());

    return hashTag;
  }
}
