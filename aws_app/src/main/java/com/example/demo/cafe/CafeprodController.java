package com.example.demo.cafe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth/cafe")
public class CafeprodController {
	@Autowired
	private CafeprodService service;
	
	@Value("${spring.servlet.multipart.location}")
	private String path;
	
	@GetMapping("/add")  //view 경로:cafe/add
	public String addform() {
		return "cafe/add";
	}
	
	
	@PostMapping("/add") //=> index_seller로 이동
	public String add(CafeprodDto dto) {
		CafeprodDto c = service.save(dto);// 상품등록. 이미지 정보 빠짐. 상품번호 할당됨.
		String oname = dto.getF().getOriginalFilename();
		String fname = c.getNum() + oname;  //1이미지명.jpg
		File f = new File(path+fname);
		try {
			dto.getF().transferTo(f);//업로드 파일 복사
			c.setFname(f.getName());
			service.save(c);//수정. 이미지 정보 추가
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/auth/cafe/list?storeid="+c.getStore().getStoreid();
	}
	
	
	@RequestMapping("/list")  //한 매장의 상품목록(상품명/가격)  view 경로:cafe/list
	public String list(ModelMap map, String storeid, HttpSession session) {
		map.addAttribute("list", service.getbyStore(storeid));
		String path = "cafe/list"; ////로그인자가 판매자이면 상품목록
		String type = (String) session.getAttribute("type");
		System.out.println("(cafe controller)user type:"+type);
		if(type.equals("consumer")) {
			path = "order/add";  //로그인자가 구매자이면 구매페이지
		}
		return path;
	}
	
	@GetMapping("/read-img")
	public ResponseEntity<byte[]> read_img(String fname) {
		ResponseEntity<byte[]> result = null;
		File f = new File(path + fname);
		HttpHeaders header = new HttpHeaders(); //import org.springframework.http.HttpHeaders;
		try {
			header.add("Content-Type", Files.probeContentType(f.toPath()));
			result = new ResponseEntity<byte[]>(
						FileCopyUtils.copyToByteArray(f),header, HttpStatus.OK
					);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
//	@GetMapping("/detail") 상세페이지 / 수정, 삭제 처리 view 경로:cafe/detail

}
