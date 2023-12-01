package com.example.netologydiplom.service;

import com.example.netologydiplom.DataTest;
import com.example.netologydiplom.dto.request.FileEditNameRequest;
import com.example.netologydiplom.dto.response.FileWebResponse;
import com.example.netologydiplom.entyties.CloudFile;
import com.example.netologydiplom.entyties.User;
import com.example.netologydiplom.mapper.FileMapper;
import com.example.netologydiplom.repositories.AuthRepository;
import com.example.netologydiplom.repositories.FilesRepository;
import com.example.netologydiplom.repositories.UserRepository;
import com.example.netologydiplom.services.FilesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("=== Testing File service ===")
public class FileServiceTest {

    @InjectMocks
    FilesService fileService;
    @Mock
    FilesRepository fileRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    AuthRepository authRepository;
    @Mock
    FileMapper fileMapper;

    @BeforeEach
    void setUp() {
        when(authRepository.getUsernameByToken(DataTest.TOKEN)).thenReturn(DataTest.USERNAME);
        when(fileService.getUsername(DataTest.BEARER_TOKEN)).thenReturn(DataTest.USERNAME);
        when(fileService.getUserFromToken(DataTest.BEARER_TOKEN)).thenReturn(Optional.of(new User(DataTest.USER_ID, DataTest.USERNAME, DataTest.PASSWORD, DataTest.ROLES)));
    }

    @Test
    void uploadFileTest() {
        fileService.uploadFile(DataTest.BEARER_TOKEN, DataTest.FILENAME_1, DataTest.MULTIPART_FILE);
        when(fileRepository.findByFileNameAndUserId(DataTest.FILENAME_1, DataTest.USER_ID)).thenReturn(Optional.of(DataTest.TEST_FILE_1));
        CloudFile result = fileRepository.findByFileNameAndUserId(DataTest.FILENAME_1, DataTest.USER_ID).get();
        Assertions.assertEquals(DataTest.TEST_FILE_1, result);
    }

    @Test
    void deleteFileTest() {
        when(fileRepository.findByFileNameAndUserId(DataTest.FILENAME_1, DataTest.USER_ID)).thenReturn(Optional.of(DataTest.TEST_FILE_1));
        fileService.deleteFile(DataTest.BEARER_TOKEN, DataTest.FILENAME_1);
        CloudFile result = fileRepository.findByFileNameAndUserId(DataTest.FILENAME_1, DataTest.USER_ID).get();
        verify(fileRepository, Mockito.times(1)).delete(result);
    }

    @Test
    void downloadFileTest() {
        when(fileRepository.findByFileNameAndUserId(DataTest.FILENAME_1, DataTest.USER_ID)).thenReturn(Optional.of(DataTest.TEST_FILE_1));
        fileService.downloadFile(DataTest.BEARER_TOKEN, DataTest.FILENAME_1);
        verify(fileRepository, Mockito.times(1)).findByFileNameAndUserId(DataTest.FILENAME_1, DataTest.USER_ID);
    }

    @Test
    void editFileTest() {
        when(fileRepository.findByFileNameAndUserId(DataTest.FILENAME_1, DataTest.USER_ID)).thenReturn(Optional.of(DataTest.TEST_FILE_1));
        fileService.editFile(DataTest.BEARER_TOKEN, DataTest.FILENAME_1, new FileEditNameRequest(DataTest.FILENAME_NEW));
        verify(fileRepository, Mockito.times(1)).save(DataTest.FILE_EDIT_NAME);
    }

    @Test
    void getAllFilesTest() {
        List<FileWebResponse> expectedList = List.of(
                new FileWebResponse(DataTest.FILENAME_1, DataTest.TEST_FILE_1.getSize().intValue()),
                new FileWebResponse(DataTest.FILENAME_2, DataTest.TEST_FILE_2.getSize().intValue()));
        when(fileRepository.findByUserId(DataTest.USER_ID)).thenReturn(Optional.of(DataTest.CLOUD_FILES));
        when(fileMapper.cloudFileToFileWebResponse(DataTest.TEST_FILE_1)).thenReturn(new FileWebResponse(DataTest.TEST_FILE_1.getFileName(), DataTest.TEST_FILE_1.getSize().intValue()));
        when(fileMapper.cloudFileToFileWebResponse(DataTest.TEST_FILE_2)).thenReturn(new FileWebResponse(DataTest.TEST_FILE_2.getFileName(), DataTest.TEST_FILE_2.getSize().intValue()));
        List<FileWebResponse> resultList = fileService.getAllFiles(DataTest.BEARER_TOKEN);
        assertEquals(expectedList, resultList);
    }

}
