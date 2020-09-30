package com.interdiscount.demo.domain.posts;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PostEntityRepositoty
 */
public interface PostEntityRepositoty extends JpaRepository<PostEntity, UUID> { /** */ }