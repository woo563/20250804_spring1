package com.example.ex4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

  // 1. int -> Integer 로 변경 (가장 중요!)
  private Integer page;
  private Integer size;
  private String type;
  private String keyword;

  public PageRequestDTO() {
    this.page = 1;
    this.size = 10;
  }

  // 2. getPage(), getSize() 메소드를 추가하여 null일 때 기본값을 반환하도록 수정
  public int getPage() {
    // page가 null이거나 0 이하면 1을 반환, 아니면 원래 page 값을 반환
    return (this.page == null || this.page <= 0) ? 1 : this.page;
  }

  public int getSize() {
    // size가 null이거나 비정상적인 값이면 10을 반환, 아니면 원래 size 값을 반환
    return (this.size == null || this.size <= 0 || this.size > 100) ? 10 : this.size;
  }


  public Pageable getPageable(Sort sort) {
    // getPage()를 호출하여 안전하게 값을 가져옴.
    // 페이지 번호는 0부터 시작하므로 1을 빼줍니다.
    return PageRequest.of(getPage() - 1, getSize(), sort);
  }
}