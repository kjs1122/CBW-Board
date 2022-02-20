package org.zerock.mapper;

import static org.junit.Assert.assertNotNull;

import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;
import org.zerock.service.BoardService;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyMapperTests {

	private Long[] bnoArr = {46L, 47L, 48L, 49L, 50L};
	@Autowired
	private ReplyMapper mapper;
	
	@Test
	public void test() {
//		ReplyVO vo = new ReplyVO();
//		vo.setReply("2323");
//		vo.setRno(2L);
//		
//		mapper.update(vo);
		
		log.info(mapper.getCountByBno(46L));
	}
}
