package com.inventorsoft.junit.repository;

import com.inventorsoft.junit.model.Book;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Simple implementation of repository, stores data in map.
 */
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookRepository implements Repository<Book, Long> {

  Map<Long, Book> bookMap = new LinkedHashMap<>();

  @Override
  public Optional<Book> findById(Long id) {
    return Optional.ofNullable(bookMap.get(id));
  }

  @Override
  public List<Book> findAll() {
    return new ArrayList<>(bookMap.values());
  }

  @Override
  public <S extends Book> S save(S entity) {
    if (Objects.isNull(entity.getId())) {
      Long id = getNewAvailableId();
      entity.setId(id);
    }

    bookMap.put(entity.getId(), entity);

    return entity;
  }

  private Long getNewAvailableId() {
    long newId = bookMap.keySet().stream()
        .mapToLong(key -> key)
        .max()
        .orElse(-1L);

    return newId + 1;
  }

  @Override
  public void deleteById(Long id) {
    bookMap.remove(id);
  }

  public boolean existsByTitle(String title) {
    return bookMap.values()
        .stream()
        .anyMatch(author -> author.getTitle().equals(title.trim()));
  }
}
