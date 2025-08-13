package com.example.ex4.controller;

import com.example.ex4.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guestbook")
@RequiredArgsConstructor
@Log4j2
public class GuestbookController {
  // has-a 관계 :: interface
  private final GuestbookService guestbookService;

  @GetMapping({"", "/","list"})
  public String list() {
    log.info("guestbook/list.............");
    return "guestbook/list";
  }

}
