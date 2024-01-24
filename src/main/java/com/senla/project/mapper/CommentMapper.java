package com.senla.project.mapper;

import com.senla.project.dto.CommentRequest;
import com.senla.project.dto.CommentResponse;
import com.senla.project.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

  @Mapping(source = "sender.id", target = "senderId")
  CommentResponse mapToCommentResponse(Comment comment);

  Comment mapToComment(CommentRequest commentRequest);
}
