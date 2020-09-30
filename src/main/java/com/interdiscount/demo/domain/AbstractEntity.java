package com.interdiscount.demo.domain;

import static java.util.Objects.nonNull;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class AbstractEntity implements Comparable<AbstractEntity> {

    @Id
	@GeneratedValue
	@Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @CreatedBy
    @Column(name = "creation_user", length = 255, updatable = false, nullable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modification_user", length = 255, updatable = true, nullable = false)
    private String modifiedBy;

    @CreatedDate
    @Column(name = "creation_date", updatable = false, nullable = false)
    private Instant createionDate;

    @LastModifiedDate
    @Column(name = "modification_date", updatable = true, nullable = false)
    private Instant modificationDate;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreateionDate() {
        return createionDate;
    }

    public Instant getModificationDate() {
        return modificationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    @Override
    public boolean equals(Object obj) {

        if (Objects.isNull(obj) || !getClass().isInstance(obj)) {
            return false;
        }

        AbstractEntity other = AbstractEntity.class.cast(obj);
        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).toHashCode();
    }

    @Override
    public int compareTo(AbstractEntity o) {
        return new CompareToBuilder().append(this.getClass(), o.getClass()).append(this.id, o.id).toComparison();
    }

    @Override
    public String toString() {
        return getClass().getName().concat("--> ").concat(nonNull(this.id) ? this.id.toString() : "unsaved");
    }

}