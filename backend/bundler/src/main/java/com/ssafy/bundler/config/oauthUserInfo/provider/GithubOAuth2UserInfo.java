package com.ssafy.bundler.config.oauthUserInfo.provider;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

	public GithubOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() { //id = 67770370
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getName() { //login = KangHyojin1401
		return (String)attributes.get("login");
	}

	@Override
	public String getEmail() { //email = khj6354@naver.com
		return (String)attributes.get("email");
	}

	@Override
	public String getImageUrl() { //avatar_url = https://avatars.githubusercontent.com/u/67770370?v=4
		return (String)attributes.get("avatar_url");
	}

	public String getGithubUrl() { //html_url = https://github.com/KangHyojin1401
		return (String)attributes.get("html_url");
	}

}
