package com.inventorsoft.junit.mapper;

import com.inventorsoft.junit.dto.request.CreateBookRequest;
import com.inventorsoft.junit.dto.request.UpdateBookRequest;
import com.inventorsoft.junit.dto.response.AuthorResponse;
import com.inventorsoft.junit.dto.response.BookResponse;
import com.inventorsoft.junit.model.Author;
import com.inventorsoft.junit.model.Book;
import com.inventorsoft.junit.repository.AuthorRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookMapperTest {

    static final long AUTHOR_ID = 4L;
    static final String AUTHOR_NAME = "Eric Jensen";
    static final LocalDate AUTHOR_BIRTHDAY = LocalDate.of(1980, 1, 28);

    static final long ID = 1L;
    static final String TITLE = "Brain Based Learning";
    static final LocalDate RELEASE_DATA = LocalDate.of(2022, 11, 3);
    static final String DESCRIPTION = "DD";

    @Mock
    AuthorRepository authorRepository;

    @Mock
    AuthorMapper authorMapper;

    @InjectMocks
    BookMapper bookMapper;

    final Author author = new Author();
    final Book book = new Book();
    final AuthorResponse authorResponse = new AuthorResponse();
    final BookResponse bookResponse = new BookResponse();
    final CreateBookRequest createBookRequest = new CreateBookRequest();
    final UpdateBookRequest updateBookRequest = new UpdateBookRequest();
    final Author updateAuthor = new Author();
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

        updateAuthor.setId(4L);
        updateAuthor.setName("Dmytro Jensen");
        updateAuthor.setBirthday(LocalDate.of(1970, 2, 2));

        updateBook.setId(1L);
        updateBook.setTitle("ddad");
        updateBook.setReleaseDate(LocalDate.of(2033, 2, 2));
        updateBook.setDescription("aa");
        updateBook.setAuthor(updateAuthor);
    }

    @Test
    void mapEntitiesToResponses() {
        List<Book> books = List.of(book);
        List<BookResponse> expectedResults = List.of(bookResponse);

        when(authorMapper.mapEntityToResponse(book.getAuthor())).thenReturn(authorResponse);

        List<BookResponse> givenResults = bookMapper.mapEntitiesToResponses(books);

        assertEquals(expectedResults.size(), givenResults.size());
        assertArrayEquals(expectedResults.toArray(), givenResults.toArray());
    }

    @Test
    void mapEntityToResponse() {
        when(authorMapper.mapEntityToResponse(book.getAuthor())).thenReturn(authorResponse);

        BookResponse givenResult = bookMapper.mapEntityToResponse(book);

        assertEquals(bookResponse.getId(), givenResult.getId());
        assertEquals(bookResponse.getTitle(), givenResult.getTitle());
        assertEquals(bookResponse.getReleaseDate(), givenResult.getReleaseDate());
        assertEquals(bookResponse.getDescription(), givenResult.getDescription());
        assertEquals(bookResponse.getAuthor(), givenResult.getAuthor());
    }

    @Test
    void mapCreateRequestToEntity() {

        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        Book givenResult = bookMapper.mapCreateRequestToEntity(createBookRequest);

        assertEquals(book.getTitle(), givenResult.getTitle());
        assertEquals(book.getDescription(), givenResult.getDescription());
        assertEquals(book.getReleaseDate(), givenResult.getReleaseDate());
        assertEquals(book.getAuthor(), givenResult.getAuthor());
    }

    @Test
    void mapCreateRequestToEntityShouldThrowRuntimeExceptionWhenAuthorIsNotFound() {
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookMapper.mapCreateRequestToEntity(createBookRequest));
    }

    @Test
    void updateEntityFromUpdateRequest() {

        when(authorRepository.findById(updateBookRequest.getAuthorId())).thenReturn(Optional.of(author));
        bookMapper.updateEntityFromUpdateRequest(book, updateBookRequest);

        assertEquals(updateBook.getId(), book.getId());
        assertEquals(updateBook.getTitle(), book.getTitle());
        assertEquals(updateBook.getReleaseDate(), book.getReleaseDate());
        assertEquals(updateBook.getDescription(), book.getDescription());
        assertEquals(updateBook.getAuthor().getId(), book.getAuthor().getId());
    }
}