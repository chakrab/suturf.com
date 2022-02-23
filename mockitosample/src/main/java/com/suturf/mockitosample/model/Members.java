package com.suturf.mockitosample.model;

import lombok.Data;

@Data
public class Members {

	private String memberId;
	private String memberFirstName;
	private String memberLastName;
	private int    memberCountCheckouts;
}
