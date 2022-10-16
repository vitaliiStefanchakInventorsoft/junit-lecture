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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookMapperTest {

    @Mock
    AuthorRepository authorRepository;

    @Mock
    AuthorMapper authorMapper;

    @InjectMocks
    BookMapper bookMapper;

    static final long ID = 1L;
    static final String TITLE = "Mr.Mercedes";
    static final LocalDate RELEASE_DATE = LocalDate.of(2014, 6, 3);
    static final String DESCRIPTION = "Novel";

    static final Long AUTHOR_ID = 2L;
    static final String AUTHOR_NAME = "Stephen King";
    static final LocalDate AUTHOR_BIRTHDAY = LocalDate.of(1947, 9, 21);
    static Author author = new Author();
    static Book book = new Book();
    static BookResponse bookResponse = new BookResponse();
    static AuthorResponse authorResponse = new AuthorResponse();

    @BeforeEach
    void init() {
        author.setBirthday(AUTHOR_BIRTHDAY);
        author.setName(AUTHOR_NAME);
        author.setId(AUTHOR_ID);

        book.setAuthor(author);
        book.setDescription(DESCRIPTION);
        book.setReleaseDate(RELEASE_DATE);
        book.setId(ID);
        book.setTitle(TITLE);

        authorResponse.setBirthDate(AUTHOR_BIRTHDAY);
        authorResponse.setName(AUTHOR_NAME);
        authorResponse.setId(AUTHOR_ID);

        bookResponse.setId(ID);
        bookResponse.setTitle(TITLE);
        bookResponse.setReleaseDate(RELEASE_DATE);
        bookResponse.setDescription(DESCRIPTION);
        bookResponse.setAuthor(authorResponse);
    }


    @Test
    void mapEntitiesToResponses() {
        List<Book> list = List.of(book);
        List<BookResponse> expectedResult = List.of(bookResponse);
        when(authorMapper.mapEntityToResponse(book.getAuthor())).thenReturn(authorResponse);
        List<BookResponse> actualResult = bookMapper.mapEntitiesToResponses(list);


        assertEquals(expectedResult.size(), actualResult.size());
        for (int i = 0; i < actualResult.size() && i < expectedResult.size(); i++) {
            assertEquals(expectedResult.get(i).getId(), actualResult.get(i).getId());
            assertEquals(expectedResult.get(i).getTitle(), actualResult.get(i).getTitle());
            assertEquals(expectedResult.get(i).getReleaseDate(), actualResult.get(i).getReleaseDate());
            assertEquals(expectedResult.get(i).getDescription(), actualResult.get(i).getDescription());
            assertEquals(expectedResult.get(i).getAuthor(), actualResult.get(i).getAuthor());
        }
    }

    @Test
    void mapEntityToResponse() {
        when(authorMapper.mapEntityToResponse(book.getAuthor())).thenReturn(authorResponse);

        BookResponse actualResponse = bookMapper.mapEntityToResponse(book);

        assertEquals(bookResponse.getId(), actualResponse.getId());
        assertEquals(bookResponse.getTitle(), actualResponse.getTitle());
        assertEquals(bookResponse.getDescription(), actualResponse.getDescription());
        assertEquals(bookResponse.getReleaseDate(), actualResponse.getReleaseDate());
        assertEquals(bookResponse.getAuthor(), actualResponse.getAuthor());
    }

    @Test
    void mapCreateRequestToEntity() {
        CreateBookRequest br = new CreateBookRequest();
        br.setTitle(TITLE);
        br.setDescription(DESCRIPTION);
        br.setAuthorId(AUTHOR_ID);
        br.setReleaseDate(RELEASE_DATE);

        when(authorRepository.findById(br.getAuthorId())).thenReturn(Optional.ofNullable(author));

        Book actualBook = bookMapper.mapCreateRequestToEntity(br);
        assertEquals(book.getTitle(), actualBook.getTitle());
        assertEquals(book.getDescription(), actualBook.getDescription());
        assertEquals(book.getReleaseDate(), actualBook.getReleaseDate());
        assertEquals(book.getAuthor(), actualBook.getAuthor());
    }

    @Test
    void mapCreateRequestToEntityThrowsExceptionWhenAuthorIdNull() {
        assertThrows(RuntimeException.class,
                () -> authorRepository.findById(null).orElseThrow(RuntimeException::new));
    }

    @Test
    void updateEntityFromUpdateRequest() {
        Book actualBook = new Book();
        UpdateBookRequest br = new UpdateBookRequest();
        br.setAuthorId(AUTHOR_ID);
        br.setTitle(TITLE);
        br.setReleaseDate(RELEASE_DATE);
        br.setDescription(DESCRIPTION);

        when(authorRepository.findById(br.getAuthorId())).thenReturn(Optional.ofNullable(author));

        bookMapper.updateEntityFromUpdateRequest(actualBook, br);

        assertEquals(book.getTitle(), actualBook.getTitle());
        assertEquals(book.getDescription(), actualBook.getDescription());
        assertEquals(book.getReleaseDate(), actualBook.getReleaseDate());
        assertEquals(book.getAuthor(), actualBook.getAuthor());
    }
}
