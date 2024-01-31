package com.senla.project.service;

import com.senla.project.dto.request.CommentRequest;
import com.senla.project.dto.response.CommentResponse;
import java.util.List;

public interface CommentService {

  long getAdId(long commentId);

  List<CommentResponse> getCommentsOnAd(long adId);

  CommentResponse createCommentOnAd(long userId, long adId, CommentRequest commentRequest);

  boolean updateComment(long commentId, CommentRequest commentRequest);

  boolean deleteComment(long commentId);

  boolean doesCommentExist(long commentId);

  boolean doesCommentBelongToUser(long commentId, long currentUserId);
}
