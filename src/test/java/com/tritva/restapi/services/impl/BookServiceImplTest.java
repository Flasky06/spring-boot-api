package com.tritva.restapi.services.impl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.tritva.restapi.TestData.testBook;
import static com.tritva.restapi.TestData.testBookEntity;
import com.tritva.restapi.domain.Book;
import com.tritva.restapi.domain.BookEntity;
import com.tritva.restapi.repositories.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl underTest;

    @Test
    public void testThatBookIsSaved(){
        final Book book =testBook();

        final BookEntity bookEntity =testBookEntity();

        when(bookRepository.save(eq(bookEntity))).thenReturn(bookEntity);

        final Book result = underTest.save(book);
        assertEquals(book, result);
    }

    @Test
    public void testThatFindByIdReturnsEmptyWhenNoBook(){
        final String isbn = "123123123";

        when(bookRepository.findById(eq(isbn))).thenReturn(Optional.empty());

        final Optional<Book> result =  underTest.findById(isbn);

        assertEquals(Optional.empty(), result);
    }


    @Test
    public void testThatFindByIdReturnsBookWhenExists(){
        final Book book =testBook();
        final BookEntity bookEntity =testBookEntity();

        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.of(bookEntity));

        final Optional<Book> result =  underTest.findById(book.getIsbn());

        assertEquals(Optional.of(book), result);
    }

    @Test
    public void testListBooksReturnsEmptyListWhenNoBooksExists(){
        final List<Book> result = underTest.listBooks();
        assertEquals(0, result.size());
    }

    @Test 
    public void testListBooksReturnsBooksWhenExists(){

        final BookEntity bookEntity =testBookEntity();

        when(bookRepository.findAll()).thenReturn(List.of(bookEntity));

        final List<Book> result =underTest.listBooks();

        assertEquals(1, result.size());
    }

    @Test
    public void testsIsBookExistsReturnsFalseWhenBookDoesnExist(){     
        when(bookRepository.existsById(any())).thenReturn(false);
        final boolean result = underTest.isBookExists(testBook());
        assertEquals(false,result);
    }

    @Test
    public void testsIsBookExistsReturnsTrueWhenBookDoesnExist(){     
        when(bookRepository.existsById(any())).thenReturn(true);
        final boolean result = underTest.isBookExists(testBook());
        assertEquals(true,result);
    }

    @Test
    public void testDeleteBookDeletesBook(){
        final String isbn ="123123123";
        underTest.deleteBookById(isbn);
        verify(bookRepository,times(1)).deleteById(eq(isbn));
    }
    
}
