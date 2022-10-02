package com.inventorsoft.junit.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {

  Optional<T> findById(ID id);

  List<T> findAll();

  <S extends T> S save(S entity);

  void deleteById(ID id);
}
