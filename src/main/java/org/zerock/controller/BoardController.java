package org.zerock.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
public class BoardController {

	@Autowired
	private BoardService service;
	
	@GetMapping("/list")
	public void list(Model model, Criteria cri) {
		
		log.info("list............" + cri);
		model.addAttribute("list", service.getList(cri));
		model.addAttribute("pageMaker", new PageDTO(cri, service.getTotal(cri)));
	}
	@GetMapping("/register")
	public void register() {
		
	}
	@PostMapping("/register")
	public String register(BoardVO board, RedirectAttributes rttr) {
		log.info("register............" + board);
		

		service.register(board);
		
		
		log.info("board : " + board.getBno());
		rttr.addFlashAttribute("result", board.getBno());
		return "redirect:/board/list";
	}
	
	@GetMapping({"/get","/modify"})
	public void get(@RequestParam("bno")Long bno, Model model, @ModelAttribute("cri")Criteria cri) {
		
		log.info("get or modify.................." + bno);
		model.addAttribute("board", service.get(bno));	
	}
	@PostMapping("/modify")
	public String modify(BoardVO board, RedirectAttributes rttr, @ModelAttribute("cri")Criteria cri) {
		
		log.info("modify................" + board);
		if(service.modify(board)) {
			rttr.addFlashAttribute("result", "success");

		}

		return "redirect:/board/list" + cri.getListLink();
	}
	
	@PostMapping("/remove")
	public String remove(@RequestParam("bno")Long bno, RedirectAttributes rttr, @ModelAttribute("cri")Criteria cri) {
		
		log.info("remove................" + bno);
		List<BoardAttachVO> attachList = service.getAttachList(bno);
		attachList.forEach(attach -> log.info(attach));
		if(service.remove(bno)) {
			deleteFiles(attachList);
			rttr.addFlashAttribute("result", "success");
		}

		return "redirect:/board/list" + cri.getListLink();
	}
	
	@GetMapping(value = "/getAttachList", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
		
		log.info("getAttachList" + bno);
		return new ResponseEntity<List<BoardAttachVO>>(service.getAttachList(bno), HttpStatus.OK);
	}
	
	private void deleteFiles(List<BoardAttachVO> attachList) {
		if(attachList == null || attachList.size() == 0) {
			return;
		}
		
		log.info("delete attach files...........");
		log.info(attachList);
		
		attachList.forEach(attach -> {
			try {
				Path file = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName());
				
				Files.deleteIfExists(file);
				
				if(Files.probeContentType(file).startsWith("image")) {
					Path thumbNail = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_" + attach.getFileName());
					Files.delete(thumbNail);		
				}
			} catch (Exception e) {
				log.error("delete file error" + e.getMessage());
			}
		});
		
		
	}
	
}
