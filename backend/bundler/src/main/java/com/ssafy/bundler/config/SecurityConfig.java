package com.ssafy.bundler.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ssafy.bundler.config.auth.AuthTokenProvider;
import com.ssafy.bundler.config.properties.AppProperties;
import com.ssafy.bundler.domain.UserRole;
import com.ssafy.bundler.exception.RestAuthenticationEntryPoint;
import com.ssafy.bundler.filter.TokenAuthenticationFilter;
import com.ssafy.bundler.handler.CustomLogoutHandler;
import com.ssafy.bundler.handler.CustomLogoutSuccessHandler;
import com.ssafy.bundler.handler.OAuth2AuthenticationFailureHandler;
import com.ssafy.bundler.handler.OAuth2AuthenticationSuccessHandler;
import com.ssafy.bundler.handler.TokenAccessDeniedHandler;
import com.ssafy.bundler.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.ssafy.bundler.repository.UserRefreshTokenRepository;
import com.ssafy.bundler.repository.UserRepository;
import com.ssafy.bundler.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// https://github.com/spring-projects/spring-security/issues/10822 참고
@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

	// private final PrincipalOauth2UserService principalOauth2UserService;
	// private final UserRepository userRepository;

	// private final CorsConfig corsConfig;

	////////////////////////////////
	// private final AuthenticationManager authenticationManager;
	// private final CorsProperties corsProperties;
	private final AppProperties appProperties;
	private final AuthTokenProvider tokenProvider;
	// private final CustomUserDetailsService userDetailsService;

	private final CustomOAuth2UserService oAuth2UserService;
	private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
	private final UserRefreshTokenRepository userRefreshTokenRepository;
	private final UserRepository userRepository;

	//로그아웃
	private final CustomLogoutSuccessHandler logoutSuccessHandler;
	private final CustomLogoutHandler logoutHandler;
	////////////////////////////////

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http

			.cors(httpSecurityCorsConfigurer ->
				httpSecurityCorsConfigurer
					.configurationSource(corsConfigurationSource())
			)

			.sessionManagement(httpSecuritySessionManagementConfigurer ->
				httpSecuritySessionManagementConfigurer
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)

			.csrf(httpSecurityCsrfConfigurer ->
				httpSecurityCsrfConfigurer
					.disable()
			)

			.formLogin(httpSecurityFormLoginConfigurer ->
				httpSecurityFormLoginConfigurer
					.disable()
			)

			.httpBasic(httpSecurityHttpBasicConfigurer ->
				httpSecurityHttpBasicConfigurer
					.disable()
			)

			.exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
				httpSecurityExceptionHandlingConfigurer
					.authenticationEntryPoint(new RestAuthenticationEntryPoint())
					.accessDeniedHandler(tokenAccessDeniedHandler)
			)

			// .apply(new MyCustomDsl()) // 커스텀 필터 등록
			// .and()

			// .securityMatcher("/api/**")
			.authorizeHttpRequests(authroize ->
				authroize
					.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
					.requestMatchers("/auth/admin/**").hasAuthority(UserRole.ADMIN.getCode())
					// .requestMatchers("/auth/manager/**").hasRole("MANAGER")
					.requestMatchers("/auth/**").hasAuthority(UserRole.USER.getCode())
					.anyRequest().permitAll()
			)

			// .userDetailsService(userDetailsService)

			.logout(httpSecurityLogoutConfigurer ->
				httpSecurityLogoutConfigurer
					.logoutUrl("/api/v1/logout")
					.deleteCookies("refreshToken")
					.clearAuthentication(true)
					// .addLogoutHandler(logoutHandler)
					.addLogoutHandler(logoutHandler)
					.logoutSuccessHandler(logoutSuccessHandler)
			)

			.oauth2Login(httpSecurityOAuth2LoginConfigurer ->
				httpSecurityOAuth2LoginConfigurer
					.authorizationEndpoint(authorizationEndpointConfig ->
						authorizationEndpointConfig
							.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
					)

					.redirectionEndpoint(redirectionEndpointConfig ->
						redirectionEndpointConfig
							.baseUri("/login/oauth2/code/*")
					)

					.userInfoEndpoint(userInfoEndpointConfig ->
						userInfoEndpointConfig
							.userService(oAuth2UserService)
					)

					.successHandler(oAuth2AuthenticationSuccessHandler())
					.failureHandler(oAuth2AuthenticationFailureHandler())
			)

			.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

			.build();
	}

	/*
	 * auth 매니저 설정
	 * */
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws
		Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(
			List.of("http://localhost:5500", "http://localhost:3000", "http://127.0.0.1/3000",
				"http://i8a810.p.ssafy.io:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(List.of("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		// source.registerCorsConfiguration("/api/**", configuration);

		return source;
	}

	public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
				AuthenticationManagerBuilder.class);
			AuthenticationManager authenticationManager = authenticationManagerBuilder.getSharedObject(
				AuthenticationManager.class);
			// authenticationManagerBuilder
			// 	.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

			// LogoutConfigurer logoutConfigurer = http.getSharedObject(LogoutConfigurer.class);

			// http
			// 	.addFilter(corsConfig.corsFilter());
			// .addFilter(new JwtAuthenticationFilter(authenticationManager))
			// .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
		}
	}

	/*
	 * 토큰 필터 설정
	 * */
	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter(tokenProvider);
	}

	/*
	 * 쿠키 기반 인가 Repository
	 * 인가 응답을 연계 하고 검증할 때 사용.
	 * */
	@Bean
	public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
		return new OAuth2AuthorizationRequestBasedOnCookieRepository();
	}

	/*
	 * Oauth 인증 성공 핸들러
	 * */
	@Bean
	public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
		log.debug(OAuth2AuthenticationSuccessHandler.class + ": success");

		return OAuth2AuthenticationSuccessHandler.builder()
			.tokenProvider(tokenProvider)
			.appProperties(appProperties)
			.userRefreshTokenRepository(userRefreshTokenRepository)
			// .authorizationRequestRepository()
			.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
			.build();
	}

	/*
	 * Oauth 인증 실패 핸들러
	 * */
	@Bean
	public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
		log.debug(OAuth2AuthenticationFailureHandler.class + ": fail");
		return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
	}

}
