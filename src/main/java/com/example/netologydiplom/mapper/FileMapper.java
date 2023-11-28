package com.example.netologydiplom.mapper;

import com.example.netologydiplom.dto.response.FileWebResponse;
import com.example.netologydiplom.entyties.CloudFile;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface FileMapper {
    FileWebResponse cloudFileToFileWebResponse(CloudFile cloudFile);
}

