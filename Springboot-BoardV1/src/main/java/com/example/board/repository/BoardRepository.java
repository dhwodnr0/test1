package com.example.board.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.example.board.model.Board;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BoardRepository {
   private Map<Long, Board> boards = new HashMap<>();
   private Long sequence = 0L;
   
   
   public BoardRepository() {
      log.info("BoardRepository() 생성");
   }
   //게시글 등록
   public void saveBoard(Board board) {
      board.setId(++sequence);
      boards.put(board.getId(), board);      
   }
   
   //게시글 검색
   public Board findBoard(Long id) {
      Board board = boards.get(id);
      return board;
   }
   
   //게시글 수정
   public void updateBoard(Long id, Board updateBoard) {
      Board board = boards.get(id);
      if(board != null) {
         board.setTitle(updateBoard.getTitle());
         board.setContents(updateBoard.getContents());
         board.setUsername(updateBoard.getUsername());
      }
   }
   
   //게시글 삭제
   public void removeBoard(Long id) {
      boards.remove(id);
   }
   
   //게시글 전체 목록
   public List<Board> findAllBoards(){
      List<Board> list = new ArrayList<>();
      list.addAll(boards.values());
      return list;
   }
}