package com.inventorsoft.junit.repository;

import com.inventorsoft.junit.model.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BookRepositoryTest {

    private final BookRepository bookRepository = new BookRepository();

    @Test
    void save() {
        Book book = new Book();
        bookRepository.save(book);
        assertNotNull(book.getId());
    }

    @Test
    void testSaveWhenIdIsPresent() {
        Book book = new Book();
        book.setId(1L);
        bookRepository.save(book);
        assertEquals(1L, book.getId());
    }
}