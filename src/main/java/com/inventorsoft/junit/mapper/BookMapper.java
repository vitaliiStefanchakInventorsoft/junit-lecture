package com.inventorsoft.junit.mapper;

import com.inventorsoft.junit.dto.request.CreateBookRequest;
import com.inventorsoft.junit.dto.request.UpdateBookRequest;
import com.inventorsoft.junit.dto.response.AuthorResponse;
import com.inventorsoft.junit.dto.response.BookResponse;
import com.inventorsoft.junit.model.Author;
import com.inventorsoft.junit.model.Book;
import com.inventorsoft.junit.repository.AuthorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BookMapper {

  AuthorRepository authorRepository;
  AuthorMapper authorMapper;

  public List<BookResponse> mapEntitiesToResponses(List<Book> books) {
    return books.stream()
        .map(this::mapEntityToResponse)
        .toList();
  }

  public BookResponse mapEntityToResponse(Book book) {
    BookResponse response = new BookResponse();
    response.setId(book.getId());
    response.setTitle(book.getDescription());
    response.setReleaseDate(book.getReleaseDate());
    response.setDescription(book.getDescription());

    AuthorResponse author = authorMapper.mapEntityToResponse(book.getAuthor());
    response.setAuthor(author);

    return response;
  }

  public Book mapCreateRequestToEntity(CreateBookRequest request) {
    Book book = new Book();
    book.setTitle(request.getTitle());
    book.setReleaseDate(request.getReleaseDate());
    book.setDescription(request.getDescription());

    initAuthorToBook(book, request);

    return book;
  }

  private void initAuthorToBook(Book book, CreateBookRequest request) {
    Author author = authorRepository
        .findById(request.getAuthorId())
        .orElseThrow(() -> new RuntimeException("Author not found with id: " + request.getAuthorId()));

    book.setAuthor(author);
  }

  public void updateEntityFromUpdateRequest(Book book, UpdateBookRequest updateRequest) {
    book.setTitle(updateRequest.getTitle());
    book.setReleaseDate(updateRequest.getReleaseDate());
    book.setDescription(updateRequest.getDescription());

    initAuthorToBook(book, updateRequest);
  }
}
