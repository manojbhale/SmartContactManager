package com.bns.entites;

import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractTimestampEntity {

	@CreationTimestamp
	@JsonIgnore
	private Date createdDate;

	@CreatedBy
	private String createdBy;

	@LastModifiedBy
	private String updatedBy;

	@UpdateTimestamp
	@JsonIgnore
	private Date updatedDate;
}
