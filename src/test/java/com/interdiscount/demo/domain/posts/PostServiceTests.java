package com.interdiscount.demo.domain.posts;

import static com.interdiscount.demo.domain.TestDataProvider.generateString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.interdiscount.demo.DemoApplicationTestBase;
import com.interdiscount.demo.domain.TestDataProvider;

public class PostServiceTests extends DemoApplicationTestBase {

    @Autowired
    private PostService service;

    @Test
    public void createPostEntity() {
        String title = generateString(100);
        String content = generateString(100000);

        PostEntity post = this.service.createPost(title, content);

        assertThat(post.getId()).isNotNull();
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
    }
    
    @Test
    public void getById() {
    	PostEntity post = this.postRepostitory.save(TestDataProvider.createPostEntity());
        assertThat(this.service.getById(post.getId())).isEqualTo(post);
    }
    
    @Test
    public void getByInvalidId() {
    	
    	UUID id = UUID.randomUUID();
    	
    	try {
    		this.service.getById(id);
    		failBecauseExceptionWasNotThrown(EntityNotFoundException.class);
		} catch (EntityNotFoundException e) {
			assertThat(e.getMessage()).contains(id.toString());
		}
    }
    
    @Test
    public void deleteById() {
    	PostEntity post = this.postRepostitory.save(TestDataProvider.createPostEntity());
        assertThat(this.postRepostitory.existsById(post.getId())).isTrue();
        this.service.deleteById(post.getId());
        assertThat(this.postRepostitory.existsById(post.getId())).isFalse();
    }
    
    @Test
    public void deleteByInvalidId() {
    	
    	UUID id = UUID.randomUUID();
    	
    	try {
    		this.service.deleteById(id);
    		failBecauseExceptionWasNotThrown(EntityNotFoundException.class);
		} catch (EntityNotFoundException e) {
			assertThat(e.getMessage()).contains(id.toString());
		}
    }
    
    @Test
    public void updateTitleById() {
    	PostEntity post = this.postRepostitory.save(TestDataProvider.createPostEntity());
        assertThat(this.postRepostitory.existsById(post.getId())).isTrue();
        
        String newTitle = generateString(10);
        PostEntity updatePost = this.service.updatePost(post.getId(), newTitle, null);
        assertThat(updatePost.getTitle()).isEqualTo(newTitle);
        assertThat(this.postRepostitory.findById(post.getId()).get().getTitle()).isEqualTo(newTitle);
    }
    
    @Test
    public void updateContentById() {
    	PostEntity post = this.postRepostitory.save(TestDataProvider.createPostEntity());
        assertThat(this.postRepostitory.existsById(post.getId())).isTrue();
        
        String newContent = generateString(10);
        PostEntity updatePost = this.service.updatePost(post.getId(), null, newContent);
        assertThat(updatePost.getContent()).isEqualTo(newContent);
        assertThat(this.postRepostitory.findById(post.getId()).get().getContent()).isEqualTo(newContent);
    }
    
    @Test
    public void updateByInvalidId() {
    	
    	UUID id = UUID.randomUUID();
    	
    	try {
    		this.service.updatePost(id, null, generateString(10));
    		failBecauseExceptionWasNotThrown(EntityNotFoundException.class);
		} catch (EntityNotFoundException e) {
			assertThat(e.getMessage()).contains(id.toString());
		}
    }

}