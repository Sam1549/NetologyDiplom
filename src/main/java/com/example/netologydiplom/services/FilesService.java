package com.example.netologydiplom.services;

import com.example.netologydiplom.dto.request.FileEditNameRequest;
import com.example.netologydiplom.dto.response.FileWebResponse;
import com.example.netologydiplom.entyties.CloudFile;
import com.example.netologydiplom.entyties.User;
import com.example.netologydiplom.exceptions.FileCloudException;
import com.example.netologydiplom.exceptions.InputDataException;
import com.example.netologydiplom.exceptions.UnauthorizedException;
import com.example.netologydiplom.mapper.FileMapper;
import com.example.netologydiplom.repositories.FilesRepository;
import com.example.netologydiplom.repositories.UserRepository;
import com.example.netologydiplom.utils.JwtTokenUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilesService {
    private final FilesRepository filesRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    private final FileMapper fileMapper;


    @Transactional
    public List<FileWebResponse> getAllFiles(String authToken) {
        String username = getUsername(authToken);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UnauthorizedException("Пользователь не найден"));
        Long id = user.getId();
        List<CloudFile> list = filesRepository.findByUserId(id).orElseThrow(() -> new FileCloudException("Файл(ы) не найден(ы)"));
        return list.stream()
                .map(fileMapper::cloudFileToFileWebResponse)
                .collect(Collectors.toList());
    }

    private String getUsername(String authToken) {
        String jwt = null;
        if (authToken.startsWith("Bearer ")) {
            jwt = authToken.substring(7);
        }
        return jwtTokenUtils.getUsername(jwt);
    }

    @Transactional
    public void uploadFile(String authToken, String filename, MultipartFile file) {
        CloudFile cloudFile;
        String username = getUsername(authToken);
        Optional<CloudFile> fileIsHave = filesRepository.findByFileNameAndUserId(filename, userRepository.findByUsername(username).get().getId());

        if (fileIsHave.isEmpty()) {
            try {
                cloudFile = CloudFile.builder()
                        .fileName(filename)
                        .date(LocalDateTime.now())
                        .fileData(file.getBytes())
                        .size(file.getSize())
                        .user(userRepository.findByUsername(username).get())
                        .build();
                filesRepository.save(cloudFile);
            } catch (IOException e) {
                throw new InputDataException("Upload file: Input data exception");
            }
        } else {
            try {
                cloudFile = fileIsHave.get();
                cloudFile.setFileData(file.getBytes());
                cloudFile.setSize(file.getSize());
                filesRepository.save(cloudFile);
            } catch (IOException e) {
                throw new InputDataException("Upload file: Input data exception");
            }

        }

    }

    @Transactional
    public ResponseEntity<?> editFile(String authToken, String filename, FileEditNameRequest fileEditNameRequest) {
        String username = getUsername(authToken);
        Optional<CloudFile> fileIsHave = filesRepository.findByFileNameAndUserId(filename, userRepository.findByUsername(username).get().getId());
        if (fileIsHave.isEmpty()) {
            throw new FileCloudException("Файл не найден");
        } else {
            CloudFile cloudFile = fileIsHave.get();
            cloudFile.setFileName(fileEditNameRequest.getFilename());
            filesRepository.save(cloudFile);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteFile(String authToken, String filename) {
        String username = getUsername(authToken);
        Optional<CloudFile> optionalCloudFile = filesRepository.findByFileNameAndUserId(filename, userRepository.findByUsername(username).get().getId());
        if (optionalCloudFile.isEmpty()) {
            throw new FileCloudException("Файл не найден!");
        } else {
            filesRepository.delete(optionalCloudFile.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<Resource> downloadFile(String authToken, String filename) {
        String username = getUsername(authToken);
        Optional<CloudFile> optionalCloudFile = filesRepository.findByFileNameAndUserId(filename, userRepository.findByUsername(username).get().getId());

        if (optionalCloudFile.isEmpty()) {
            throw new FileCloudException("Файл не найден!");
        } else {
            return ResponseEntity.ok().body(new ByteArrayResource(optionalCloudFile.get().getFileData()));
        }
    }
}
