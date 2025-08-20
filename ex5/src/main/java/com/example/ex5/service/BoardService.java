package com.example.ex5.service;


import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.dto.PageResultDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.entity.Member;
import com.example.ex5.entity.Reply;

public interface BoardService {
  default Board dtoToEntity(BoardDTO boardDTO) {
    return Board
        .builder()
        .bno(boardDTO.getBno())
        .title(boardDTO.getTitle())
        .content(boardDTO.getContent())
        .build();
  }

  default BoardDTO entityToDto(Board board, Member member, Long replyCount) {
    return BoardDTO
        .builder()
        .bno(board.getBno())
        .title(board.getTitle())
        .content(board.getContent())
        .regDate(board.getRegDate())
        .modDate(board.getModDate())
        .writerEmail(member.getEmail())
        .writerName(member.getName())
        .replyCount(replyCount.intValue())
        .build();
  }


  Long register(BoardDTO boardDTO);

  //PageResultDTO는 view로 갈때, 핵심되는 BoardDTO만 받고 나머진 Object[]로 처리
  PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

  BoardDTO get(Long bno, PageRequestDTO pageRequestDTO);//상세보기

  void removeWithReplies(Long bno); //삭제(댓글도 같이)

  void modify(BoardDTO boardDTO); //수정


}
