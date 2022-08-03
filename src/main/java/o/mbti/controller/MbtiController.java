package o.mbti.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import o.mbti.service.MbtiService;

/**
 * MBTI 서비스에 대한 Rest API
 * 
 * @author jsw
 *
 */
@RestController
@Slf4j
@RequestMapping("mbti")
public class MbtiController {
	@Autowired
	MbtiService mbtiService;

	/**
	 * Read
	 * @QueryParam
	 * @param id: 아이디 (String)
	 * @param name: 이름 (String)
	 * @return Map<String, Object>
	 */
	@GetMapping("test")
	public Map<String, Object> read(@RequestParam(name = "id", required = false) String id,
																				@RequestParam(name = "name", required = false) String name) {
		// 기본 변수 설정
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("name", name);
		
		return mbtiService.read(param, result);		
	}

}
