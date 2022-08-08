package o.mbti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	/**
	 * 인터셉터 Bean 주입
	 * @return
	 */
	@Bean
	public CommonInterceptor commonInterceptor() {
		return new CommonInterceptor();
	}
	
	/**
	 * 인터셉터에서 제외할 항목 설정
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(commonInterceptor())
				.order(1)
				.addPathPatterns("/**")
				.excludePathPatterns("/css/**", "/vendor/**", "/js/**", "/fonts/**", "/img/**", "/scss/**", "/svg/**", "/assets/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}
}
