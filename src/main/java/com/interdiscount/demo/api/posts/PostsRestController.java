package com.interdiscount.demo.api.posts;

import static com.interdiscount.demo.api.ApiConstants.DEFAULT_PAGE;
import static com.interdiscount.demo.api.ApiConstants.DEFAULT_PAGE_SIZE;
import static com.interdiscount.demo.api.ApiConstants.PARAM_PAGE;
import static com.interdiscount.demo.api.ApiConstants.PARAM_SIZE;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.interdiscount.demo.domain.posts.PostEntity;
import com.interdiscount.demo.domain.posts.PostService;

@RestController
@RequestMapping(path = PostsRestController.REST_POST_PATH)
public class PostsRestController {

	public final static String REST_POST_PATH = "/rest/posts";

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final PostService postService;

	private final RepresentationModelAssemblerSupport<PostEntity, PostOutputDTO> resourceAssembler;
	private final PagedResourcesAssembler<PostEntity> pageResourceAssembler;

	@Autowired
	public PostsRestController(PostService postService,
			RepresentationModelAssemblerSupport<PostEntity, PostOutputDTO> resourceAssembler,
			PagedResourcesAssembler<PostEntity> pageResourceAssembler) {
		this.postService = postService;
		this.resourceAssembler = resourceAssembler;
		this.pageResourceAssembler = pageResourceAssembler;
	}

	@ResponseBody
	@ResponseStatus(CREATED)
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = HAL_JSON_VALUE)
	public PostOutputDTO createPost(@RequestBody @Valid PostInputDTO post) {

		this.logger.info("Create post {}", post);
		PostEntity postEntity = this.postService.createPost(post.getTitle(), post.getContent());
		PostOutputDTO postDTO = this.resourceAssembler.toModel(postEntity);
		this.logger.info("Post created {}", postDTO);
		return postDTO;
	}

	@ResponseBody
	@ResponseStatus(OK)
	@PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = HAL_JSON_VALUE)
	public PostOutputDTO updatePost(@PathVariable("id") UUID id,
			@RequestBody PostInputDTO post) {

		this.logger.info("Update post {}", post);
		PostEntity postEntity = this.postService.updatePost(id, post.getTitle(), post.getContent());
		PostOutputDTO postDTO = this.resourceAssembler.toModel(postEntity);
		this.logger.info("Post updated {}", postDTO);
		return postDTO;
	}

	@ResponseBody
	@ResponseStatus(OK)
	@GetMapping(path = "/{id}", produces = HAL_JSON_VALUE)
	public PostOutputDTO get(@PathVariable("id") UUID id) {

		this.logger.info("Get post with {}", id);
		PostEntity entity = this.postService.getById(id);
		PostOutputDTO dto = this.resourceAssembler.toModel(entity);
		this.logger.info("Post found {}", dto);
		return dto;
	}

	@ResponseBody
	@ResponseStatus(OK)
	@GetMapping(produces = HAL_JSON_VALUE)
	public PagedModel<PostOutputDTO> getPaged(
			@RequestParam(value = PARAM_PAGE, defaultValue = DEFAULT_PAGE, required = false) Integer page,
			@RequestParam(value = PARAM_SIZE, defaultValue = DEFAULT_PAGE_SIZE, required = false) Integer size) {

		this.logger.info("Get Posts with page {} and size {}", page, size);
		Link self = linkTo(methodOn(getClass()).getPaged(page, size)).withSelfRel();

		Page<PostEntity> posts = this.postService.getPaged(page, size);
		PagedModel<PostOutputDTO> results = this.pageResourceAssembler.toModel(posts, this.resourceAssembler, self);

		return results;
	}

	@ResponseBody
	@ResponseStatus(OK)
	@DeleteMapping(path = "/{id}")
	public void delete(@PathVariable("id") UUID id) {
		this.logger.info("Delete post with {}", id);
		this.postService.deleteById(id);
		this.logger.info("Post with id {} deleted", id);
	}

}