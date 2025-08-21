package com.example.ex5.repository.search;

import com.example.ex5.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SearchBoardRepository {


  Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
