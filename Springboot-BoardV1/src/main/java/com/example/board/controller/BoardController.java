package com.example.board.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.board.model.Board;
import com.example.board.repository.BoardRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class BoardController {
   //private BoardRepository boardRepository = new BosrdRepository();
   //자바에서는 직접 생성을 해주지만. 스프링에서는 다르다.직접 생성해 주지 않고 가지고만 있다.
   
   //스프링에서는 스프링 컨테이너가 생성해서 가지고 있다가
   //필요하면 의존성을 주입해서 가져온다.
   
   /**
    * 의존성 주입(DI)
    * 1. 필드 주입
    * 2. setter 주입
    * 3. 생성자 주입(제일 선호?)
    */
   
   //1. 필드 주입 방법
   @Autowired
   private BoardRepository boardRepository;
   
   //초기데이터 생성
   @PostConstruct
   public void initData() {
	   log.info("initData() 초기 데이터 생성");
	   boardRepository.saveBoard(new Board("제목1", "내용1" , "홍길동", "1234"));
	   boardRepository.saveBoard(new Board("제목2", "내용2" , "김개똥", "1234"));
	   boardRepository.saveBoard(new Board("제목3", "내용3" , "이말똥", "1234"));
   }
   //메인 페이지 이동
   @GetMapping("/")
   public String home() {
      log.info("home() 실행");
      return "redirect:/list";
   }
   
   // 게시글 작성 페이지 이동
   @GetMapping("write")
   public String wirteForm() {
      return "write"; //리턴 되는 부분의 HTML을 찾아온다
   }
   
   // 게시글 등록
   // 따로따로 받으려면 리퀘스트로 아니면 미리 만들어둔 보드 타입으로.
   // 객체를 받아오는 방법은 @ModelAttribute
   // 따로 받으려면 @RequestParam
   @PostMapping("write")
   public String write(@ModelAttribute Board board) {
      log.info("board: {}", board);
      // 받아온 보드 객체를 보드레퍼지토리에다가 넘겨준다.
      boardRepository.saveBoard(board);
      return "redirect:/";
   }
   
   // 게시글 목록 출력
   @GetMapping("list")
   public String list(Model model) {
      List<Board> findAllBoards = boardRepository.findAllBoards();
      model.addAttribute("boards", findAllBoards);
      return "list";
   }

   //게시글 읽기
   @GetMapping("read")
   public String read(@RequestParam Long id, Model model) {
	   Board board = boardRepository.findBoard(id);
	   board.addHit();
//	   board.setHit(board.getHit() + 1);
	   model.addAttribute("board", board);
	   return "read";
   }
   
   //게시글 수정 페이지 이동
   @GetMapping("update")
   public String updateForm(@RequestParam Long id,Model model) {
	   log.info("id:{}",id);
	   model.addAttribute("board", boardRepository.findBoard(id));
	   return "update";
   }
   
   //게시글 수정
   @PostMapping("update")
   public String update(@RequestParam Long id,@ModelAttribute Board updateBoard) {
	   log.info("id:{}",id);
	   log.info("updateBoard:{}",updateBoard);
	   //id 에 해당하는 게시글이 존재하고 패스워드가 일치하면 수정한다.
	   Board board = boardRepository.findBoard(id);
	   if(board != null && board.getPassword().equals(updateBoard.getPassword())) {		   
		   boardRepository.updateBoard(id, updateBoard);
	   return "redirect:/";
	   }else{
	   return "redirect:/update?id="+board.getId();
	   }
   }
   
   //게시글 삭제
   @PostMapping("delete")
   public String remove(@RequestParam Long id, @RequestParam String password) {
	   log.info("id: {},paaword: {}", id, password);
	   //id 에 해당하는 게시글이 있고 입력한 패스워드가 일치하면 게시글을 삭제한다.
	   Board board = boardRepository.findBoard(id);
	   if(board != null && board.getPassword().equals(password)) {		   
		   boardRepository.removeBoard(id);
	   }
	   return "redirect:/";
   }
   
}