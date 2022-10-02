package com.inventorsoft.junit.repository;

import com.inventorsoft.junit.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthorRepositoryTest {

  private static final String NAME = "John Doe";
  private final AuthorRepository authorRepository = new AuthorRepository();

  @BeforeEach
  public void setUp() {
    Author author = new Author();
    author.setName(NAME);

    Map<Long, Author> authorMap = new LinkedHashMap<>();
    authorMap.put(1L, author);

    // since authorMap field in repository class is private final, there is no way to set it, except reflection
    ReflectionTestUtils.setField(authorRepository, "authorMap", authorMap);
  }

  @Test
  public void existsByNameShouldReturnTrueWhenExists() {
    boolean exists = authorRepository.existsByName(NAME);
    assertTrue(exists);
  }

  @Test
  public void existsByNameShouldReturnFalseWhenNotExists() {
    boolean exists = authorRepository.existsByName("Some other name");
    assertFalse(exists);
  }
}