package com.inventorsoft.junit.mapper;

import com.inventorsoft.junit.dto.request.CreateAuthorRequest;
import com.inventorsoft.junit.dto.response.AuthorResponse;
import com.inventorsoft.junit.model.Author;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class AuthorMapper {

  public List<AuthorResponse> mapEntitiesToResponses(List<Author> authors) {
    return authors.stream()
        .map(this::mapEntityToResponse)
        .toList();
  }

  public AuthorResponse mapEntityToResponse(Author author) {
    AuthorResponse response = new AuthorResponse();
    response.setId(author.getId());
    response.setName(author.getName());
    response.setBirthDate(author.getBirthday());

    return response;
  }

  public Author mapCreateRequestToEntity(CreateAuthorRequest request) {
    if (Objects.isNull(request)) {
      return null;
    }

    Author author = new Author();
    author.setName(request.getName());
    author.setBirthday(request.getBirthday());

    return author;
  }
}
