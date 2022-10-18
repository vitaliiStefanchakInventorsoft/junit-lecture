package com.inventorsoft.junit.service;

import com.inventorsoft.junit.dto.request.CreateBookRequest;
import com.inventorsoft.junit.dto.request.UpdateBookRequest;
import com.inventorsoft.junit.dto.response.AuthorResponse;
import com.inventorsoft.junit.dto.response.BookResponse;
import com.inventorsoft.junit.mapper.BookMapper;
import com.inventorsoft.junit.model.Author;
import com.inventorsoft.junit.model.Book;
import com.inventorsoft.junit.repository.AuthorRepository;
import com.inventorsoft.junit.repository.BookRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookServiceTest {

    @Mock
    AuthorRepository authorRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @InjectMocks
    BookService bookService;

    static final long ID = 1L;
    static final String TITLE = "Java book";
    static final LocalDate RELEASE_DATE = LocalDate.of(2020, 12, 22);
    static final String DESCRIPTION = "Book about Java language";

    static final String NAME = "John Doe";
    static final LocalDate BIRTHDAY = LocalDate.of(2000, 10, 1);

    final Author author = new Author();
    final AuthorResponse authorResponse = new AuthorResponse();
    final Book book = new Book();
    final BookResponse bookResponse = new BookResponse();

    @BeforeEach
    void initAuthorAndBook() {
        author.setId(ID);
        author.setName(NAME);
        author.setBirthday(BIRTHDAY);

        authorResponse.setId(ID);
        authorResponse.setName(NAME);
        authorResponse.setBirthDate(BIRTHDAY);

        book.setId(ID);
        book.setTitle(TITLE);
        book.setReleaseDate(RELEASE_DATE);
        book.setDescription(DESCRIPTION);
        book.setAuthor(author);

        bookResponse.setId(ID);
        bookResponse.setTitle(TITLE);
        bookResponse.setReleaseDate(RELEASE_DATE);
        bookResponse.setDescription(DESCRIPTION);
        bookResponse.setAuthor(authorResponse);
    }

    @Test
    void getById() {
        // given
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(bookMapper.mapEntityToResponse(book)).thenReturn(bookResponse);

        // when
        BookResponse givenResult = bookService.getById(ID);

        // then
        assertEquals(bookResponse, givenResult);
    }

    @Test
    void getByIdShouldThrowRuntimeExceptionWhenUserNotFound() {
        assertThrows(RuntimeException.class, () -> bookService.getById(null));
    }

    @Test
    void getAll() {
        // given
        List<BookResponse> bookResponses = List.of(bookResponse);
        List<Book> books = List.of(book);

        when(bookMapper.mapEntitiesToResponses(books)).thenReturn(bookResponses);
        when(bookRepository.findAll()).thenReturn(books);

        // when
        List<BookResponse> result = bookService.getAll();

        // then
        assertEquals(bookResponses.size(), result.size());
        assertArrayEquals(bookResponses.toArray(), result.toArray());
    }

    @Test
    void create() {
        // given
        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setAuthorId(ID);
        createBookRequest.setTitle(TITLE);
        createBookRequest.setDescription(DESCRIPTION);
        createBookRequest.setReleaseDate(RELEASE_DATE);

        when(bookMapper.mapCreateRequestToEntity(createBookRequest)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        // when
        Long bookId = bookService.create(createBookRequest);

        // then
        assertEquals(ID, bookId);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).mapCreateRequestToEntity(createBookRequest);
    }

    @Test
    void update() {
        // given
        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setAuthorId(ID);
        updateBookRequest.setTitle(TITLE);
        updateBookRequest.setDescription(DESCRIPTION);
        updateBookRequest.setReleaseDate(RELEASE_DATE);

        Book updateBook = new Book();
        updateBook.setId(book.getId());
        updateBook.setTitle(book.getTitle());
        updateBook.setReleaseDate(book.getReleaseDate());
        updateBook.setDescription(book.getDescription());

        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        bookMapper.updateEntityFromUpdateRequest(book, updateBookRequest);

        // when
        bookService.update(ID, updateBookRequest);

        // then
        assertEquals(updateBook.getId(), book.getId());
        assertEquals(updateBook.getTitle(), book.getTitle());
        assertEquals(updateBook.getReleaseDate(), book.getReleaseDate());
        assertEquals(updateBook.getDescription(), book.getDescription());
    }

    @Test
    void updateShouldThrowRuntimeExceptionWhenBookNotFound() {
        // given
        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setAuthorId(ID);
        updateBookRequest.setTitle(TITLE);
        updateBookRequest.setDescription(DESCRIPTION);
        updateBookRequest.setReleaseDate(RELEASE_DATE);

        // when
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        // then
        assertThrows(RuntimeException.class, () ->
                bookService.update(ID, updateBookRequest));
    }

    @Test
    void changeAuthorForBook() {
        // given
        Author newAuthor = new Author();
        Long newAuthorId = 11L;
        newAuthor.setId(newAuthorId);

        //when
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(authorRepository.findById(newAuthorId)).thenReturn(Optional.of(author));
        bookService.changeAuthorForBook(ID, newAuthorId);

        //then
        verify(bookRepository, times(1)).findById(ID);
        verify(authorRepository, times(1)).findById(newAuthorId);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void changeAuthorForBookShouldThrowRuntimeExceptionWhenBookNotFound() {
        // given
        Long authorId = 11L;

        //when
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        //then
        assertThrows(RuntimeException.class, () -> bookService.changeAuthorForBook(ID, authorId));
    }

    @Test
    void changeAuthorForBookShouldThrowRuntimeExceptionWhenAuthorNotFound() {
        // given
        Long authorId = 11L;

        //when
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        //then
        assertThrows(RuntimeException.class, () -> bookService.changeAuthorForBook(ID, authorId));
    }

    @Test
    void deleteById() {
        bookService.deleteById(ID);
        verify(bookRepository, times(1)).deleteById(ID);
    }

}
