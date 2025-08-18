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
    log.info("guestbook/list..." + pageRequestDTO);
    model.addAttribute("pageResultDTO", guestbookService.getList(pageRequestDTO));
    return "/guestbook/list";
  }
  @GetMapping("/register")
  public void register(){}
  @PostMapping("/register")
  public String registerPost(GuestbookDTO guestbookDTO, RedirectAttributes ra){
    Long result = guestbookService.register(guestbookDTO);
    log.info(">>"+result+" 번 글이 등록되었습니다.");
    ra.addFlashAttribute("msg", result); // RedirectAttributes 일회성, 최종 view단까지 보냄
    return "redirect:/guestbook/list";
  }

  @GetMapping("/read")
  public void read(Long gno, PageRequestDTO pageRequestDTO, Model model){
    log.info(">>gno" + gno);
    log.info(">>pageRequestDTO" + pageRequestDTO);
    GuestbookDTO guestbookDTO = guestbookService.read(gno);
    model.addAttribute("guestbookDTO", guestbookDTO);
    model.addAttribute("pageRequestDTO", pageRequestDTO);
  }

  @GetMapping("/modify")
  public void modify(Long gno, PageRequestDTO pageRequestDTO, Model model) {
    log.info("modify GET... gno: " + gno);
    GuestbookDTO guestbookDTO = guestbookService.read(gno);
    model.addAttribute("guestbookDTO", guestbookDTO);
    model.addAttribute("pageRequestDTO", pageRequestDTO); // 목록으로 돌아갈 때를 위해 이것도 추가!
  }

  @PostMapping("/modify")
  public String modifyPost(GuestbookDTO dto, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {
    log.info("modify POST........................" + dto);

    // 1. 서비스 계층을 호출하여 실제 수정을 처리합니다.
    guestbookService.modify(dto);

    // 2. RedirectAttributes에 페이지 정보를 추가하여 리다이렉트 후에도 유지되도록 합니다.
    redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
    redirectAttributes.addAttribute("type", pageRequestDTO.getType());
    redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());

    // 3. 수정된 글을 바로 확인할 수 있도록, 해당 글의 gno를 추가합니다.
    redirectAttributes.addAttribute("gno", dto.getGno());

    // 4. 수정이 완료되면, 수정한 글의 조회 페이지로 리다이렉트합니다.
    return "redirect:/guestbook/read";
  }

}