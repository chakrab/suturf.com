package com.suturf.mockitosample.controller;

import com.suturf.mockitosample.service.BooksService;

import spark.Spark;

public class BooksController {

	public BooksController(final BooksService bookService) {
		Spark.post("/books", "application/json", (req, res) -> 
			bookService.addBook(req.body()), json());
	}
}
