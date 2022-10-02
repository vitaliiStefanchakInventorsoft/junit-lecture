package com.inventorsoft.junit.util.validation;

import com.inventorsoft.junit.dto.request.CreateAuthorRequest;
import com.inventorsoft.junit.repository.AuthorRepository;
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
class AuthorConstraintValidatorTest {

  @Mock
  AuthorRepository authorRepository;

  @InjectMocks
  AuthorConstraintValidator validator;

  @Test
  void isValidShouldReturnTrue() {

    // given
    CreateAuthorRequest request = new CreateAuthorRequest();
    request.setName("John Doe");
    request.setBirthday(LocalDate.of(1910, 1, 1));

    when(authorRepository.existsByName(request.getName())).thenReturn(false);

    // when
    boolean isValid = validator.isValid(request, null);

    // then
    assertTrue(isValid);
  }

  @Test
  void isValidShouldReturnFalseWhenNameIsNull() {

    // given
    CreateAuthorRequest request = new CreateAuthorRequest();
    request.setName(null);
    request.setBirthday(LocalDate.of(1910, 1, 1));

    // when
    boolean isValid = validator.isValid(request, null);

    // then
    assertFalse(isValid);
  }

  @Test
  void isValidShouldReturnFalseWhenNameIsEmpty() {

    // given
    CreateAuthorRequest request = new CreateAuthorRequest();
    request.setName("");
    request.setBirthday(LocalDate.of(1910, 1, 1));

    // when
    boolean isValid = validator.isValid(request, null);

    // then
    assertFalse(isValid);
  }

  @Test
  void isValidShouldReturnFalseWhenBirthDayIsNull() {

    // given
    CreateAuthorRequest request = new CreateAuthorRequest();
    request.setName("John Doe");
    request.setBirthday(null);

    // when
    boolean isValid = validator.isValid(request, null);

    // then
    assertFalse(isValid);
  }

  @Test
  void isValidShouldReturnFalseWhenNameIsExist() {

    // given
    CreateAuthorRequest request = new CreateAuthorRequest();
    request.setName("John Doe");
    request.setBirthday(LocalDate.of(1910, 1, 1));

    when(authorRepository.existsByName(request.getName())).thenReturn(true);

    // when
    boolean isValid = validator.isValid(request, null);

    // then
    assertFalse(isValid);
  }
}