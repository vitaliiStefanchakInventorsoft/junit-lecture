package com.inventorsoft.junit.mapper;

import com.inventorsoft.junit.dto.request.CreateBookRequest;
import com.inventorsoft.junit.dto.request.UpdateBookRequest;
import com.inventorsoft.junit.dto.response.AuthorResponse;
import com.inventorsoft.junit.dto.response.BookResponse;
import com.inventorsoft.junit.model.Author;
import com.inventorsoft.junit.model.Book;
import com.inventorsoft.junit.repository.AuthorRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookMapperTest {

    @Mock
    AuthorRepository authorRepository;

    @Mock
    AuthorMapper authorMapper;

    @InjectMocks
    BookMapper bookMapper;

    static final long AUTHOR_ID = 1L;
    static final String AUTHOR_NAME = "Charlotte Bronte";
    static final LocalDate AUTHOR_BIRTHDAY = LocalDate.of(1816, 4, 21);

    static final long ID = 2L;
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
    public void mapEntityToResponse() {
        when(authorMapper.mapEntityToResponse(book.getAuthor())).thenReturn(authorResponse);

        BookResponse givenResult = bookMapper.mapEntityToResponse(book);

        assertEquals(bookResponse.getId(), givenResult.getId());
        assertEquals(bookResponse.getTitle(), givenResult.getTitle());
        assertEquals(bookResponse.getReleaseDate(), givenResult.getReleaseDate());
        assertEquals(bookResponse.getDescription(), givenResult.getDescription());
        assertEquals(bookResponse.getAuthor(), givenResult.getAuthor());
    }

    @Test
    public void mapEntitiesToResponses() {
        List<Book> books = List.of(book);
        List<BookResponse> expectedResults = List.of(bookResponse);

        when(authorMapper.mapEntityToResponse(book.getAuthor())).thenReturn(authorResponse);

        List<BookResponse> givenResults = bookMapper.mapEntitiesToResponses(books);

        assertEquals(expectedResults.size(), givenResults.size());
        assertArrayEquals(expectedResults.toArray(), givenResults.toArray());
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
        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setTitle("The Little Prince");
        updateBookRequest.setDescription("Description");
        updateBookRequest.setReleaseDate(LocalDate.of(1943, 04, 23));
        updateBookRequest.setAuthorId(11L);

        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Antoine de Saint-Exupéry");
        author1.setBirthday(LocalDate.of(1900, 07, 29));

        Book book1 = new Book();
        book1.setId(2L);
        book1.setTitle("The Little Prince");
        book1.setDescription("Description");
        book1.setReleaseDate(LocalDate.of(1943, 04, 23));
        book1.setAuthor(author1);

        when(authorRepository.findById(updateBookRequest.getAuthorId())).thenReturn(Optional.of(author));
        bookMapper.updateEntityFromUpdateRequest(book, updateBookRequest);

        assertEquals(book1.getId(), book.getId());
        assertEquals(book1.getTitle(), book.getTitle());
        assertEquals(book1.getReleaseDate(), book.getReleaseDate());
        assertEquals(book1.getDescription(), book.getDescription());
        assertEquals(book1.getAuthor().getId(), book.getAuthor().getId());
    }
}
