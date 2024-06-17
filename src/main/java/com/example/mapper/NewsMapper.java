package com.example.mapper;

import com.example.dto.NewsDto;
import com.example.entity.News;

public class NewsMapper {
  public static NewsDto toDto(News news) {
    NewsDto newsDto = new NewsDto();
    newsDto.setTitle(news.getTitle());
    newsDto.setContent(news.getContent());

    return newsDto;
  }

  public static News toEntity(NewsDto newsDto) {
    News news = new News();
    news.setTitle(newsDto.getTitle());
    news.setContent(newsDto.getContent());
    return news;
  }
}
