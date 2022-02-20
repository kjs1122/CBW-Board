package org.zerock.mapper;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.service.BoardService;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardMapperTests {

	@Autowired
	private BoardMapper mapper;
	
	@Test
	public void getList() {
		Criteria cri = new Criteria();
		cri.setKeyword("새");	
		cri.setType("TCW");
		mapper.getListWithPaging(cri).forEach(board -> log.info(board));
	}
//	@Test
//	public void insert() {
//		BoardVO board = new BoardVO();
//		board.setTitle("������");
//		board.setWriter("���۰�");
//		board.setContent("����");
//		mapper.insertSelectKey(board);
//		log.info(board);
//	}
//	@Test
//	public void read() {
//		log.info(mapper.read(796101L));
//		
//	}
//	@Test
//	public void delete() {
//		log.info(mapper.delete(42L));
//	}
//	@Test
//	public void update() {
//		BoardVO board = new BoardVO();
//		board.setTitle("������");
//		board.setWriter("���۰�");
//		board.setContent("����");
//		board.setBno(43L);
//		mapper.update(board);
//		log.info(mapper.read(43L));
//		
//	}
//	@Autowired
//	private BoardService service;
//	@Test
//	public void testService() {
//		assertNotNull(service);
//	
//	
//	}
}
