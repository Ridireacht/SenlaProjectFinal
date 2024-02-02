package com.senla.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String email;
  private String address;
  private String password;


  @OneToOne(mappedBy = "user")
  private Rating rating;

  @OneToMany(mappedBy = "sender")
  private List<Message> sentMessages;

  @OneToMany(mappedBy = "sender")
  private List<Proposal> sentProposals;

  @OneToMany(mappedBy = "seller")
  private List<Ad> sellingAds;

  @OneToMany(mappedBy = "buyer")
  private List<Ad> boughtAds;

  @OneToMany(mappedBy = "sender")
  private List<Comment> sentComments;

  @OneToMany(mappedBy = "user")
  private List<UserScore> setScores;

  @OneToMany(mappedBy = "initiator")
  private List<Conversation> conversationsAsInitiator;

  @OneToMany(mappedBy = "receiver")
  private List<Conversation> conversationsAsReceiver;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;
}
