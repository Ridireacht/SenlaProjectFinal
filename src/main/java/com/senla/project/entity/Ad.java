package com.senla.project.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ads")
public class Ad {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "is_premium")
  private boolean isPremium = false;

  @Column(name = "posted_at")
  private LocalDateTime postedAt;

  @Column(name = "is_closed")
  boolean isClosed = false;

  private String title;
  private String content;
  private int price;


  @OneToOne(mappedBy = "ad")
  private UserScore score;

  @OneToMany(mappedBy = "ad")
  private List<Conversation> conversations;

  @OneToMany(mappedBy = "ad")
  private List<Proposal> proposals;

  @OneToMany(mappedBy = "ad")
  private List<Comment> comments;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  private User seller;

  @ManyToOne
  @JoinColumn(name = "buyer_id")
  private User buyer;

}
