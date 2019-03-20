package com.signzy.ubuntu.service.mapper;

import com.signzy.ubuntu.domain.*;
import com.signzy.ubuntu.service.dto.DirectoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Directory and its DTO DirectoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DirectoryMapper extends EntityMapper<DirectoryDTO, Directory> {



    default Directory fromId(Long id) {
        if (id == null) {
            return null;
        }
        Directory directory = new Directory();
        directory.setId(id);
        return directory;
    }
}
