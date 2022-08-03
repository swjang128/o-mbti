package o.mbti.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 참고 사이트: https://dev-coco.tistory.com/m/120
// oAuth2.0 참고 사이트: https://velog.io/@vencott/05-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0%EC%99%80-OAuth-2.0%EC%9C%BC%EB%A1%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B01

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)	// 특정 페이지에 특정 권한이 있는 유저만 접근을 허용할 경우 권한 및 인증을 미리 체크하겠다는 설정
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	BCryptPasswordEncoder encoder;
	
	private final AuthSuccessHandler authSuccessHandler;
	private final AuthFailureHandler authFailureHandler;	
	private final AuthLogoutSuccessHandler authLogoutSuccessHandler;
	private final AccountUserDetailsService memberUserDetailsService;

	/**
	 * 스프링 시큐리티 규칙 설정
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {		
		httpSecurity.csrf().disable()	// csrf 토큰 비활성화
					// 해당 경로들은 인증이 없어도 접근을 허용
					.authorizeRequests().antMatchers("/**", "/api/checkEmail/**", "/oms**", "/api/position/**", "/api/department/**", "/api/account/**", "/shop/**").permitAll()
										// 이외의 다른 모든 요청은 인증된 유저만 접근 허용
										.anyRequest().authenticated()					
					.and()					
					// /login URL로 요청이 들어오면 스프링 시큐리티가 로그인 처리를 한다
					.formLogin().loginPage("/oms").loginProcessingUrl("/login").successHandler(authSuccessHandler).failureHandler(authFailureHandler)
					.and()
					// 로그아웃하면 인증정보 및 세션 무효화, 리턴 URL, 로그아웃 URL, JSESSIONID, remember-me 쿠키 삭제
					.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessHandler(authLogoutSuccessHandler).logoutSuccessUrl("/oms").invalidateHttpSession(true).deleteCookies("JSESSIONID", "remember-me").permitAll()
					.and()
					// 세션 최대 허용수(-1은 무제한 세션 허용), 중복 로그인시 처리(true: 중복 로그인 막음, false: 기존 세션을 해제), 세션이 만료된 경우 이동할 페이지 지정 
					.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false).expiredUrl("/oms")
					.and()
					// 로그인 유지, 항상 세션을 기억할 것인지 여부, 토큰 정보 지속시간(24시간), 쿠키 이름 지정(remember-me)
					.and().rememberMe().alwaysRemember(false).tokenValiditySeconds(86400).rememberMeParameter("remember_me")
					.and()
					// 로그인에서 아이디는 email, 비밀번호는 password 파라미터로 설정
					.formLogin().usernameParameter("email").passwordParameter("password");
		log.info("****** Spring Security HttpSecurity -> {}", httpSecurity);
	}
	
	/**
	 * Spring Security 로그인 처리 시 비밀번호 검증 작업
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
		managerBuilder.userDetailsService(memberUserDetailsService).passwordEncoder(encoder);
	}
	
	/**
	 * 아래 경로들은 인증없이 접근이 가능한 곳
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**", "/scss/**", "/img/**", "/fonts/**", "/vendor/**", "/svg/**", "/assets/**");
	}
	
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		
	}
	
}
