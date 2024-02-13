package by.arvisit.modsenlibapp.libraryservice.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import by.arvisit.modsenlibapp.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;
import by.arvisit.modsenlibapp.innerfilterstarter.filter.JwtInnerFilter;
import by.arvisit.modsenlibapp.libraryservice.dto.LibraryBookDto;
import by.arvisit.modsenlibapp.libraryservice.service.BookService;
import by.arvisit.modsenlibapp.libraryservice.service.LibraryService;
import by.arvisit.modsenlibapp.libraryservice.util.LibraryTestData;

@WebMvcTest(controllers = LibraryController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtInnerFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerAdvice.class)
class LibraryControllerTest {

    private static final String INVALID_UUID_MESSAGE = "must be a valid UUID";
    private static final String BOOK_ALREADY_EXISTS_MESSAGE = "Book with such id already exists.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibraryService libraryService;

    @MockBean
    private BookService bookService;

    @Nested
    class AddNewBook {

        @Test
        void shouldReturn201_when_passValidInput() throws Exception {
            LibraryBookDto requestDto = LibraryTestData.getNewBook().build();

            Mockito.when(bookService.isBookExist(Mockito.any())).thenReturn(false);

            mockMvc.perform(post(LibraryTestData.URL_BOOKS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldMapsToBusinessModel_when_passValidInput() throws Exception {
            LibraryBookDto requestDto = LibraryTestData.getNewBook().build();
            LibraryBookDto responseDto = LibraryTestData.getNewBook().build();

            Mockito.when(bookService.isBookExist(Mockito.any())).thenReturn(false);
            Mockito.when(libraryService.addNewBook(requestDto)).thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(post(LibraryTestData.URL_BOOKS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            Mockito.verify(libraryService, Mockito.times(1)).addNewBook(requestDto);
            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            LibraryBookDto result = objectMapper.readValue(actualResponseBody, LibraryBookDto.class);

            Assertions.assertThat(result).isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidBook_when_passValidInput() throws Exception {
            LibraryBookDto requestDto = LibraryTestData.getNewBook().build();
            LibraryBookDto responseDto = LibraryTestData.getNewBook().build();

            Mockito.when(bookService.isBookExist(Mockito.any())).thenReturn(false);
            Mockito.when(libraryService.addNewBook(requestDto)).thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(post(LibraryTestData.URL_BOOKS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            Assertions.assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.modsenlibapp.libraryservice.controller.LibraryControllerTest#invalidUUID")
        void shouldReturn400_when_passInvalidUUID(String id) throws Exception {
            LibraryBookDto requestDto = LibraryTestData.getNewBook()
                    .withId(id)
                    .build();

            String expectedContent = INVALID_UUID_MESSAGE;

            mockMvc.perform(post(LibraryTestData.URL_BOOKS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_when_passExistingId() throws Exception {
            LibraryBookDto requestDto = LibraryTestData.getNewBook().build();

            Mockito.when(bookService.isBookExist(Mockito.any())).thenReturn(true);

            String expectedContent = BOOK_ALREADY_EXISTS_MESSAGE;

            mockMvc.perform(post(LibraryTestData.URL_BOOKS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

    }

    @Nested
    class GetAvailableBooks {

        @Test
        void shouldReturn200_when_invokeGetAvailableBooks() throws Exception {
            mockMvc.perform(get(LibraryTestData.URL_AVAILABLE_BOOKS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldMapsToBusinessModel_when_invokeGetAvailableBooks() throws Exception {
            List<LibraryBookDto> responseDtos = List.of(LibraryTestData.getNewBook().build());

            Mockito.when(libraryService.getAvailableBooks()).thenReturn(responseDtos);

            MvcResult mvcResult = mockMvc.perform(get(LibraryTestData.URL_AVAILABLE_BOOKS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            Mockito.verify(libraryService, Mockito.times(1)).getAvailableBooks();
            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            List<LibraryBookDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<List<LibraryBookDto>>() {
                    });

            Assertions.assertThat(result).isEqualTo(responseDtos);
        }

        @Test
        void shouldReturnValidBooks_when_invokeGetAvailableBooks() throws Exception {
            List<LibraryBookDto> responseDtos = List.of(LibraryTestData.getNewBook().build());

            Mockito.when(libraryService.getAvailableBooks()).thenReturn(responseDtos);

            MvcResult mvcResult = mockMvc.perform(get(LibraryTestData.URL_AVAILABLE_BOOKS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            Assertions.assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDtos));
        }

    }

    @Nested
    class GetBorrowedBooks {

        @Test
        void shouldReturn200_when_invokeGetBorrowedBooks() throws Exception {
            mockMvc.perform(get(LibraryTestData.URL_BORROWED_BOOKS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldMapsToBusinessModel_when_invokeGetBorrowedBooks() throws Exception {
            List<LibraryBookDto> responseDtos = List.of(LibraryTestData.getNewBook().build());

            Mockito.when(libraryService.getBorrowedBooks()).thenReturn(responseDtos);

            MvcResult mvcResult = mockMvc.perform(get(LibraryTestData.URL_BORROWED_BOOKS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            Mockito.verify(libraryService, Mockito.times(1)).getBorrowedBooks();
            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            List<LibraryBookDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<List<LibraryBookDto>>() {
                    });

            Assertions.assertThat(result).isEqualTo(responseDtos);
        }

        @Test
        void shouldReturnValidBooks_when_invokeGetBorrowedBooks() throws Exception {
            List<LibraryBookDto> responseDtos = List.of(LibraryTestData.getNewBook().build());

            Mockito.when(libraryService.getBorrowedBooks()).thenReturn(responseDtos);

            MvcResult mvcResult = mockMvc.perform(get(LibraryTestData.URL_BORROWED_BOOKS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            Assertions.assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDtos));
        }

    }

    static Stream<String> invalidUUID() {
        return Stream.of("abc", "66701adf364d-44bd-8d46-46ef01d2af4d", "0123-456-789-jd",
                "66701adf-364d-44gd-8d46-46ef01d2af4d");
    }
}
