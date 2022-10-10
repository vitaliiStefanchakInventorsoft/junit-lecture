package com.inventorsoft.junit.repository;

import com.inventorsoft.junit.model.Author;
import com.inventorsoft.junit.model.Book;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookRepositoryTest {

    final BookRepository bookRepository = new BookRepository();

    static final long AUTHOR_ID = 35L;
    static final String AUTHOR_NAME = "Leo Tolstoy";
    static final LocalDate AUTHOR_BIRTHDAY = LocalDate.of(1828, 9, 9);

    static final long BOOK_ID = 5L;
    static final String BOOK_TITLE = "War and Peace";
    static final LocalDate BOOK_RELEASE_DATE = LocalDate.of(1869, 5, 23);
    static final String BOOK_DESCRIPTION = "Text";

    static Author author;
    static Book book;
    static Map<Long, Book> bookMap;

    @BeforeEach
    public void setUp() {
        author = new Author();
        author.setName(AUTHOR_NAME);
        author.setBirthday(AUTHOR_BIRTHDAY);
        author.setId(AUTHOR_ID);

        book = new Book();
        book.setReleaseDate(BOOK_RELEASE_DATE);
        book.setTitle(BOOK_TITLE);
        book.setDescription(BOOK_DESCRIPTION);
        book.setId(BOOK_ID);
        book.setAuthor(author);

        bookMap = new LinkedHashMap<>();
        bookMap.put(1L, book);

        ReflectionTestUtils.setField(bookRepository, "bookMap", bookMap);
    }

    @AfterAll
    static void clear() {
        author = null;
        book = null;
        bookMap = null;
    }

    @Test
    void deleteByIdWhenGetBookId() {
        assertEquals(1, bookMap.size());
        bookRepository.deleteById(1L);
        assertEquals(0, bookMap.size());
    }

    @Test
    void existsByTitleShouldReturnTrueWhenExists() {
        boolean exists = bookRepository.existsByTitle(BOOK_TITLE);
        assertTrue(exists);
    }

    @Test
    void existsByTitleShouldReturnFalseWhenNotExists() {
        boolean exists = bookRepository.existsByTitle("unknown");
        assertFalse(exists);
    }

    @Test
    void findByIdShouldReturnOptionalBookWhenIdExists() {
        var result = bookRepository.findById(1L);
        var expected = Optional.of(book);
        assertEquals(expected,result);
    }

    @Test
    void findByIdShouldReturnOptionalEmptyWhenIdNotExists() {
        var result = bookRepository.findById(55L);
        var expected = Optional.empty();
        assertEquals(expected,result);
    }

    @Test
    void findAllShouldReturnListOfBook() {
        List<Book> expected = List.of(book);
        List<Book> given = bookRepository.findAll();
        assertArrayEquals(expected.toArray(),given.toArray());
    }

    @Test
    void saveShouldThrowNullPointerExceptionWhenEntityIsNull(){
        Exception exception = assertThrows(NullPointerException.class, () ->
                bookRepository.save(null));
        assertEquals("Cannot invoke \"com.inventorsoft.junit.model.Book.getId()\" " +
                "because \"entity\" is null", exception.getMessage());
    }

    @Test
    void saveShouldReturnBookWhenGiveBook(){
        int size = bookMap.size();
        book.setId(2L);
        var result = bookRepository.save(book);
        assertEquals(size+1,bookMap.size());
        assertEquals(book,result);
    }

    @Test
    void saveWhenBookIdNull(){
        int size = bookMap.size();
        book.setId(null);
        var result = bookRepository.save(book);
        assertEquals(size+1,bookMap.size());
        assertEquals(book,result);
        assertEquals(2L,book.getId());
    }
}