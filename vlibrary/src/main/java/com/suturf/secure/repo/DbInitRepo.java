package com.suturf.secure.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Singleton
public class DbInitRepo {

    private final static Logger LOG = LoggerFactory.getLogger(DbInitRepo.class);

    private final static String CR_LGN_TBL =
            "create table logintbl (usr varchar(30) not null, pwd varchar(30) not null)";
    private final static String CR_LBK_TBL = "create table lbooktbl " +
            "(isbn char(25) not null, name varchar(80) not null, " +
            "author varchar(80) not null, status char(1))";
    private final static String IN_LGN_DATA = "insert into logintbl values (?, ?)";
    private final static String IN_LBK_DATA = "insert into lbooktbl values (?, ?, ?, ?)";

    @Inject
    private DataSource ds;

    public DbInitRepo() {

    }

    public void createTables()
    throws SQLException {

        try (final Connection con = ds.getConnection();
             final Statement st = con.createStatement()) {

            // Create tables
            st.execute(CR_LGN_TBL);
            st.execute(CR_LBK_TBL);
        }
    }

    public void insertLoginData(final String uid, final String pwd)
    throws SQLException {

        try (final Connection con = ds.getConnection();
             final PreparedStatement ps = con.prepareStatement(IN_LGN_DATA)) {

            ps.setString(1, uid);
            ps.setString(2, pwd);
            ps.executeUpdate();
        }
    }

    public void insertLibraryBook(final String isbn, final String name, final String author, final String stat)
    throws SQLException {

        try (final Connection con = ds.getConnection();
             final PreparedStatement ps = con.prepareStatement(IN_LBK_DATA)) {

            ps.setString(1, isbn);
            ps.setString(2, name);
            ps.setString(3, author);
            ps.setString(4, stat);
            ps.executeUpdate();
        }
    }
}
