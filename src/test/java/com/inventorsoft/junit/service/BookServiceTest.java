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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorRepository authorRepository;

    @Mock
    BookMapper bookMapper;

    @InjectMocks
    BookService bookService;

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
    static UpdateBookRequest updateBookRequest = new UpdateBookRequest();

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
    void getById() {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(bookMapper.mapEntityToResponse(book)).thenReturn(bookResponse);
        BookResponse br = bookService.getById(ID);

        assertEquals(bookResponse, br);
    }

    @Test
    void getByIdMustThrowExceptionIfIdIsMissing() {
        assertThrows(RuntimeException.class, () -> bookService.getById(null));
    }

    @Test
    void getAll() {
        List<Book> bookList = List.of(book);
        List<BookResponse> bookResponseList = List.of(bookResponse);

        when(bookRepository.findAll()).thenReturn(bookList);
        when(bookMapper.mapEntitiesToResponses(bookList)).thenReturn(bookResponseList);

        List<BookResponse> actualList = bookService.getAll();

        for (int i = 0; i < bookList.size() && i < actualList.size(); i++) {
            assertEquals(bookResponseList.get(i).getTitle(), actualList.get(i).getTitle());
            assertEquals(bookResponseList.get(i).getDescription(), actualList.get(i).getDescription());
            assertEquals(bookResponseList.get(i).getReleaseDate(), actualList.get(i).getReleaseDate());
            assertEquals(bookResponseList.get(i).getAuthor(), actualList.get(i).getAuthor());
        }
    }

    @Test
    void create() {
        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setAuthorId(AUTHOR_ID);
        createBookRequest.setTitle(TITLE);
        createBookRequest.setDescription(DESCRIPTION);
        createBookRequest.setReleaseDate(RELEASE_DATE);

        when(bookMapper.mapCreateRequestToEntity(createBookRequest)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        Long result = bookService.create(createBookRequest);

        assertEquals(ID, result);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).mapCreateRequestToEntity(createBookRequest);
    }

    @Test
    void update() {
        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setTitle(TITLE);
        updateBookRequest.setDescription(DESCRIPTION);
        updateBookRequest.setReleaseDate(RELEASE_DATE);

        Book bookToUpdate = new Book();

        when(bookRepository.findById(ID)).thenReturn(Optional.of(bookToUpdate));
        doAnswer(
                (x) -> {
                    bookToUpdate.setAuthor(book.getAuthor());
                    bookToUpdate.setTitle(updateBookRequest.getTitle());
                    bookToUpdate.setReleaseDate(updateBookRequest.getReleaseDate());
                    bookToUpdate.setDescription(updateBookRequest.getDescription());
                    bookToUpdate.setReleaseDate(updateBookRequest.getReleaseDate());
                    return null;
                }
        ).when(bookMapper).updateEntityFromUpdateRequest(bookToUpdate, updateBookRequest);
        bookService.update(ID, updateBookRequest);

        assertEquals(book.getAuthor(), bookToUpdate.getAuthor());
        assertEquals(book.getTitle(), bookToUpdate.getTitle());
        assertEquals(book.getReleaseDate(), bookToUpdate.getReleaseDate());
        assertEquals(book.getDescription(), bookToUpdate.getDescription());
    }

    @Test
    void updateMustThrowExceptionWhenBookIdNotFound() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookService.update(ID, updateBookRequest));
    }

    @Test
    void changeAuthorForBook() {
        Author authorTemporary = new Author();
        authorTemporary.setId(1L);
        authorTemporary.setName("Temporary guy");

        Book bookToUpdate = new Book();
        bookToUpdate.setId(5L);
        bookToUpdate.setAuthor(authorTemporary);

        when(bookRepository.findById(5L)).thenReturn(Optional.of(bookToUpdate));
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));

        bookService.changeAuthorForBook(5L, AUTHOR_ID);

        assertEquals(book.getAuthor().getId(), bookToUpdate.getAuthor().getId());
    }

    @Test
    void changeAuthorForBookMustThrowErrorIfBookIdNotFound() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookService.changeAuthorForBook(ID, AUTHOR_ID));
    }

    @Test
    void changeAuthorForBookMustThrowErrorIfAuthorIdNotFound() {
        Long invalidId = 99999L;
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(authorRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookService.changeAuthorForBook(ID, invalidId));
    }

    @Test
    void changeAuthorForBookNewAuthorIdEqualsOld() {
        Book bookToUpdate = new Book();
        bookToUpdate.setId(5L);
        bookToUpdate.setAuthor(author);

        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));

        bookService.changeAuthorForBook(ID, AUTHOR_ID);

        verify(bookRepository, times(1)).findById(ID);
        verify(authorRepository, times(0)).findById(AUTHOR_ID);
        assertEquals(author, bookToUpdate.getAuthor());
    }

    @Test
    void deleteById() {
        bookService.deleteById(ID);
        verify(bookRepository, times(1)).deleteById(ID);
    }
}