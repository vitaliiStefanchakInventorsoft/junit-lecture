package com.inventorsoft.junit.util.validation;

import com.inventorsoft.junit.dto.request.CreateAuthorRequest;
import com.inventorsoft.junit.dto.request.CreateBookRequest;
import com.inventorsoft.junit.repository.AuthorRepository;
import com.inventorsoft.junit.repository.BookRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookValidatorConstraintTest {

    @Mock AuthorRepository authorRepository;
    @Mock BookRepository bookRepository;
    @InjectMocks BookValidatorConstraint validator;

    static final long AUTHOR_ID = 35L;
    static final String AUTHOR_NAME = "Leo Tolstoy";
    static final LocalDate AUTHOR_BIRTHDAY = LocalDate.of(1828, 9, 9);

    static final String BOOK_TITLE = "War and Peace";
    static final LocalDate BOOK_RELEASE_DATE = LocalDate.of(1869, 5, 23);
    static final String BOOK_DESCRIPTION = """
             War and Peace broadly focuses on Napoleon’s invasion of Russia in 1812 and
             follows three of the most well-known characters in literature: Pierre Bezukhov, the illegitimate
             son of a count who is fighting for his inheritance and yearning for spiritual fulfillment;
             Prince Andrei Bolkonsky, who leaves his family behind to fight in the war against Napoleon;
             and Natasha Rostov, the beautiful young daughter of a nobleman who intrigues both men.
            """;

    static CreateAuthorRequest authorRequest = new CreateAuthorRequest();
    static CreateBookRequest bookRequest = new CreateBookRequest();

    @BeforeEach
    void startInit() {
        authorRequest.setName(AUTHOR_NAME);
        authorRequest.setBirthday(AUTHOR_BIRTHDAY);

        bookRequest.setTitle(BOOK_TITLE);
        bookRequest.setDescription(BOOK_DESCRIPTION);
        bookRequest.setReleaseDate(BOOK_RELEASE_DATE);
        bookRequest.setAuthorId(AUTHOR_ID);
    }

    @AfterAll
    static void clear() {
        authorRequest = null;
        bookRequest = null;
    }

    @Test
    void isValidShouldReturnTrue() {
        when(authorRepository.existsById(bookRequest.getAuthorId())).thenReturn(true);
        boolean isValid = validator.isValid(bookRequest, null);
        assertTrue(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenTitleIsNull() {
        bookRequest.setTitle(null);
        boolean isValid = validator.isValid(bookRequest, null);
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenTitleIsEmpty() {
        bookRequest.setTitle("");
        boolean isValid = validator.isValid(bookRequest, null);
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenTitleIsExist() {
        when(bookRepository.existsByTitle(bookRequest.getTitle())).thenReturn(true);
        boolean isValid = validator.isValid(bookRequest, null);
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenReleaseDateIsNull() {
        bookRequest.setReleaseDate(null);
        boolean isValid = validator.isValid(bookRequest, null);
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenReleaseDateIsFuture() {
        bookRequest.setReleaseDate(LocalDate.of(2075, 5, 23));
        boolean isValid = validator.isValid(bookRequest, null);
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnTrueWhenReleaseDateIsOldThanNow() {
        bookRequest.setReleaseDate(LocalDate.of(2005, 5, 23));
        when(authorRepository.existsById(bookRequest.getAuthorId())).thenReturn(true);
        boolean isValid = validator.isValid(bookRequest, null);
        assertTrue(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenAuthorIdIsNull() {
        bookRequest.setAuthorId(null);
        boolean isValid = validator.isValid(bookRequest, null);
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenAuthorIdNotExist() {
        bookRequest.setAuthorId(-1L);
        when(authorRepository.existsById(bookRequest.getAuthorId())).thenReturn(false);
        boolean isValid = validator.isValid(bookRequest, null);
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnTrueWhenAuthorIdExist() {
        when(authorRepository.existsById(bookRequest.getAuthorId())).thenReturn(true);
        boolean isValid = validator.isValid(bookRequest, null);
        assertTrue(isValid);
    }
}