package com.senla.project.service;

import com.senla.project.dto.request.CommentRequest;
import com.senla.project.dto.response.CommentResponse;
import java.util.List;

public interface CommentService {

  Long getAdId(Long commentId);

  List<CommentResponse> getCommentsOnAd(Long adId);

  CommentResponse createCommentOnAd(Long userId, Long adId, CommentRequest commentRequest);

  CommentResponse updateComment(Long commentId, CommentRequest commentRequest);

  boolean deleteComment(Long commentId);

  boolean doesCommentExist(Long commentId);

  boolean doesCommentBelongToUser(Long commentId, Long currentUserId);
}
