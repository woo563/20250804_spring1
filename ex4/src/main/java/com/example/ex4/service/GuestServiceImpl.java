package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestbookService {
  private final GuestbookRepository guestbookRepository;

  @Override
  public Long register(GuestbookDTO guestbookDTO) {
    return guestbookRepository.save(dtoToEntity(guestbookDTO)).getGno();
  }

}
