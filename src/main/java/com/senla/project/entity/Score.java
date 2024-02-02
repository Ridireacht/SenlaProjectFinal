package com.senla.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "scores")
public class Score {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int value;


  @OneToOne
  @JoinColumn(name = "ad_id")
  private Ad ad;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User setter;

  @ManyToOne
  @JoinColumn(name = "rating_id")
  private Rating rating;
}
