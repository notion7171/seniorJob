package com.company.shopping.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.shopping.service.ShoppingService;
import com.company.shopping.service.ShoppingVO;

@Service
public class ShoppingServiceImpl implements ShoppingService {

	@Autowired ShoppingMapper dao;
	
	@Override
	public int insertShopping(ShoppingVO vo) {
		return dao.insertShopping(vo);
	}

	@Override
	public int updateShopping(ShoppingVO vo) {
		return dao.updateShopping(vo);
	}

	@Override
	public int deleteShopping(ShoppingVO vo) {
		return dao.deleteShopping(vo);
	}

	@Override
	public ShoppingVO getShopping(ShoppingVO vo) {
		return dao.getShopping(vo);
	}

	
	//양소민 추가
	
	@Override
	public List<ShoppingVO> getSearchShopping(ShoppingVO vo) { //마이페이지_수강중인 멘토링
		return dao.getSearchShopping(vo);
	}

	@Override
	public List<ShoppingVO> getSearchPay(ShoppingVO vo) { //마이페이지_결제내역
		return dao.getSearchPay(vo);
	}


}
