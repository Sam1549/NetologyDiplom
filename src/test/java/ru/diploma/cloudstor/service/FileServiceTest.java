package ru.diploma.cloudstor.service;

import com.example.netologydiplom.dto.request.FileEditNameRequest;
import com.example.netologydiplom.dto.response.FileWebResponse;
import com.example.netologydiplom.entyties.CloudFile;
import com.example.netologydiplom.entyties.User;
import com.example.netologydiplom.mapper.FileMapper;
import com.example.netologydiplom.repositories.AuthRepository;
import com.example.netologydiplom.repositories.FilesRepository;
import com.example.netologydiplom.repositories.UserRepository;
import com.example.netologydiplom.services.FilesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.diploma.cloudstor.DataTest.*;

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
        when(authRepository.getUsernameByToken(TOKEN)).thenReturn(USERNAME);
        when(fileService.getUsername(BEARER_TOKEN)).thenReturn(USERNAME);
        when(fileService.getUserFromToken(BEARER_TOKEN)).thenReturn(Optional.of(new User(USER_ID, USERNAME, PASSWORD, ROLES)));
    }

    @Test
    void uploadFileTest() {
        fileService.uploadFile(BEARER_TOKEN, FILENAME_1, MULTIPART_FILE);
        when(fileRepository.findByFileNameAndUserId(FILENAME_1, USER_ID)).thenReturn(Optional.of(TEST_FILE_1));
        CloudFile result = fileRepository.findByFileNameAndUserId(FILENAME_1, USER_ID).get();
        assertEquals(TEST_FILE_1, result);
    }

    @Test
    void deleteFileTest() {
        when(fileRepository.findByFileNameAndUserId(FILENAME_1, USER_ID)).thenReturn(Optional.of(TEST_FILE_1));
        fileService.deleteFile(BEARER_TOKEN, FILENAME_1);
        CloudFile result = fileRepository.findByFileNameAndUserId(FILENAME_1, USER_ID).get();
        verify(fileRepository, Mockito.times(1)).delete(result);
    }

    @Test
    void downloadFileTest() {
        when(fileRepository.findByFileNameAndUserId(FILENAME_1, USER_ID)).thenReturn(Optional.of(TEST_FILE_1));
        fileService.downloadFile(BEARER_TOKEN, FILENAME_1);
        verify(fileRepository, Mockito.times(1)).findByFileNameAndUserId(FILENAME_1, USER_ID);
    }

    @Test
    void editFileTest() {
        when(fileRepository.findByFileNameAndUserId(FILENAME_1, USER_ID)).thenReturn(Optional.of(TEST_FILE_1));
        fileService.editFile(BEARER_TOKEN, FILENAME_1, new FileEditNameRequest(FILENAME_NEW));
        verify(fileRepository, Mockito.times(1)).save(FILE_EDIT_NAME);
    }

    @Test
    void getAllFilesTest() {
        List<FileWebResponse> expectedList = List.of(
                new FileWebResponse(FILENAME_1, TEST_FILE_1.getSize().intValue()),
                new FileWebResponse(FILENAME_2, TEST_FILE_2.getSize().intValue()));
        when(fileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(CLOUD_FILES));
        when(fileMapper.cloudFileToFileWebResponse(TEST_FILE_1)).thenReturn(new FileWebResponse(TEST_FILE_1.getFileName(), TEST_FILE_1.getSize().intValue()));
        when(fileMapper.cloudFileToFileWebResponse(TEST_FILE_2)).thenReturn(new FileWebResponse(TEST_FILE_2.getFileName(), TEST_FILE_2.getSize().intValue()));
        List<FileWebResponse> resultList = fileService.getAllFiles(BEARER_TOKEN);
        assertEquals(expectedList, resultList);
    }

}
