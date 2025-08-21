package com.example.ex5.repository.search;

import com.example.ex5.entity.Board;
import com.example.ex5.entity.QBoard;
import com.example.ex5.entity.QMember;
import com.example.ex5.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

  public SearchBoardRepositoryImpl() {
    super(Board.class);
  }



  @Override
  public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
    log.info("searchPage.............................");

    QBoard board = QBoard.board;
    QReply reply = QReply.reply;
    QMember member = QMember.member;

    // 1. 핵심 뼈대: JOIN과 SELECT, GROUP BY 설정 (searchTest에서 했던 내용)
    JPQLQuery<Board> jpqlQuery = from(board);
    jpqlQuery.leftJoin(member).on(board.writer.eq(member));
    jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

    JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());
    tuple.groupBy(board);

    // 2. 검색 조건 추가 (똑똑한 WHERE 기능의 심장!)
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    BooleanExpression expression = board.bno.gt(0L); // bno가 0보다 크다는 기본 조건
    booleanBuilder.and(expression);

    // type과 keyword가 비어있지 않은 경우에만 검색 조건 생성
    if (type != null && !type.trim().isEmpty() && keyword != null && !keyword.trim().isEmpty()) {
      String[] typeArr = type.split(""); // "tcw" 같은 문자열을 "t", "c", "w"로 쪼갬
      BooleanBuilder conditionBuilder = new BooleanBuilder();

      for (String t : typeArr) {
        switch (t) {
          case "t":
            conditionBuilder.or(board.title.contains(keyword));
            break;
          case "w":
            conditionBuilder.or(member.name.contains(keyword)); // 작성자 이름으로 검색
            break;
          case "c":
            conditionBuilder.or(board.content.contains(keyword));
            break;
        }
      }
      booleanBuilder.and(conditionBuilder);
    }

    tuple.where(booleanBuilder); // 완성된 검색 조건을 쿼리에 적용

    // 3. 정렬 기능 추가
    Sort sort = pageable.getSort();
    sort.stream().forEach(order -> {
      Order direction = order.isAscending() ? Order.ASC : Order.DESC;
      String prop = order.getProperty(); // 정렬 기준 컬럼 이름 (예: "bno")

      PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
      tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
    });

    // 4. 페이징 처리
    tuple.offset(pageable.getOffset()); // 몇 번째 데이터부터 가져올지
    tuple.limit(pageable.getPageSize());  // 한 페이지에 몇 개를 가져올지

    // 5. 쿼리 실행 및 결과 포장
    List<Tuple> result = tuple.fetch();
    log.info(result);

    long count = tuple.fetchCount(); // 검색 조건에 맞는 전체 데이터 개수
    log.info("COUNT: " + count);

    // Page<Object[]> 형태로 최종 결과를 만들어서 반환
    return new PageImpl<Object[]>(
        result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
        pageable,
        count
    );
  }
}