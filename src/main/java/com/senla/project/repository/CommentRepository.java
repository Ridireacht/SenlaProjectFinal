package com.senla.project.repository;

import com.senla.project.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Query("SELECT c FROM Comment c WHERE c.ad.id = :adId")
  List<Comment> findAllByAdId(@Param("adId") long adId);
}
