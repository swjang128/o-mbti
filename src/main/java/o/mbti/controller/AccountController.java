package o.mbti.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import o.mbti.dto.AccountRequestDTO;
import o.mbti.service.AccountService;

/**
 * 계정 관련 Rest API
 * 
 * @author jsw
 *
 */
@RestController
@RequestMapping("account")
public class AccountController {
	@Autowired
	AccountService accountService;

	/**
	 * 계정 정보 Read
	 * @return Map<String, Object>
	 */
	@GetMapping("")
	public Map<String, Object> result(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
		// 기본 변수 설정
		Map<String, Object> result = new HashMap<String, Object>();

		return accountService.read(accountRequestDTO, result);
	}

}
