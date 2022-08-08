package o.mbti.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import o.mbti.entity.Account;
import o.mbti.entity.Account.Role;
import o.mbti.entity.Account.Status;
import o.mbti.entity.Account.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequestDTO {	
	private Long id;										// 사번
	
	@NotBlank(message="이름을 입력해주세요.")
	@Size(min=1, max=64, message="이름은 64자 이하로 입력해주세요.")
	private String name;				 					// 이름
	
	@NotBlank(message="이메일을 입력해주세요.")
	@Size(min=1, max=64, message="이메일은 64자 이하로 입력해주세요.")
	@Email(message="이메일 형식에 맞지 않습니다.")
    private String email;									// 이메일
	
	@NotBlank(message="비밀번호를 입력해주세요.")
	@Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
					message="비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;								// 비밀번호
	
	private String oldPassword;						// 기존 비밀번호
	
	private int failCount;									// 비밀번호 틀린 횟수
	
	private Status status;									// 상태(계정)
	
	private UserStatus userStatus;									// 상태(사용자)
	
	private Role role;										// 권한
	
	private LocalDate birthday;									// 생일
	
	private LocalDateTime lastLoginTime;				// 마지막 로그인 시간

	/**
	 * (Request) DTO -> Entity
	 * accountDTO를 Entity로 변환 
	 * @return
	 */
	public Account toEntity() {
		return Account.builder()
				.id(id)
				.name(name)
				.email(email)
				.password(password)
				.status(status)
				.userStatus(userStatus)
				.role(role)
				.birthday(birthday)
				.lastLoginTime(lastLoginTime)
				.build();
	}
	
	/**
	 * (Response) Entity -> DTO
	 * account Entity 정보를 DTO로 받아 응답받는 메소드
	 * @param account
	 */
	public AccountRequestDTO (Account account) {
		this.id = account.getId();
		this.name = account.getName();
		this.email = account.getEmail();
		this.password = account.getPassword();
		this.failCount = account.getFailCount();
		this.status = account.getStatus();
		this.userStatus = account.getUserStatus();
		this.role = account.getRole();
		this.birthday = account.getBirthday();
		this.lastLoginTime = account.getLastLoginTime();
	}
	
}
