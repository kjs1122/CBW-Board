package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@AllArgsConstructor
public class BoardServiceImpl implements BoardService{

	@Setter(onMethod_= @Autowired)
	private BoardMapper mapper;
	
	@Setter(onMethod_= @Autowired)
	private BoardAttachMapper attachMapper;
	

	@Override
	public void register(BoardVO board) {

		log.info("register.............");
		
		mapper.insertSelectKey(board);	
		
		if (board.getAttachList() == null|| board.getAttachList().size() <= 0){
			return;
		}
		board.getAttachList().forEach(attach ->{
			log.info(attach);
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
			
		});
	}

	@Override
	public BoardVO get(Long bno) {
		log.info("get.............");
		return mapper.read(bno);
	}

	@Transactional
	@Override
	public boolean modify(BoardVO board) {
		log.info("modify.............");
		attachMapper.deleteAll(board.getBno());
		
		boolean modifyResult = mapper.update(board) == 1;
		
		if(modifyResult && board.getAttachList() != null && board.getAttachList().size() > 0) {
			board.getAttachList().forEach(attach -> {
				attach.setBno(board.getBno());
				attachMapper.insert(attach);
			});
		}
		return modifyResult;
	}

	@Transactional
	@Override
	public boolean remove(Long bno) {
		log.info("delete.............");
		attachMapper.deleteAll(bno);
		return mapper.delete(bno) == 1;
	}

	@Override
	public List<BoardVO> getList(Criteria cri) {
		log.info("getList.............");
		return mapper.getListWithPaging(cri);
	}

	@Override
	public int getTotal(Criteria cri) {
		
		return mapper.getTotal(cri);
	}

	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		log.info("getAttachList by bno............." + bno);
		return attachMapper.findByBno(bno);
	}

}
