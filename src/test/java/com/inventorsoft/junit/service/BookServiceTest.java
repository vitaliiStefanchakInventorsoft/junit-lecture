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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookServiceTest {

    @Mock
    AuthorRepository authorRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    BookMapper bookMapper;
    @InjectMocks
    BookService bookService;

    static final long AUTHOR_ID = 35L;
    static final String AUTHOR_NAME = "Leo Tolstoy";
    static final LocalDate AUTHOR_BIRTHDAY = LocalDate.of(1828, 9, 9);

    static final long BOOK_ID = 5L;
    static final String BOOK_TITLE = "War and Peace";
    static final LocalDate BOOK_RELEASE_DATE = LocalDate.of(1869, 5, 23);
    static final String BOOK_DESCRIPTION = """
             War and Peace broadly focuses on Napoleon’s invasion of Russia in 1812 and 
             follows three of the most well-known characters in literature: Pierre Bezukhov, the illegitimate 
             son of a count who is fighting for his inheritance and yearning for spiritual fulfillment;
             Prince Andrei Bolkonsky, who leaves his family behind to fight in the war against Napoleon; 
             and Natasha Rostov, the beautiful young daughter of a nobleman who intrigues both men.
            """;

    static AuthorResponse authorResponse = new AuthorResponse();
    static BookResponse bookResponse = new BookResponse();
    static Author author = new Author();
    static Book book = new Book();

    @BeforeEach
    void startInit() {
        authorResponse.setName(AUTHOR_NAME);
        authorResponse.setId(AUTHOR_ID);
        authorResponse.setBirthDate(AUTHOR_BIRTHDAY);

        bookResponse.setId(BOOK_ID);
        bookResponse.setDescription(BOOK_DESCRIPTION);
        bookResponse.setTitle(BOOK_TITLE);
        bookResponse.setReleaseDate(BOOK_RELEASE_DATE);
        bookResponse.setAuthor(authorResponse);

        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        author.setBirthday(AUTHOR_BIRTHDAY);

        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setDescription(BOOK_DESCRIPTION);
        book.setReleaseDate(BOOK_RELEASE_DATE);
        book.setAuthor(author);
    }

    @AfterAll
    static void clear() {
        author = null;
        book = null;
        bookResponse = null;
        authorResponse = null;
    }

    @Test
    void getByIdShouldReturnBookResponseWhenGetBookId() {
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(bookMapper.mapEntityToResponse(book)).thenReturn(bookResponse);

        BookResponse resultBookResponse = bookService.getById(BOOK_ID);

        assertEquals(bookResponse, resultBookResponse);
    }

    @Test
    void getByIdShouldThrowRuntimeExceptionWhenNotFoundBookId() {
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.getById(BOOK_ID));

        assertEquals("Not found by id: " + BOOK_ID, exception.getMessage());
    }

    @Test
    void getAllShouldReturnListOfBookResponse() {
        List<BookResponse> bookResponses = List.of(bookResponse);
        List<Book> books = List.of(book);
        when(bookMapper.mapEntitiesToResponses(books)).thenReturn(bookResponses);
        when(bookRepository.findAll()).thenReturn(books);

        List<BookResponse> result = bookService.getAll();

        assertEquals(bookResponses.size(), result.size());
        assertArrayEquals(bookResponses.toArray(), result.toArray());
    }

    @Test
    void createShouldReturnLongIdWhenGetCreateBookRequest() {
        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setAuthorId(AUTHOR_ID);
        createBookRequest.setTitle(BOOK_TITLE);
        createBookRequest.setDescription(BOOK_DESCRIPTION);
        createBookRequest.setReleaseDate(BOOK_RELEASE_DATE);

        when(bookMapper.mapCreateRequestToEntity(createBookRequest)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        Long result = bookService.create(createBookRequest);

        assertEquals(BOOK_ID, result);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).mapCreateRequestToEntity(createBookRequest);
    }

    @Test
    void updateShouldThrowRuntimeExceptionWhenNotFoundBookId() {
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.update(BOOK_ID, null));

        assertEquals("Book not found by id: " + BOOK_ID, exception.getMessage());
    }

    @Test
    void updateWhenGetBookIdAndUpdateBookRequest() {
        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setAuthorId(AUTHOR_ID);
        updateBookRequest.setTitle(BOOK_TITLE);
        updateBookRequest.setDescription(BOOK_DESCRIPTION);
        updateBookRequest.setReleaseDate(BOOK_RELEASE_DATE);

        Book updatedBook = new Book();
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(updatedBook));
        doAnswer((i) -> {
            updatedBook.setId(book.getId());
            updatedBook.setAuthor(book.getAuthor());
            updatedBook.setDescription(book.getDescription());
            updatedBook.setTitle(book.getTitle());
            updatedBook.setReleaseDate(book.getReleaseDate());
            return updatedBook;
        }).when(bookMapper).updateEntityFromUpdateRequest(updatedBook,updateBookRequest);

        bookService.update(BOOK_ID, updateBookRequest);

        assertEquals(book.getId(), updatedBook.getId());
        assertEquals(book.getAuthor(), updatedBook.getAuthor());
        assertEquals(book.getTitle(), updatedBook.getTitle());
        assertEquals(book.getReleaseDate(), updatedBook.getReleaseDate());
        assertEquals(book.getDescription(), updatedBook.getDescription());
    }

    @Test
    void changeAuthorForBookShouldThrowRuntimeExceptionWhenNotFoundBookId() {
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.changeAuthorForBook(BOOK_ID, null));

        assertEquals("Book not found by id: " + BOOK_ID, exception.getMessage());
    }

    @Test
    void changeAuthorForBookShouldThrowRuntimeExceptionWhenNotFoundAuthorId() {
        Long newAuthorId = 100L;
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(authorRepository.findById(newAuthorId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.changeAuthorForBook(BOOK_ID, newAuthorId));

        assertEquals("Author not found by id: " + newAuthorId, exception.getMessage());
    }

    @Test
    void changeAuthorForBookWhenAuthorIdEqualsNewAuthorId() {
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

        bookService.changeAuthorForBook(BOOK_ID, AUTHOR_ID);

        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(authorRepository, times(0)).findById(AUTHOR_ID);
    }

    @Test
    void changeAuthorForBookWhenAllCorrect() {
        Author newAuthor = new Author();
        Long newAuthorId = 100L;
        newAuthor.setId(newAuthorId);
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(authorRepository.findById(newAuthorId)).thenReturn(Optional.of(newAuthor));

        bookService.changeAuthorForBook(BOOK_ID, newAuthorId);

        assertEquals(newAuthorId,book.getAuthor().getId());
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(authorRepository, times(1)).findById(newAuthorId);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void deleteById() {
        bookService.deleteById(BOOK_ID);
        verify(bookRepository, times(1)).deleteById(BOOK_ID);
    }
}