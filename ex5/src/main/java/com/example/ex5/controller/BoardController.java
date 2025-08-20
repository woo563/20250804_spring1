package com.example.ex5.controller;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {
  private final BoardService boardService;
  @GetMapping({"","/","list"})
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    log.info("board/list..." + pageRequestDTO);
    model.addAttribute("pageResultDTO", boardService.getList(pageRequestDTO));
    return "/board/list";
  }

  @GetMapping("/register")
  public void register() {
  }

  @PostMapping("/register")
  public String registerPost(BoardDTO boardDTO, RedirectAttributes ra) {
    Long result = boardService.register(boardDTO);
    log.info(">>" + result + " 번 글이 등록되었습니다.");
    ra.addFlashAttribute("registerMsg", result); // RedirectAttributes 일회성, 최종 view단까지 보냄
    return "redirect:/board/list";
  }

  @GetMapping({"/read", "/modify"})
  public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
    log.info(">>bno:" + bno);
    log.info(">>pageRequestDTO:" + pageRequestDTO);
    model.addAttribute("boardDTO", boardService.get(bno, pageRequestDTO));
  }

  @PostMapping("/modify")
  public String modify(BoardDTO boardDTO,
                       PageRequestDTO pageRequestDTO, RedirectAttributes ra) {
    boardService.modify(boardDTO);
    ra.addFlashAttribute("msg", boardDTO.getBno());
    ra.addAttribute("bno", boardDTO.getBno());
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/board/read";  // 수정후 상세보기페이지로 이동
  }

  // 실제 삭제할 때(삭제 페이지 이동은 없음)
  @PostMapping("/removeWithReplies")
  public String remove(Long bno,
                       PageRequestDTO pageRequestDTO, RedirectAttributes ra) {
    // 실제 삭제처리
    boardService.removeWithReplies(bno);

    // 지우는 페이지에 목록 개수가 하나일 때 다음페이지로 보냄
    // 목록 가져와서 좋은 코드 아님. 페이지 목록 개수는 pageRequestDTO에 별도 저장 필요
    if (boardService.getList(pageRequestDTO).getDtoList().size() == 0
        && pageRequestDTO.getPage() != 1) {
      pageRequestDTO.setPage(pageRequestDTO.getPage() - 1);
    }

    typeKeywordInit(pageRequestDTO);  //null 문자열을 삭제처리
    ra.addFlashAttribute("removeMsg", bno); //일회성
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/board/list";

  }
  private void typeKeywordInit(PageRequestDTO pageRequestDTO){
    if (pageRequestDTO.getType().equals("null")) pageRequestDTO.setType("");
    if (pageRequestDTO.getKeyword().equals("null")) pageRequestDTO.setKeyword("");
  }

}
