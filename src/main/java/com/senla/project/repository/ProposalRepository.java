package com.senla.project.repository;

import com.senla.project.entity.Ad;
import com.senla.project.entity.Proposal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {

  @Query("SELECT p FROM Proposal p WHERE p.sender.id = :userId")
  List<Proposal> findAllBySenderId(@Param("userId") Long userId);

  @Query("SELECT p FROM Proposal p WHERE p.ad IN :ads")
  List<Proposal> findAllByAds(@Param("ads") List<Ad> ads);

  @Modifying
  @Query("DELETE FROM Proposal p WHERE p.ad = :ad")
  void deleteAllByAd(@Param("ad") Ad ad);
}
