package com.inventorsoft.junit.service;

import com.inventorsoft.junit.dto.request.CreateBookRequest;
import com.inventorsoft.junit.dto.request.UpdateBookRequest;
import com.inventorsoft.junit.dto.response.BookResponse;
import com.inventorsoft.junit.mapper.BookMapper;
import com.inventorsoft.junit.model.Author;
import com.inventorsoft.junit.model.Book;
import com.inventorsoft.junit.repository.AuthorRepository;
import com.inventorsoft.junit.repository.BookRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BookService {

  BookRepository bookRepository;
  AuthorRepository authorRepository;
  BookMapper bookMapper;

  public BookResponse getById(Long id) {
    Optional<Book> optionalBook = bookRepository.findById(id);
    return optionalBook
        .map(bookMapper::mapEntityToResponse)
        .orElseThrow(() -> new RuntimeException("Not found by id: " + id));
  }

  public List<BookResponse> getAll() {
    return bookMapper.mapEntitiesToResponses(bookRepository.findAll());
  }

  public Long create(CreateBookRequest createBookRequest) {
    Book book = bookMapper.mapCreateRequestToEntity(createBookRequest);
    Book savedBook = bookRepository.save(book);
    return savedBook.getId();
  }

  public void update(Long id, UpdateBookRequest updateBookRequest) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Book not found by id: " + id));

    bookMapper.updateEntityFromUpdateRequest(book, updateBookRequest);

    bookRepository.save(book);
  }

  public void changeAuthorForBook(Long bookId, Long newAuthorId) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("Book not found by id: " + bookId));

    boolean isNewAuthorNotEqualsToCurrent = !newAuthorId.equals(book.getAuthor().getId());
    if (isNewAuthorNotEqualsToCurrent) {
      Author author = authorRepository.findById(newAuthorId)
          .orElseThrow(() -> new RuntimeException("Author not found by id: " + newAuthorId));

      book.setAuthor(author);

      bookRepository.save(book);
    }
  }

  public void deleteById(Long id) {
    bookRepository.deleteById(id);
  }
}
