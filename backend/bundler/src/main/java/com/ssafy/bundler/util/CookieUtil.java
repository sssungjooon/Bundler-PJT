package com.ssafy.bundler.util;

import java.util.Base64;
import java.util.Optional;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.SerializationUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookieUtil {

	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return Optional.of(cookie);
				}
			}
		}
		return Optional.empty();
	}

	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		log.info("addCookie 진입");

		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(maxAge);
		cookie.setDomain("localhost");

		log.info("cookie의 domain: " + cookie.getDomain());

		response.addCookie(cookie);

		if (name.equals("oauth2_auth_request")) {
			OAuth2AuthorizationRequest oAuth2AuthorizationRequest = deserialize(cookie,
				OAuth2AuthorizationRequest.class);
			log.info("getAuthorizationRequestUri : " + oAuth2AuthorizationRequest.getAuthorizationRequestUri());
			log.info("getClientId : " + oAuth2AuthorizationRequest.getClientId());
			log.info("getRedirectUri : " + oAuth2AuthorizationRequest.getRedirectUri());
			log.info("getAuthorizationUri : " + oAuth2AuthorizationRequest.getAuthorizationUri());
			log.info("getState : " + oAuth2AuthorizationRequest.getState());
		}

	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}

	public static String serialize(Object obj) {
		return Base64.getUrlEncoder()
			.encodeToString(SerializationUtils.serialize(obj));
	}

	public static <T> T deserialize(Cookie cookie, Class<T> cls) {
		return cls.cast(
			SerializationUtils.deserialize(
				Base64.getUrlDecoder().decode(cookie.getValue())
			)
		);
	}

}
