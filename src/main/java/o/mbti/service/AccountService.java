package o.mbti.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import o.mbti.config.ApiResponse;
import o.mbti.dto.AccountDTO;
import o.mbti.dto.AccountRequestDTO;
import o.mbti.dto.MailDTO;
import o.mbti.entity.Account;
import o.mbti.entity.Account.Status;
import o.mbti.entity.Account.UserStatus;
import o.mbti.repository.AccountRepository;
import o.mbti.specification.AccountSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountService {
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	BCryptPasswordEncoder encoder;
	@Autowired
	JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String FROM_ADDRESS;

	/**
	 * 특정 계정정보 조회
	 * 
	 * @param accountDTO
	 * @param account
	 * @return
	 */
	public Account readAccount(AccountDTO accountDTO, Account account) {
		account = accountRepository.findByEmail(accountDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다. email: " + accountDTO.getEmail()));
		return account;
	}
	
	/**
	 * 계정 등록
	 * 
	 * @param accountDTO
	 * @return Map<String, Object>
	 */
	@Transactional
	public Map<String, Object> create(AccountDTO accountDTO, Map<String, Object> resultMap) {
		ApiResponse apiResponse = ApiResponse.CREATED;
		// Status는 Active로 Set
		accountDTO.setStatus(Account.Status.ACTIVE);
		// 입력받아온 비밀번호는 암호화
		accountDTO.setPassword(encoder.encode(accountDTO.getPassword()));
		// UserStatus는 OFFLINE으로 Set
		accountDTO.setUserStatus(Account.UserStatus.OFFLINE);
		// accountDTO를 Entity로 변환
		Account account = accountDTO.toEntity();
		// 회원 가입 진행
		try {
			accountRepository.save(account);
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = ApiResponse.ERROR_ABORT;
		} finally {
			resultMap.put("status", apiResponse.status);
			resultMap.put("result", apiResponse.result);
		}
		return resultMap;
	}

	/**
	 * 계정 목록 조회 (READ)
	 * 
	 * @return List<Account>
	 */
	public Map<String, Object> read(AccountRequestDTO accountRequestDTO, Map<String, Object> result) {
		// 기본 변수 설정
		ApiResponse apiResponse = ApiResponse.SUCCESS;
		List<Account> account = new ArrayList<Account>();
		List<AccountDTO> accountDTO = new ArrayList<AccountDTO>();
		Object accountStatus = accountRequestDTO.getStatus();
		Object userStatus = accountRequestDTO.getUserStatus();
		Object role = accountRequestDTO.getRole();
		LocalDate birthday = (LocalDate) accountRequestDTO.getBirthday();
		Specification<Account> specification = (root, query, criteriaBuilder) -> null;
		// 계정 목록 조회
		try {
			if (accountStatus != null) {
				specification = specification.and(AccountSpecification.findByStatus(accountStatus));
			}
			if (userStatus != null) {
				specification = specification.and(AccountSpecification.findByUserStatus(userStatus));
			}
			if (role != null) {
				specification = specification.and(AccountSpecification.findByRole(role));
			}
			account = accountRepository.findAll(specification);
			accountDTO = account.stream().map(a -> modelMapper.map(a, AccountDTO.class)).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = ApiResponse.ERROR_ABORT;
		}
		// resultMap에 담기
		result.put("status", apiResponse.status);
		result.put("result", apiResponse.result);
		result.put("accountList", accountDTO);		
		return result;
	}
	
	/**
	 * 계정 개수 (READ)
	 * 
	 * @return Integer
	 */
	public Map<String, Object> count(Map<String, Object> paramMap, Map<String, Object> resultMap) {
		// 기본 변수 설정
		ApiResponse apiResponse = ApiResponse.SUCCESS;
		long count = 0;
		Object accountStatus = paramMap.get("status");
		Object userStatus = paramMap.get("userStatus");
		Object role = paramMap.get("role");
		Object department = paramMap.get("department");
		Object position = paramMap.get("position");
		LocalDate startDate = (LocalDate) paramMap.get("startDate");
		LocalDate endDate = (LocalDate) paramMap.get("endDate");
		Specification<Account> specification = (root, query, criteriaBuilder) -> null;
		// Specification 조건 작성
		if (accountStatus != null)
			specification = specification.and(AccountSpecification.findByStatus(accountStatus));
		if (userStatus != null)
			specification = specification.and(AccountSpecification.findByUserStatus(userStatus));
		if (role != null)
			specification = specification.and(AccountSpecification.findByRole(role));
		if (department != null)
			specification = specification.and(AccountSpecification.findByDepartment(department));
		if (position != null)
			specification = specification.and(AccountSpecification.findByPosition(position));
		if (startDate != null || endDate != null)
			specification = specification.and(AccountSpecification.findByHireDate(startDate, endDate));
		// Specification 조건에 맞는 계정 개수 조회
		try {
			count = accountRepository.count(specification);	
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = ApiResponse.ERROR_ABORT;
		}
		// resultMap에 담기
		resultMap.put("status", apiResponse.status);
		resultMap.put("result",  apiResponse.result);
		resultMap.put("count", count);
		return resultMap;
	}
	
	/**
	 * 특정 계정 조회 (READ)
	 * @param Long
	 * @return Map<String, Object>
	 */
	public Map<String, Object> readOne(Long id, Map<String, Object> resultMap) {
		// 기본 변수 설정
		ApiResponse apiResponse = ApiResponse.SUCCESS;
		Account account = null;
		AccountDTO accountDTO = null;
		// 특정 계정 조회
		try {			
			account = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 계정 정보가 없습니다. id: " + id));
			accountDTO = new AccountDTO(account);
			resultMap.put("account", accountDTO);
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = ApiResponse.ACCOUNT_DOES_NOT_EXISTS;
		}
		// resultMap에 담기
		resultMap.put("status", apiResponse.status);
		resultMap.put("result", apiResponse.result);
		resultMap.put("message", apiResponse.message);
		return resultMap;
	}

	/**
	 * 계정 정보 수정 (UPDATE)
	 * 
	 * @param @RequestBody
	 * @return
	 * @return
	 */
	@Transactional
	public Map<String, Object> update(AccountDTO accountDTO, Map<String, Object> resultMap) {
		ApiResponse apiResponse = ApiResponse.SUCCESS;
		long id = accountDTO.getId();
		Optional<Account> account = Optional.empty();
		// 해당 계정이 있는지 확인
		account = accountRepository.findById(id);
		if (!account.isPresent()) {
			apiResponse = ApiResponse.ACCOUNT_DOES_NOT_EXISTS;
			resultMap.put("status", apiResponse.status);
			resultMap.put("result", apiResponse.result);
			return resultMap;
		}
		// 비밀번호 확인
		if (!encoder.matches(accountDTO.getPassword(), account.get().getPassword())) {
			apiResponse = ApiResponse.PASSWORD_DOES_NOT_MATCHED;
			resultMap.put("status", apiResponse.status);
			resultMap.put("result", apiResponse.result);
			return resultMap;
		}
		// 계정 정보 수정 (UPDATE)
		try {
			accountRepository.save(accountDTO.toEntity());
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = ApiResponse.ERROR_ABORT;
			resultMap.put("status", apiResponse.status);
		}
		resultMap.put("status", apiResponse.status);
		resultMap.put("result", apiResponse.result);
		resultMap.put("message", apiResponse.message);
		return resultMap;
	}

	/**
	 * 계정 정보 삭제 (DELETE)
	 * @return http 응답코드
	 */
	@Transactional
	public Map<String, Object> delete(List<Long> payload, Map<String, Object> resultMap) {
		ApiResponse apiResponse = ApiResponse.SUCCESS;
		// 대상 계정이 존재하는지 확인
		try {
			for (int p=0; p<payload.size(); p++) {
				accountRepository.findById(payload.get(p)).orElseThrow(() -> new IllegalArgumentException("payload 중 존재하지 않는 계정이 있습니다: " + payload));
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = ApiResponse.ACCOUNT_DOES_NOT_EXISTS;
			resultMap.put("status", apiResponse.status);
			resultMap.put("result", apiResponse.result);
			resultMap.put("message", apiResponse.message);
			return resultMap;
		}
		// 계정이 존재하면 삭제
		for (int d=0; d<payload.size(); d++) {
			try {
				accountRepository.deleteById(payload.get(d));
			} catch (Exception e) {
				e.printStackTrace();
				apiResponse = ApiResponse.ERROR_ABORT;
				resultMap.put("status", apiResponse.status);
				resultMap.put("result", apiResponse.result);
				resultMap.put("message", apiResponse.message);
				return resultMap;
			}
		}
		resultMap.put("status", apiResponse.status);
		resultMap.put("result", apiResponse.result);
		resultMap.put("message", apiResponse.message);
		resultMap.put("deletedId", payload);
		return resultMap;
	}

	/**
	 * 비밀번호 초기화
	 * 
	 * @param accountDTO
	 * @return
	 */
	public Map<String, Object> resetPassword(AccountDTO accountDTO, Map<String, Object> resultMap) {
		// 기본 변수 선언
		ApiResponse apiResponse = ApiResponse.SUCCESS;
		String email = accountDTO.getEmail();
		// 임시 비밀번호 생성
		String tempPassword = getTempPassword();
		// account 객체 선언
		Account account = null;
		// 계정이 존재하는지 확인
		try {
			account = readAccount(accountDTO, account);
		} catch (Exception e) { // 계정이 존재하지 않는 경우
			e.printStackTrace();
			apiResponse = ApiResponse.ACCOUNT_DOES_NOT_EXISTS;
			resultMap.put("status", apiResponse.status);
			resultMap.put("result", apiResponse.result);
			resultMap.put("message", apiResponse.message);
			return resultMap;
		}
		// 임시 비밀번호 메일 발송
		try {
			// 이메일로 임시 비밀번호를 발송할 내용 설정
			MailDTO mailDTO = setMail(email, tempPassword);
			// 임시 비밀번호 메일 발송
			sendMail(mailDTO);
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = ApiResponse.ERROR_ABORT;
			resultMap.put("status", apiResponse.status);
			resultMap.put("result", apiResponse.result);
			resultMap.put("message", apiResponse.message);
			return resultMap;
		}
		// 해당 사용자의 비밀번호를 임시 비밀번호로 변경
		try {
			accountRepository.updatePassword(account.getId(), encoder.encode(tempPassword), 0, Account.Status.EXPIRED.getKey(), Account.UserStatus.OFFLINE.getKey());	
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = ApiResponse.ERROR_ABORT;
			resultMap.put("status", apiResponse.status);
			resultMap.put("result", apiResponse.result);
			resultMap.put("message", apiResponse.message);
			return resultMap;
		}
		// 모든 작업이 정상적으로 끝나면 성공 결과로 리턴
		resultMap.put("status", apiResponse.status);
		resultMap.put("result", apiResponse.result);
		resultMap.put("message", apiResponse.message);
		return resultMap;
	}
	
	/**
	 * 비밀번호 변경
	 * 
	 * @param accountDTO
	 * @return status
	 */
	public Map<String, Object> updatePassword(AccountDTO accountDTO, Map<String, Object> resultMap) {
		// 기본 변수 선언
		ApiResponse apiResponse = ApiResponse.SUCCESS;
		Account account = null;
		// 계정의 유무 확인
		try {
			account = readAccount(accountDTO, account);
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = ApiResponse.ACCOUNT_DOES_NOT_EXISTS;
			resultMap.put("status", apiResponse.status);
			resultMap.put("result", apiResponse.result);
			resultMap.put("message", apiResponse.message);
			return resultMap;
		}
		// 계정의 상태가 BLOCKED인 경우 비밀번호 변경이 불가능
		if (account.getStatus() == Account.Status.BLOCKED) {
			apiResponse = ApiResponse.BLOCKED_ACCOUNT;
			resultMap.put("status", apiResponse.status);
			resultMap.put("result", apiResponse.result);
			resultMap.put("message", apiResponse.message);
			return resultMap;
		}
		// 비밀번호 변경
		if (encoder.matches(accountDTO.getOldPassword(), account.getPassword())) {
			// 해당 사용자의 비밀번호를 신규 비밀번호로 변경
			try {
				accountRepository.updatePassword(account.getId(), encoder.encode(accountDTO.getPassword()), 0, Account.Status.ACTIVE.getKey(), Account.UserStatus.OFFLINE.getKey());	
			} catch (Exception e) {
				e.printStackTrace();
				apiResponse = ApiResponse.ERROR_ABORT;
				resultMap.put("status", apiResponse.status);
				resultMap.put("result", apiResponse.result);
				resultMap.put("message", apiResponse.message);
			}
		} else {
			apiResponse = ApiResponse.PASSWORD_DOES_NOT_MATCHED;
		}
		// 결과 리턴
		resultMap.put("status", apiResponse.status);
		resultMap.put("result", apiResponse.result);
		resultMap.put("message", apiResponse.message);
		return resultMap;
	}

	/**
	 * 8자리 난수로 임시 비밀번호 생성
	 * 
	 * @return
	 */
	public String getTempPassword() {
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		String tempPassword = ""; // 임시 비밀번호 값
		int index = 0; // 임시 비밀번호로 생성할 난수 문자열의 index 값
		int length = 8; // 임시 비밀번호 길이

		for (int i = 0; i < length; i++) {
			index = (int) (charSet.length * Math.random());
			tempPassword += charSet[index];
		}

		log.info("**** 생성된 임시 비밀번호: {}", tempPassword);

		return tempPassword;
	}

	/**
	 * 이메일로 임시 비밀번호를 발송할 내용 설정
	 */
	public MailDTO setMail(String email, String tempPassword) {
		String title = "[OMS 발송] " + email + " 계정의 임시 비밀번호 안내 메일입니다.";
		String message = """
				안녕하세요. %email 계정의 임시 비밀번호를 아래와 같이 보내드립니다.
				임시 비밀번호로 접속하시면 비밀번호 초기화로 안내드리겠습니다.

				==================================
				◆ 계정의 임시 비밀번호: %tempPassword
				==================================

				감사합니다!
				""".replace("%tempPassword", tempPassword).replace("%email", email);

		MailDTO mailDTO = new MailDTO();
		mailDTO.setAddress(email);
		mailDTO.setTitle(title);
		mailDTO.setMessage(message);

		return mailDTO;
	}

	/**
	 * 임시 비밀번호를 이메일로 전송
	 * 
	 * @param mailDTO
	 */
	public void sendMail(MailDTO mailDTO) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(mailDTO.getAddress());
		simpleMailMessage.setFrom(FROM_ADDRESS);
		simpleMailMessage.setSubject(mailDTO.getTitle());
		simpleMailMessage.setText(mailDTO.getMessage());

		javaMailSender.send(simpleMailMessage);
	}

	/**
	 * 이메일 중복 체크
	 * 
	 * @param email
	 * @return Map<String, Object>
	 */
	public Map<String, Object> checkEmail(String email, Map<String, Object> resultMap) {
		ApiResponse apiResponse = ApiResponse.ACCOUNT_ALREADY_EXISTS;
		try {
			Optional<Account> account = accountRepository.findByEmail(email);
			if (!account.isPresent()) {
				apiResponse = ApiResponse.ACCOUNT_DOES_NOT_EXISTS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = ApiResponse.ERROR_ABORT;
		} finally {
			resultMap.put("status", apiResponse.status);
			resultMap.put("result", apiResponse.result);
			resultMap.put("message", apiResponse.message);
		}
		return resultMap;
	}
	
	/**
	 * 특정 상태(Status)인 계정 조회 (READ)
	 * 
	 * @return List<Account>
	 */
	public Map<String, Object> findByStatus(List<Status> accountStatus, Map<String, Object> resultMap) {
		ApiResponse apiResponse = ApiResponse.SUCCESS;
		List<Account> account = new ArrayList<Account>();;
		List<AccountDTO> accountDTO = new ArrayList<AccountDTO>();		
		// 특정 상태(Status)인 계정
		try {
			for (int s=0; s<accountStatus.size(); s++) {
				account = accountRepository.findByStatus(accountStatus.get(s));
				accountDTO.addAll(account.stream().map(a -> modelMapper.map(a, AccountDTO.class)).collect(Collectors.toList())) ;
			}
			resultMap.put("accountList", accountDTO);
			resultMap.put("accountSize", accountDTO.size());
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = ApiResponse.ERROR_ABORT;
		}
		// resultMap에 담기
		resultMap.put("status", apiResponse.status);
		resultMap.put("result", apiResponse.result);
		resultMap.put("message", apiResponse.message);		
		return resultMap;
	}
	
	/**
	 * 특정 UserStatus인 계정 조회 (READ)
	 * 
	 * @return List<Account>
	 */
	public Map<String, Object> findByUserStatus(List<UserStatus> userStatus, Map<String, Object> resultMap) {
		ApiResponse apiResponse = ApiResponse.SUCCESS;
		List<Account> account = new ArrayList<Account>();;
		List<AccountDTO> accountDTO = new ArrayList<AccountDTO>();
		// 특정 계정 목록 조회
		try {
			for (int u=0; u<userStatus.size(); u++) {
				account = accountRepository.findByUserStatus(userStatus.get(u));
				accountDTO.addAll(account.stream().map(a -> modelMapper.map(a, AccountDTO.class)).collect(Collectors.toList()));	
			}
			resultMap.put("accountList", accountDTO);
			resultMap.put("accountSize", accountDTO.size());
		} catch (Exception e) {
			apiResponse = ApiResponse.ERROR_ABORT;
		}
		// resultMap에 담기
		resultMap.put("status", apiResponse.status);
		resultMap.put("result", apiResponse.result);
		resultMap.put("message", apiResponse.message);
		return resultMap;
	}
	
	/**
	 * 사용자의 userStatus 변경
	 * (UPDATE)
	 * @return List<Account>
	 */
	public Map<String, Object> updateUserStatus(AccountDTO accountDTO, Map<String, Object> resultMap) {
		ApiResponse apiResponse = ApiResponse.SUCCESS;
		// 사용자의 상태 변경
		try {
			accountRepository.updateUserStatus(accountDTO.getEmail(), accountDTO.getUserStatus().getKey());
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = ApiResponse.ERROR_ABORT;
		}
		resultMap.put("status", apiResponse.status);
		resultMap.put("result", apiResponse.result);
		return resultMap;
	}

}

