package o.mbti.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rest API Common Response DTO
 * @author jsw
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseDTO {	
	private Integer status;				// Status
	private String result;				// Result
	private String message;			// Message
}
