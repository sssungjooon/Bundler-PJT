package com.ssafy.bundler.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.bundler.common.ApiResponse;
import com.ssafy.bundler.config.auth.UserPrincipal;
import com.ssafy.bundler.domain.User;
import com.ssafy.bundler.dto.user.FollowingListResponseDto;
import com.ssafy.bundler.dto.user.Profile;
import com.ssafy.bundler.dto.user.UserCalendarResponseDto;
import com.ssafy.bundler.dto.user.UserMypageResponseDto;
import com.ssafy.bundler.dto.user.UserUpdateRequestDto;
import com.ssafy.bundler.service.FollowService;
import com.ssafy.bundler.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@Autowired
	private FollowService followService;

	@GetMapping
	public ApiResponse getUser() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();

		User user = userService.getUser(principal.getUsername());

		return ApiResponse.success("user", user);
	}

	public ResponseEntity<List<Profile>> getUserList(@RequestParam String keyword) {
		// PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();
		// System.out.println("principal : " + principal.getUser().getUserId());
		// System.out.println("principal : " + principal.getUser().getUserNickname());
		// System.out.println("principal : " + principal.getUser().getUserPassword());

		return ResponseEntity.ok(userService.getUserListByUserNickname(keyword));
	}

	// @GetMapping
	// public String user(Authentication authentication) {
	// 	PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();
	// 	System.out.println("principal : " + principal.getUser().getUserId());
	// 	System.out.println("principal : " + principal.getUser().getUserNickname());
	// 	System.out.println("principal : " + principal.getUser().getUserPassword());
	//
	// 	return "<h1>user</h1>";
	// }

	//회원 정보 수정
	@PutMapping("/{userId}")
	public ResponseEntity updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto) {
		userService.updateUser(userUpdateRequestDto);
		return ResponseEntity.ok().build();
	}

	//회원 정보 삭제
	@DeleteMapping("/{userId}")
	public ResponseEntity deleteUser(Authentication authentication, @PathVariable Long userId) {

		UserPrincipal principal = (UserPrincipal)authentication.getPrincipal();

		if (principal.getUserId().equals(userId)) {
			System.out.println("성공");
			userService.deleteUser(userId);
		}

		return ResponseEntity.ok().build();

	}

	//fromUserId가 toUserId를 팔로잉
	@PostMapping("/{fromUserId}/following/users/{toUserId}")
	public ResponseEntity followUser(@PathVariable Long fromUserId, @PathVariable Long toUserId) {
		followService.followUser(fromUserId, toUserId);
		return ResponseEntity.ok().build();
	}

	//fromUserId가 toUserId를 언팔로잉
	@DeleteMapping("/{fromUserId}/following/users/{toUserId}")
	public ResponseEntity unfollowUser(@PathVariable Long fromUserId, @PathVariable Long toUserId) {
		if (followService.unfollowUser(fromUserId, toUserId)) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.internalServerError().build();
	}

	//user의 팔로잉 목록 조회
	@GetMapping("/{userId}/followings")
	public ResponseEntity<FollowingListResponseDto> getUserFollowingList(@PathVariable Long userId) throws
		Exception {

		return ResponseEntity.ok(followService.getUserFollowingList(userId));
	}

	//user의 팔로우 목록 조회
	// @GetMapping("/{userId}/followers")
	// public ResponseEntity getUserFollowerList(@PathVariable Long userId) {
	//
	// }

	@GetMapping("/{userId}/mypage")
	public ResponseEntity<UserMypageResponseDto> mypage(@PathVariable Long userId) {

		UserCalendarResponseDto calendar = userService.getDayFeedCount(userId);
		UserMypageResponseDto mypageResponseDto = UserMypageResponseDto.builder().userCalendar(calendar).build();

		User user = userService.getUserByUserId(userId);
		mypageResponseDto.userInit(user);

		return ResponseEntity.ok(mypageResponseDto);
	}
}
