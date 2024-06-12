package com.tritva.restapi.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext; // Import MediaType from org.springframework.http
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tritva.restapi.TestData;
import com.tritva.restapi.domain.Book;
import com.tritva.restapi.services.BookService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)

public class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;



    @Test
    public void testThatBookIsCreatedReturnsHttp200() throws Exception {
        final Book book = TestData.testBook();

        final ObjectMapper objectMapper = new ObjectMapper();

        final String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + book.getIsbn())
        .contentType(MediaType.APPLICATION_JSON)
        .content(bookJson))      
        .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatBookIsUpdatedReturnsHttp201() throws Exception {
        final Book book = TestData.testBook();
        bookService.save(book);

        book.setAuthor("Virginia Wolf");

        final ObjectMapper objectMapper = new ObjectMapper();

        final String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + book.getIsbn())
        .contentType(MediaType.APPLICATION_JSON)
        .content(bookJson))    
        .andExpect(MockMvcResultMatchers.status().isOk())  
        .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }


    @Test
    public void testThatRetrievedBookReturns404WhenBookNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/123123123")).andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void testThatRetrievedBookReturnsHttp200WhenExists()throws Exception {
        final Book book =TestData.testBook();
        bookService.save(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/"+ book.getIsbn())).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));

    }

    @Test
    public void testThatListBooksReturnsHttp200EmptyListWhenNoBooksExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string("[]"));
       
    }

    @Test
    public void testThatListBooksReturnsHttp200AndBooksWhenBooksExist() throws Exception{
        final Book book =TestData.testBook();
        bookService.save(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value(book.getTitle()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].author").value(book.getAuthor()));
        
    }

    @Test
    public void testThatHttp204isReturnedWhenBookDoesntExist() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/123123"))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void testThatHttp204IsReturnedWhenExistingBookIsDeleted()throws Exception{
        final Book book =TestData.testBook();
        bookService.save(book);

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/"+ book.getIsbn()))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
    }



   
}
