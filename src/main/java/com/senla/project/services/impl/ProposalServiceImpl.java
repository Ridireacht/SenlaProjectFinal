package com.senla.project.services.impl;

import com.senla.project.repositories.ProposalRepository;
import com.senla.project.services.ProposalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProposalServiceImpl implements ProposalService {

  private final ProposalRepository proposalRepository;

  
}
