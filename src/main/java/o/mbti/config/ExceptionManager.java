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

@ControllerAdvice	// 예외 처리를 전역 설정	// Controller, RestController에서만 이 핸들러를 호출하여 사용할 수 있다!
@RestController
public class ExceptionManager {
	Map<String, Object> resultMap = new HashMap<String, Object>();
	String message = "";
	int status = 0;
	
	/**
	 * @Valid 어노테이션에 대한 예외처리
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, Object> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
		// 필요한 객체 생성		
		BindingResult bindingResult = exception.getBindingResult();
		
		// Set Status
		status = 401;
		
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
		message = builder.toString();
		
		// 예외처리 결과를 리턴 
		resultMap.put("status",  status);
		resultMap.put("message",  message);
		
		return resultMap; 
	}
	
	/**
	 * EmptyResultDataAccessException Exception Handler
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public Map<String, Object> emptyResultDataAccessExceptionHandler(EmptyResultDataAccessException exception) {
		// 예외처리 결과를 리턴 
		resultMap.put("status",  404);
		resultMap.put("message",  "데이터가 존재하지 않습니다");
		
		return resultMap;
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
