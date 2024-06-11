package com.tritva.restapi.services.impl;

import java.lang.foreign.Linker;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tritva.restapi.domain.Book;
import com.tritva.restapi.domain.BookEntity;
import com.tritva.restapi.repositories.BookRepository;
import com.tritva.restapi.services.BookService;

@Service
public class BookServiceImpl implements BookService {


    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(final BookRepository bookRepository){
        this.bookRepository =bookRepository;
    }
    
       @Override
    public Book create(final Book book){

        final BookEntity bookEntity = bookToBookEntity(book);

        final BookEntity saveBookEntity = bookRepository.save(bookEntity);

        return bookEntityToBook(saveBookEntity);
    }


    private BookEntity bookToBookEntity(Book book) {
    return BookEntity.builder()
        .isbn(book.getIsbn())
        .title(book.getTitle())
        .author(book.getAuthor())
        .build();
    }

   private Book bookEntityToBook(BookEntity bookEntity) {
    return Book.builder()
        .isbn(bookEntity.getIsbn())
        .title(bookEntity.getTitle())
        .author(bookEntity.getAuthor())
        .build();
    }

    @Override
    public Optional<Book> findById(String isbn){
        final Optional<BookEntity> foundBook = bookRepository.findById(isbn);

        return foundBook.map(book->bookEntityToBook(book));
    }
    
}

