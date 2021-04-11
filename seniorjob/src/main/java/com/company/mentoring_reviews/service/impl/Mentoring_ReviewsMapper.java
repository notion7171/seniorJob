package com.company.mentoring_reviews.service.impl;

import java.util.List;

import com.company.mentoring_reviews.service.MenPagingVO;
import com.company.mentoring_reviews.service.Mentoring_ReviewsVO;
import com.company.mentoring_reviews.service.MenSearchCriteria;

public interface Mentoring_ReviewsMapper {
	
		// 페이징 처리 게시글 조회
		public List<Mentoring_ReviewsVO> selectBoard(MenPagingVO vo);

		// 페이징 처리 및 후기 전체 조회
		public List<Mentoring_ReviewsVO> list(MenSearchCriteria scri);

		// 게시글 검색
	    public List<Mentoring_ReviewsVO> searchMenService(String searchKeyword);		
				
		// 공지사항 게시물 총갯수
		public int listCount(MenSearchCriteria scri);
		
		// 게시물 총 갯수
		public int countBoard();
		
		// 후기 단건 조회
		public Mentoring_ReviewsVO getSearchMenReview(Mentoring_ReviewsVO vo);
		
		// 후기 등록
		public int insertMenReview(Mentoring_ReviewsVO vo);

		// 후기 수정
		public int updateMenReview(Mentoring_ReviewsVO vo);

		// 후기 삭제
		public int deleteMenReview(Mentoring_ReviewsVO vo);

		// 후기 조회수 증가
		public boolean upnumMenReview(Mentoring_ReviewsVO vo);
		
		// 후기 게시물 단건조회 후 이전/다음글
		public Mentoring_ReviewsVO preNext(Mentoring_ReviewsVO vo);

}
