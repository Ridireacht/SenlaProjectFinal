package com.senla.project.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
@Table(name = "ratings")
public class Rating {

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "average_score")
  private double averageScore;

  @OneToMany(mappedBy = "rating", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserScore> userRatings = new ArrayList<>();

}