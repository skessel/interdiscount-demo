package com.interdiscount.demo;

import java.net.URI;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.hateoas.Link;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.util.UriComponentsBuilder;

import com.interdiscount.demo.domain.posts.PostEntityRepositoty;

@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
public abstract class DemoApplicationTestBase {
	
	@Autowired
	protected DemoApplicationProperties properties;
	
	@Autowired
	protected PostEntityRepositoty postRepostitory;
	
	@Autowired
	protected ClientRegistrationRepository clientRegistrationRepository;
	
	@BeforeEach
	@AfterEach
	public void cleanUp(){
		this.postRepostitory.deleteAll();
	}
	
	public URI toURI(Optional<Link> link) {
		return link.isPresent() ? UriComponentsBuilder.fromHttpUrl(link.get().getHref()).build().toUri() : null;
	}
	
}
