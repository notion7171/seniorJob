package com.company.temp;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.company.users.service.impl.UsersMapper;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	
	
	// ���� ����Ʈ(��ü�˻�)
	@RequestMapping("/mentorList")
	public String mentorList() {
		return "Mentor/mentorList";
	}
		
	@RequestMapping("/mypage")
	public String mypage() {
		return "mypage";
	}
	
	@RequestMapping("/seniorTimes")
	public String seniorTimes() {
		return "seniorTimes";		  	  //시니어 타임즈
	}
	
	@RequestMapping("/myWriteList")
	public String myWriteList() {
		return "myWriteList";		  		 //내 글 목록(게시글, 문의사항)
	}
	

	
	
	
	@RequestMapping("/businessPlanInsert")
	public String businessPlanInsert() {
		return "business/businessPlanInsert";
	}
	
	@RequestMapping("/businessPlanList")
	public String businessPlanList() {
		return "business/businessPlanList";
	}
	
	@RequestMapping("/mentoringStatus")
	public String mentoringStatus() {
		return "mypage/mentoringStatus";
	}
	
	@RequestMapping("/profileModification")
	public String profileModification() {
		return "mypage/profileModification";
	}
	
	@RequestMapping("/pwNotFound")
	public String pwNotFound() {
		return "pwNotFound";
	}
	
	@RequestMapping("/mypageHome")
	public String mypageHome() {
		return "mypage/mypageHome";
	}
	
	@RequestMapping("/payDetail")
	public String payDetail() {
		return "mypage/payDetail";
	}
	
	@RequestMapping("/mentoringCourse")
	public String mentoringCourse() {
		return "mypage/mentoringCourse";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/memberRegister")
	public String memberRegister() {
		return "memberRegister";
	}
	
}
