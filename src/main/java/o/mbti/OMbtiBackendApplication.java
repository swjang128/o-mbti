package o.mbti;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;
import o.mbti.dto.AccountDTO;
import o.mbti.entity.Account;
import o.mbti.repository.AccountRepository;
import o.mbti.service.AccountService;

@SpringBootApplication
@EnableEurekaClient
@Slf4j
public class OMbtiBackendApplication {
	@Value("${spring.profiles.active}")
	private String profile;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	AccountService accountService;
	@Autowired
	BCryptPasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(OMbtiBackendApplication.class, args);
	}

	/**
	 * 애플리케이션 실행 시 Member 테이블에 계정이 하나도 없으면 테스트용 계정을 생성
	 */
	@PostConstruct
	public void init() {
		List<Account> accountList = accountRepository.findAll();
		String initEmail = "admin@oms.com";
		String initPassword = "!Q2w3e4r";
		if (accountList.size() == 0) {
			log.info("****** 계정 정보가 존재하지 않습니다. 테스트용 계정을 생성합니다.");
			AccountDTO accountDTO = new AccountDTO();
			LocalDate localDate = LocalDate.now();
			accountDTO.setEmail(initEmail);
			accountDTO.setPassword(encoder.encode(initPassword));
			accountDTO.setName("admin");
			accountDTO.setBirthday(localDate);
			accountDTO.setStatus(Account.Status.ACTIVE);
			accountDTO.setUserStatus(Account.UserStatus.OFFLINE);
			accountDTO.setRole(Account.Role.ADMIN);
			
			// 기본 계정을 등록
			Account account = accountDTO.toEntity();
			try {
				accountRepository.save(account);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 모든 계정의 상태를 OFFLINE으로 변경
			accountRepository.updateAllUserStatus(Account.UserStatus.OFFLINE.getKey());
		}
		
		if (!"prod".equals(profile)) {
			log.info("************************************************************");
			log.info("****************** << 테스트용 계정 정보 >> *******************");
			log.info("********** [메일]: [{}] **********************************", initEmail);
			log.info("********** [암호]: [{}] **********************************", initPassword);
			log.info("************************************************************");
		}
	}
}
