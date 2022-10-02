package com.inventorsoft.junit.repository;

import com.inventorsoft.junit.model.Author;
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
public class AuthorRepository implements Repository<Author, Long> {

  Map<Long, Author> authorMap = new LinkedHashMap<>();

  @Override
  public Optional<Author> findById(Long id) {
    return Optional.ofNullable(authorMap.get(id));
  }

  @Override
  public List<Author> findAll() {
    return new ArrayList<>(authorMap.values());
  }

  @Override
  public <S extends Author> S save(S entity) {
    if (Objects.isNull(entity.getId())) {
      Long id = getNewAvailableId();
      entity.setId(id);
    }

    authorMap.put(entity.getId(), entity);

    return entity;
  }

  private Long getNewAvailableId() {
    long newId = authorMap.keySet().stream()
        .mapToLong(key -> key)
        .max()
        .orElse(-1L);

    return newId + 1;
  }

  @Override
  public void deleteById(Long id) {
    authorMap.remove(id);
  }

  public boolean existsByName(String name) {
    return authorMap.values()
        .stream()
        .anyMatch(author -> author.getName().equals(name.trim()));
  }

  public boolean existsById(Long authorId) {
    return authorMap.containsKey(authorId);
  }
}
