package com.example.ex2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

  @RequestMapping("/join")
  public void join() {

  }
  @RequestMapping("/loginPrc")
  public String login() {
    return "loginPrc";
  }
}
