package com.inventorsoft.junit.controller;

import com.inventorsoft.junit.dto.request.CreateBookRequest;
import com.inventorsoft.junit.dto.request.UpdateBookRequest;
import com.inventorsoft.junit.dto.response.BookResponse;
import com.inventorsoft.junit.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/books")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BookController {

  BookService bookService;

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public BookResponse getById(@PathVariable Long id) {
    return bookService.getById(id);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<BookResponse> getAll() {
    return bookService.getAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Long create(@Valid @RequestBody CreateBookRequest createBookRequest) {
    return bookService.create(createBookRequest);
  }

  @PutMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public void update(@PathVariable Long id, @RequestBody UpdateBookRequest updateBookRequest) {
    bookService.update(id, updateBookRequest);
  }

  @PatchMapping("/{id}/authors")
  @ResponseStatus(HttpStatus.OK)
  public void changeAuthorForBook(@PathVariable Long id, @RequestParam Long authorId) {
    bookService.changeAuthorForBook(id, authorId);
  }

  @DeleteMapping("/{id}")
  public void deleteById(@PathVariable Long id) {
    bookService.deleteById(id);
  }
}
