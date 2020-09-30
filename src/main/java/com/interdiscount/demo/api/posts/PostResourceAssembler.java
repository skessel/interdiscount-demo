package com.interdiscount.demo.api.posts;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.interdiscount.demo.domain.posts.PostEntity;

@Component
public class PostResourceAssembler extends RepresentationModelAssemblerSupport<PostEntity, PostOutputDTO>{
	
  public PostResourceAssembler() {
    super(PostsRestController.class, PostOutputDTO.class);
  }


  @Override
  public PostOutputDTO toModel(PostEntity entity) {
    PostOutputDTO post = instantiateResource(entity);
    post.add(linkTo(getControllerClass()).slash(entity.getId())
      .withSelfRel()
      .withTitle(post.getTitle())
      .withType(post.getAuthor()));
    
    return post;
  }

	private static PostOutputDTO instantiateResource(PostEntity entity) {
		return new PostOutputDTO(entity.getId(), entity.getTitle(), entity.getContent(),
				entity.getCreatedBy(), entity.getCreatedBy(),
				entity.getCreateionDate());
	}

}