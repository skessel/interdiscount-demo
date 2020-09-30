package com.interdiscount.demo.domain;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.interdiscount.demo.domain.posts.PostEntity;

public class TestDataProvider {

    public static String generateString(int length) {
        return randomAlphanumeric(length);
    }

    public static String generateEmail() {
        return generateEmail(randomAlphanumeric(10));
    }

    public static String generateEmail(String prefix){
        return prefix.concat("@procuration.org");
    }

    public static PostEntity createPostEntity() {
        return createPostEntity(generateString(5));
    }

    public static PostEntity createPostEntity(String title) {
        return createPostEntity(title, generateString(10));
    }
    
    public static PostEntity createPostEntity(String title, String content) {
        PostEntity post = new PostEntity();
        post.setTitle(title);
        post.setContent(content);
        return post;
    }
    
}