package com.example.ex3.repository;

import com.example.ex3.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

  // 쿼리 메서드 : 약속된 단어를 조합하여 메서드명만으로 질의가 가능.
  // 검색의 다양성을 확보하기 위함.
  List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

  Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

  void deleteMemoByMnoLessThan(Long num);


  //@Query : 복잡한 조건을 처리할 때 쿼리메서드로 불편하여 JPQL을 활용하여 개선
  //table명 대신에 entity 이름을 사용
  @Query("select m from Memo m order by m.mno desc")
  List<Memo> getListDesc();

  @Transactional
  @Modifying
  @Query("update Memo m set m.memoText = :memoText where m.mno = :mno ")
  int updateMemoText(@Param("mno") Long mno, String memoText);

  // JPQL 대신에 SQL을 활용할 경우 nativeQuery = true 적용
  @Query(value = "select = from tbl_memo where mno <= 20 ", nativeQuery = true)
  List<Memo> getNativeResult();
}