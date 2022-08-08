package o.mbti.specification;

import java.time.LocalDate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import o.mbti.entity.Account;
import lombok.extern.slf4j.Slf4j;

/**
 * Account Specification 클래스
 * @author jsw
 *
 */
@Slf4j
public class AccountSpecification {
	/**
	 * WHERE status in (List<Status> status)
	 * @param status
	 * @return
	 */
	public static Specification<Account> findByStatus(Object status) {
		return new Specification<Account>() {
			private static final long serialVersionUID = -587488762192925433L;
			@Override
			public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get("status").in(status);
			}
		};
	}
	
	/**
	 * WHERE user_status in (List<UserStatus> userStatus)
	 * @param userStatus
	 * @return
	 */
	public static Specification<Account> findByUserStatus(Object userStatus) {
		return new Specification<Account>() {
			private static final long serialVersionUID = -587488762192925433L;
			@Override
			public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get("userStatus").in(userStatus);
			}
		};
	} 
	
	/**
	 * WHERE role in (List<Role> role)
	 * @param role
	 * @return
	 */
	public static Specification<Account> findByRole(Object role) {
		return new Specification<Account>() {
			private static final long serialVersionUID = -587488762192925433L;
			@Override
			public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get("role").in(role);
			}
		};
	} 
	/**
	 * WHERE department in (List<String> department)
	 * @param department
	 * @return
	 */
	public static Specification<Account> findByDepartment(Object department) {
		return new Specification<Account>() {
			private static final long serialVersionUID = -587488762192925433L;
			@Override
			public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get("department").in(department);
			}
		};
	} 
	/**
	 * WHERE position in (List<String> position)
	 * @param position
	 * @return
	 */
	public static Specification<Account> findByPosition(Object position) {
		return new Specification<Account>() {
			private static final long serialVersionUID = -587488762192925433L;
			@Override
			public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get("position").in(position);
			}
		};
	}
	/**
	 * WHERE hireDate in (List<String> position)
	 * @param position
	 * @return
	 */
	public static Specification<Account> findByHireDate(LocalDate startDate, LocalDate endDate) {
		return new Specification<Account>() {
			private static final long serialVersionUID = -587488762192925433L;
			@Override
			public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (startDate != null && endDate == null) {
					return criteriaBuilder.greaterThanOrEqualTo(root.get("hireDate"), startDate);
				} else if (startDate == null && endDate !=null) {
					return criteriaBuilder.lessThanOrEqualTo(root.get("hireDate"), endDate);
				} else if (startDate != null && endDate != null) {
					return criteriaBuilder.between(root.get("hireDate"), startDate, endDate);	
				} else {
					return null;
				}
			}
		};
	}
}
