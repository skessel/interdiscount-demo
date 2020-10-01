package com.interdiscount.demo.api.posts;

import static com.interdiscount.demo.api.ApiConstants.PARAM_PAGE;
import static com.interdiscount.demo.api.ApiConstants.PARAM_SIZE;
import static com.interdiscount.demo.api.posts.PostsRestController.REST_POST_PATH;
import static com.interdiscount.demo.domain.TestDataProvider.createPostEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import java.net.URI;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.interdiscount.demo.DemoApplicationTestBase;

public class PostsRestControllerPagingTests extends DemoApplicationTestBase {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void whenResourcesAreRetrievedPaged_then200IsReceived() {
		createEntities(5);
		RequestEntity<Void> request = RequestEntity.get(createPageUri(0, 2)).build();
		ResponseEntity<PagedModel<PostOutputDTO>> response = this.restTemplate.exchange(request, PostOutputDTO.LIST_TYPE);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getContent()).isNotEmpty();
	}

	@Test
	public void whenPageOfResourcesAreRetrievedOutOfBounds_then200IsReceived() {
		createEntities(5);
		RequestEntity<Void> request = RequestEntity.get(createPageUri(10, 2)).build();
		ResponseEntity<PagedModel<PostOutputDTO>> response = this.restTemplate.exchange(request, PostOutputDTO.LIST_TYPE);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getContent()).isEmpty();
	}

	@Test
	public void givenResourcesExist_whenFirstPageIsRetrieved_thenPageContainsResources() {
		createEntities(1);
		RequestEntity<Void> request = RequestEntity.get(createPageUri(0, 2)).build();
		ResponseEntity<PagedModel<PostOutputDTO>> response = this.restTemplate.exchange(request, PostOutputDTO.LIST_TYPE);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getContent()).hasSize(1);
	}

	@Test
	public void whenFirstPageOfResourcesAreRetrieved_thenSecondPageIsNext() {
		createEntities(5);
		RequestEntity<Void> request = RequestEntity.get(createPageUri(0, 2)).build();
		ResponseEntity<PagedModel<PostOutputDTO>> response = this.restTemplate.exchange(request, PostOutputDTO.LIST_TYPE);

		assertThat(response.getBody().getLink(IanaLinkRelations.NEXT)).isPresent();
		assertThat(toURI(response.getBody().getLink(IanaLinkRelations.NEXT))).hasQuery(createPageUri(1, 2).getQuery());
	}

	@Test
	public void whenFirstPageOfResourcesAreRetrieved_thenNoPreviousPage() {
		createEntities(5);
		RequestEntity<Void> request = RequestEntity.get(createPageUri(0, 2)).build();
		ResponseEntity<PagedModel<PostOutputDTO>> response = this.restTemplate.exchange(request, PostOutputDTO.LIST_TYPE);

		assertThat(response.getBody().getLink(IanaLinkRelations.PREV)).isEmpty();
	}

	@Test
	public void whenSecondPageOfResourcesAreRetrieved_thenFirstPageIsPrevious() {
		createEntities(5);
		RequestEntity<Void> request = RequestEntity.get(createPageUri(1, 2)).build();
		ResponseEntity<PagedModel<PostOutputDTO>> response = this.restTemplate.exchange(request, PostOutputDTO.LIST_TYPE);

		assertThat(response.getBody().getLink(IanaLinkRelations.PREV)).isNotNull();
		assertThat(toURI(response.getBody().getLink(IanaLinkRelations.PREV))).hasQuery(createPageUri(0, 2).getQuery());
	}

	@Test
	public void whenLastPageOfResourcesIsRetrieved_thenNoNextPageIsDiscoverable() {
		createEntities(0);
		RequestEntity<Void> request = RequestEntity.get(createPageUri(0, 2)).build();
		ResponseEntity<PagedModel<PostOutputDTO>> response = this.restTemplate.exchange(request, PostOutputDTO.LIST_TYPE);

		assertThat(response.getBody().getLink(IanaLinkRelations.LAST)).isEmpty();
		assertThat(response.getBody().getLink(IanaLinkRelations.NEXT)).isEmpty();
	}

	private void createEntities(int count) {
		Stream.generate(() -> createPostEntity()).limit(count).forEach(entity -> this.postRepostitory.save(entity));
	}

	private static URI createPageUri(int page, int size) {
		return fromPath(REST_POST_PATH).queryParam(PARAM_PAGE, page).queryParam(PARAM_SIZE, size).build().toUri();
	}

}
