package by.arvisit.modsenlibapp.libraryservice.controller.openapi;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.hibernate.validator.constraints.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import by.arvisit.modsenlibapp.exceptionhandlingstarter.response.ExceptionResponse;
import by.arvisit.modsenlibapp.libraryservice.dto.BorrowedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.dto.LibraryBookDto;
import by.arvisit.modsenlibapp.libraryservice.dto.ReturnedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.validation.IsBookExist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Library Controller", description = "API for borrowing books")
public interface LibraryOpenApi {

    String BAD_REQUEST = "BAD REQUEST";
    String UNAUTHORIZED = "UNAUTHORIZED";
    String OK = "OK";
    String CREATED = "CREATED";
    String FORBIDDEN = "FORBIDDEN";
    String NOT_FOUND = "NOT FOUND";

    @Operation(
            summary = "Adding a new book",
            description = "Util endpoint used by Book Service via Feign Client. Should not be used for direct requests. Admin role is required",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = CREATED,
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LibraryBookDto.class))),
                    @ApiResponse(
                            responseCode = "400", description = BAD_REQUEST,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403", description = FORBIDDEN,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            })
    ResponseEntity<LibraryBookDto> addNewBook(@Parameter(description = "New book identificator",
            required = true, content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = LibraryBookDto.class))) @RequestBody @Valid LibraryBookDto newBook);

    @Operation(
            summary = "Obtainment of the available books",
            description = "Obtain list of the available books identificators. Admin or User role is required",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = OK,
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LibraryBookDto.class))),
                    @ApiResponse(
                            responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403", description = FORBIDDEN,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            })
    List<LibraryBookDto> getAvailableBooks();

    @Operation(
            summary = "Obtainment of the borrowed books",
            description = "Obtain list of the borrowed books identificators. Admin or User role is required",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = OK,
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LibraryBookDto.class))),
                    @ApiResponse(
                            responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403", description = FORBIDDEN,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            })
    List<LibraryBookDto> getBorrowedBooks();

    @Operation(
            summary = "Borrowing a book",
            description = "Used to return a book. User role is required",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = CREATED,
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BorrowedBookResponseDto.class))),
                    @ApiResponse(
                            responseCode = "400", description = BAD_REQUEST,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403", description = FORBIDDEN,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404", description = NOT_FOUND,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            })
    ResponseEntity<BorrowedBookResponseDto> borrowBook(@Parameter(description = "Book to borrow identificator",
            required = true,
            example = "d4cee2e6-cb52-4a17-ae47-f4ad85dd8b86") @PathVariable @UUID @IsBookExist String id);

    @Operation(
            summary = "Returning a book",
            description = "Used to return a book. Admin role is required",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = OK,
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReturnedBookResponseDto.class))),
                    @ApiResponse(
                            responseCode = "400", description = BAD_REQUEST,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403", description = FORBIDDEN,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404", description = NOT_FOUND,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            })
    ReturnedBookResponseDto returnBook(@Parameter(description = "Book to return identificator",
            required = true,
            example = "d4cee2e6-cb52-4a17-ae47-f4ad85dd8b86") @PathVariable @UUID @IsBookExist String id);

    @Operation(
            summary = "Borrowed book info obtainment",
            description = "Used to obtain info about borrowed book. Admin role is required",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = OK,
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BorrowedBookResponseDto.class))),
                    @ApiResponse(
                            responseCode = "400", description = BAD_REQUEST,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403", description = FORBIDDEN,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404", description = NOT_FOUND,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            })
    BorrowedBookResponseDto getBorrowedBookInfo(@Parameter(description = "Borrowed book of interest identificator",
            required = true,
            example = "d4cee2e6-cb52-4a17-ae47-f4ad85dd8b86") @PathVariable @UUID @IsBookExist String id);

}