package com.tritva.restapi.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Book save(final Book book){

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

    @Override
    public List<Book> listBooks(){
        final List <BookEntity> foundBooks = bookRepository.findAll();

        return foundBooks.stream().map(book->bookEntityToBook(book)).collect(Collectors.toList());

    }

    @Override
    public boolean isBookExists(Book book){
        return bookRepository.existsById(book.getIsbn());
    }


    
}

