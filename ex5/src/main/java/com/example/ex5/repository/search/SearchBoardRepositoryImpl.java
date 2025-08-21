package com.example.ex5.repository.search;

import com.example.ex5.entity.Board;
import com.example.ex5.entity.QBoard;
import com.example.ex5.entity.QMember;
import com.example.ex5.entity.QReply;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport
    implements SearchBoardRepository {

  public SearchBoardRepositoryImpl() {super(Board.class);}


  @Override
  public Board searchTest(){
    log.info(">>>!!");
    QBoard qBoard = QBoard.board;
    QReply qReply = QReply.reply;
    QMember qMember = QMember.member;


    JPQLQuery<Board> jpqlQuery = from(qBoard);
    jpqlQuery.leftJoin(qMember).on(qBoard.writer.eq(qMember));
    jpqlQuery.leftJoin(qReply).on(qReply.board.eq(qBoard));

    jpqlQuery.select(qBoard, qMember, qReply.count()).where(qBoard.bno.eq(50L));
    log.info(jpqlQuery);
    List<Board> result = jpqlQuery.fetch();


    return null;
  }
}
