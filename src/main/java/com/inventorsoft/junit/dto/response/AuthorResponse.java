package com.inventorsoft.junit.dto.response;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@EqualsAndHashCode
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorResponse {

  Long id;
  String name;
  LocalDate birthDate;
}
