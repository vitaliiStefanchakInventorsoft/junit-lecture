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
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookMapperTest {

    @Mock AuthorRepository authorRepository;
    @Mock AuthorMapper authorMapper;
    @InjectMocks BookMapper bookMapper;

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

    static Author author = new Author();
    static Book book = new Book();
    static BookResponse bookResponse = new BookResponse();
    static AuthorResponse authorResponse = new AuthorResponse();

    @BeforeEach
    void startInit() {
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        author.setBirthday(AUTHOR_BIRTHDAY);

        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setDescription(BOOK_DESCRIPTION);
        book.setReleaseDate(BOOK_RELEASE_DATE);
        book.setAuthor(author);

        authorResponse.setId(AUTHOR_ID);
        authorResponse.setName(AUTHOR_NAME);
        authorResponse.setBirthDate(AUTHOR_BIRTHDAY);

        bookResponse.setId(BOOK_ID);
        bookResponse.setDescription(BOOK_DESCRIPTION);
        bookResponse.setReleaseDate(BOOK_RELEASE_DATE);
        bookResponse.setTitle(BOOK_TITLE);
        bookResponse.setAuthor(authorResponse);
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
        IntStream.range(0, expectedResults.size()).forEachOrdered(i -> {
            assertEquals(expectedResults.get(i).getId(), givenResults.get(i).getId());
            assertEquals(expectedResults.get(i).getDescription(), givenResults.get(i).getDescription());
            assertEquals(expectedResults.get(i).getReleaseDate(), givenResults.get(i).getReleaseDate());
            assertEquals(expectedResults.get(i).getTitle(), givenResults.get(i).getTitle());
            assertEquals(expectedResults.get(i).getAuthor().getId(), givenResults.get(i).getAuthor().getId());
            assertEquals(expectedResults.get(i).getAuthor().getName(), givenResults.get(i).getAuthor().getName());
            assertEquals(expectedResults.get(i).getAuthor().getBirthDate(), givenResults.get(i).getAuthor().getBirthDate());
        });
    }

    @Test
    public void mapCreateRequestToEntity() {
        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setTitle(BOOK_TITLE);
        createBookRequest.setDescription(BOOK_DESCRIPTION);
        createBookRequest.setReleaseDate(BOOK_RELEASE_DATE);
        createBookRequest.setAuthorId(AUTHOR_ID);

        when(authorRepository.findById(createBookRequest.getAuthorId())).thenReturn(Optional.of(author));
        Book givenResult = bookMapper.mapCreateRequestToEntity(createBookRequest);

        assertEquals(book.getId(), givenResult.getId());
        assertEquals(book.getTitle(), givenResult.getTitle());
        assertEquals(book.getDescription(), givenResult.getDescription());
        assertEquals(book.getReleaseDate(), givenResult.getReleaseDate());
        assertEquals(book.getAuthor(), givenResult.getAuthor());
    }

    @Test
    public void whenAuthorIdIsNull_thenExceptionIsThrown() {
        assertThrows(RuntimeException.class,
                () -> authorRepository.findById(null)
                        .orElseThrow(RuntimeException::new));
    }

    @Test
    public void mapCreateRequestToEntityShouldThrowNullPointExceptionWhenRequestIsNull() {
        CreateBookRequest createBookRequest = null;
        Exception exception = assertThrows(NullPointerException.class, () ->
                bookMapper.mapCreateRequestToEntity(createBookRequest));
        assertEquals("Cannot invoke \"com.inventorsoft.junit.dto.request.CreateBookRequest.getTitle()" +
                "\" because \"request\" is null", exception.getMessage());
    }

    @Test
    void mapUpdateRequestToEntity() {
        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setTitle("New title");
        updateBookRequest.setDescription("description");
        updateBookRequest.setReleaseDate(LocalDate.now());
        updateBookRequest.setAuthorId(44L);

        Author newAuthor = new Author();
        newAuthor.setId(44L);
        newAuthor.setName("author");
        newAuthor.setBirthday(LocalDate.now());

        Book newBook = new Book();
        newBook.setDescription("description");
        newBook.setTitle("New title");
        newBook.setReleaseDate(LocalDate.now());
        newBook.setId(null);
        newBook.setAuthor(newAuthor);

        when(authorRepository.findById(updateBookRequest.getAuthorId())).thenReturn(Optional.of(author));
        bookMapper.updateEntityFromUpdateRequest(book,updateBookRequest);

        assertEquals(newBook.getId(), book.getId());
        assertEquals(newBook.getTitle(), book.getTitle());
        assertEquals(newBook.getReleaseDate(), book.getReleaseDate());
        assertEquals(newBook.getDescription(), book.getDescription());
        assertEquals(newBook.getAuthor().getId(), book.getAuthor().getId());
        assertEquals(newBook.getAuthor().getName(), book.getAuthor().getName());
        assertEquals(newBook.getAuthor().getBirthday(), book.getAuthor().getBirthday());
    }
}