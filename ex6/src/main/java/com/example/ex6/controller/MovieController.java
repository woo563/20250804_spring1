package com.example.ex6.controller;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {
  private final MovieService movieService;

  @GetMapping("register")
  public void register(){}

  @PostMapping("/register")
  public String register(MovieDTO movieDTO, RedirectAttributes ra) {
    Long mno = movieService.register(movieDTO);
    ra.addFlashAttribute("msg", mno);
    return "redirect:/movie/list";
  }

  @GetMapping({"", "/", "/list"})
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    model.addAttribute("pageResultDTO", movieService.getList(pageRequestDTO));
    return "/movie/list";
  }

}
