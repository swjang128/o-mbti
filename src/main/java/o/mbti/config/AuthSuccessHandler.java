package o.mbti.config;

import java.io.IOException;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import o.mbti.dto.AccountDTO;
import o.mbti.entity.Account;
import o.mbti.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그인 성공 핸들러
 * @author JSW
 *
 */
@RequiredArgsConstructor
@Component(value="authenticationSuccessHandler")
@Slf4j
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Autowired
	ModelMapper modelMapper;
	
	private final AccountRepository accountRepository;	
	private final HttpSession session;

	/**
	 * 로그인 성공시 실행하는 작업들
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		// Account 객체 생성
		Account account = accountRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 계정입니다." + authentication.getName()));
		// 로그인한 계정의 상태가 BLOCKED면 LockedException 예외 throw
		switch(account.getStatus().getKey()) {
			case "BLOCKED" -> {
				throw new LockedException("계정이 정지 상태입니다");
			}
			case "EXPIRED" -> {
				throw new AccountExpiredException("계정이 만료 상태입니다");
			}
		}
		
		// lastLoginTime(현재시간), 비밀번호 틀린 횟수(0), 사용자 상태(ONLINE)으로 변경
		accountRepository.updateLastLogin(authentication.getName(), LocalDateTime.now());
		accountRepository.updateFailCount(authentication.getName(), 0);
		accountRepository.updateUserStatus(authentication.getName(), Account.UserStatus.ONLINE.getKey());
		
		// 로그인한 사용자 정보를 조회하여 세션에 담기
		account = accountRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 계정입니다." + authentication.getName()));
		AccountDTO accountDTO = new AccountDTO(account);
		session.setAttribute("sessionId", accountDTO.getId());
		session.setAttribute("sessionName", accountDTO.getName());
		session.setAttribute("sessionEmail", accountDTO.getEmail());
		session.setAttribute("sessionUserStatus",  accountDTO.getUserStatus().getValue());
		session.setAttribute("sessionRole",  accountDTO.getRole().getValue());
		session.setAttribute("sessionBirthday", accountDTO.getBirthday());
		
		//  모든 작업이 끝나고 이동할 url 설정
		setDefaultTargetUrl("/oms/account");
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
