package com.interdiscount.demo.domain.posts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.interdiscount.demo.domain.AbstractEntity;

/**
 * PostEntity
 */
@Entity(name = "Post")
@Table(name = "posts")
public class PostEntity extends AbstractEntity {

	@NotBlank
	@Column(name = "title", length = 255, nullable = false)
	private String title;

	@NotBlank
	@Column(name = "content", nullable = false, length = 100000)
	private String content;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}