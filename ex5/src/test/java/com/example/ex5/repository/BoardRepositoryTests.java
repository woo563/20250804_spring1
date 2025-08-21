package com.example.ex5.repository;

import com.example.ex5.entity.Board;
import com.example.ex5.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardRepositoryTests {
  @Autowired
  private BoardRepository boardRepository;

  @Test
  public void insertBoard() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      Board board = Board
          .builder()
          .title("Title..." + i)
          .content("Content..." + i)
          .writer(Member.builder().email("user" + i + "@a.a").build())
          .build();
      boardRepository.save(board);
    });
  }

  @Transactional
  @Test
  public void testRead1() {
    Optional<Board> result = boardRepository.findById(100L);
    Board board = result.get();
    System.out.println(board);
    System.out.println(board.getWriter());
  }

  @Test
  public void testReadWithWriter() {
    Object result = boardRepository.getBoardWithWriter(100L);
    Object[] arr = (Object[]) result;
    System.out.println(Arrays.toString(arr));
  }

  @Test
  public void testReadWithReply() {
    List<Object[]> result = boardRepository.getBoardWithReply(100L);
    for (Object[] arr : result) {
      System.out.println(Arrays.toString(arr));
    }
  }

  @Test
  public void testWithReplyCount() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
    Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);
    result.get().forEach(row -> {
      Object[] arr = (Object[]) row;
      System.out.println(Arrays.toString(arr));
    });
  }

  @Test
  public void testGetBoardByBno() {
    Object result = boardRepository.getBroadByBno(100L);
    Object[] arr = (Object[]) result;
    System.out.println(Arrays.toString(arr));

  }

  @Test
  public void testSearchtest() {
    Board b = boardRepository.searchTest();
    System.out.println(b);
  }
}