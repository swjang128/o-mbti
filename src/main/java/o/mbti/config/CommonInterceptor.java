package o.mbti.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 요청한 servletPath가 API가 아닌 Web 요청이면 menuName과 url 설정
		String servletPath[] = request.getServletPath().split("/");
		
		String remoteUser = "";
		if (request.getRemoteUser() != null) {
			remoteUser = request.getRemoteUser();
		}
		
//		if ("mbti".equals(servletPath[1])) {
//			log.info("================ Web 요청");
//			Map<String, Object> param = new HashMap<String, Object>();
//			Map<String, Object> result = new HashMap<String, Object>();
//			param.put("url", servletPath[servletPath.length-1]);
//			log.info("---- Request URL: {}", servletPath[2]);
//		}
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// 모든 요청과 응답이 끝나면 요청 로그 기록
		if (request.getRemoteUser() != null) {
			log.info("**** HttpServletRequest [ RequestURI: {} | Method: {} | ClientIP: {} | RemoteUser: {} ]", request.getRequestURI(), request.getMethod(), request.getRemoteAddr(), request.getRemoteUser());
		} else {
			log.info("**** HttpServletRequest [ RequestURI: {} | Method: {} | ClientIP: {} ]", request.getRequestURI(), request.getMethod(), request.getRemoteAddr());	
		}
		// 인터셉터 afterCompletion 처리 완료
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
