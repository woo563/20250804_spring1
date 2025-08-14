package com.example.ex4.controller;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {
  // has-a관계  :: interface
  private final GuestbookService guestbookService;

  @GetMapping({"","/","list"})
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    log.info("guestbook/list...............");
    model.addAttribute("pageResultDTO", guestbookService.getList(pageRequestDTO));
    return "/guestbook/list";
  }

  @GetMapping("/register")
  public void register() {

  }
  @PostMapping("/register")
  public String registerPost(GuestbookDTO guestbookDTO, RedirectAttributes ra) {
    Long result = guestbookService.register(guestbookDTO);
    log.info(">>" + result + " 번 글이 등록 되었습니다.");
    ra.addFlashAttribute("msg", result);


    return "redirect:/guestbook/list";
  }
}