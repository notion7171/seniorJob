package com.company.businesspalna.controller;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.company.businesspalna.service.BusinessPlanAService;
import com.company.businesspalna.service.impl.BusinessPlanAMapper;
import com.company.mentor.service.MentorService;
import com.company.mentor.service.MentorVO;
import com.company.mentoring.service.MentoringVO;
import com.company.portfolio.service.FileRenamePolicy;
import com.company.businesspalna.service.BusinessPalnAVO;

//사업계획서 조회, 첨삭
@Controller
public class BusinessPlanAController {

	@Autowired BusinessPlanAService bpService;
	@Autowired MentorService mtService;
	@Autowired BusinessPlanAMapper bpMapper;

	
	@RequestMapping("/testmyhome")			
	public String testmyhome() {
		return "mypage/testmyhome";
	}
	
	
	@GetMapping("/getSearchBusinessPlanA") //사업계획서 리스트 
	public String getSearchBusinessPlanA(BusinessPalnAVO vo, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		vo.setId(id);
		model.addAttribute("list", bpService.getSearchBusinessPlanA(vo));
		return "business/getSearchBusinessPlanA";
	}
	
	//사업계획서 목록에서 사업계획서 제목을 클릭할 경우, 그 사업계획서 상세조회
	@RequestMapping("/getBusinessPlanA")			
	public String getBusinessPlanA(BusinessPalnAVO vo, Model model) {
		bpService.getBusinessPlanA(vo); //사업계획서 제목을 클릭할 때 사업계획서 번호가 vo에 설정되고, 그 번호를 기준으로 사업계획서 단건 조회
		model.addAttribute("bpp", vo); //조회한 결과값을 모델에 담음.
		return "business/getBusinessPlanA";
	}
	
	
	@GetMapping("/insertBusinessPlanA") //사업계획서 등록 페이지로 이동
	public String insertBusinessPlanA(BusinessPalnAVO vo) {
		return "business/insertBusinessPlanA";
	}
	
	@PostMapping("/insertBusinessPlanA") //사업계획서 등록 처리
	public String insertBusinessPlanAProc(BusinessPalnAVO vo, HttpServletRequest request, Model model ) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id"); //login할 때 session에 저장해둔 id 값을 꺼내씀.
		int seq = bpService.getSeq();
		vo.setId(id);
		vo.setSeq(seq);
		bpService.insertBusinessPlanA(vo);
		
		if(vo.getPhidden() != null) {
			bpService.insertBusinessPlanB(vo);
		};
		if(vo.getMhidden() != null) {
			bpService.insertBusinessPlanC(vo);
		};
		if(vo.getShidden() != null) {
			bpService.insertBusinessPlanD(vo);
		};
		
		model.addAttribute("msg", "등록완료");
		model.addAttribute("url", "throughCerti");
		
		return "common/Success";
	}
	
	@GetMapping("/updateBusinessPlanA")			//수정페이지로
	public String updateBusinessPlanA(BusinessPalnAVO vo, Model model) {
		bpService.getBusinessPlanA(vo);
		model.addAttribute("bpp", vo);
		return "/business/updateBusinessPlanA";
	}

	@PostMapping("/updateBusinessPlanA")			//수정처리
	public String updateBusinessPlanAProc(BusinessPalnAVO vo, HttpServletRequest request ) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		vo.setId(id);
		bpService.updateBusinessPlanA(vo);
		
		if("1".equals(vo.getPhidden())) {
			bpService.updateBusinessPlanB(vo);
		};
		if("1".equals(vo.getMhidden())) {
			bpService.updateBusinessPlanC(vo);
		};
		if("1".equals(vo.getShidden())) {
			bpService.updateBusinessPlanD(vo);
		};
		return "redirect:/getSearchBusinessPlanA";
	}

	@RequestMapping("/deleteBusinessPlanA")			//삭제처리
	public String deleteBusinessPlanA(BusinessPalnAVO vo) {
		bpService.deleteBusinessPlanA(vo);
		return "redirect:/getSearchBusinessPlanA";
	}
	
	@GetMapping("/printBusinessPlanA")			//인쇄
	public String printBusinessPlanA(BusinessPalnAVO vo, Model model) {
		bpService.getBusinessPlanA(vo);
		model.addAttribute("bp", vo);
		return "business/printBusinessPlanA";
	}
	
	
	@RequestMapping("/getBpWord") //word 다운 수정중
	public String wordView(Model model, BusinessPalnAVO vo) {
		model.addAttribute("bp", bpService.getBusinessPlanA(vo));
		return "wordView";
	}
	

	@RequestMapping("/getBpExcel") //엑셀 다운
	public ModelAndView getBpExcel(BusinessPalnAVO vo, HttpServletResponse response) throws IOException{
		List<Map<String, Object>> list = bpService.getBpExcel(vo);
		
		System.out.println("excel:"+bpService.getBpExcel(vo));
		HashMap<String, Object> map = new HashMap<String, Object>(); 
		String[] header = {"TITLE","BUSINESS_A","BUSINESS_B","BUSINESS_C","PROD_A","PROD_B","PROD_C","MARKET_A","MARKET_B","MARKET_C","MARKET_D","SELLING_A","SELLING_B","SELLING_C"};
		map.put("headers", header);
		map.put("filename", "excel_dept");
		map.put("datas", list);
		return new ModelAndView("commonExcelView", map);
	}

	//첨삭 요청 창으로 이동
	@GetMapping("/checkBp")
	public String checkBp(BusinessPalnAVO vo, Model model) {
		bpService.getBusinessPlanA(vo);
		model.addAttribute("bpp", vo);
		return "/business/checkBusinessPlan";
	}
	
	//첨삭요청 팝업창
	@GetMapping("/checkBpForm")
	public String checkBpForm(MentoringVO vo, Model model, HttpServletRequest request, String seq) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		vo.setMenteeId(id);
		model.addAttribute("ck", bpService.ckMenName(vo));
		model.addAttribute("seq", seq);
		return "/business/checkBpForm";
	};
	
	
	//첨삭요청하면 사업계획서에 멘토아이디 추가
	@PostMapping("/ckUpdate")
	public String ckUpdate(BusinessPalnAVO vo) {
		System.out.println("vo:"+vo);
		bpService.ckUpdate(vo);
		return "/users/throughCerti";
	}
	
	//첨삭요청목록
	@GetMapping("/checkP")
	public String checkP(BusinessPalnAVO vo, MentorVO mvo, HttpServletRequest request, Model model ) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		mvo.setId(id);
		String mId = mtService.getMentorId(mvo);
		vo.setMentorId(mId);
		model.addAttribute("list", bpService.checkP(vo));
		return "/Mentor/checkP";
	}
	
	@GetMapping("/checkBusinessPlan") //첨삭 페이지로 이동
	public String checkBusinessPlan(BusinessPalnAVO vo, Model model) {
		bpService.getBusinessPlanA(vo);
		model.addAttribute("bpp", vo);
		return "business/checkBusinessPlan";
	}
	
	@PostMapping("/collectionUpdate")			//첨삭입력
	public String collectionUpdate(BusinessPalnAVO vo) {
		bpService.collectionUpdate(vo);
		
		return "mypage/mypageHome";
	}
	
	//멘티가 사업계획서 리스트에서 '첨삭보기'버튼을 누를 시
	@GetMapping("/seeCkBp") 
	public String seeCkBp(BusinessPalnAVO vo, Model model) {
		bpService.getBusinessPlanA(vo);
		String col = bpMapper.getCollection(vo); // 멘토가 첨삭한 내용이 들어있는 html파일의 이름을 조회.
 		model.addAttribute("bpp", vo);
		model.addAttribute("col", col);
		return "business/seeCkBp";
	}
	
	@RequestMapping("/previewBusiness") //미리보기
	public String previewBusiness() {
		return "business/previewBusiness";
		
	}
	
	@RequestMapping("/checkPBadge")
	@ResponseBody
	public String checkPBadge(BusinessPalnAVO vo, MentorVO mvo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		mvo.setId(id);
		String mId = mtService.getMentorId(mvo);
		vo.setMentorId(mId);
		String cpBadge = bpMapper.checkPBadge(vo);
		
		return cpBadge;
	}
	
	@RequestMapping("/checkRBadge")//<!-- 수정 -->
	@ResponseBody
	public String checkRBadge(BusinessPalnAVO vo, MentorVO mvo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		mvo.setId(id);
		String mId = mtService.getMentorId(mvo);
		vo.setMentorId(mId);
		String crBadge = bpMapper.checkRBadge(vo);
		System.out.println("crBadge:"+crBadge);
		return crBadge;
	}
	
	// 멘토가 사업계획서 첨삭한 내용(views/business/checkBusinessPlan.jsp)을 html 파일로 저장
	@RequestMapping("/htmlSaveSom")
	public String htmlSaveSom(@RequestParam(value="contents") String contents   //ajax로 넘어온 param contents를 String contents 변수로 받아줌
							, @RequestParam(value="seq") int seq				//ajax로 넘어온 param seq를 int seq로 받아줌.
							, Model model										//model 객체 생성
							, BusinessPalnAVO vo								//BusinessPalnAVO 객체 생성
							, HttpServletRequest req) 							//HttpServletRequest 객체 생성
									throws IOException {						//예외처리
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");		//포맷형식을 "yyyyMMddHHmmss"형태로 정해줌.
		Date time = new Date();													//날짜 객체 생성.
		String filename = format.format(time);									//생성한 날짜를 위에서 지정한 포맷형식에 맞게 변환해서 filename으로 사용.

		
		if (contents != null ||! "".equals(contents)) {							//contents가 null이거나 공백이 아니라면 실행
			//String -> file
			File file = new File(filename+".html");								//html 확장자명을 가진 file 생성
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));   //생성된 file에 contents 입력하고 fileDescriptor 닫아주기
			writer.write(contents);
			writer.close();
			
			//file -> MultiPartFile
			FileInputStream input = new FileInputStream(file);
			MultipartFile multipartFile = new MockMultipartFile("file", 						//name
																file.getName(), 				//originalFileName
																"text/html",					//contentType 
																IOUtils.toByteArray(input));	//content
			//파일 저장소 경로 확인
			String path = req.getServletContext().getRealPath("resources/upload");
			System.out.println("htmlSaveSom:"+path);
			
			//새로운 파일 이름
			String newFname = filename+seq+".html";
			//저장소에 파일 저장
			multipartFile.transferTo(new File(path, newFname));
			
			vo.setCollection(newFname);			//BusinessPlanAVO의 collection에 파일이름저장
			vo.setSeq(seq);						//BusinessPlanAVO의 seq에 사업계획서 번호 저장
			
			bpService.collectionUpdate(vo);     //BusinessPlanA 테이블 update함.
			
			model.addAttribute("msg", "첨삭을 완료하였습니다.");			//알림 창에 보여질 메시지
			model.addAttribute("url", "throughCerti");				//최종 이동할 페이지(현재 팝업 창 닫아주고, 부모창 reload하는 페이지로 이동)
			return "common/Success";								//거쳐가는 페이지
			
		}
		 else{
				 model.addAttribute("msg", "파일이 없습니다.");			//contents가 null일 경우 반환되는 메시지
				 return "common/Fail";
		 }
		
	}
	
	

}
