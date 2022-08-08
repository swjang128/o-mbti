package o.mbti.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import o.mbti.dto.ApiResponseDTO;

@ControllerAdvice	// 예외 처리를 전역 설정	// Controller, RestController에서만 이 핸들러를 호출하여 사용할 수 있다!
@RestController
public class ResponseManager {
	Map<String, Object> apiResult = new HashMap<String, Object>();	
	/**
	 * REST API 응답값 가공
	 * @return CommonResponseDTO
	 */	
	public ApiResponseDTO apiResponse(ApiResponseDTO apiResponseDTO, ApiResponse apiResponse) {
		apiResponseDTO.setStatus(apiResponse.status);
		apiResponseDTO.setResult(apiResponse.result);
		apiResponseDTO.setMessage(apiResponse.message);
		
		return apiResponseDTO;
	}
	
	/**
	 * @Valid 어노테이션에 대한 예외처리 (400 에러)
	 * @param exception 
	 * @return 
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, Object> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
		// 필요한 객체 생성		
		BindingResult bindingResult = exception.getBindingResult();
		
		// Set Message
		StringBuilder builder = new StringBuilder();
		for (FieldError fieldError: bindingResult.getFieldErrors()) {
			builder.append("[");
			builder.append(fieldError.getField());
			builder.append("](은)는 ");
			builder.append(fieldError.getDefaultMessage());
			builder.append(" 입력된 값: [");
			builder.append(fieldError.getRejectedValue());
			builder.append("]");
		}
		String message = builder.toString();
		
		// 예외처리 결과를 리턴 
		apiResult.put("status", ApiResponse.BAD_REQUEST.status);
		apiResult.put("result", ApiResponse.BAD_REQUEST.result);
		apiResult.put("message",  message);
		
		return apiResult; 
	}
	
	/**
	 * EmptyResultDataAccessException Exception Handler (404 에러)
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public Map<String, Object> emptyResultDataAccessExceptionHandler(EmptyResultDataAccessException exception) {
		// 예외처리 결과를 리턴 
		apiResult.put("status",  ApiResponse.EMPTY_RESULT_DATA_ACCESS.status);
		apiResult.put("result",  ApiResponse.EMPTY_RESULT_DATA_ACCESS.result);
		apiResult.put("message",  ApiResponse.EMPTY_RESULT_DATA_ACCESS.message);
		
		return apiResult;
	}
	
	
	/**
	 * 계정 관련(AuthenticationException) 예외처리에 대한 메시지와 상태코드를 리턴하는 메소드
	 * @param AuthenticationException
	 * @return
	 */
	@ExceptionHandler(AuthenticationException.class)
	public String authenticationExceptionHandler(AuthenticationException exception) {
		String result = "error";

		if (exception instanceof LockedException) {
			result = "BLOCKED_ACCOUNT";
        } else if (exception instanceof AccountExpiredException) {
        	result = "EXPIRED_ACCOUNT";
        } else if (exception instanceof CredentialsExpiredException) {
        	result = "EXPIRED_ACCOUNT";
        } else if (exception instanceof DisabledException) {
        	result = "DISABLED_ACCOUNT";
        } else if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
        	result = "BAD_CREDENTIALS";
        }
		
		return result;
	}
}
