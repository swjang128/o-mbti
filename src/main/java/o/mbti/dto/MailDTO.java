package o.mbti.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Email 전송을 위한 DTO
 * @author user
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailDTO {	
	private String address;			// 메일 주소
	private String title;					// 메일 제목
	private String message;			// 메일 내용
}
