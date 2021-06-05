package com.company.schedule.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.mentor.service.MentorService;
import com.company.mentor.service.MentorVO;
import com.company.schedule.service.ScheduleService;
import com.company.schedule.service.ScheduleVO;
import com.company.schedule.service.impl.ScheduleMapper;

@Controller
public class ScheduleController {
	
	@Autowired ScheduleService scService;
	@Autowired MentorService mtService;
	@Autowired ScheduleMapper scMapper;
	
	
	//마이홈페이지에 있는 calendar에서 일정이 보이는데, mentor와 mentee가 일정을 공유. 
	@RequestMapping("/getSearchSchedule")
	@ResponseBody
	public List<Map<String, String>> getSearchSchedule(ScheduleVO vo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String auth = (String) session.getAttribute("auth");  //로그인 시 session에 저장된 auth(권한)
		vo.setAuth(auth);
		String id = (String)session.getAttribute("id");
			vo.setMenteeid(id);
			vo.setMentorid(id);
		List<Map<String, String>> list = scService.getSearchSchedule(vo);
		return list;
	
	}
	
	
	//멘토가 보낸 요청을 멘티의 마이페이지 홈에서 확인.
	@RequestMapping("/getSearchRequest")
	@ResponseBody
	public List<Map> getSearchRequest(ScheduleVO vo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		vo.setMenteeid(id);
		List<Map> list = scService.getSearchRequest(vo);
		return list;
	
	}
	
	//멘토가 멘티에게 멘토링 요청
	@RequestMapping("/insertSchedule") 
	public String insertScheduleProc(ScheduleVO vo, Model model) {
		System.out.println("insertSchedule:" + vo);
		model.addAttribute("msg", "요청이 완료되었습니다.");
		model.addAttribute("url", "throughCerti");
		scService.insertSchedule(vo);
		return "common/Success";
	}
	
	
	//멘티가 멘토의 요청 승낙
	@RequestMapping("/updateSchedule")
	public String updateSchedule(String seq, ScheduleVO vo, String resume_no, Model model) {
		vo.setSeq(seq);
		vo.setResume_no(resume_no);
		System.out.println("updateSchedule:"+vo);
		scService.updateSchedule(vo);
		model.addAttribute("msg", "요청이 승낙되었습니다.");
		model.addAttribute("url", "throughCerti");
		return "common/Success";
		
	}
	
	
	
	//멘티가 멘토의 요청 거절
		@RequestMapping("/updateScheduleReject")
		public String updateScheduleReject(String seq, ScheduleVO vo) {
			vo.setSeq(seq);
			scService.updateScheduleReject(vo);
			return "mypage/mypageHome";
			
		}
	
	//질문등록
	@GetMapping("/insertQuest")
	public String insertQuestForm(ScheduleVO vo, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		vo.setMenteeid(id);
		model.addAttribute("vo", vo);
		return "schedule/insertQuest";
		
	}
		
	@PostMapping("/insertQuest")
	public String insertQuest(ScheduleVO vo, Model model) {
		scService.insertQuest(vo);
		model.addAttribute("msg", "질문등록완료");
		model.addAttribute("url", "throughCerti");
		return "common/Success";
	}

	
	
	
	//멘토가 받은 질문 확인
	@RequestMapping("/getSearchQuest")
	@ResponseBody
	public List<Map> getSearchQuest(MentorVO mVo, ScheduleVO vo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		mVo.setId(id);
		String mId = mtService.getMentorId(mVo);
		vo.setMentorid(mId);
		List<Map> list = scService.getSearchQuest(vo);
		return list;
	}
	
	//멘토가 질문에 답변하는 창으로 이동
	
	@GetMapping("/updateQuest")
	public String updateQuestForm(ScheduleVO vo, Model model) {
		model.addAttribute("list", scService.getQuest(vo));
		
		return "schedule/updateQuest";
		
	}
	
	
	//멘토가 질문에 답변
	@PostMapping("/updateQuest")
	public String updateQuest(String seq, ScheduleVO vo) {
		vo.setSeq(seq);
		scService.updateQuest(vo);
		return "mypage/mypageHome";
		
	}
	
	//답변 목록 확인 창으로 이동
	@GetMapping("/replyQuestFormSom")
	public String replyQuestFormSom(ScheduleVO vo, Model model,HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		vo.setMenteeid(id);
		model.addAttribute("list", scService.replyQuestFormSom(vo));
		return "mypage/replyQuestFormSom";
	}
	
	//답변목록뱃지
	@RequestMapping("/replyQuestBadge")
	@ResponseBody
	public String replyQuestBadge(ScheduleVO vo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		vo.setMenteeid(id);
		String requBadge = scMapper.replyQuestBadge(vo);
		return requBadge;
	}
	
	//답변 상세보기
	@GetMapping("/replyDetailSom")
	public String replyDetailSom(ScheduleVO vo, Model model, MentorVO mVo) {
		ScheduleVO quest = scService.getQuest(vo);
		model.addAttribute("vo",quest);
		String menId = quest.getMentorid();
		String menName = mtService.getMentorName(menId);
		model.addAttribute("menName", menName);
		return "mypage/replyDetailSom";
		
	}
	
	// 멘토가 날짜와 시간을 정해 '모의영상면접' 요청을 하고, 멘티가 그 요청을 승낙하면
 	// 그 날짜와 시간에만 '멘토링 기능(영상통화,채팅,이력서보기)'버튼이 활성화된다.
	@RequestMapping("/videoCallButton")
	@ResponseBody
	public String videoCallButton(HttpServletRequest request, ScheduleVO vo) {
		//오늘 날짜 구하기
		Date date = new Date();
		SimpleDateFormat dF = new SimpleDateFormat("yyyyMMddHHmmss");
		String today = dF.format(date);
		Long inToday = Long.parseLong(today);
		
		//로그인한 아이디를 mentorId와 menteeId에 저장
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		vo.setMenteeid(id);
		vo.setMentorid(id);
		
		//id를 통회 스케줄 조회
		List<Map> list = scService.getSearchMentoringDate(vo);
		for(Map<String, Object> map:list) {
			
			for(String mapKey : map.keySet()) {
				String start = (String) map.get("SCHEDULE_START");  //일정시작시점
				String end = (String) map.get("SCHEDULE_END"); //일정끝지점
				String resume_no = (String) map.get("RESUME_NO"); //이력서번호
				Long startInt = Long.parseLong(start);
				Long endInt = Long.parseLong(end);
				// 오늘 날짜가 '일정시작지점'과 '일정끝지점' 사이에 있다면 '이력서번호'를 반환
				if(startInt <= inToday) {
					  
					  if(inToday <= endInt) {
						  return resume_no;
					  } else {continue;}
					  
					  
				  } else {return "false";}
			}
			  
			 
			
			
		} //end of for
		return "false";
		
		
	}
}
