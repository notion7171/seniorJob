package com.company.users.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.company.users.service.PwValidation;
import com.company.api.common.Kakaoapi;
import com.company.portfolio.service.FileRenamePolicy;
import com.company.users.service.UsersService;
import com.company.users.service.UsersVO;
import com.company.users.service.UsersValidation;
import com.company.users.service.impl.UsersMapper;

@Controller
public class UsersController {

	@Autowired
	UsersService usersService;
	@Autowired
	UsersMapper usersMapper;
	@Autowired
	Kakaoapi kakaoapi;

	@Inject
	BCryptPasswordEncoder pwdEncoder;
	

	@RequestMapping("/getUsersList") // 전체조회
	public String userstest(Model model) {
		model.addAttribute("list", usersMapper.getUsersList());
		return "/users/getUsersList";
	}

	@RequestMapping("/insertUsers") // 회원가입 폼
	public String insertInquire(UsersVO vo) {
		return "memberRegister";
	}

	@RequestMapping("/insertUsersProc") // 회원가입 처리, 중복체크, 암호화, 유효성검사
	public String insertUsersProc(@Validated UsersVO vo, BindingResult bresult, Model model) {
		if (bresult.hasErrors()) {
			return "memberRegister";
		}

		int result = usersMapper.idCheck(vo);
		try {
			if (result == 1) {
				return "memberRegister";

			} else if (result == 0) {
				String Pass = vo.getPassword();
				String pwd = pwdEncoder.encode(Pass);
				vo.setPassword(pwd);

				usersMapper.insertUsers(vo);
			}

		} catch (Exception e) {
			throw new RuntimeException();
		}
		return "memregSuccess";
	}

	@RequestMapping("/updateUsers") // 유저 정보 수정폼
	public String updateUsers(UsersVO vo, Model model,  HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		vo.setId(id);
		model.addAttribute("list", usersMapper.getUsers(vo));
		return "/users/updateUsers";
	}

	@RequestMapping("/updateUsersProc") // 유저 수정처리  //양소민 수정
	public String updateUsersProc(UsersVO vo, BindingResult bresult, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		vo.setId(id);
		System.out.println("vo:"+vo);
		usersMapper.updateUsersProc(vo);
		
		return "redirect:/updateUsers";
	}

	@RequestMapping("/deleteUsers") // 유저 삭제
	public String deleteUsers(UsersVO vo) {
		usersMapper.deleteUsers(vo);
		return "redirect:/getInquireList";
	}

	// login
	@RequestMapping("/login")
	public String login(UsersVO vo) {
		return "login";
	}

	// kakao login
	@RequestMapping("/kakaologin")
	public String kakaologin(@RequestParam(value = "code", required = false) String code, UsersVO vo,
			HttpSession session) {
		System.out.println("######" + code);
		String accessToken = kakaoapi.getAccessToken(code);
		HashMap<String, Object> userInfo = kakaoapi.getUserInfo(accessToken);
		System.out.println("@AccessToken@" + accessToken);
		System.out.println("userInfo:" + userInfo.get("email"));
		System.out.println("nickname:" + userInfo.get("nickname"));
		System.out.println("profile:" + userInfo.get("profile_image"));
		System.out.println("kakaoId:" + userInfo.get("kakaoId"));

		vo.setId((String) userInfo.get("kakaoId"));
		UsersVO users = usersMapper.kakaoCheck(vo);
		System.out.println(users);

		session.setAttribute("accessToken", accessToken);
		session.setAttribute("userInfo", userInfo);
		
		if (users == null) {
			vo.setId((String) userInfo.get("kakaoId"));
			vo.setEmail((String) userInfo.get("email"));
			vo.setDistinction((String) userInfo.get("nickname"));
			usersMapper.insertUsers(vo);
		}
		return "redirect:/";
	}

	// login 처리, 암호화
	@RequestMapping(value = "/loginProc", method = { RequestMethod.GET, RequestMethod.POST })
	public String loginProc(UsersVO vo, BindingResult result, HttpServletRequest request, RedirectAttributes rttr,
			Model model) {
		HttpSession session = request.getSession();
		UsersValidation usersvalidation = new UsersValidation();
		usersvalidation.validate(vo, result);

		if (result.hasErrors()) {
			return "login";
		}
		UsersVO users = usersMapper.logCheck(vo);
		
		if (users != null) {
			boolean psMatch = pwdEncoder.matches(vo.getPassword(), users.getPassword()); //
			if (psMatch == true) {
				session.setAttribute("users", users);
				session.setAttribute("id", users.getId());   //session에 id란 이름으로 id 저장  
				session.setAttribute("auth", users.getAuth());   //session에 auth란 이름으로 auth 저장
				session.setAttribute("name", users.getName()); //session에 name이란 이름으로 name 저장
				
				return "redirect:/";
			} else {
				session.setAttribute("users", null);
				rttr.addFlashAttribute("msg", false);
				model.addAttribute("message2", "패스워드가 다릅니다.");
			}
		} else {
			model.addAttribute("message", "아이디가 다릅니다.");
			return "login";
		}
		return "login";
	}

	// logout
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		String access_token = (String) session.getAttribute("access_Token");
		kakaoapi.kakaoLogout(access_token);
		session.invalidate();
		return "redirect:/";
	}

	// 아이디 중복체크
	@ResponseBody
	@RequestMapping(value = "/idCheck", method = RequestMethod.POST)
	public int idCheck(UsersVO vo) {
		int result = usersMapper.idCheck(vo);
		return result;
	}

	// email 중복체크
	@ResponseBody
	@RequestMapping(value = "/emailCheck", method = RequestMethod.POST)
	public String emailCheck(String email) {
		int result = usersMapper.emailCheck(email);
		System.out.println("result:" + result);
		if (result != 0) {
			return "fail";
		} else {
			return "success";
		}
	}
	//아이디 찾기 페이지 이동
	@RequestMapping("/findId")
	public String findId(UsersVO vo){
		
		return "findId";
	}
	
	// 아이디 찾기
	@ResponseBody
	@RequestMapping("/findIdProc")
	public List<UsersVO> findIdCheck(UsersVO vo){
		List<UsersVO> list = usersMapper.findId(vo);
		System.out.println("asdf:" + list);
			return list;
	}
	
	
	// 비밀번호 찾기
	@RequestMapping(value = "/findpw", method = RequestMethod.GET)
	public void findPwGET() throws Exception {

	}

	@RequestMapping(value = "/findpw", method = RequestMethod.POST)
	public void findPwPOST(@ModelAttribute UsersVO vo, HttpServletResponse response) throws Exception {
		usersService.findPw(response, vo);
	}

	
	
	@RequestMapping("/updateInfo") // 비밀번호 수정폼
	public String updateInfo() {
		return "/users/updateInfo";
	}
	
	//비밀번호 변경
	@RequestMapping("/pwProc") //비밀번호 수정 처리
	public String pwProc(UsersVO vo, BindingResult result, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		PwValidation pwValidation = new PwValidation(); // 유효성 검사
		pwValidation.validate(vo, result);
		
		String id = (String) session.getAttribute("id");  //로그인 시 세션에 저장된 id값 가져옴
		String pw = vo.getPassword();  //새로 입력받은 비밀번호
		String pwe = pwdEncoder.encode(pw);   // 새로 입력받은 비밀번호 암호화
		vo.setId(id);
		UsersVO users = usersMapper.logCheck(vo); // id 값으로 db에 저장된 정보 가져옴
		boolean psMatch = pwdEncoder.matches(vo.getPasswordold(), users.getPassword()); // 입력한 '현재 비밀번호'와 db에 저장된 비밀번호를 비교
		if (psMatch == true) { //일치할 경우에만
			vo.setPassword(pwe);
			usersService.updateInfo(vo);  // 새로 입력받은 비밀번호로 비밀번호 수정
			return "mypage/mypageHome";
		} else {
			System.out.println("error");
			return "users/updateInfo";
		}			
		
	} //비밀번호 수정 처리 끝
	
	@GetMapping("/popCerti")  //자격증 등록 폼으로 이동
	public String popCerti(String valSom, Model model) {
		model.addAttribute("valSom", valSom);
		return "/users/popCerti";
	}
	
	@GetMapping("/popCareer")  //경력인증서 등록 폼으로 이동
	public String popCareer(String valSom, Model model) {
		model.addAttribute("valSom", valSom);
		return "/users/popCareer";
	}
	
	@GetMapping("/popCertiSee")  //
	public String popCertiSee() {
		return "/users/popCertiSee";
	}
	
	@GetMapping("/popCareerSee")  //
	public String popCareerSee() {
		return "/users/popCareerSee";
	}
	
	@GetMapping("/popCertiSeeResume")  //
	public String popCertiSeeResume() {
		return "/users/popCertiSeeResume";
	}
	
	
	
	
	
	@RequestMapping("/insertCerti") //자격증/경력인증서 등록 처리
	public String insertCerti(UsersVO vo, HttpServletRequest request, String valSom) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		vo.setId(id);
		usersService.insertCerti(vo);
		if("mRegi".equals(valSom)) {
			return "/users/throughSom";
		} else {
			return "/users/throughCerti";
		}
		
	}
	
	@RequestMapping("/getCertiList") //자격증, 경력인증서 조회
	@ResponseBody
	public List<Map> getCertiList(UsersVO vo, HttpServletRequest request, Model model, String idx, String condi ) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		
		if("manUser".equals(condi)) {
			vo.setId(idx);
		} else {
			vo.setId(id);
		}
		
		List<Map> list = usersService.getCertiList(vo);
		System.out.println("getCertiList:"+list);
		
		return list;
	}
	
	@RequestMapping("/getCarList") //자격증, 경력인증서 조회
	@ResponseBody
	public List<Map> getCarList(UsersVO vo, HttpServletRequest request, Model model, String idx, String condi ) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		if("manUser".equals(condi)) {
			vo.setId(idx);
		} else {
			vo.setId(id);
		}
		List<Map> list = usersService.getCarList(vo);
		return list;
	}
	
	//마이페이지에서 경력증명서 등록
	@RequestMapping("/certiUpload")
	public String certiUpload(
		@RequestParam("uploadFile") MultipartFile[] files ,HttpServletRequest request,Model model,UsersVO vo, String valSom) throws Exception
	{	
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		vo.setId(id);
		String filenames ="";
		boolean start = true;
		
		for(MultipartFile file : files) {
			if(file != null && ! file.isEmpty() && file.getSize() > 0) {
				String filename = file.getOriginalFilename();    //업로드 된 파일의 파일명
				//파일명 중복체크 -> rename
				File rename = FileRenamePolicy.rename(new File("/upload", filename)); //transfer 하기 전에 이 파일이 있는지 검사
				  
				if(! start ) {
					
					filenames += ",";
				} else {
					start = false;
				}
				
				filenames += rename.getName();
				System.out.println("1:"+filenames);
				//임시폴더에서 업로드 폴더로 파일 이동
				file.transferTo(rename);
			
			
			
			} else {
				
			         model.addAttribute("msg", "Please select at least one mediaFile..");
			         return "/users/popCareer";
			      }
			   } //end of for
		System.out.println("valSom:"+valSom);
				vo.setCarrer_certi(filenames);   //vo에 업로드된 파일명을 담아서 DB에 파일이름만 저장할 것
				usersService.insertCerti(vo);
				model.addAttribute("msg", "Multiple files uploaded successfully.");
				System.out.println("valSom:"+valSom);
			   if("mRegi".equals(valSom)) {
				   System.out.println("valSom:"+valSom);
					return "/users/throughSom";
				} else if("".equals(valSom)) {
					System.out.println("valSom3:"+valSom);
					return "/users/throughCerti";
				} else {
					return null;
				}
	}
	
	//마이페이지에서 경력증명서 삭제
	@RequestMapping("/delCareer")
	public String certiDelete(UsersVO vo, Model model) {
		UsersVO usersVo =  usersService.selCareer(vo);
		String fileName = usersVo.getCarrer_certi();
		new File("/upload/"+(fileName)).delete();  // 삭제
		usersService.delCareer(vo);
		model.addAttribute("msg", "삭제되었습니다.");
		model.addAttribute("url", "throughSom");
		
		return "common/Success";
		
	}
	
	//마이페이지 자격증 수정 폼
	@RequestMapping("/updateCertiForm")
	public String updateCertiForm(UsersVO vo, Model model) {
		
		model.addAttribute("cer", usersService.getCerti(vo));
		return "/users/updatePopCerti";
	}
	
	//마이페이지 자격증 수정처리
	@RequestMapping("/updateCerti") 
	public String updateCerti(UsersVO vo) {
		
		System.out.println("updateCerti:"+vo.getCerti_date());
		usersService.updateCerti(vo);
		return "/users/throughCerti";
	}
	
	//마이페이지에서 자격증 삭제
		@RequestMapping("/deleteCerti")
		public String deleteCerti(UsersVO vo, Model model) {
			usersService.delCareer(vo);
			model.addAttribute("msg", "삭제되었습니다.");
			model.addAttribute("url", "throughSom");
			return "common/Success";
			
		}
				

	

}
