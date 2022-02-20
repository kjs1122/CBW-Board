package org.zerock.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.context.annotation.Conditional;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.SampleVO;
import org.zerock.domain.Ticket;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/sample")
@Log4j
public class SampleController {


	@GetMapping("/all")
	public void doAll() {
		log.info("do all can access everrbody");
	}
	@GetMapping("/member")
	public void doMember() {
		log.info("logined member");
	}
	@GetMapping("/admin")
	public void doAdmin() {
		log.info("admin only");
	}
}
