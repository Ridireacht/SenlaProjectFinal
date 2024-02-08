package com.senla.project.repository;

import com.senla.project.entity.Ad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

  @Query("SELECT a FROM Ad a WHERE a.isClosed = false AND a.seller.id <> :userId")
  List<Ad> findAllByNotSellerIdAndIsClosedFalse(@Param("userId") long userId);

  @Query("SELECT a FROM Ad a WHERE a.isClosed = false AND a.seller.id = :userId")
  List<Ad> findAllBySellerIdAndIsClosedFalse(@Param("userId") long userId);

  @Query("SELECT a FROM Ad a WHERE a.isClosed = true AND a.seller.id = :userId")
  List<Ad> findAllBySellerIdAndIsClosedTrue(@Param("userId") long userId);

  @Query("SELECT a FROM Ad a WHERE a.buyer.id = :userId")
  List<Ad> findAllByBuyerId(@Param("userId") long userId);

  @Query("SELECT a FROM Ad a WHERE a.isClosed = false")
  List<Ad> findAllByIsClosedFalse();
}
