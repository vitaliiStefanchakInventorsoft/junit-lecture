package com.inventorsoft.junit.util.validation;

import com.inventorsoft.junit.dto.request.CreateBookRequest;
import com.inventorsoft.junit.repository.AuthorRepository;
import com.inventorsoft.junit.repository.BookRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookValidatorConstraintTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    BookValidatorConstraint bookValidator;

    static final long AUTHOR_ID = 2L;
    static final String TITLE = "Mr.Mercedes";
    static final LocalDate RELEASE_DATE = LocalDate.of(2014, 6, 3);
    static final String DESCRIPTION = "Novel";

    @BeforeEach
    void startInit() {
        createBookRequest.setTitle(TITLE);
        createBookRequest.setReleaseDate(RELEASE_DATE);
        createBookRequest.setDescription(DESCRIPTION);
        createBookRequest.setAuthorId(AUTHOR_ID);
    }

    CreateBookRequest createBookRequest = new CreateBookRequest();

    @Test
    void isValidMustReturnFalseIfReleaseDateIsNull() {
        createBookRequest.setReleaseDate(null);

        assertFalse(bookValidator.isValid(createBookRequest, null));
    }

    @Test
    void isValidMustReturnFalseIfReleaseDateIsInvalid() {
        createBookRequest.setReleaseDate(LocalDate.of(9999999, 1, 1));

        assertFalse(bookValidator.isValid(createBookRequest, null));
    }

    @Test
    void isValidMustReturnFalseIfTitleIsNull() {
        createBookRequest.setTitle(null);

        assertFalse(bookValidator.isValid(createBookRequest, null));
    }

    @Test
    void isValidMustReturnFalseIfTitleIsEmpty() {
        createBookRequest.setTitle("");

        assertFalse(bookValidator.isValid(createBookRequest, null));
    }

    @Test
    void isValidMustReturnFalseIfAuthorIdIsNull() {
        createBookRequest.setAuthorId(null);

        assertFalse(bookValidator.isValid(createBookRequest, null));
    }

    @Test
    void isValidShouldReturnFalseIfTitleExists() {
        when(bookRepository.existsByTitle(createBookRequest.getTitle())).thenReturn(true);

        assertFalse(bookValidator.isValid(createBookRequest, null));
    }

    @Test
    void isValidShouldReturnFalseIfAuthorIsNotExists() {
        when(authorRepository.existsById(createBookRequest.getAuthorId())).thenReturn(false);

        assertFalse(bookValidator.isValid(createBookRequest, null));
    }

    @Test
    void isValidShouldReturnTrueIfConditionsWereFollowed() {
        when(authorRepository.existsById(createBookRequest.getAuthorId())).thenReturn(true);

        assertTrue(bookValidator.isValid(createBookRequest, null));
    }

}