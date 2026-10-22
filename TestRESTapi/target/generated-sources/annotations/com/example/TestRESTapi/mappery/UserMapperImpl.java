package com.example.TestRESTapi.mappery;

import com.example.TestRESTapi.DTO.ResponseUserDTO;
import com.example.TestRESTapi.model.UserModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-23T19:29:37+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public ResponseUserDTO toDto(UserModel model) {
        if ( model == null ) {
            return null;
        }

        ResponseUserDTO responseUserDTO = new ResponseUserDTO();

        responseUserDTO.setId( model.getId() );
        responseUserDTO.setFirstName( model.getFirstName() );
        responseUserDTO.setLastName( model.getLastName() );
        responseUserDTO.setEmail( model.getEmail() );
        responseUserDTO.setBirthDate( model.getBirthDate() );
        responseUserDTO.setAddress( model.getAddress() );
        responseUserDTO.setActive( model.isActive() );
        List<String> list = model.getRoles();
        if ( list != null ) {
            responseUserDTO.setRoles( new ArrayList<String>( list ) );
        }
        responseUserDTO.setCreatedAt( model.getCreatedAt() );

        return responseUserDTO;
    }

    @Override
    public UserModel toModel(ResponseUserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        userModel.setId( dto.getId() );
        userModel.setFirstName( dto.getFirstName() );
        userModel.setLastName( dto.getLastName() );
        userModel.setEmail( dto.getEmail() );
        userModel.setBirthDate( dto.getBirthDate() );
        userModel.setAddress( dto.getAddress() );
        userModel.setActive( dto.isActive() );
        List<String> list = dto.getRoles();
        if ( list != null ) {
            userModel.setRoles( new ArrayList<String>( list ) );
        }
        userModel.setCreatedAt( dto.getCreatedAt() );

        return userModel;
    }
}
