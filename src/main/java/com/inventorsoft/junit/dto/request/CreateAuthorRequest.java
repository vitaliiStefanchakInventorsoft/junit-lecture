package com.inventorsoft.junit.dto.request;

import com.inventorsoft.junit.util.validation.AuthorValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@AuthorValidator
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAuthorRequest {

  String name;
  LocalDate birthday;
}
