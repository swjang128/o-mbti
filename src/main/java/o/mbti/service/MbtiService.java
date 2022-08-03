package o.mbti.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import o.mbti.config.ResponseManager;

@Service
public class MbtiService {
	/**
	 * Read
	 * @param Map<String, Object>, Map<String, Object>
	 * @return Map<String, Object>
	 */
	public Map<String, Object> read(Map<String, Object> param, Map<String, Object> result) {
		// 결과는 성공으로 내려주기 (테스트)
		ResponseManager responseManager = ResponseManager.SUCCESS;
		
		result.put("id", param.get("id"));
		result.put("name", param.get("name"));
		result.put("status", responseManager.status);
		result.put("result", responseManager.result);
		result.put("message", responseManager.message);
		
		return result;
	}
}
