package by.arvisit.modsenlibapp.libraryservice.controller;

import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.AVAILABLE_NOT_BORROWED_YET_BOOK_ID;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.BORROWED_RETURNED_BOOK_ID;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.URL_AVAILABLE_BOOKS_ENDPOINT;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.URL_BOOKS_ENDPOINT;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.URL_BORROWED_BOOKS_ENDPOINT;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.URL_BORROWED_BOOK_INFO_ENDPOINT;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.URL_BORROW_BOOK_ENDPOINT;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.URL_RETURN_BOOK_ENDPOINT;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.getAdmin;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.getAvailableNotBorrowedBook;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.getAvailableReturnedBook;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.getBorrowedBookRecordFromDB;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.getBorrowedReturnedBook;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.getNewBook;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.getNewBorrowedBookRecord;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.getReturnedBookRecord;
import static by.arvisit.modsenlibapp.libraryservice.util.LibraryITData.getUser;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import by.arvisit.modsenlibapp.innerfilterstarter.dto.UserDto;
import by.arvisit.modsenlibapp.libraryservice.PostgreSQLTestContainerExtension;
import by.arvisit.modsenlibapp.libraryservice.dto.LibraryBookDto;
import by.arvisit.modsenlibapp.libraryservice.dto.BorrowedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.dto.ReturnedBookResponseDto;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-books-and-records.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-books-and-records.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
@WireMockTest(httpPort = 8480)
class LibraryControllerIT {

    private static final String USERS_VALIDATE_URL = "/api/v1/users/validate";
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturn201AndJsonContentType_when_invokeAddNewBook() throws Exception {
        LibraryBookDto requestBody = getNewBook().build();

        UserDto user = getAdmin();
        wireMockResponse(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);
        HttpEntity<LibraryBookDto> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_BOOKS_ENDPOINT,
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldReturnExpectedResponse_when_invokeAddNewBook() throws Exception {
        LibraryBookDto requestBody = getNewBook().build();

        UserDto user = getAdmin();
        wireMockResponse(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);
        HttpEntity<LibraryBookDto> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<LibraryBookDto> responseEntity = restTemplate.exchange(
                URL_BOOKS_ENDPOINT,
                HttpMethod.POST,
                requestEntity,
                LibraryBookDto.class);

        LibraryBookDto expected = getNewBook().build();

        LibraryBookDto result = responseEntity.getBody();
        assertThat(result.id()).isEqualTo(expected.id());
    }

    @Test
    void shouldReturn200AndJsonContentType_when_invokeGetAvailableBooks() throws Exception {
        UserDto user = getUser();
        wireMockResponse(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_AVAILABLE_BOOKS_ENDPOINT,
                HttpMethod.GET,
                requestEntity,
                String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldReturnExpectedResponse_when_invokeGetAvailableBooks() throws Exception {
        UserDto user = getUser();
        wireMockResponse(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<LibraryBookDto>> responseEntity = restTemplate.exchange(
                URL_AVAILABLE_BOOKS_ENDPOINT,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        List<LibraryBookDto> result = responseEntity.getBody();
        List<LibraryBookDto> expected = List.of(getAvailableNotBorrowedBook().build(), getAvailableReturnedBook().build());

        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void shouldReturn200AndJsonContentType_when_invokeGetBorrowedBooks() throws Exception {
        UserDto user = getUser();
        wireMockResponse(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_BORROWED_BOOKS_ENDPOINT,
                HttpMethod.GET,
                requestEntity,
                String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldReturnExpectedResponse_when_invokeGetBorrowedBooks() throws Exception {
        UserDto user = getUser();
        wireMockResponse(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<LibraryBookDto>> responseEntity = restTemplate.exchange(
                URL_BORROWED_BOOKS_ENDPOINT,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        List<LibraryBookDto> result = responseEntity.getBody();
        List<LibraryBookDto> expected = List.of(getBorrowedReturnedBook().build());

        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void shouldReturn201AndJsonContentType_when_invokeBorrowBook() throws Exception {
        UserDto user = getUser();
        wireMockResponse(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_BORROW_BOOK_ENDPOINT,
                HttpMethod.POST,
                requestEntity,
                String.class,
                AVAILABLE_NOT_BORROWED_YET_BOOK_ID);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldReturnExpectedResponseAndExpectedBorrowedList_when_invokeBorrowBook() throws Exception {
        UserDto user = getUser();
        wireMockResponse(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<BorrowedBookResponseDto> responseEntity = restTemplate.exchange(
                URL_BORROW_BOOK_ENDPOINT,
                HttpMethod.POST,
                requestEntity,
                BorrowedBookResponseDto.class,
                AVAILABLE_NOT_BORROWED_YET_BOOK_ID);

        BorrowedBookResponseDto expected = getNewBorrowedBookRecord().build();

        BorrowedBookResponseDto result = responseEntity.getBody();
        assertThat(result).isEqualTo(expected);

        HttpEntity<Void> getBorrowedRequestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<LibraryBookDto>> getBorrowedResponseEntity = restTemplate.exchange(
                URL_BORROWED_BOOKS_ENDPOINT,
                HttpMethod.GET,
                getBorrowedRequestEntity,
                new ParameterizedTypeReference<>() {
                });

        List<LibraryBookDto> getBorrowedResult = getBorrowedResponseEntity.getBody();
        List<LibraryBookDto> getBorrowedExpected = List.of(getBorrowedReturnedBook().build(),
                getAvailableNotBorrowedBook().build());

        assertThat(getBorrowedResult).containsExactlyInAnyOrderElementsOf(getBorrowedExpected);
    }

    @Test
    void shouldReturn200AndJsonContentType_when_invokeReturnBook() throws Exception {
        UserDto user = getAdmin();
        wireMockResponse(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_RETURN_BOOK_ENDPOINT,
                HttpMethod.PATCH,
                requestEntity,
                String.class,
                BORROWED_RETURNED_BOOK_ID);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldReturnExpectedResponseAndExpectedAvailableList_when_invokeReturnBook() throws Exception {
        UserDto user = getAdmin();
        wireMockResponse(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ReturnedBookResponseDto> responseEntity = restTemplate.exchange(
                URL_RETURN_BOOK_ENDPOINT,
                HttpMethod.PATCH,
                requestEntity,
                ReturnedBookResponseDto.class,
                BORROWED_RETURNED_BOOK_ID);

        ReturnedBookResponseDto expected = getReturnedBookRecord().build();

        ReturnedBookResponseDto result = responseEntity.getBody();
        assertThat(result).isEqualTo(expected);

        HttpEntity<Void> getReturnedRequestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<LibraryBookDto>> getBorrowedResponseEntity = restTemplate.exchange(
                URL_AVAILABLE_BOOKS_ENDPOINT,
                HttpMethod.GET,
                getReturnedRequestEntity,
                new ParameterizedTypeReference<>() {
                });

        List<LibraryBookDto> getAvailableResult = getBorrowedResponseEntity.getBody();
        List<LibraryBookDto> getAvailableExpected = List.of(getBorrowedReturnedBook().build(),
                getAvailableNotBorrowedBook().build(), getAvailableReturnedBook().build());

        assertThat(getAvailableResult).containsExactlyInAnyOrderElementsOf(getAvailableExpected);
    }

    @Test
    void shouldReturn200AndJsonContentType_when_invokeGetBorrowedBookInfo() throws Exception {
        UserDto user = getAdmin();
        wireMockResponse(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_BORROWED_BOOK_INFO_ENDPOINT,
                HttpMethod.GET,
                requestEntity,
                String.class,
                BORROWED_RETURNED_BOOK_ID);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldReturnExpectedResponse_when_invokeGetBorrowedBook() throws Exception {
        UserDto user = getAdmin();
        wireMockResponse(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<BorrowedBookResponseDto> responseEntity = restTemplate.exchange(
                URL_BORROWED_BOOK_INFO_ENDPOINT,
                HttpMethod.GET,
                requestEntity,
                BorrowedBookResponseDto.class,
                BORROWED_RETURNED_BOOK_ID);

        BorrowedBookResponseDto expected = getBorrowedBookRecordFromDB().build();

        BorrowedBookResponseDto result = responseEntity.getBody();
        assertThat(result).isEqualTo(expected);
    }

    private void wireMockResponse(UserDto user) throws JsonProcessingException {
        String body = objectMapper.writeValueAsString(user);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(USERS_VALIDATE_URL))
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(body)));
    }
}
