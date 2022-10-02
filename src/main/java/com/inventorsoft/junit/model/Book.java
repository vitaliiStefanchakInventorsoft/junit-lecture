
package com.inventorsoft.junit.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {

  Long id;
  String title;
  LocalDate releaseDate;
  String description;
  Author author;
}
