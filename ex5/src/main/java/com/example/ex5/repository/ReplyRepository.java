package com.example.ex5.repository;

import com.example.ex5.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

  // 쿼리어노테이션은 직접 sql로 변환하므로 효율적인 자원
  @Modifying // deleteByBno가 DML임을 스크링에 알려주는 역할
  @Query("delete from Reply r where r.board.bno = :bno ")
  void deleteByBoard_Bno(@Param("bno") Long bno);

  //쿼리메서드는 내부 최적화가 되어있으나, 대량 데이터일 경우 성능 저하 발생?
//  void deleteByBoard_Bno(Long bno);
}
