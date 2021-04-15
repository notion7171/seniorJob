package com.company.cart.service.impl;

import java.util.List;

import com.company.cart.service.CartVO;

public interface CartMapper {
	//전체
	public List<CartVO> getSearchCartList(CartVO vo);
	//입력
	public int insertCart(CartVO vo);
	//삭제
	public void deleteCart(String no);
	//수정
	public int updateCart(CartVO vo);
	//단건
	public CartVO getCart(CartVO vo);
	
	public int countCart(CartVO vo);
	//전체삭제
	public int deleteCartAll(CartVO vo);
}