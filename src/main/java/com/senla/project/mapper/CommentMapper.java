package com.senla.project.mapper;

import com.senla.project.dto.CommentResponse;
import com.senla.project.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

  CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);


  CommentResponse mapToCommentResponse(Comment comment);

  Comment mapToComment(CommentResponse commentResponse);
}
