package com.inventorsoft.junit.util.validation;

import com.inventorsoft.junit.dto.request.CreateBookRequest;
import com.inventorsoft.junit.repository.AuthorRepository;
import com.inventorsoft.junit.repository.BookRepository;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookValidatorConstraintTest {

    static final String TITLE = "Brain Based Learning";
    static final LocalDate RELEASE_DATA = LocalDate.of(2022, 11, 3);
    static final String DESCRIPTION = "DD";

    static final long AUTHOR_ID = 4L;

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    BookValidatorConstraint bookValidator;

    @Test
    void isValidShouldReturnTrue() {

        // given
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(TITLE);
        request.setReleaseDate(LocalDate.of(2021, 2, 2));
        request.setDescription(DESCRIPTION);
        request.setAuthorId(AUTHOR_ID);
        when(authorRepository.existsById(request.getAuthorId())).thenReturn(true);
        when(bookRepository.existsByTitle(request.getTitle())).thenReturn(false);

        // when
        boolean isValid = bookValidator.isValid(request, null);


        // then
        assertTrue(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenReleaseDateIsNull() {

        CreateBookRequest request = new CreateBookRequest();
        request.setReleaseDate(null);
        request.setAuthorId(AUTHOR_ID);

        // when
        boolean isValid = bookValidator.isValid(request, null);

        // then
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenReleaseDateIsAfterTheSpecifiedDate() {
        // given
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(TITLE);
        request.setReleaseDate(RELEASE_DATA);
        request.setAuthorId(AUTHOR_ID);
        // when
        boolean isValid = bookValidator.isValid(request, null);
        // then
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenAuthorIdIsNull() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(TITLE);
        request.setReleaseDate(LocalDate.of(2021, 2, 2));
        request.setDescription(DESCRIPTION);
        request.setAuthorId(null);

        boolean isValid = bookValidator.isValid(request, null);
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenTitleIsExist() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(TITLE);
        request.setReleaseDate(LocalDate.of(2021, 2, 2));
        request.setDescription(DESCRIPTION);
        request.setAuthorId(AUTHOR_ID);
        when(bookRepository.existsByTitle(request.getTitle())).thenReturn(true);

        boolean isValid = bookValidator.isValid(request, null);
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenAuthorIdNotExist() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(TITLE);
        request.setReleaseDate(LocalDate.of(2021, 2, 2));
        request.setDescription(DESCRIPTION);
        request.setAuthorId(1L);

        boolean isValid = bookValidator.isValid(request, null);
        assertFalse(isValid);
    }
}