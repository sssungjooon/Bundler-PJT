package com.ssafy.bundler.service;

import com.ssafy.bundler.domain.Follow;
import com.ssafy.bundler.dto.user.FollowingListResponseDto;

public interface FollowService {

	public void followUser(Long fromUserId, Long toUserId); //fromUserId가 toUserId를 팔로잉

	public boolean unfollowUser(Long fromUserId, Long toUserId); //fromUserId가 toUserId를 언팔로잉

	public FollowingListResponseDto getUserFollowingList(Long userId) throws Exception; //user의 팔로잉 목록 조회

	public Follow getUserFollowerList(Long userId); //user의 팔로워 목록 조회

}
