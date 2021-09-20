package com.bns.entites;

import javax.persistence.Lob;



public class Avatar {

	private String avatarId;

	private String fileName;

	private String fileType;

	@Lob
	private byte[] data;
}
