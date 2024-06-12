package com.tritva.restapi.services;

import java.util.List;
import java.util.Optional;

import com.tritva.restapi.domain.Book;

public interface BookService {
    
        Book create(Book book);

        Optional<Book> findById(String isbn);

        List<Book> listBooks();

}
