package com.suturf.mockitosample.service;

import com.suturf.mockitosample.db.DbUtils;
import com.suturf.mockitosample.model.Books;

public class BooksService {

	public Books addBook(final Books book) {
		DbUtils.addBook(book);
		return book;
	}
	
	public Books findBook(final String bookId) {
		return DbUtils.findBookById(bookId);
	}
	
	public Books deleteBook(final String bookId) {
		final Books book = DbUtils.findBookById(bookId);
		if (book != null)
			DbUtils.delBook(bookId);
		return book;
	}
}
