package com.senla.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "proposals")
public class Proposal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ad_id")
  private Ad ad;

  @ManyToOne
  @JoinColumn(name = "proposed_by_user_id")
  private User proposedBy;

  @ManyToOne
  @JoinColumn(name = "proposed_to_user_id")
  private User proposedTo;

  private int price;
}
