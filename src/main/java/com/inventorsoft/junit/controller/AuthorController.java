package com.inventorsoft.junit.controller;

import com.inventorsoft.junit.dto.request.CreateAuthorRequest;
import com.inventorsoft.junit.dto.response.AuthorResponse;
import com.inventorsoft.junit.service.AuthorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthorController {

  AuthorService authorService;

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public AuthorResponse getById(@PathVariable Long id) {
    return authorService.getById(id);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<AuthorResponse> getAll() {
    return authorService.getAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Long create(@Valid @RequestBody CreateAuthorRequest request) {
    return authorService.create(request);
  }

  @DeleteMapping("/{id}")
  public void deleteById(@PathVariable Long id) {
    authorService.deleteById(id);
  }
}
