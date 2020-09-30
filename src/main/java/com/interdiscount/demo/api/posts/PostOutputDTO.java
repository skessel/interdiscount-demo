package com.interdiscount.demo.api.posts;

import java.time.Instant;
import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Relation(value = "post", collectionRelation = "posts")
public class PostOutputDTO extends RepresentationModel<PostOutputDTO> {

	public static final ParameterizedTypeReference<PagedModel<PostOutputDTO>> LIST_TYPE = new ParameterizedTypeReference<>() { /** */ };
	
	@JsonProperty("id")
	private final UUID uuid;

	@JsonProperty("title")
	private final String title;

	@JsonProperty("content")
	private final String content;

	@JsonProperty("authorId")
	private final String authorId;

	@JsonProperty("author")
	private final String author;

	@JsonProperty("published")
	private final Instant published;

	@JsonCreator
	public PostOutputDTO(@JsonProperty("id") UUID uuid, 
			@JsonProperty("title") String title,
			@JsonProperty("content") String content,
			@JsonProperty("authorId") String authorId,
			@JsonProperty("author") String author,
			@JsonProperty("published") Instant published) {
		super();
		this.uuid = uuid;
		this.title = title;
		this.content = content;
		this.authorId = authorId;
		this.author = author;
		this.published = published;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
	
	public String getAuthorId() {
		return authorId;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public Instant getPublished() {
		return published;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.JSON_STYLE);
	}
	
}