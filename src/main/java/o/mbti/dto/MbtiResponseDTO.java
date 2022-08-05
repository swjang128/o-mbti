package o.mbti.dto;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import o.mbti.config.MbtiResponse;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MbtiResponseDTO {
	@NotBlank(message="type이 공백")
	@Size(min=4, max=4, message="4개의 문자열 고정")
	private String type;				// Type
	
	@NotBlank(message="title이 공백")	
	private String title;				// Title
	
	@NotBlank(message="content이 공백")
	private String content;		// Content
	
}
