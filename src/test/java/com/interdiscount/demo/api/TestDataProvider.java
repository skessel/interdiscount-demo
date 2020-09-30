package com.interdiscount.demo.api;

import com.interdiscount.demo.api.posts.PostInputDTO;

public class TestDataProvider {

	public static String generateString(int length) {
		return com.interdiscount.demo.domain.TestDataProvider.generateString(length);
	}

	public static String generateEmail() {
		return generateEmail(generateString(10));
	}

	public static String generateEmail(String prefix) {
		return com.interdiscount.demo.domain.TestDataProvider.generateEmail(prefix);
	}
	
	public static PostInputDTO createPostDTO() {
		return createPostDTO(generateString(10));
	}
	
	public static PostInputDTO createPostDTO(String title) {
		return createPostDTO(title, generateString(30));
	}
	
	public static PostInputDTO createPostDTO(String title, String content) {
		return new PostInputDTO(title, content);
	}

}
