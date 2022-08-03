package o.mbti.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import o.mbti.entity.Account;
import o.mbti.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

/**
 * 로그아웃 핸들러
 * 참조: http://trandent.com/article/spring/detail/319343
 * @author jsw
 *
 */
@RequiredArgsConstructor
@Service
public class AuthLogoutSuccessHandler implements LogoutSuccessHandler {
	private final AccountRepository accountRepository;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		// 해당 계정의 상태를 OFFLINE으로 전환
		try {
			accountRepository.updateUserStatus(authentication.getName(), Account.UserStatus.OFFLINE.getKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 로그인 페이지로 이동
		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect("/oms");
	}

}
