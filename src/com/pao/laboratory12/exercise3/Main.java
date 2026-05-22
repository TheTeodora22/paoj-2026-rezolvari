package com.pao.laboratory12.exercise3;

import com.pao.laboratory12.model.Author;
import com.pao.laboratory12.model.Book;
import com.pao.laboratory12.model.Reader;
import com.pao.laboratory12.repository.AuthorRepository;
import com.pao.laboratory12.repository.BookRepository;
import com.pao.laboratory12.repository.ReaderRepository;
import com.pao.laboratory12.service.LibraryService;
import com.pao.laboratory12.util.DatabaseConnection;
import com.pao.laboratory12.util.SchemaInitializer;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        System.out.println("Baza: " + detectDb(conn.getMetaData().getURL()));

        SchemaInitializer.init(conn);

        AuthorRepository authorRepo = new AuthorRepository();
        BookRepository bookRepo = new BookRepository();
        ReaderRepository readerRepo = new ReaderRepository();
        LibraryService lib = LibraryService.getInstance();

        Author author = new Author("Mihai Eminescu", "RO");
        authorRepo.save(author);
        Book book = new Book("Luceafarul", author.getId());
        bookRepo.save(book);
        Reader reader = new Reader("Ana Ionescu", "ana@email.com");
        readerRepo.save(reader);

        lib.borrowBook(reader.getId(), book.getId());

        System.out.println("Imprumuturi active:");
        lib.getActiveLoansWithDetails().forEach(System.out::println);

        System.out.println("Top carti:");
        lib.getTopBorrowedBooksWithAuthor().forEach(System.out::println);

        System.out.println("Per cititor:");
        lib.getLoansCountPerReader().forEach(System.out::println);

        DatabaseConnection.getInstance().close();
        System.out.println("Gata.");
    }

    private static String detectDb(String url) {
        if (url.contains("mysql")) {
            return "MySQL";
        }
        if (url.contains("sqlite")) {
            return "SQLite";
        }
        if (url.contains("h2")) {
            return "H2";
        }
        return "necunoscuta";
    }
}
