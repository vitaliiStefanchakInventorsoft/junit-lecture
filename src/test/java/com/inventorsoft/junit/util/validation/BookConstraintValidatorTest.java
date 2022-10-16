package com.inventorsoft.junit.util.validation;

import com.inventorsoft.junit.dto.request.CreateBookRequest;
import com.inventorsoft.junit.repository.AuthorRepository;
import com.inventorsoft.junit.repository.BookRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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
public class BookConstraintValidatorTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    BookValidatorConstraint bookValidatorConstraint;

    static final String TITLE = "Jane Eyre";
    static final LocalDate RELEASE_DATE = LocalDate.of(1847, 10, 19);
    static final String DESCRIPTION = "Description";
    static final long AUTHOR_ID = 1L;

    @Test
    void isValidShouldReturnTrue() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(TITLE);
        request.setReleaseDate(RELEASE_DATE);
        request.setDescription(DESCRIPTION);
        request.setAuthorId(AUTHOR_ID);
        when(authorRepository.existsById(request.getAuthorId())).thenReturn(true);
        when(bookRepository.existsByTitle(request.getTitle())).thenReturn(false);

        boolean isValid = bookValidatorConstraint.isValid(request, null);

        assertTrue(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenTitleIsNull() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(null);

        boolean isValid = bookValidatorConstraint.isValid(request, null);

        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenTitleIsEmpty() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("");

        boolean isValid = bookValidatorConstraint.isValid(request, null);

        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenReleaseDateIsNull() {
        CreateBookRequest request = new CreateBookRequest();
        request.setReleaseDate(null);

        boolean isValid = bookValidatorConstraint.isValid(request, null);

        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenReleaseDateIsAfterNow() {
        CreateBookRequest request = new CreateBookRequest();
        request.setReleaseDate(LocalDate.of(2043, 05, 11));

        boolean isValid = bookValidatorConstraint.isValid(request, null);

        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenAuthorIdIsNull() {
        CreateBookRequest request = new CreateBookRequest();
        request.setAuthorId(null);

        boolean isValid = bookValidatorConstraint.isValid(request, null);

        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenTitleIsExist() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(TITLE);
        request.setReleaseDate(RELEASE_DATE);
        request.setDescription(DESCRIPTION);
        request.setAuthorId(AUTHOR_ID);
        when(bookRepository.existsByTitle(request.getTitle())).thenReturn(true);

        boolean isValid = bookValidatorConstraint.isValid(request, null);
        assertFalse(isValid);
    }

    @Test
    void isValidShouldReturnFalseWhenAuthorIdNotExist() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(TITLE);
        request.setReleaseDate(RELEASE_DATE);
        request.setDescription(DESCRIPTION);
        request.setAuthorId(1L);

        boolean isValid = bookValidatorConstraint.isValid(request, null);
        assertFalse(isValid);
    }
}
