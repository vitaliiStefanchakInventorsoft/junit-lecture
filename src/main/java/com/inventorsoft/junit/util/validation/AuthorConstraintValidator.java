package com.inventorsoft.junit.util.validation;

import com.inventorsoft.junit.dto.request.CreateAuthorRequest;
import com.inventorsoft.junit.repository.AuthorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthorConstraintValidator implements ConstraintValidator<AuthorValidator, CreateAuthorRequest> {

  AuthorRepository authorRepository;

  @Override
  public void initialize(AuthorValidator constraintAnnotation) {
  }

  @Override
  public boolean isValid(CreateAuthorRequest request, ConstraintValidatorContext context) {

    if (Objects.isNull(request.getName()) || request.getName().equals("")) {
      return false;
    }

    if (Objects.isNull(request.getBirthday())) {
      return false;
    }

    boolean nameExists = authorRepository.existsByName(request.getName());
    if (nameExists) {
      return false;
    }

    return true;
  }
}
