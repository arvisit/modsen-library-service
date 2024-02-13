package by.arvisit.modsenlibapp.libraryservice.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import by.arvisit.modsenlibapp.libraryservice.dto.BorrowedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.dto.ReturnedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.persistence.model.BookRecord;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface BookRecordMapper {

    @Mapping(target = "borrowerUsername", source = "username")
    BorrowedBookResponseDto fromEntityToBorrowedBookDto(BookRecord entity);

    @Mapping(target = "borrowerUsername", source = "username")
    ReturnedBookResponseDto fromEntityToReturnedBookDto(BookRecord entity);
}
