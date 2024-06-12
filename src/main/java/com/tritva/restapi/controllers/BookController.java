package com.tritva.restapi.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tritva.restapi.domain.Book;
import com.tritva.restapi.services.BookService;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(final BookService bookService){
        this.bookService = bookService;
    }

    @PutMapping(path="/books/{isbn}")
    public ResponseEntity<Book> createUpdateBook( @PathVariable final String isbn,
    @RequestBody final Book book){
        book.setIsbn(isbn);

        final boolean isBookExists = bookService.isBookExists(book);

        final Book savedBook =bookService.save(book);

        if(isBookExists){               
            return new ResponseEntity<Book>(savedBook,HttpStatus.OK);

        }else{
            return new ResponseEntity<Book>(savedBook,HttpStatus.CREATED);
        }

        
    }

    @GetMapping(path="/books/{isbn}")
    public ResponseEntity<Book> retrievedBook(@PathVariable final String isbn){
        final Optional<Book> foundBook = bookService.findById(isbn);

        return foundBook.map(book->new ResponseEntity<Book>(book,HttpStatus.OK))
        .orElse(new ResponseEntity<Book>(HttpStatus.NOT_FOUND));  
    }

    @GetMapping(path="/books")
    public ResponseEntity<List<Book>> listBooks(){
        return new ResponseEntity<List<Book>>(bookService.listBooks(),HttpStatus.OK);

    }


    
}
