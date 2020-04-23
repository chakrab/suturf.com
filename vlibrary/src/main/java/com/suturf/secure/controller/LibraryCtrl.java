package com.suturf.secure.controller;

import com.suturf.secure.dto.BookListBean;
import com.suturf.secure.repo.LibraryRepo;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the controller for Virtual Book Library
 */
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/books")
public class LibraryCtrl {

    private static final Logger LOG = LoggerFactory.getLogger(LibraryCtrl.class);

    protected final LibraryRepo lib;

    /**
     * Constructor
     * @param lb Injects Library Repository
     */
    public LibraryCtrl(final LibraryRepo lb) {
        this.lib = lb;
    }

    /**
     * Get a List of All books
     * @return BookListBean
     */
    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    public BookListBean getAllBooks() {

        return lib.getAllBooks();
    }

    /**
     * Get a list of all Available Books
     * @return BookListBean
     */
    @Get("/available")
    @Produces(MediaType.APPLICATION_JSON)
    public BookListBean getAvailableBooks() {

        return lib.getSelBooks("A");
    }

    /**
     * Get a list of all Checked Out books
     * @return BookListBean
     */
    @Get("/checkedout")
    @Produces(MediaType.APPLICATION_JSON)
    public BookListBean getCheckedOutBooks() {

        return lib.getSelBooks("U");
    }

    /**
     * Checkout a book
     * @param isbn ISBN for the book
     * @return String indicating Success or Failure
     */
    @Post("/checkout/{isbn}")
    @Produces(MediaType.TEXT_PLAIN)
    public String checkout(final String isbn) {

        return lib.updateStatus(isbn, "U") == 1?"Checkout Success":"Checkout Fail";
    }

    /**
     * Checkin a book
     * @param isbn ISBN for the book
     * @return String indicating Success or Failure
     */
    @Post("/checkin/{isbn}")
    @Produces(MediaType.TEXT_PLAIN)
    public String checkin(final String isbn) {

        return lib.updateStatus(isbn, "A") == 1?"Checkin Success":"Checkin Fail";
    }
}
