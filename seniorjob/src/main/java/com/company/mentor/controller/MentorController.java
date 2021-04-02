package com.company.mentor.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.company.mentor.common.FileRenamePolicy;
import com.company.mentor.service.MentorVO;
import com.company.mentor.service.impl.MentorMapper;

@Controller
public class MentorController {
	
	@Autowired MentorMapper mentorMapper;
	
		// 로그인, 회원가입 미비 시 호출되는 페이지
		// 로그아웃 상태에서 멘토등록 클릭하면 호출
		@RequestMapping("loginCheckAlert")
		public String loginCheckAlert() {
			return "Mentor/loginCheckAlert";
		}
	
		// 멘토 리스트 페이지 호출
		@RequestMapping("/MentorList")
		public String MentorList(Model model) {
			model.addAttribute("list", mentorMapper.MentorList());
			return "Mentor/mentorList";
		}
		
		// 멘토 등록 페이지 호출
		@RequestMapping("/MentorRegister")
		public String MentorRegister() {
			return "Mentor/mentorRegister";
		}
		
		// 멘토 등록 요청
		@RequestMapping("/MentorRegisterProc") // RequestMapping 설정만 해도 GET,POST 둘 다 사용가능
		public String MentorRegisterProc(Model model, MentorVO vo) throws Exception {
			MentorVO mentorCheck = mentorMapper.mentorRegisterCheck(vo); // 멘토 등록 여부 확인
			if(mentorCheck != null) {
				model.addAttribute("msg", "이미 멘토로 등록하셨습니다. 마이페이지를 확인하세요.");
				model.addAttribute("url", "MentorList");
				return "Mentor/mentorDuplicateAlert"; // 이미 멘토 등록되어 있으면 해당 주소로 리턴
			}else {
				MultipartFile photoFile = vo.getMentor_photo_file(); // 멘토 사진 파일
				MultipartFile licenseFile = vo.getMentor_license_file(); // 멘토 자격증 파일
				MultipartFile careerFile = vo.getMentor_career_certificate_file(); // 멘토 경력 인증 파일
				
				if(
					photoFile != null && !photoFile.isEmpty() && photoFile.getSize() > 0
					&& licenseFile != null && !licenseFile.isEmpty() && licenseFile.getSize() > 0
					&& careerFile != null && !careerFile.isEmpty() && careerFile.getSize() > 0
				  ) {
					// 파일 이름만 추출
					String photoFileName = photoFile.getOriginalFilename();
					String licenseFileName = licenseFile.getOriginalFilename();
					String careerFileName = careerFile.getOriginalFilename();
					
					// 파일 이름 중복 처리
					// 동일 파일 처리 시 파일 이름 뒤에 숫자 삽입
					File photoRename = FileRenamePolicy.rename(new File("c:/uploadTest/", photoFileName));
					File licenseRename = FileRenamePolicy.rename(new File("c:/uploadTest/", licenseFileName));
					File careerRename = FileRenamePolicy.rename(new File("c:/uploadTest/", careerFileName));
					
					// DB에 파일 이름만 저장
					vo.setMentor_photo(photoFileName);
					vo.setMentor_license(licenseFileName);
					vo.setMentor_career_certificate(careerFileName);
					
					// 물리 저장소에 파일 저장
					photoFile.transferTo(photoRename);
					licenseFile.transferTo(licenseRename);
					careerFile.transferTo(careerRename);

					mentorMapper.MentorRegisterProc(vo);
					return "Mentor/mentorRegisterSuccess";
				}else {
					// 멘토 등록 오류 발생 시 해당 페이지로 리턴
					// 오류 발생 조건: 빈칸 제출
					model.addAttribute("msg", "멘토 등록 처리 실패. 다시 작성 해주세요.");
					model.addAttribute("url", "MentorList");
					return "Mentor/registerAlert"; 
				}
			}
		}
		
		// 멘토 상세 페이지 호출
		@RequestMapping("/getMentor")
		public String getMentor(Model model, MentorVO vo) {
			model.addAttribute("list", mentorMapper.getMentor(vo));
			return "Mentor/getMentor";
		}

}
