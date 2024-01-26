package com.senla.project.repository;

import com.senla.project.entity.Ad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

  @Query("SELECT a FROM Ad a WHERE a.isClosed = false AND a.seller.id = :userId")
  List<Ad> findAllBySellerIdAndIsClosedFalse(@Param("userId") Long userId);
}
