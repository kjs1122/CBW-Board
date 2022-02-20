package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.ReplyMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyMapper mapper;
	@Autowired
	private BoardMapper boardMapper;
	
	@Transactional
	@Override
	public int register(ReplyVO vo) {
		log.info("register........................");
		
		boardMapper.updateReplyCnt(vo.getBno(), 1);
		return mapper.insert(vo);
	}

	@Override
	public ReplyVO get(Long rno) {
		log.info("get........................" + rno);
		return mapper.read(rno);
	}

	@Transactional
	@Override
	public int remove(Long rno) {
		log.info("remove........................" + rno);
		
		boardMapper.updateReplyCnt(mapper.read(rno).getBno(), -1);
		return mapper.delete(rno);
	}

	@Override
	public int modify(ReplyVO vo) {
		log.info("modify........................");
		return mapper.update(vo);
	}

	@Override
	public List<ReplyVO> getList(Criteria cri, Long bno) {
		log.info("modify........................" + cri + " " + bno);
		return mapper.getListWithPaging(cri, bno);
	}

	@Override
	public ReplyPageDTO getListPage(Criteria cri, Long bno) {
		
		return new ReplyPageDTO(mapper.getCountByBno(bno), mapper.getListWithPaging(cri, bno));
	}

}
