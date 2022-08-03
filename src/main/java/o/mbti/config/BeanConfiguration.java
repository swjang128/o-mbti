package o.mbti.config;

import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import lombok.extern.slf4j.Slf4j;

/**
 * Bean 의존성 주입
 * @author user
 *
 */
@Configuration
@Slf4j
public class BeanConfiguration {
	@Value("${spring.profiles.active}")
	private String profile;
	/**
	 * 다국어 처리 Bean 주입(Application이 기동될 때 Initialize)
	 */
	@Bean
	public SessionLocaleResolver localeResolver() {
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		sessionLocaleResolver.setDefaultLocale(Locale.KOREA);	// 기본 언어값: 한국어
		log.info("****** Language Pack: {}", Locale.KOREA);
		log.info("****** Activated spring.profiles.active: {}", profile);
		
		return sessionLocaleResolver;
	}
	
	/**
	 * ModelMapper Bean 주입
	 * @return
	 */
	@Bean
	public ModelMapper modelMapper() {
		log.info("****** Model Mapper Activated...");
		return new ModelMapper();
	}
	
	/**
	 * BCryptPasswordEncoder Bean 주입
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder encryptPassword() {
		log.info("****** Spring Security BCyrptPasswordEncoder Activated...");
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * MappingJackson2JsonView Bean 주입
	 * @return
	 */
	@Bean
	public MappingJackson2JsonView jsonView() {
		log.info("****** MappingJackson2JsonView Activated...");
		return new MappingJackson2JsonView();
	}
}
