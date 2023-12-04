package com.example.netologydiplom.mapper;

import com.example.netologydiplom.dto.response.FileWebResponse;
import com.example.netologydiplom.entyties.CloudFile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FileMapper {
    FileWebResponse cloudFileToFileWebResponse(CloudFile cloudFile);
}

