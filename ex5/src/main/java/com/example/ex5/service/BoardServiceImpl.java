package com.example.ex5.service;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.dto.PageResultDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.entity.Member;
import com.example.ex5.repository.BoardRepository;
import com.example.ex5.repository.MemberRepository; // MemberRepository 임포트
import com.example.ex5.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

import static com.example.ex5.entity.QBoard.board;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
  private final BoardRepository boardRepository;
  private final ReplyRepository replyRepository;
  private final MemberRepository memberRepository; // MemberRepository 의존성 주입 추가


  @Override
  public Long register(BoardDTO boardDTO) {

    Board board = dtoToEntity(boardDTO);

    Member member = memberRepository.findById(boardDTO.getWriterEmail())
        .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 회원이 존재하지 않습니다. " + boardDTO.getWriterEmail()));

    board.setWriter(member);
    boardRepository.save(board);
    return board.getBno();
  }
  // -----------------------------------------------------------

  @Override
  // PageResultDTO<DTO, EN> : EN으로 입력되면 DTO로 받는 구조
  public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
//    Page<Object[]> page = boardRepository
//        .getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno")
//            .descending())
//        );
    log.info("BoardService getList........" + pageRequestDTO);
    Function<Object[], BoardDTO> fn =
        (entity) -> entityToDto((Board) entity[0]
            , (Member) entity[1]
            , (Long) entity[2]);

    Page<Object[]> result = boardRepository.searchPage(
        pageRequestDTO.getType(),
        pageRequestDTO.getKeyword(),
        pageRequestDTO.getPageable(Sort.by("bno").descending())
    );

    return new PageResultDTO<>(result, fn);
  }

  @Override
  public BoardDTO get(Long bno, PageRequestDTO pageRequestDTO) {
    Object[] arr = (Object[]) boardRepository.getBroadByBno(bno);
    return entityToDto((Board) arr[0], (Member) arr[1], (Long) arr[2]);
  }

  @Transactional
  @Override
  public void removeWithReplies(Long bno) {
    replyRepository.deleteByBoard_Bno(bno);
    boardRepository.deleteById(bno);
  }

  @Override
  public void modify(BoardDTO boardDTO) {
    Optional<Board> result = boardRepository.findById(boardDTO.getBno());
    if (result.isPresent()) {
      Board board = result.get();
      board.changeTitle(boardDTO.getTitle());
      board.changeContent(boardDTO.getContent());
      boardRepository.save(board);
      log.info("modify board!!!!!!: " + board);
    }
  }

  // BoardService 인터페이스에 정의된 dtoToEntity, entityToDto 메소드가 있다면
  // 해당 메소드들도 여기에 구현되어 있어야 합니다.
  // 만약 인터페이스에 default 메소드로 구현되어 있다면,
  // dtoToEntity가 writer를 설정하지 않도록 확인해야 합니다.
}