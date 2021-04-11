package com.company.mentoring_reviews.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.mentoring_reviews.service.MenPagingVO;
import com.company.mentoring_reviews.service.MenSearchCriteria;
import com.company.mentoring_reviews.service.Mentoring_ReviewsService;
import com.company.mentoring_reviews.service.Mentoring_ReviewsVO;

@Service
public class Mentoring_ReviewsImpl implements Mentoring_ReviewsService {

	@Autowired Mentoring_ReviewsMapper dao;
	
	@Override
	public List<Mentoring_ReviewsVO> selectBoard(MenPagingVO vo) {
		// TODO Auto-generated method stub
		return dao.selectBoard(vo);
	}

	@Override
	public List<Mentoring_ReviewsVO> list(MenSearchCriteria scri) {
		// TODO Auto-generated method stub
		return dao.list(scri);
	}

	@Override
	public List<Mentoring_ReviewsVO> searchMenService(String searchKeyword) {
		// TODO Auto-generated method stub
		return dao.searchMenService(searchKeyword);
	}

	@Override
	public int listCount(MenSearchCriteria scri) {
		// TODO Auto-generated method stub
		return dao.listCount(scri);
	}

	@Override
	public int countBoard() {
		// TODO Auto-generated method stub
		return dao.countBoard();
	}

	@Override
	public Mentoring_ReviewsVO getSearchMenReview(Mentoring_ReviewsVO vo) {
		// TODO Auto-generated method stub
		return dao.getSearchMenReview(vo);
	}

	@Override
	public int insertMenReview(Mentoring_ReviewsVO vo) {
		// TODO Auto-generated method stub
		return dao.insertMenReview(vo);
	}

	@Override
	public int updateMenReview(Mentoring_ReviewsVO vo) {
		// TODO Auto-generated method stub
		return dao.updateMenReview(vo);
	}

	@Override
	public int deleteMenReview(Mentoring_ReviewsVO vo) {
		// TODO Auto-generated method stub
		return dao.deleteMenReview(vo);
	}

	@Override
	public boolean upnumMenReview(Mentoring_ReviewsVO vo) {
		// TODO Auto-generated method stub
		return dao.upnumMenReview(vo);
	}

	@Override
	public Mentoring_ReviewsVO preNext(Mentoring_ReviewsVO vo) {
		// TODO Auto-generated method stub
		return dao.preNext(vo);
	}

}
