package com.suturf.mockitosample.db;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;

import com.suturf.mockitosample.model.Books;
import com.suturf.mockitosample.model.Members;
import com.suturf.mockitosample.model.Transactions;

public class DbUtils {

	private static Nitrite db;

	static {
		db = Nitrite.builder().openOrCreate();
	}

	// Members
	public static void addMember(final Members member) {
		final ObjectRepository<Members> mrepo = db.getRepository(Members.class);
		mrepo.insert(member);
	}

	public static void delMember(final String memberId) {
		final ObjectRepository<Members> mrepo = db.getRepository(Members.class);
		mrepo.remove(ObjectFilters.eq("memberId", memberId));
	}

	public static Members findMemberById(final String memberId) {
		final ObjectRepository<Members> mrepo = db.getRepository(Members.class);
		return mrepo.find(ObjectFilters.eq("memberId", memberId)).firstOrDefault();
	}
	
	// Books
	public static void addBook(final Books books) {
		final ObjectRepository<Books> mrepo = db.getRepository(Books.class);
		mrepo.insert(books);
	}

	public static void delBook(final String bookId) {
		final ObjectRepository<Books> mrepo = db.getRepository(Books.class);
		mrepo.remove(ObjectFilters.eq("bookId", bookId));
	}

	public static Books findBookById(final String bookId) {
		final ObjectRepository<Books> mrepo = db.getRepository(Books.class);
		return mrepo.find(ObjectFilters.eq("bookId", bookId)).firstOrDefault();
	}
	
	// Transactions
	public static void addTransaction(final Transactions trans) {
		final ObjectRepository<Transactions> mrepo = db.getRepository(Transactions.class);
		mrepo.insert(trans);
	}

	public static void delTransaction(final String transId) {
		final ObjectRepository<Transactions> mrepo = db.getRepository(Transactions.class);
		mrepo.remove(ObjectFilters.eq("transId", transId));
	}

	public static Transactions findTransactionById(final String transId) {
		final ObjectRepository<Transactions> mrepo = db.getRepository(Transactions.class);
		return mrepo.find(ObjectFilters.eq("transId", transId)).firstOrDefault();
	}
}
