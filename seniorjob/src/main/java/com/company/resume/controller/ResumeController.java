package com.company.resume.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.certificate.service.CertificateVO2;
import com.company.certificate.service.impl.CertificateMapper;
import com.company.portfolio.service.PortfolioVO;
import com.company.portfolio.service.impl.PortfolioMapper;
import com.company.resume.service.ResumeRequestVO;
import com.company.resume.service.ResumeService;
import com.company.resume.service.ResumeVO;
import com.company.resume.service.impl.ResumeMapper;
import com.company.self_info.service.Self_InfoVO;
import com.company.self_info.service.impl.Self_InfoMapper;

@Controller
public class ResumeController {

	@Autowired
	ResumeService resumeservice;
	@Autowired
	ResumeMapper resumemapper;
	@Autowired
	Self_InfoMapper selfmapper;
	@Autowired
	PortfolioMapper portmapper;
	@Autowired
	CertificateMapper certimapper;

	// 이력서 전체조회
	@RequestMapping("/getSearchResumeList")
	public String getSearchResumeList(Model model) {
		model.addAttribute("list", resumemapper.getSearchResumeList());
		return "resume/resumeList";
	}

	// 이력서 등록폼
	@RequestMapping("/resumeInsertForm")
	public String resumeInsertForm() {
		return "resume/resumeInsert";
	}

	// 이력서 등록
	@RequestMapping("/resumeInsert")
	@ResponseBody
	public String resumeInsert(HttpServletRequest req, ResumeVO vo, ResumeRequestVO reqvo,
			PortfolioVO portvo) throws Exception {
		System.out.println(vo);
		resumeservice.insertResume(vo, reqvo.getClist(), reqvo.getSlist(), portvo, req);
		return "resume/resumeList";
	}

	// 이력서 수정폼
	@RequestMapping("/resumeUpdateForm")
	public String resumeUpdateForm(Model model, ResumeVO vo, Self_InfoVO selfvo, CertificateVO2 certivo,
			PortfolioVO portvo) {
		vo = resumemapper.getResume(vo);
		model.addAttribute("resumeVO", vo);
		selfvo.setResume_no(vo.getResume_no()); // resume 테이블의 resume_no라는 거 명시
		model.addAttribute("slist", selfmapper.getSelf(selfvo));
		certivo.setResume_no(vo.getResume_no());
		model.addAttribute("clist", certimapper.getCerti(certivo));
		portvo.setResume_no(vo.getResume_no());
		model.addAttribute("portvo", portmapper.getPort(portvo));
		return "resume/resumeUpdate";
	}

	// 이력서 워드폼
	@RequestMapping("/resumeWord")
	public String resumeWord(Model model, ResumeVO vo, Self_InfoVO selfvo, CertificateVO2 certivo, PortfolioVO portvo) {
		vo = resumemapper.getResume(vo);
		model.addAttribute("resumeVO", vo);
		selfvo.setResume_no(vo.getResume_no()); // resume 테이블의 resume_no라는 거 명시
		model.addAttribute("slist", selfmapper.getSelf(selfvo));
		certivo.setResume_no(vo.getResume_no());
		model.addAttribute("clist", certimapper.getCerti(certivo));
		portvo.setResume_no(vo.getResume_no());
		model.addAttribute("portvo", portmapper.getPort(portvo));
		return "resume/resumeWord";
	}

	// 이력서 수정
	@RequestMapping("/resumeUpdate")
	public String resumeUpdate(HttpServletRequest req, ResumeVO vo, Self_InfoVO selfvo, CertificateVO2 certivo,
			PortfolioVO portvo) throws Exception {
		// portfolio(파일) 수정하는 중(nullpoint 오류)
//		MultipartFile[] ports = portvo.getPortFile();
//		String portnames = "";
//		boolean start = true;
//		for (MultipartFile port : ports) {
//			if (port != null && port.getSize() > 0) {
//				String portfolio = port.getOriginalFilename();
//				File rename = FileRenamePolicy.rename(new File("c:/upload", portfolio));
//				if (!start) {
//					portnames += ",";
//				} else {
//					start = false;
//				}
//				portnames += rename.getName();
//				port.transferTo(rename);
//			}
//		}
//		portvo.setPortfolio(portnames);
		
		resumemapper.updateResuem(vo);
		if(portvo.getPortfolio() != null && !portvo.getPortfolio().isEmpty()){
			portvo.setResume_no(vo.getResume_no());
			portmapper.updatePort(portvo);
		}
		if(selfvo.getSelf_name() != null && !selfvo.getSelf_name().isEmpty()) {
			selfvo.setResume_no(vo.getResume_no());
			selfmapper.updateSelf(selfvo);
		}
		if(certivo.getCerti_name() != null && !certivo.getCerti_name().isEmpty()) {
			certivo.setResume_no(vo.getResume_no());
			certimapper.updateCerti(certivo);
		}
		return "resume/resumeList";
	}

	// 이력서 삭제
	@RequestMapping("/resumeDelete")
	public String resumeDelete(ResumeVO vo) {
		resumemapper.deleteResume(vo);
		return "resume/resumeList";
	}

	// 이력서 단건 조회
	@RequestMapping("/getResume")
	public String getResume(Model model, ResumeVO vo, Self_InfoVO selfvo, PortfolioVO portvo, CertificateVO2 certivo) {
		model.addAttribute("resumeVO", resumemapper.getResume(vo));
		model.addAttribute("selfvo", selfmapper.getSelf(selfvo));
		model.addAttribute("portvo", portmapper.getPort(portvo));
		model.addAttribute("certivo", certimapper.getCerti(certivo));
		return "resume/resumeList";
	}

	@RequestMapping("/preview")
	public String preview() {
		return "resume/preview"; // 미리보기
	}

	@RequestMapping("/collection")
	public String collection(Model model, ResumeVO vo, Self_InfoVO selfvo, CertificateVO2 certivo, PortfolioVO portvo) {
		vo = resumemapper.getResume(vo);
		model.addAttribute("resumeVO", vo);
		model.addAttribute("selfvo", selfmapper.getSelf(selfvo));
		model.addAttribute("certivo", certimapper.getCerti(certivo));
		model.addAttribute("portvo", portmapper.getPort(portvo));
		return "resume/collection"; // 첨삭
	}
}
