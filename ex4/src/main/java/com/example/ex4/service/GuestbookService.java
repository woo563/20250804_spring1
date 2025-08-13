package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.entity.Guestbook;

import java.util.List;


public interface GuestbookService {
  Long register(GuestbookDTO guestbookDTO);

  default Guestbook dtoToEntity(GuestbookDTO guestbookDTO) {
    return Guestbook.builder()
        .gno(guestbookDTO.getGno())
        .title(guestbookDTO.getTitle())
        .content(guestbookDTO.getContent())
        .writer(guestbookDTO.getWriter())
        .build();
  }

  default GuestbookDTO dtoToDto(Guestbook guestbook) {
    return GuestbookDTO.builder()
        .gno(guestbook.getGno())
        .title(guestbook.getTitle())
        .content(guestbook.getContent())
        .writer(guestbook.getWriter())
        .build();
  }



}
