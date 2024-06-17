package com.example.mapper;

import com.example.dto.CommentDto;
import com.example.entity.Comment;

public class CommentMapper {
  public static CommentDto toDto(Comment comment) {
    CommentDto commentDto = new CommentDto();
    commentDto.setContent(comment.getContent());
    return commentDto;
  }

  public static Comment toEntity(CommentDto commentDto) {
    Comment comment = new Comment();
    comment.setContent(commentDto.getContent());
    return comment;
  }
}
