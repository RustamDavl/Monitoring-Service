package ru.rstdv.monitoringservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.User;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    ReadUserDto toReadUserDto(User user);


    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "address.street", source = "street")
    @Mapping(target = "address.houseNumber", source = "houseNumber")
    User toUser(CreateUpdateUserDto from);
}
