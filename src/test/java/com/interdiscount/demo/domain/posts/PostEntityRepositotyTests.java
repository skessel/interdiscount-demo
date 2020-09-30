package com.interdiscount.demo.domain.posts;

import static com.interdiscount.demo.domain.TestDataProvider.createPostEntity;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.interdiscount.demo.DemoApplicationTestBase;
import com.interdiscount.demo.domain.TestDataProvider;

public class PostEntityRepositotyTests extends DemoApplicationTestBase {

    @Test
    public void create() {

        PostEntity post = createPostEntity();
        PostEntity entity = this.postRepostitory.save(post);

        assertThat(entity.getId()).isNotNull();
        assertThat(entity.getCreatedBy()).isNotNull();
        assertThat(entity.getModifiedBy()).isNotNull();
        assertThat(entity.getCreateionDate()).isNotNull();
        assertThat(entity.getModificationDate()).isNotNull();
        assertThat(entity.getTitle()).isEqualTo(post.getTitle());
        assertThat(entity.getContent()).isEqualTo(post.getContent());
    }

    @Test
    public void read() {

        PostEntity entity = this.postRepostitory.save(createPostEntity());

        assertThat(this.postRepostitory.findById(entity.getId())).isPresent();
        assertThat(this.postRepostitory.findById(entity.getId()).get()).isEqualTo(entity);
    }

    @Test
    public void readList() {

        PostEntity entity1 = this.postRepostitory.save(createPostEntity("bbb"));
        PostEntity entity2 = this.postRepostitory.save(createPostEntity("aaa"));
        assertThat(this.postRepostitory.findAll()).containsExactlyInAnyOrder(entity1, entity2);
    }

    @Test
    public void readPage() {

        PostEntity entity1 = this.postRepostitory.save(createPostEntity("bbb"));
        PostEntity entity2 = this.postRepostitory.save(createPostEntity("aaa"));
        PostEntity entity3 = this.postRepostitory.save(createPostEntity("ddd"));
        PostEntity entity4 = this.postRepostitory.save(createPostEntity("ccc"));
        
        PageRequest page1 = PageRequest.of(0, 2, Sort.by(Direction.DESC, "title"));
        PageRequest page2 = PageRequest.of(1, 2, Sort.by(Direction.DESC, "title"));

        assertThat(this.postRepostitory.findAll(page1)).containsExactly(entity3, entity4);
        assertThat(this.postRepostitory.findAll(page2)).containsExactly(entity1, entity2);
    }

    @Test
    public void udpdate() {

        String title = TestDataProvider.generateString(10);
        String updateTitle = TestDataProvider.generateString(10);

        PostEntity entity = this.postRepostitory.save(createPostEntity(title));

        assertThat(this.postRepostitory.findById(entity.getId())).isPresent();
        assertThat(this.postRepostitory.findById(entity.getId()).get()).isEqualTo(entity);
        assertThat(this.postRepostitory.findById(entity.getId()).get().getTitle()).isEqualTo(title);

        entity.setTitle(updateTitle);
        entity = this.postRepostitory.save(entity);

        assertThat(this.postRepostitory.findById(entity.getId())).isPresent();
        assertThat(this.postRepostitory.findById(entity.getId()).get()).isEqualTo(entity);
        assertThat(this.postRepostitory.findById(entity.getId()).get().getTitle()).isEqualTo(updateTitle);
    }

    @Test
    public void delete() {

        PostEntity entity = this.postRepostitory.save(createPostEntity());
        assertThat(this.postRepostitory.existsById(entity.getId())).isTrue();

        this.postRepostitory.delete(entity);
        assertThat(this.postRepostitory.existsById(entity.getId())).isFalse();
    }

}