package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;
import com.example.ex4.entity.QGuestbook;
import com.example.ex4.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {

  private final GuestbookRepository guestbookRepository;

  @Override
  public Long register(GuestbookDTO guestbookDTO) {
    return guestbookRepository.save(dtoToEntity(guestbookDTO)).getGno();

  }

  @Override
  public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO) {
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("gno").descending());
    //검색 조건 처리
    BooleanBuilder booleanBuilder = getSearch(pageRequestDTO);
    //queryDSL 적용
    Page<Guestbook> page = guestbookRepository.findAll(booleanBuilder, pageable);
    Function<Guestbook, GuestbookDTO> fn = guestbook -> entityToDto(guestbook);
    return new PageResultDTO<>(page, fn);

  }

  private BooleanBuilder getSearch(PageRequestDTO pageRequestDTO) {
    String type = pageRequestDTO.getType();
    String keyword = pageRequestDTO.getKeyword();
    QGuestbook qGuestbook = QGuestbook.guestbook;
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    BooleanExpression booleanExpression = qGuestbook.gno.gt(0);
    booleanBuilder.and(booleanExpression);
    BooleanBuilder conditionBuilder = new BooleanBuilder();

    //isEmpty 문자열의 길이가 0이면 true 아니면 false
    if (type == null || type.trim().isEmpty()) return booleanBuilder; // 그대로 리턴
    //.length() == 0랑 isEmpty()는 같은 말(버전의 차이)
    //type.trim()으로 문자열의 앞뒤 공백을 제거한 후, 그 결과가 빈 문자열인지 확인
    if (keyword == null || keyword.trim().length() == 0) return booleanBuilder; // 그대로 리턴


    if (type.contains("t")) conditionBuilder.or(qGuestbook.title.contains(keyword));
    if (type.contains("c")) conditionBuilder.or(qGuestbook.content.contains(keyword));
    if (type.contains("w")) conditionBuilder.or(qGuestbook.writer.contains(keyword));
    booleanBuilder.and(conditionBuilder);
    return booleanBuilder;
  }

  @Override
  public GuestbookDTO read(Long gno) {
    // gno 를 이용해 데이터베이스에서 해당 게시물을 찾기
    Optional<Guestbook> result = guestbookRepository.findById(gno);

    // 결과가 존재하면 entityToDto로 변환해서 반환하고, 없으면 null을 반환.
    return result.isPresent() ? entityToDto(result.get()) : null;
  }

  @Override
  public void modify(GuestbookDTO dto) {
    // 1. 수정할 게시물의 gno를 이용해서, 데이터베이스에서 해당 엔티티를 찾아옵니다.
    Optional<Guestbook> result = guestbookRepository.findById(dto.getGno());

    // 2. 해당 게시물이 존재하는지 확인합니다.
    if(result.isPresent()) {
      // 3. 존재한다면, 엔티티 객체를 꺼냅니다.
      Guestbook entity = result.get();

      // 4. 엔티티의 제목과 내용만! DTO에 담겨온 새로운 값으로 변경합니다.
      entity.changeTitle(dto.getTitle());
      entity.changeContent(dto.getContent());

      // 5. 변경된 내용을 데이터베이스에 저장(UPDATE)합니다.
      guestbookRepository.save(entity);
    }
  }
}