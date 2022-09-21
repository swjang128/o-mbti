package o.mbti.config;

import lombok.AllArgsConstructor;

/**
 * Rest API 결과로 보내줄 응답 메시지에 대한 정의
 * @author JSW
 *	status(Integer), result(String), message(String)
 */
@AllArgsConstructor
public enum ApiResponse {	
	/* HttpStatusCode */
	// Success (2xx)
	SUCCESS(200, "Success", "성공"),
	CREATED(201, "Created", "생성완료"),
	// Request Error (4xx)
	BAD_REQUEST(400, "Bad Request", "잘못된 요청입니다"),
	UNAUTHORIZED(401, "Unauthorized", "권한이 없습니다"),
	FORBIDDEN(403, "Forbidden", "페이지에 접근할 수 없습니다"),
	NOT_FOUND(404, "Not Found", "페이지를 찾을 수 없습니다"),
	METHOD_NOT_ALLOWED(405, "Method Not Allowed", "허용되지 않은 메소드입니다"),
	NOT_ACCEPTABLE(406, "Not Acceptable", "허용되지 않은 접근입니다"),
	PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required", "프록시 인증이 필요합니다"),
	REQUEST_TIMEOUT(408, "Request Timeout", "요청시간을 초과하였습니다"),
	CONFLICT(409, "Conflict", "요청 충돌이 발생하였습니다"),
	// Server Error (5xx)
	INTERNAL_SERVER_ERROR(500, "Internal Server Error", "내부 서버 오류가 발생하였습니다"),
	NOT_IMPLEMENTED(501, "Not Implemented", "서버에 요청을 수행할 수 있는 기능이 없습니다"),
	BAD_GATEWAY(502, "Bad Gateway", "서버가 게이트웨이로부터 잘못된 응답을 받았습니다"),
	SERVICE_UNAVAILABLE(503, "Service Unavailable", "서비스를 이용할 수 없습니다"),
	GATEWAY_TIMEOUT(504, "Gateway Timeout", "게이트웨이에서 요청시간을 초과했습니다"),
	HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported", "지원하지않는 HTTP 버전입니다"),
	// Database Error (6xx)
	DB_ERROR(600, "DB Error", "데이터베이스 에러가 발생하였습니다"),
	
	/* O-MBTI Custom Response Code */
	// Common (9xx)
	EMPTY_RESULT_DATA_ACCESS(904, "Empty Result Data Access", "비어있는 데이터에 접근하였습니다"),
	ERROR_ABORT(999, "Error Abort", "에러가 발생하였습니다"),
	// Account (1xxx)
	ACCOUNT_ALREADY_EXISTS(1000, "Account Already Exists", "계정이 이미 존재합니다"),
	EXPIRED_ACCOUNT(1003, "Expired Account", "계정이 만료되었습니다. 비밀번호를 변경해주세요"),
	ACCOUNT_DOES_NOT_EXISTS(1004, "Account Does Not Exist", "존재하지 않는 계정입니다"),	
	PASSWORD_DOES_NOT_MATCHED(1005, "Password Does Not Matched", "비밀번호가 일치하지 않습니다"),
	BAD_CREDENTIALS(1014, "The Account Does Not Exist Or The Password Does Not Matched", "계정이 없거나 비밀번호가 일치하지 않습니다"),
	BLOCKED_ACCOUNT(1023, "Blocked Account", "계정이 잠겨있습니다. 비밀번호 초기화 후 진행해주세요"),
	DISABLED_ACCOUNT(1024, "Disabled Account", "비활성화된 계정입니다");		
	
	public int status;	
	public String result;
	public String message;
}
	
