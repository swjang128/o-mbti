package o.mbti.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import o.mbti.config.MbtiResponse;
import o.mbti.config.ResponseManager;
import o.mbti.config.ApiResponse;
import o.mbti.config.CommonResponse;
import o.mbti.dto.ApiResponseDTO;
import o.mbti.dto.MbtiRequestDTO;
import o.mbti.dto.MbtiResponseDTO;

@Service
@Slf4j
@AllArgsConstructor
public class MbtiService {
	private final ResponseManager responseManager;
	
	/**
	 * MBTI Result 가져오기
	 * 
	 * @param MbtiRequestDTO, Map<String, Object>
	 * @return Integer status
	 * @return String result
	 * @message String message
	 */
	public Map<String, Object> result(MbtiRequestDTO mbtiRequestDTO, 
																			Map<String, Object> result) {
		// DTO Set
		ApiResponseDTO commonResponseDTO = new ApiResponseDTO();
		MbtiResponseDTO mbtiResponseDTO = new MbtiResponseDTO();
		
		// 파라미터로 받아온 성향을 분석하여 MBTI값을 Response		
		mbtiResponseDTO = analyze(mbtiRequestDTO, mbtiResponseDTO);

		// Common Response
		commonResponseDTO = responseManager.apiResponse(commonResponseDTO, ApiResponse.SUCCESS);
		
		// 결과 리턴
		result.put("name", mbtiRequestDTO.getName());		// 이름은 요청받은 값 그대로 응답
		result.put("response", commonResponseDTO);
		result.put("mbti", mbtiResponseDTO);
		return result;
	}

	private MbtiResponseDTO analyze(MbtiRequestDTO mbtiRequestDTO, MbtiResponseDTO mbtiResponseDTO) {
		// 파라미터로 받아온 값을 변수로 선언
		Integer introversion = mbtiRequestDTO.getIntroversion();
		Integer extroversion = mbtiRequestDTO.getExtroversion();
		Integer sensing = mbtiRequestDTO.getSensing();
		Integer intuition = mbtiRequestDTO.getIntuition();
		Integer thinking = mbtiRequestDTO.getThinking();
		Integer feeling = mbtiRequestDTO.getFeeling();
		Integer judging = mbtiRequestDTO.getJudging();
		Integer perceiving = mbtiRequestDTO.getPerceiving();
		String tendency = "I"; // 주의초점
		String recognition = "N"; // 인식기능
		String judgment = "F"; // 판단기능
		String lifestyle = "P"; // 생활양식
		StringBuffer mbti = new StringBuffer();

		// 파라미터로 받아온 값으로 MBTI 값 가공
		if (extroversion > introversion) {
			tendency = "E";
		}
		if (sensing > intuition) {
			recognition = "S";
		}
		if (thinking > feeling) {
			judgment = "T";
		}
		if (judging > perceiving) {
			lifestyle = "J";
		}
		mbti.append(tendency);
		mbti.append(recognition);
		mbti.append(judgment);
		mbti.append(lifestyle);
		
		// MBTI Response
		mbtiResponseDTO.setType(MbtiResponse.valueOf(new String(mbti)).type);
		mbtiResponseDTO.setTitle(MbtiResponse.valueOf(new String(mbti)).title);
		mbtiResponseDTO.setContent(MbtiResponse.valueOf(new String(mbti)).content);
		
		return mbtiResponseDTO;
	}
}
