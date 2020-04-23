package com.suturf.secure.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class LoginRepo {

    private final static Logger LOG = LoggerFactory.getLogger(LoginRepo.class);

    private final static String SL_VAL_USERS = "select usr, pwd from logintbl " +
            "where usr = ?";

    @Inject
    private DataSource ds;

    public LoginRepo() {

    }

    public boolean validateUser(final String usr, final String pwd)
    throws SQLException {

        try (final Connection con = ds.getConnection();
             final PreparedStatement ps = con.prepareStatement(SL_VAL_USERS)) {

            ps.setString(1, usr);
            try (final ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final String pstore = rs.getString("pwd");
                    return pstore.equals(pwd);
                }
            }
        }

        return false;
    }
}
