package com.senla.project.entities;

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

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String title;
  private String content;
  private int price;

  private boolean available;
  private boolean isPremium;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Conversation> conversations = new ArrayList<>();

  @Column(name = "is_closed")
  boolean isClosed = false;

  @ManyToOne
  @JoinColumn(name = "buyer_id")
  private User buyer;

  @OneToOne(mappedBy = "ad", cascade = CascadeType.ALL)
  private UserScore score;
}
