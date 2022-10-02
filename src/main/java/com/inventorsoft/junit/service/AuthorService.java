package com.inventorsoft.junit.service;

import com.inventorsoft.junit.dto.request.CreateAuthorRequest;
import com.inventorsoft.junit.dto.response.AuthorResponse;
import com.inventorsoft.junit.mapper.AuthorMapper;
import com.inventorsoft.junit.model.Author;
import com.inventorsoft.junit.repository.AuthorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthorService {

  AuthorRepository authorRepository;
  AuthorMapper authorMapper;

  public AuthorResponse getById(Long id) {
    Optional<Author> optionalAuthor = authorRepository.findById(id);
    return optionalAuthor
        .map(authorMapper::mapEntityToResponse)
        .orElseThrow(() -> new RuntimeException("Not found by id: " + id));
  }

  public List<AuthorResponse> getAll() {
    return authorMapper.mapEntitiesToResponses(authorRepository.findAll());
  }

  public Long create(CreateAuthorRequest request) {
    Author author = authorMapper.mapCreateRequestToEntity(request);
    Author savedAuthor = authorRepository.save(author);

    return savedAuthor.getId();
  }

  public void deleteById(Long id) {
    authorRepository.deleteById(id);
  }
}
