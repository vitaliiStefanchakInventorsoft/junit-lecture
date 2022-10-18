package com.inventorsoft.junit.util.validation;

import com.inventorsoft.junit.dto.request.CreateAuthorRequest;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookValidatorConstraintTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    BookValidatorConstraint validator;

    static final long ID = 1L;
    static final String TITLE = "Java book";
    static final LocalDate RELEASE_DATE = LocalDate.of(2020, 12, 22);
    static final String DESCRIPTION = "Book about Java language";

    static final String NAME = "John Doe";
    static final LocalDate BIRTHDAY = LocalDate.of(2000, 10, 1);

    final CreateAuthorRequest authorRequest = new CreateAuthorRequest();
    final CreateBookRequest bookRequest = new CreateBookRequest();

    @BeforeEach
    void initAuthorAndBookRequest() {
        authorRequest.setName(NAME);
        authorRequest.setBirthday(BIRTHDAY);

        bookRequest.setTitle(TITLE);
        bookRequest.setDescription(DESCRIPTION);
        bookRequest.setReleaseDate(RELEASE_DATE);
        bookRequest.setAuthorId(ID);
    }

    @Test
    void isValidShouldReturnTrue() {
        //given
        when(authorRepository.existsById(bookRequest.getAuthorId())).thenReturn(true);

        //when
        boolean isValid = validator.isValid(bookRequest, null);

        //then
        assertTrue(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenTitleIsEmpty() {
        //given
        bookRequest.setTitle("");

        //when
        boolean isValid = validator.isValid(bookRequest, null);

        //then
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenTitleIsNull() {
        //given
        bookRequest.setTitle(null);

        //when
        boolean isValid = validator.isValid(bookRequest, null);

        //then
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenTitleExist() {
        //given
        when(bookRepository.existsByTitle(bookRequest.getTitle())).thenReturn(true);

        //when
        boolean isValid = validator.isValid(bookRequest, null);

        //then
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenReleaseDateIsFuture() {
        //given
        bookRequest.setReleaseDate(LocalDate.of(2222, 2, 22));

        //when
        boolean isValid = validator.isValid(bookRequest, null);

        //then
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenReleaseDateIsNull() {
        //given
        bookRequest.setReleaseDate(null);

        //when
        boolean isValid = validator.isValid(bookRequest, null);

        //then
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenAuthorIsNotExist() {
        //given
        bookRequest.setAuthorId(1111L);
        when(authorRepository.existsById(bookRequest.getAuthorId())).thenReturn(false);

        //when
        boolean isValid = validator.isValid(bookRequest, null);

        //then
        assertFalse(isValid);
    }

}
