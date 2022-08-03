package o.mbti.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import o.mbti.entity.Account;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccountUserDetails implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6564227628629058405L;
	private Account account;
	
	/**
	 * 로그인할 때 아이디로 사용할 값
	 */
	@Override
	public String getUsername() {
		return account.getEmail();
	}

	/**
	 * 로그인할 때 비밀번호로 사용할 값
	 */
	@Override
	public String getPassword() {
		return account.getPassword();
	}
	
	/**
	 * 계정이 갖고있는 권한 목록 리턴
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection <GrantedAuthority> collectors = new ArrayList<>();
		collectors.add(() -> {
			return "계정별 등록할 권한";
		});
		
		return collectors;
	}
	
	/**
	 * 계정이 만료되지 않았는지 리턴 (true: 만료 안됨)
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 계정이 잠겨있는지 리턴 (true: 잠기지 않음)
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 비밀번호가 만료되지 않았는지 리턴 (true: 만료 안됨)
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 계정이 활성화 상태인지 리턴 (true: 활성화)
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}
