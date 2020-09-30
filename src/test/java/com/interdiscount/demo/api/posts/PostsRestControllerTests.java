package com.interdiscount.demo.api.posts;

import static com.interdiscount.demo.api.TestDataProvider.createPostDTO;
import static com.interdiscount.demo.api.TestDataProvider.generateString;
import static com.interdiscount.demo.api.posts.PostsRestController.REST_POST_PATH;
import static com.interdiscount.demo.domain.TestDataProvider.createPostEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.springframework.http.RequestEntity.delete;
import static org.springframework.http.RequestEntity.get;
import static org.springframework.http.RequestEntity.patch;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.interdiscount.demo.DemoApplicationTestBase;
import com.interdiscount.demo.domain.posts.PostEntity;
import com.interdiscount.demo.security.OAuthTokenRequestProvider;

public class PostsRestControllerTests extends DemoApplicationTestBase {
	
	@Autowired
	private OAuthTokenRequestProvider tokenProvider;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void createPostAuthenticatedAndAuthorized() throws URISyntaxException {
		
		String header = this.tokenProvider.loginHeader(this.properties.getAdminUsername(), this.properties.getAdminPassword());
		PostInputDTO post = createPostDTO();
		
		RequestEntity<PostInputDTO> request = post(new URI(REST_POST_PATH)).header(HttpHeaders.AUTHORIZATION, header).body(post);
		ResponseEntity<PostOutputDTO> response = this.restTemplate.exchange(request, PostOutputDTO.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getTitle()).isEqualTo(post.getTitle());
		assertThat(response.getBody().getContent()).isEqualTo(post.getContent());
		assertThat(response.getBody().getAuthor()).isEqualTo(this.properties.getAdminId());
		assertThat(response.getBody().getAuthorId()).isEqualTo(this.properties.getAdminId());
		assertThat(response.getBody().getPublished()).isNotNull();
		assertThat(response.getBody().getLink(IanaLinkRelations.SELF)).isNotNull();
	}
	
	@Test
	public void createPostAuthenticatedAndUnauthorized() throws URISyntaxException {
		
		String header = this.tokenProvider.loginHeader(this.properties.getUserUsername(), this.properties.getUserPassword());
		PostInputDTO post = createPostDTO();
		
		RequestEntity<PostInputDTO> request = post(new URI(REST_POST_PATH)).header(HttpHeaders.AUTHORIZATION, header).body(post);
		ResponseEntity<PostOutputDTO> response = this.restTemplate.exchange(request, PostOutputDTO.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void createPostUnauthenticated() throws URISyntaxException {
		
		RequestEntity<PostInputDTO> request = post(new URI(REST_POST_PATH)).body(createPostDTO());
		ResponseEntity<PostOutputDTO> response = this.restTemplate.exchange(request, PostOutputDTO.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/oauth2/authorization/procuration");
	}
	
	@Test
	public void getPostsAuthenticated() throws URISyntaxException {
		
		PostEntity entity = this.postRepostitory.save(createPostEntity());
		
		String header = this.tokenProvider.loginHeader(this.properties.getAdminUsername(), this.properties.getAdminPassword());
		RequestEntity<Void> request = get(new URI(REST_POST_PATH)).header(HttpHeaders.AUTHORIZATION, header).build();
		ResponseEntity<PagedModel<PostOutputDTO>> response = this.restTemplate.exchange(request, PostOutputDTO.LIST_TYPE);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getMetadata().getNumber()).isEqualTo(0);
		assertThat(response.getBody().getMetadata().getSize()).isEqualTo(10);
		assertThat(response.getBody().getMetadata().getTotalPages()).isEqualTo(1);
		assertThat(response.getBody().getMetadata().getTotalElements()).isEqualTo(1);
		assertThat(newArrayList(response.getBody().getContent()).get(0).getTitle()).isEqualTo(entity.getTitle());
		assertThat(newArrayList(response.getBody().getContent()).get(0).getContent()).isEqualTo(entity.getContent());
	}
	
	@Test
	public void getPostsUnauthenticated() throws URISyntaxException {
		
		PostEntity entity = this.postRepostitory.save(createPostEntity());
		
		RequestEntity<Void> request = get(new URI(REST_POST_PATH)).build();
		ResponseEntity<PagedModel<PostOutputDTO>> response = this.restTemplate.exchange(request, PostOutputDTO.LIST_TYPE);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getMetadata().getNumber()).isEqualTo(0);
		assertThat(response.getBody().getMetadata().getSize()).isEqualTo(10);
		assertThat(response.getBody().getMetadata().getTotalPages()).isEqualTo(1);
		assertThat(response.getBody().getMetadata().getTotalElements()).isEqualTo(1);
		assertThat(newArrayList(response.getBody().getContent()).get(0).getTitle()).isEqualTo(entity.getTitle());
		assertThat(newArrayList(response.getBody().getContent()).get(0).getContent()).isEqualTo(entity.getContent());
	}
	
	@Test
	public void getPostAuthenticated() {
		
		PostEntity entity = this.postRepostitory.save(createPostEntity());
		
		URI uri = fromPath(REST_POST_PATH).pathSegment(entity.getId().toString()).build().toUri();
		String header = this.tokenProvider.loginHeader(this.properties.getAdminUsername(), this.properties.getAdminPassword());
		
		RequestEntity<Void> request = get(uri).header(HttpHeaders.AUTHORIZATION, header).build();
		ResponseEntity<PostOutputDTO> response = this.restTemplate.exchange(request, PostOutputDTO.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo(entity.getTitle());
		assertThat(response.getBody().getContent()).isEqualTo(entity.getContent());
	}
	
	@Test
	public void getPostUnauthenticated() {
		
		PostEntity entity = this.postRepostitory.save(createPostEntity());
		URI uri = fromPath(REST_POST_PATH).pathSegment(entity.getId().toString()).build().toUri();
		
		RequestEntity<Void> request = get(uri).header(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE).build();
		ResponseEntity<PostOutputDTO> response = this.restTemplate.exchange(request, PostOutputDTO.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo(entity.getTitle());
		assertThat(response.getBody().getContent()).isEqualTo(entity.getContent());
	}
	
	@Test
	public void deletePostAuthenticatedAndAuthorized() {
		
		PostEntity entity = this.postRepostitory.save(createPostEntity());
		URI uri = fromPath(REST_POST_PATH).pathSegment(entity.getId().toString()).build().toUri();
		String header = this.tokenProvider.loginHeader(this.properties.getAdminUsername(), this.properties.getAdminPassword());
		
		RequestEntity<Void> request = delete(uri).header(HttpHeaders.AUTHORIZATION, header).build();
		ResponseEntity<Void> response = this.restTemplate.exchange(request, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void deletePostAuthenticatedAndUnauthorized() {
		
		PostEntity entity = this.postRepostitory.save(createPostEntity());
		URI uri = fromPath(REST_POST_PATH).pathSegment(entity.getId().toString()).build().toUri();
		String header = this.tokenProvider.loginHeader(this.properties.getUserUsername(), this.properties.getUserPassword());
		
		RequestEntity<Void> request = delete(uri).header(HttpHeaders.AUTHORIZATION, header).build();
		ResponseEntity<Void> response = this.restTemplate.exchange(request, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void deletePostUnauthenticated() {
		
		PostEntity entity = this.postRepostitory.save(createPostEntity());
		URI uri = fromPath(REST_POST_PATH).pathSegment(entity.getId().toString()).build().toUri();
		
		RequestEntity<Void> request = delete(uri).build();
		ResponseEntity<Void> response = this.restTemplate.exchange(request, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/oauth2/authorization/procuration");
	}
	
	@Test
	public void updatePostAuthenticatedAndAuthorizedTitle() {
		
		PostEntity entity = this.postRepostitory.save(createPostEntity());
		URI uri = fromPath(REST_POST_PATH).pathSegment(entity.getId().toString()).build().toUri();
		String header = this.tokenProvider.loginHeader(this.properties.getAdminUsername(), this.properties.getAdminPassword());
		
		PostInputDTO patch = createPostDTO(generateString(10), null);
		
		RequestEntity<PostInputDTO> request = patch(uri).header(HttpHeaders.AUTHORIZATION, header).body(patch);
		ResponseEntity<PostOutputDTO> response = this.restTemplate.exchange(request, PostOutputDTO.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo(patch.getTitle());
		assertThat(response.getBody().getContent()).isEqualTo(entity.getContent());
		assertThat(response.getBody().getAuthor()).isEqualTo(this.properties.getAdminId());
		assertThat(response.getBody().getAuthorId()).isEqualTo(this.properties.getAdminId());
		assertThat(response.getBody().getPublished()).isNotNull();
		assertThat(response.getBody().getLink(IanaLinkRelations.SELF)).isNotNull();
	}
	
	@Test
	public void updatePostAuthenticatedAndAuthorizedContent() {
		
		PostEntity entity = this.postRepostitory.save(createPostEntity());
		URI uri = fromPath(REST_POST_PATH).pathSegment(entity.getId().toString()).build().toUri();
		String header = this.tokenProvider.loginHeader(this.properties.getAdminUsername(), this.properties.getAdminPassword());
		
		PostInputDTO patch = createPostDTO(null, generateString(10));
		
		RequestEntity<PostInputDTO> request = patch(uri).header(HttpHeaders.AUTHORIZATION, header).body(patch);
		ResponseEntity<PostOutputDTO> response = this.restTemplate.exchange(request, PostOutputDTO.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo(entity.getTitle());
		assertThat(response.getBody().getContent()).isEqualTo(patch.getContent());
		assertThat(response.getBody().getAuthor()).isEqualTo(this.properties.getAdminId());
		assertThat(response.getBody().getAuthorId()).isEqualTo(this.properties.getAdminId());
		assertThat(response.getBody().getPublished()).isNotNull();
		assertThat(response.getBody().getLink(IanaLinkRelations.SELF)).isNotNull();
	}
	
	@Test
	public void updatePostAuthenticatedAndUnauthorized() {
		
		PostEntity entity = this.postRepostitory.save(createPostEntity());
		URI uri = fromPath(REST_POST_PATH).pathSegment(entity.getId().toString()).build().toUri();
		String header = this.tokenProvider.loginHeader(this.properties.getUserUsername(), this.properties.getUserPassword());
		
		PostInputDTO patch = createPostDTO(generateString(10), null);
		
		RequestEntity<PostInputDTO> request = patch(uri).header(HttpHeaders.AUTHORIZATION, header).body(patch);
		ResponseEntity<PostOutputDTO> response = this.restTemplate.exchange(request, PostOutputDTO.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void updatePostUnauthenticated() {
		
		PostEntity entity = this.postRepostitory.save(createPostEntity());
		URI uri = fromPath(REST_POST_PATH).pathSegment(entity.getId().toString()).build().toUri();
		
		PostInputDTO patch = createPostDTO(generateString(10), null);
		
		RequestEntity<PostInputDTO> request = patch(uri).body(patch);
		ResponseEntity<PostOutputDTO> response = this.restTemplate.exchange(request, PostOutputDTO.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/oauth2/authorization/procuration");
	}
}
