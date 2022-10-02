package com.inventorsoft.junit.util.validation;

import com.inventorsoft.junit.dto.request.CreateBookRequest;
import com.inventorsoft.junit.repository.AuthorRepository;
import com.inventorsoft.junit.repository.BookRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Objects;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BookValidatorConstraint implements ConstraintValidator<BookValidator, CreateBookRequest> {

  BookRepository bookRepository;
  AuthorRepository authorRepository;

  @Override
  public void initialize(BookValidator constraintAnnotation) {
  }

  @Override
  public boolean isValid(CreateBookRequest request, ConstraintValidatorContext context) {

    if (Objects.isNull(request.getTitle()) || request.getTitle().equals("")) {
      return false;
    }

    if (Objects.isNull(request.getReleaseDate()) || request.getReleaseDate().isAfter(LocalDate.now())) {
      return false;
    }

    if (Objects.isNull(request.getAuthorId())) {
      return false;
    }

    boolean titleExists = bookRepository.existsByTitle(request.getTitle());
    if (titleExists) {
      return false;
    }

    boolean authorNotExists = !authorRepository.existsById(request.getAuthorId());
    if (authorNotExists) {
      return false;
    }

    return true;
  }
}
