package o.mbti.service;

import java.util.Map;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import o.mbti.config.MbtiResponse;
import o.mbti.config.RestAPIResponse;

@Service
@Slf4j
public class MbtiService {
	/**
	 * Read
	 * @param Map<String, Object>, Map<String, Object>
	 * @return Integer status
	 * @return String result
	 * @message String message
	 */
	public Map<String, Object> read(Map<String, Object> param, Map<String, Object> result) {
		// Response Enum 선언
		RestAPIResponse restAPIResponse = RestAPIResponse.SUCCESS;
		
		// 파라미터로 받아온 값을 변수로 선언
		Integer introversion = (Integer) param.get("introversion");
		Integer extroversion = (Integer) param.get("extroversion");
		Integer sensing = (Integer) param.get("sensing");
		Integer intuition = (Integer) param.get("intuition");
		Integer thinking = (Integer) param.get("thinking");
		Integer feeling = (Integer) param.get("feeling");
		Integer judging = (Integer) param.get("judging");
		Integer perceiving = (Integer) param.get("perceiving");
		String tendency = "I";			// 주의초점
		String recognition = "N";		// 인식기능
		String judgment = "F";			// 판단기능
		String lifestyle = "P";				// 생활양식
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
		
		// 변수에 담긴 성향을 MbtiResponse로 변환하고 결과에 담아서 리턴
		result.put("name", param.get("name"));
		// MBTI Response
		result.put("type", MbtiResponse.valueOf(new String(mbti)).type);
		result.put("title", MbtiResponse.valueOf(new String(mbti)).title);
		result.put("content", MbtiResponse.valueOf(new String(mbti)).content);
		// REST API Response
		result.put("status", restAPIResponse.status);
		result.put("result", restAPIResponse.result);
		result.put("message", restAPIResponse.message);
		return result;
	}
}
