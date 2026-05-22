package com.pao.laboratory12;

import com.pao.laboratory12.model.Author;
import com.pao.laboratory12.model.Book;
import com.pao.laboratory12.model.Reader;
import com.pao.laboratory12.repository.AuthorRepository;
import com.pao.laboratory12.repository.BookRepository;
import com.pao.laboratory12.repository.LoanRepository;
import com.pao.laboratory12.repository.ReaderRepository;
import com.pao.laboratory12.service.AuditService;
import com.pao.laboratory12.service.LibraryService;
import com.pao.laboratory12.util.DatabaseConnection;
import com.pao.laboratory12.util.SchemaInitializer;

import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        AuditService audit = AuditService.getInstance();
        AuthorRepository authorRepo = new AuthorRepository();
        BookRepository bookRepo = new BookRepository();
        ReaderRepository readerRepo = new ReaderRepository();
        LoanRepository loanRepo = new LoanRepository();
        LibraryService libraryService = LibraryService.getInstance();

        Connection conn = DatabaseConnection.getInstance().getConnection();
        SchemaInitializer.init(conn);

        Author author = new Author("Gabriel Garcia Marquez", "CO");
        authorRepo.save(author);
        audit.log("add_author");
        System.out.println("Autor: " + author);

        Book book1 = new Book("100 de ani de singuratate", author.getId());
        Book book2 = new Book("Dragostea in vremea holerei", author.getId());
        bookRepo.save(book1);
        bookRepo.save(book2);
        audit.log("add_book");
        System.out.println("Carti: " + book1 + ", " + book2);

        Reader reader = new Reader("Ion Popescu", "ion.popescu@email.com");
        readerRepo.save(reader);
        audit.log("add_reader");
        System.out.println("Cititor: " + reader);

        List<Book> allBooks = bookRepo.findAll();
        audit.log("list_books");
        System.out.println("Lista carti:");
        allBooks.forEach(System.out::println);

        bookRepo.findById(book1.getId()).ifPresentOrElse(
                b -> System.out.println("Carte: " + b),
                () -> System.out.println("Cartea nu a fost gasita.")
        );
        audit.log("find_book_by_id");

        book1.setTitle("100 de ani de singuratate (Ed. speciala)");
        bookRepo.update(book1);
        audit.log("update_book");
        System.out.println("Carte actualizata: " + book1);

        long loanId = libraryService.borrowBook(reader.getId(), book1.getId());
        audit.log("borrow_book");

        libraryService.returnBook(loanId);
        audit.log("return_book");

        List<String> activeLoans = libraryService.getActiveLoansWithDetails();
        audit.log("report_active_loans");
        System.out.println("Imprumuturi active:");
        if (activeLoans.isEmpty()) {
            System.out.println("niciuna");
        } else {
            activeLoans.forEach(System.out::println);
        }

        loanRepo.delete(loanId);
        readerRepo.delete(reader.getId());
        audit.log("delete_reader");
        System.out.println("Cititor sters.");

        System.out.println("Gata.");
        DatabaseConnection.getInstance().close();
    }
}
