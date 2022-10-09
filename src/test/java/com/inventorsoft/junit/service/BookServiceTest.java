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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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

    static final long AUTHOR_ID = 4L;
    static final String AUTHOR_NAME = "Eric Jensen";
    static final LocalDate AUTHOR_BIRTHDAY = LocalDate.of(1980, 1, 28);

    static final long ID = 1L;
    static final String TITLE = "Brain Based Learning";
    static final LocalDate RELEASE_DATA = LocalDate.of(2022, 11, 3);
    static final String DESCRIPTION = "DD";

    final Book book = new Book();
    final Author author = new Author();
    final BookResponse bookResponse = new BookResponse();
    final AuthorResponse authorResponse = new AuthorResponse();
    final CreateBookRequest createBookRequest = new CreateBookRequest();
    final UpdateBookRequest updateBookRequest = new UpdateBookRequest();
    final Book updateBook = new Book();

    @BeforeEach
    void init() {

        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        author.setBirthday(AUTHOR_BIRTHDAY);

        book.setId(ID);
        book.setTitle(TITLE);
        book.setReleaseDate(RELEASE_DATA);
        book.setDescription(DESCRIPTION);
        book.setAuthor(author);

        authorResponse.setId(AUTHOR_ID);
        authorResponse.setName(AUTHOR_NAME);
        authorResponse.setBirthDate(AUTHOR_BIRTHDAY);

        bookResponse.setId(ID);
        bookResponse.setTitle(TITLE);
        bookResponse.setReleaseDate(RELEASE_DATA);
        bookResponse.setDescription(DESCRIPTION);
        bookResponse.setAuthor(authorResponse);

        createBookRequest.setTitle(TITLE);
        createBookRequest.setDescription(DESCRIPTION);
        createBookRequest.setReleaseDate(RELEASE_DATA);
        createBookRequest.setAuthorId(AUTHOR_ID);

        updateBookRequest.setTitle("ddad");
        updateBookRequest.setReleaseDate(LocalDate.of(2033, 2, 2));
        updateBookRequest.setDescription("aa");
        updateBookRequest.setAuthorId(4L);

        updateBook.setId(book.getId());
        updateBook.setTitle(book.getTitle());
        updateBook.setReleaseDate(book.getReleaseDate());
        updateBook.setDescription(book.getDescription());


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
    void ifIdUserIsNullAnExceptionShouldBeThrown() {
        assertThrows(RuntimeException.class, () -> bookService.getById(null));
    }

    @Test
    void getAll() {
        //given
        List<Book> books = List.of(book);

        when(bookRepository.findAll()).thenReturn(books);

        List<BookResponse> bookResponses = List.of(bookResponse);

        when(bookMapper.mapEntitiesToResponses(books)).thenReturn(bookResponses);

        // when
        List<BookResponse> givenResults = bookService.getAll();

        // then
        assertEquals(bookResponses.size(), givenResults.size());
        assertArrayEquals(bookResponses.toArray(), givenResults.toArray());
    }

    @Test
    void create() {

        // given
        when(bookMapper.mapCreateRequestToEntity(createBookRequest)).thenReturn(book);

        book.setId(ID);

        when(bookRepository.save(book)).thenReturn(book);

        // when
        Long id = bookService.create(createBookRequest);

        // then
        assertEquals(ID, id);
        verify(bookRepository, times(1)).save(book);

    }

    //todo:does it work correctly?
    @Test
    void update() {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        bookMapper.updateEntityFromUpdateRequest(book, updateBookRequest);

        bookService.update(ID, updateBookRequest);

        assertEquals(updateBook.getId(), book.getId());
        assertEquals(updateBook.getTitle(), book.getTitle());
        assertEquals(updateBook.getReleaseDate(), book.getReleaseDate());
        assertEquals(updateBook.getDescription(), book.getDescription());


    }

    @Test
    void anExceptionShouldBeThrownIfTheBookIdIsNotFound() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookService.changeAuthorForBook(ID, AUTHOR_ID));
    }

    @Test
    void anExceptionShouldBeThrownIfTheAuthorIdIsNotFound() {
        Long idForAuthor = 2L;
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(authorRepository.findById(idForAuthor)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookService.changeAuthorForBook(ID, idForAuthor));

    }

    //todo: change?
    @Test
    void changeAuthorForBookWhenAllIsCorrect() {
        Long newAuthorId = 112L;
        Author newAuthor = new Author();
        newAuthor.setId(newAuthorId);

        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(authorRepository.findById(newAuthorId)).thenReturn(Optional.of(author));
        bookService.changeAuthorForBook(ID, newAuthorId);
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