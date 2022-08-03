package o.mbti.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import o.mbti.entity.Account;
import o.mbti.entity.Account.Status;
import o.mbti.entity.Account.UserStatus;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
	/*********************** JPA Native Queries ***********************/
	static final String UPDATE_ALL_USER_STATUS = """
			UPDATE TB_ACCOUNT
			SET USER_STATUS = :userStatus
			  """;
	static final String UPDATE_LAST_LOGIN_TIME = """
			UPDATE TB_ACCOUNT
			SET LAST_LOGIN_TIME = :lastLoginTime
			WHERE EMAIL = :email
			  """;
	static final String UPDATE_FAIL_COUNT = """
			UPDATE TB_ACCOUNT
			SET FAIL_COUNT = :failCount
			WHERE EMAIL = :email
			""";	
	static final String UPDATE_STATUS = """
			UPDATE TB_ACCOUNT
			SET STATUS = :status
			WHERE EMAIL = :email
			""";
	static final String UPDATE_USER_STATUS = """
			UPDATE TB_ACCOUNT
			SET USER_STATUS = :userStatus
			WHERE EMAIL = :email
			""";
	static final String UPDATE_PASSWORD = """
			UPDATE TB_ACCOUNT
			SET PASSWORD = :password,
					STATUS = :status,
					USER_STATUS = :userStatus,
					FAIL_COUNT = :failCount
			WHERE ID = :id
			""";
	/**
	 * 모든 유저의 UserStatus를 변경
	 * @param String UserStatus
	 */
	@Transactional
	@Modifying
	@Query(value=UPDATE_ALL_USER_STATUS, nativeQuery=true)
	public void updateAllUserStatus(@Param("userStatus") String userStatus);
	
	/**
	 * 특정 사용자의 UserStatus를 변경
	 * @param String email
	 * @param String userStatus
	 */
	@Transactional
	@Modifying
	@Query(value=UPDATE_USER_STATUS, nativeQuery=true)
	public void updateUserStatus(@Param("email") String email, @Param("userStatus") String userStatus);
	
	/**
	 * 사용자가 로그인하면 마지막 로그인 시간 값을 UPDATE
	 * @param email
	 * @param lastLoginTime
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(value=UPDATE_LAST_LOGIN_TIME, nativeQuery=true)
	public void updateLastLogin(@Param("email") String email, @Param("lastLoginTime") LocalDateTime lastLoginTime);
	
	/**
	 * FAIL_COUNT 수정
	 * @param email
	 * @param lastLoginTime
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(value=UPDATE_FAIL_COUNT, nativeQuery=true)
	public void updateFailCount(@Param("email") String email, @Param("failCount") Integer failCount);
	
	/**
	 * 계정의 상태 변경
	 * @param email
	 * @param lastLoginTime
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(value=UPDATE_STATUS, nativeQuery=true)
	public void updateStatus(@Param("email") String email, @Param("status") String status);
	
	/**
	 * 계정의 비밀번호  변경
	 * @param email
	 * @param lastLoginTime
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(value=UPDATE_PASSWORD, nativeQuery=true)
	public void updatePassword(@Param("id") Long id, @Param("password") String password, @Param("failCount") Integer failCount, @Param("status") String status, @Param("userStatus") String userStatus);
	
	/*********************** JPA  ***********************/
	Optional<Account> findByEmail(String email); // 해당 유저가 있는지 확인
	boolean existsByEmail(String email); // 해당 email을 가진 유저가 있는지 확인
	List<Account> findByStatus(Status status);	// 특정 Status의 유저를 조회
	
	List<Account> findByUserStatus(UserStatus userStatus);	// 특정 UserStatus의 유저를 조회
}
