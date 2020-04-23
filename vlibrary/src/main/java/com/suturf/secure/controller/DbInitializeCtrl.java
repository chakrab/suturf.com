package com.suturf.secure.controller;

import com.suturf.secure.repo.DbInitRepo;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used for initializing H2 in memory database. It creates logging & book
 * tables.
 */
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/dbinit")
public class DbInitializeCtrl {

    private static final Logger LOG = LoggerFactory.getLogger(DbInitializeCtrl.class);

    protected final DbInitRepo dir;

    /**
     * Initializer
     * @param repo Initialize DbInit Repository
     */
    public DbInitializeCtrl(final DbInitRepo repo) {
        this.dir = repo;
    }

    /**
     * Function to add Data to tables
     * @return
     */
    @Get("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String addData() {

        try {
            dir.createTables();

            // Insert Login Users
            dir.insertLoginData("arthur", "password");
            dir.insertLoginData("bethany", "password");
            dir.insertLoginData("dolly", "password");
            dir.insertLoginData("phillip", "password");
            dir.insertLoginData("paulette", "password");

            // Insert Books
            dir.insertLibraryBook("9781514650462", "Treasure Island", "Robert Louis Stevenson", "A");
            dir.insertLibraryBook("9781645940098", "The Call of the Wild", "Jack London", "A");
            dir.insertLibraryBook("9780061124952", "Charlotte's Web", "E.B. White", "A");
            dir.insertLibraryBook("9781503219700", "A Tale of Two Cities", "Charles Dickens", "A");
            dir.insertLibraryBook("9780141439747", "Oliver Twist", "Charles Dickens", "A");
            dir.insertLibraryBook("9780142437230", "Don Quixote", "Cervantes", "A");
            dir.insertLibraryBook("9781515190998", "Picture of Dorian Gray", "Oscar Wilde", "A");
            dir.insertLibraryBook("9780151010264", "Animal Farm", "George Orwell", "A");
            dir.insertLibraryBook("9780060935467", "To Kill a Mockingbird", "Harper Lee", "A");
            dir.insertLibraryBook("9780679417392", "Nineteen-Eighty Four", "George Orwell", "A");

            return("Data initialized...");
        } catch (Exception e) {
            LOG.error("DbInitializeCtrl::addData", e);
            return e.getMessage();
        }
    }
}
