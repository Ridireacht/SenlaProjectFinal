package com.senla.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_scores")
public class UserScore {

  // Пользователь, который поставил оценку
  @ManyToOne    // пока не уверен насчёт связи
  @JoinColumn(name = "user_id")
  private User user;

  // Объявление, за которое поставили оценку
  @OneToOne     // пока не уверен насчёт связи
  @JoinColumn(name = "ad_id")
  private Ad ad;

  private int score;

}
