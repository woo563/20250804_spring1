package com.example.ex4.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
// list.html 페이지에서 사용할 모든 정보를 담은 객체
public class PageResultDTO<DTO, EN> {
  private List<DTO> dtoList;// 해당 페이지의 목록
  private int totalPage;// 10개씩 했을 때 총 페이지 수
  private int page;// 요청한 페이지 번호
  private int size;//한 페이지당 나올 갯수
  private int start, end;//페이지네이션의 시작번호, 끝 번호
  private boolean prev, next;// 다음, 이전 버튼에 대한 정보
  private List<Integer> pageList;// 페이지 번호 목록 :: pageList

  // Page는 repository에서 가져오고, Function은 Entity를 DTO로 변환함.
  public PageResultDTO(Page<EN> page, Function<EN, DTO> fn) {
    dtoList = page.stream().map(fn).collect(Collectors.toList());
    totalPage = page.getTotalPages();
    makePageInfo(page.getPageable());
  }

  private void makePageInfo(Pageable pageable) {
    page = pageable.getPageNumber() + 1;
    size = pageable.getPageSize();
    //페이지네이션의 시작 번호
    int tempEnd = (int)(Math.ceil(page / 10.0)) * 10;
    start = tempEnd - 9;
    //페이지네이션의 끝 번호
    //계산상 끝 번호와 실제 끝 번호를 비교해서, 절대 실제 끝 번호를 넘지 못하게
    end = Math.min(totalPage, tempEnd); //end = tempEnd < totalPage ? tempEnd : totalPage; 같은 결과 나옴
    prev = page > 1;
    next = page < totalPage;
    pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());


  }
}