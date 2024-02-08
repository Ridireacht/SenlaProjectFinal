package com.senla.project.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
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


  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private Rating rating;

  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  private List<Message> sentMessages = new ArrayList<>();

  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  private List<Proposal> sentProposals = new ArrayList<>();

  @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
  private List<Ad> sellingAds = new ArrayList<>();

  @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
  private List<Ad> boughtAds = new ArrayList<>();

  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  private List<Comment> sentComments = new ArrayList<>();

  @OneToMany(mappedBy = "setter", cascade = CascadeType.ALL)
  private List<Score> scoresSet = new ArrayList<>();

  @OneToMany(mappedBy = "initiator", cascade = CascadeType.ALL)
  private List<Conversation> conversationsAsInitiator = new ArrayList<>();

  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
  private List<Conversation> conversationsAsReceiver = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;
}
