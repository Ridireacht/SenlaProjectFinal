package com.senla.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  private String content;

  @ManyToOne
  @JoinColumn(name = "from_user_id")
  private User fromUser;

  @ManyToOne
  @JoinColumn(name = "to_user_id")
  private User toUser;

  @ManyToOne
  @JoinColumn(name = "conversation_id")
  private Conversation conversation;
  
}
