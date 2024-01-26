package com.senla.project.repository;

import com.senla.project.entity.Rating;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

  @Query("SELECT r FROM Rating r WHERE r.user.id = :userId")
  Optional<Rating> findByUserId(@Param("userId") Long userId);
}
