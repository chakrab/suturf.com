package com.suturf.secure.repo;

import com.suturf.secure.dto.BookBean;
import com.suturf.secure.dto.BookListBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.*;

@Singleton
public class LibraryRepo {

    private final static Logger LOG = LoggerFactory.getLogger(LibraryRepo.class);

    private final static String SL_ALL_BOOKS =
            "select isbn, name, author from lbooktbl order by name";
    private final static String SL_SEL_BOOKS =
            "select isbn, name, author from lbooktbl where status = ? order by name";
    private final static String UP_STT_BOOKS =
            "update lbooktbl set status = ? where isbn = ?";

    @Inject
    private DataSource ds;

    public LibraryRepo() {

    }

    public BookListBean getAllBooks() {

        final BookListBean lbn = new BookListBean();
        try (final Connection con = ds.getConnection();
             final Statement st = con.createStatement();
             final ResultSet rs = st.executeQuery(SL_ALL_BOOKS)) {

            while (rs.next()) {
                final BookBean bk = new BookBean();
                bk.setBookIsbn(rs.getString("isbn"));
                bk.setBookName(rs.getString("name"));
                bk.setBookAuthor(rs.getString("author"));
                lbn.getBooks().add(bk);
            }
            lbn.setRetCode(true);
        } catch (SQLException e) {
            LOG.error("LibraryRepo::getAllBooks", e);
            lbn.setRetCode(false);
        }

        return lbn;
    }

    public BookListBean getSelBooks(final String flg) {

        final BookListBean lbn = new BookListBean();
        try (final Connection con = ds.getConnection();
             final PreparedStatement ps = con.prepareStatement(SL_SEL_BOOKS)) {

            ps.setString(1, flg);
            try (final ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final BookBean bk = new BookBean();
                    bk.setBookIsbn(rs.getString("isbn"));
                    bk.setBookName(rs.getString("name"));
                    bk.setBookAuthor(rs.getString("author"));
                    lbn.getBooks().add(bk);
                }
            }
            lbn.setRetCode(true);
        } catch (SQLException e) {
            LOG.error("LibraryRepo::getSelBooks", e);
            lbn.setRetCode(false);
        }

        return lbn;
    }

    public int updateStatus(final String isbn, final String flg) {

        try (final Connection con = ds.getConnection();
             final PreparedStatement ps = con.prepareStatement(UP_STT_BOOKS)) {

            ps.setString(1, flg);
            ps.setString(2, isbn);
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("LibraryRepo::updateStatus", e);
            return -1;
        }
    }
}
