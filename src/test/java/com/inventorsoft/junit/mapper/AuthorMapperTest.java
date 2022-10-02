package com.inventorsoft.junit.mapper;

import com.inventorsoft.junit.dto.request.CreateAuthorRequest;
import com.inventorsoft.junit.dto.response.AuthorResponse;
import com.inventorsoft.junit.model.Author;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class AuthorMapperTest {

  static final String NAME = "John Doe";
  static final long ID = 1L;
  static final LocalDate BIRTHDAY = LocalDate.of(2000, 10, 1);

  AuthorMapper authorMapper = new AuthorMapper();

  @Test
  public void mapEntityToResponse() {

    // given
    Author author = new Author();
    author.setId(ID);
    author.setName(NAME);
    author.setBirthday(BIRTHDAY);

    AuthorResponse expectedResult = new AuthorResponse();
    expectedResult.setId(ID);
    expectedResult.setName(NAME);
    expectedResult.setBirthDate(BIRTHDAY);

    // when
    AuthorResponse givenResult = authorMapper.mapEntityToResponse(author);

    // then
    assertEquals(expectedResult.getId(), givenResult.getId());
    assertEquals(expectedResult.getName(), givenResult.getName());
    assertEquals(expectedResult.getBirthDate(), givenResult.getBirthDate());
  }

  @Test
  public void mapEntitiesToResponses() {

    // given
    Author author = new Author();
    author.setId(ID);
    author.setName(NAME);
    author.setBirthday(BIRTHDAY);

    List<Author> authors = List.of(author);

    AuthorResponse expectedResult = new AuthorResponse();
    expectedResult.setId(ID);
    expectedResult.setName(NAME);
    expectedResult.setBirthDate(BIRTHDAY);

    List<AuthorResponse> expectedResults = List.of(expectedResult);

    // when
    List<AuthorResponse> givenResults = authorMapper.mapEntitiesToResponses(authors);

    // then
    assertEquals(expectedResults.size(), givenResults.size());
    assertArrayEquals(expectedResults.toArray(), givenResults.toArray());
  }

  @Test
  public void mapCreateRequestToEntity() {

    // given
    CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest();
    createAuthorRequest.setName(NAME);
    createAuthorRequest.setBirthday(BIRTHDAY);

    Author expectedResult = new Author();
    expectedResult.setName(NAME);
    expectedResult.setBirthday(BIRTHDAY);

    // when
    Author givenResult = authorMapper.mapCreateRequestToEntity(createAuthorRequest);

    // then
    assertEquals(expectedResult.getId(), givenResult.getId());
    assertEquals(expectedResult.getName(), givenResult.getName());
    assertEquals(expectedResult.getBirthday(), givenResult.getBirthday());
  }

  @Test
  public void mapCreateRequestToEntityShouldReturnNullWhenRequestIsNull() {
    // given
    CreateAuthorRequest createAuthorRequest = null;

    // when
    Author author = authorMapper.mapCreateRequestToEntity(createAuthorRequest);

    //then
    assertNull(author);
  }
}