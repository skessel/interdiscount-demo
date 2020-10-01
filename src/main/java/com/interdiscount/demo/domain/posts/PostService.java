package com.interdiscount.demo.domain.posts;

import static java.util.Objects.nonNull;

import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PostService {

	private final PostEntityRepositoty postRepostory;

	@Autowired
	public PostService(PostEntityRepositoty postRepostory) {
		this.postRepostory = postRepostory;
	}

	public PostEntity createPost(String title, String content) {

		PostEntity post = new PostEntity();
		post.setTitle(title);
		post.setContent(content);

		return this.postRepostory.save(post);
	}

	public PostEntity updatePost(UUID id, String title, String content) {

		PostEntity entity = getById(id);
		if (nonNull(title)) {
			entity.setTitle(title);
		}
		if (nonNull(content)) {
			entity.setContent(content);
		}
		return this.postRepostory.save(entity);
	}

	public PostEntity getById(UUID id) {
		return this.postRepostory.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(
						"Post with id %s could not be found", id.toString())));
	}

	public void deleteById(UUID id) {
		this.postRepostory.delete(getById(id));
	}

	public Page<PostEntity> getPaged(int page, int size) {
		return this.postRepostory.findAll(PageRequest.of(page, size));
	}

}