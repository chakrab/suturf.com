package com.suturf.mockitosample.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data 
public class Transactions {

	private String transId;
	private String memberId;
	private String bookId;
	private LocalDateTime dateCheckout = LocalDateTime.now();
	private LocalDateTime dateReturn = dateCheckout.plusDays(15);
}
