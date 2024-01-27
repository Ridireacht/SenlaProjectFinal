package com.senla.project.repository;

import com.senla.project.entity.Ad;
import com.senla.project.entity.Conversation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

  @Query("SELECT c FROM Conversation c WHERE c.buyer.id = :userId OR c.seller.id = :userId")
  List<Conversation> findByBuyerIdOrSellerId(@Param("userId") Long userId);

  @Modifying
  @Query("DELETE FROM Conversation c WHERE c.ad = :ad")
  void deleteAllByAd(@Param("ad") Ad ad);
}
