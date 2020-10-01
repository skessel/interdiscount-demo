package com.interdiscount.demo.domain.posts;

import static com.interdiscount.demo.domain.TestDataProvider.generateString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionSystemException;

import com.interdiscount.demo.DemoApplicationTestBase;
import com.interdiscount.demo.domain.TestDataProvider;

public class PostEntityValidationTests extends DemoApplicationTestBase {

	@Test
	public void titleNull() {

		try {
			this.postRepostitory.save(TestDataProvider.createPostEntity(null));
			failBecauseExceptionWasNotThrown(TransactionSystemException.class);
		} catch (TransactionSystemException e) {
			assertThat(this.postRepostitory.count()).isZero();
		}
	}

	@Test
	public void titleEmpty() {

		try {
			this.postRepostitory.save(TestDataProvider.createPostEntity(""));
			failBecauseExceptionWasNotThrown(TransactionSystemException.class);
		} catch (TransactionSystemException e) {
			assertThat(this.postRepostitory.count()).isZero();
		}
	}

	@Test
	public void titleBlank() {

		try {
			this.postRepostitory.save(TestDataProvider.createPostEntity("   "));
			failBecauseExceptionWasNotThrown(TransactionSystemException.class);
		} catch (TransactionSystemException e) {
			assertThat(this.postRepostitory.count()).isZero();
		}
	}

	@Test
	public void contentNull() {

		try {
			this.postRepostitory.save(TestDataProvider.createPostEntity(generateString(10), null));
			failBecauseExceptionWasNotThrown(TransactionSystemException.class);
		} catch (TransactionSystemException e) {
			assertThat(this.postRepostitory.count()).isZero();
		}
	}

	@Test
	public void contentEmpty() {

		try {
			this.postRepostitory.save(TestDataProvider.createPostEntity(generateString(10), ""));
			failBecauseExceptionWasNotThrown(TransactionSystemException.class);
		} catch (TransactionSystemException e) {
			assertThat(this.postRepostitory.count()).isZero();
		}
	}

	@Test
	public void contentBlank() {

		try {
			this.postRepostitory.save(TestDataProvider.createPostEntity(generateString(10), "   "));
			failBecauseExceptionWasNotThrown(TransactionSystemException.class);
		} catch (TransactionSystemException e) {
			assertThat(this.postRepostitory.count()).isZero();
		}
	}
}