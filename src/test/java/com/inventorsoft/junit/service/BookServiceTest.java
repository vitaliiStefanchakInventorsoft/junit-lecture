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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    static final Long AUTHOR_ID = 1L;
    static final String AUTHOR_NAME = "Charlotte Bronte";
    static final LocalDate AUTHOR_BIRTHDAY = LocalDate.of(1816, 4, 21);

    static final Long ID = 2L;
    static final String TITLE = "Jane Eyre";
    static final LocalDate RELEASE_DATE = LocalDate.of(1847, 10, 19);
    static final String DESCRIPTION = "Description";

    static Author author = new Author();
    static Book book = new Book();
    static AuthorResponse authorResponse = new AuthorResponse();
    static BookResponse bookResponse = new BookResponse();
    static CreateBookRequest createBookRequest = new CreateBookRequest();

    @BeforeEach
    void init() {
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        author.setBirthday(AUTHOR_BIRTHDAY);

        book.setId(ID);
        book.setTitle(TITLE);
        book.setDescription(DESCRIPTION);
        book.setReleaseDate(RELEASE_DATE);
        book.setAuthor(author);

        authorResponse.setId(AUTHOR_ID);
        authorResponse.setName(AUTHOR_NAME);
        authorResponse.setBirthDate(AUTHOR_BIRTHDAY);

        bookResponse.setId(ID);
        bookResponse.setTitle(TITLE);
        bookResponse.setDescription(DESCRIPTION);
        bookResponse.setReleaseDate(RELEASE_DATE);
        bookResponse.setAuthor(authorResponse);

        createBookRequest.setTitle(TITLE);
        createBookRequest.setDescription(DESCRIPTION);
        createBookRequest.setReleaseDate(RELEASE_DATE);
        createBookRequest.setAuthorId(AUTHOR_ID);
    }

    @AfterAll
    static void clear() {
        author = null;
        book = null;
        bookResponse = null;
        authorResponse = null;
    }

    @Test
    void getById() {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(bookMapper.mapEntityToResponse(book)).thenReturn(bookResponse);

        BookResponse givenResult = bookService.getById(ID);

        assertEquals(bookResponse, givenResult);
    }

    @Test
    void getByIdShouldThrowRuntimeExceptionWhenNotFound() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.getById(ID));

        assertEquals("Not found by id: " + ID, exception.getMessage());
    }

    @Test
    void getAll() {
        List<Book> books = List.of(book);
        List<BookResponse> bookResponses = List.of(bookResponse);

        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.mapEntitiesToResponses(books)).thenReturn(bookResponses);

        List<BookResponse> givenResults = bookService.getAll();

        assertEquals(bookResponses.size(), givenResults.size());
        assertArrayEquals(bookResponses.toArray(), givenResults.toArray());
    }

    @Test
    void create() {
        when(bookMapper.mapCreateRequestToEntity(createBookRequest)).thenReturn(book);

        book.setId(ID);

        when(bookRepository.save(book)).thenReturn(book);

        Long id = bookService.create(createBookRequest);

        assertEquals(ID, id);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void updateShouldThrowRuntimeExceptionWhenBookIdNotFound() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.update(ID, null));
        assertEquals("Book not found by id: " + ID, exception.getMessage());
    }

    @Test
    void update() {
        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setTitle("The Little Prince");
        updateBookRequest.setDescription("Description");
        updateBookRequest.setReleaseDate(LocalDate.of(1943, 04, 23));
        updateBookRequest.setAuthorId(11L);

        Book book1 = new Book();
        book1.setId(book.getId());
        book1.setTitle(book.getTitle());
        book1.setDescription(book.getDescription());
        book1.setReleaseDate(book.getReleaseDate());

        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        bookMapper.updateEntityFromUpdateRequest(book, updateBookRequest);

        bookService.update(ID, updateBookRequest);

        assertEquals(book1.getId(), book.getId());
        assertEquals(book1.getTitle(), book.getTitle());
        assertEquals(book1.getReleaseDate(), book.getReleaseDate());
        assertEquals(book1.getDescription(), book.getDescription());
    }

    @Test
    void changeAuthorForBookShouldThrowRuntimeExceptionWhenBookIdIsNotFound() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.changeAuthorForBook(ID, null));
        assertEquals("Book not found by id: " + ID, exception.getMessage());
    }

    @Test
    void changeAuthorForBookShouldThrowRuntimeExceptionWhenAuthorIdIsNotFound() {
        Long newAuthorId = 3L;
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(authorRepository.findById(newAuthorId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.changeAuthorForBook(ID, newAuthorId));
        assertEquals("Author not found by id: " + newAuthorId, exception.getMessage());
    }

    @Test
    void changeAuthorForBook() {
        Author newAuthor = new Author();
        Long newAuthorId = 4L;
        newAuthor.setId(newAuthorId);
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(authorRepository.findById(newAuthorId)).thenReturn(Optional.of(newAuthor));

        bookService.changeAuthorForBook(ID, newAuthorId);

        assertEquals(newAuthorId,book.getAuthor().getId());
        verify(bookRepository, times(1)).findById(ID);
        verify(authorRepository, times(1)).findById(newAuthorId);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void deleteById() {
        bookService.deleteById(ID);
        verify(bookRepository, times(1)).deleteById(ID);
    }
}
