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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
public class BookMapperTest {

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
    final CreateBookRequest createBookRequest = new CreateBookRequest();


    @Mock
    AuthorRepository authorRepository;

    @Mock
    AuthorMapper authorMapper;

    @InjectMocks
    BookMapper bookMapper;

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


        createBookRequest.setTitle(TITLE);
        createBookRequest.setDescription(DESCRIPTION);
        createBookRequest.setReleaseDate(RELEASE_DATE);
        createBookRequest.setAuthorId(ID);
    }

    @Test
    public void mapEntityToResponse() {
        // when
        when(authorMapper.mapEntityToResponse(book.getAuthor())).thenReturn(authorResponse);
        BookResponse givenResult = bookMapper.mapEntityToResponse(book);

        // then
        assertEquals(bookResponse.getId(), givenResult.getId());
        assertEquals(bookResponse.getTitle(), givenResult.getTitle());
        assertEquals(bookResponse.getReleaseDate(), givenResult.getReleaseDate());
        assertEquals(bookResponse.getAuthor(), givenResult.getAuthor());
    }

    @Test
    public void mapEntitiesToResponses() {
        // given
        List<Book> books = List.of(book);
        List<BookResponse> expectedResults = List.of(bookResponse);

        // when
        when(authorMapper.mapEntityToResponse(book.getAuthor())).thenReturn(authorResponse);
        List<BookResponse> givenResults = bookMapper.mapEntitiesToResponses(books);

        // then
        assertEquals(expectedResults.size(), givenResults.size());
    }

    @Test
    public void mapCreateRequestToEntity() {
        // when
        when(authorRepository.findById(ID)).thenReturn(Optional.of(author));
        Book givenResult = bookMapper.mapCreateRequestToEntity(createBookRequest);

        // then
        assertEquals(book.getTitle(), givenResult.getTitle());
        assertEquals(book.getDescription(), givenResult.getDescription());
        assertEquals(book.getReleaseDate(), givenResult.getReleaseDate());
        assertEquals(book.getAuthor(), givenResult.getAuthor());
    }

    @Test
    public void mapCreateRequestToEntityShouldThrowRuntimeExceptionWhenAuthorNotFound() {
        // when
        when(authorRepository.findById(ID)).thenReturn(Optional.empty());

        //then
        assertThrows(RuntimeException.class, () -> bookMapper.mapCreateRequestToEntity(createBookRequest));
    }

    @Test
    public void updateEntityFromUpdateRequest() {
        // given
        final UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setTitle("update Title");
        updateBookRequest.setDescription("Update description");
        updateBookRequest.setReleaseDate(LocalDate.of(2020, 10, 10));
        updateBookRequest.setAuthorId(11L);

        Author updateAuthor = new Author();
        updateAuthor.setId(1L);
        updateAuthor.setName("updateAuthorName");
        updateAuthor.setBirthday(LocalDate.of(2020, 11, 11));

        Book updateBook = new Book();
        updateBook.setId(1L);
        updateBook.setTitle("update Title");
        updateBook.setDescription("Update description");
        updateBook.setReleaseDate(LocalDate.of(2020, 10, 10));
        updateBook.setAuthor(updateAuthor);

        // when
        when(authorRepository.findById(updateBookRequest.getAuthorId())).thenReturn(Optional.of(author));
        bookMapper.updateEntityFromUpdateRequest(book, updateBookRequest);

        // then
        assertEquals(updateBook.getId(), book.getId());
        assertEquals(updateBook.getTitle(), book.getTitle());
        assertEquals(updateBook.getReleaseDate(), book.getReleaseDate());
        assertEquals(updateBook.getDescription(), book.getDescription());
        assertEquals(updateBook.getAuthor().getId(), book.getAuthor().getId());
    }

}
