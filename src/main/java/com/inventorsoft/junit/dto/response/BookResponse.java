package com.inventorsoft.junit.dto.response;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {

  Long id;
  String title;
  LocalDate releaseDate;
  String description;
  AuthorResponse author;
}
