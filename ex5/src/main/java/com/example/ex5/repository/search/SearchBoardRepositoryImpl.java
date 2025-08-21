package com.example.ex5.repository.search;

import com.example.ex5.entity.Board;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport
    implements SearchBoardRepository {

  public SearchBoardRepositoryImpl(Class<?> domainClass) {
    super(domainClass);
  }

  @Override
  public Board searchTest(){
    log.info(">>>!!");
    return null;
  }
}
