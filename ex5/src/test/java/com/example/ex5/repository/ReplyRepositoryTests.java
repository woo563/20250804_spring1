package com.example.ex5.repository;

import com.example.ex5.entity.Board;
import com.example.ex5.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyRepositoryTests {
  @Autowired
  private ReplyRepository replyRepository;

  @Test
  public void insertReply() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      Long bno = (long) (Math.random() * 100) + 1;
      Long mSeq = (long) (Math.random() * 100) + 1;
      String email = "user" + mSeq + "@a.a";
      Reply reply = Reply
          .builder()
          .text("Reply..."+ i)
          .board(Board.builder().bno(bno).build())
          .commenter(email)
          .build();
      replyRepository.save(reply);
    });
  }

  @Test
  public void readReply1() {
    Optional<Reply> result = replyRepository.findById(1L);
    Reply reply = result.get();
    System.out.println(reply);
    System.out.println(reply.getBoard());
  }
}