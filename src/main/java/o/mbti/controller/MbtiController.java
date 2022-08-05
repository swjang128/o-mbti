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
import o.mbti.dto.MbtiDTO;
import o.mbti.service.MbtiService;

/**
 * MBTI 서비스에 대한 Rest API
 * 
 * @author jsw
 *
 */
@RestController
@RequestMapping("mbti")
public class MbtiController {
	@Autowired
	MbtiService mbtiService;

	/**
	 * Read MBTI 검사결과 응답
	 * @RequestBody <Tendency: 주의초점(Introversion / Extroversion)>
	 * @param Integer introversion(내향)
	 * @param Integer extroversion(외향) <Recognition: 인식기능(Sensing / iNtuition)>
	 * @param Integer sensing(감각)
	 * @param Integer intuition(직관) <Judgment: 판단기능(Thinking / Feeling)>
	 * @param Integer thinking(사고)
	 * @param Integer feeling(감정) <Lifestyle: 생활양식(Judging / Perceiving)>
	 * @param Integer judging(판단)
	 * @param Integer perceiving(인식)
	 * @return Map<String, Object>
	 */
	@GetMapping("result")
	public Map<String, Object> read(@RequestBody @Valid MbtiDTO mbtiDTO) {
		// 기본 변수 설정
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("introversion", mbtiDTO.getIntroversion());
		param.put("extroversion", mbtiDTO.getExtroversion());
		param.put("sensing", mbtiDTO.getSensing());
		param.put("intuition", mbtiDTO.getIntuition());
		param.put("thinking", mbtiDTO.getThinking());
		param.put("feeling", mbtiDTO.getFeeling());
		param.put("judging", mbtiDTO.getJudging());
		param.put("perceiving", mbtiDTO.getPerceiving());
		param.put("name", mbtiDTO.getName());

		return mbtiService.read(param, result);
	}

}
