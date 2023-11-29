package com.example.netologydiplom.services;

import com.example.netologydiplom.dto.request.FileEditNameRequest;
import com.example.netologydiplom.dto.response.FileWebResponse;
import com.example.netologydiplom.entyties.CloudFile;
import com.example.netologydiplom.entyties.User;
import com.example.netologydiplom.exceptions.FileCloudException;
import com.example.netologydiplom.exceptions.InputDataException;
import com.example.netologydiplom.exceptions.UnauthorizedException;
import com.example.netologydiplom.mapper.FileMapper;
import com.example.netologydiplom.repositories.AuthRepository;
import com.example.netologydiplom.repositories.FilesRepository;
import com.example.netologydiplom.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilesService {
    private final FilesRepository filesRepository;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    private final FileMapper fileMapper;


    @Transactional
    public List<FileWebResponse> getAllFiles(String authToken) {
        String username = getUsername(authToken);
//        Long id = getUserIdFromToken(authToken).orElseThrow(() -> new UnauthorizedException(String.format("User '%s' not found", username)));
        User user = getUserFromToken(authToken).orElseThrow(() -> new UnauthorizedException(String.format("User '%s' not found", username)));
        List<CloudFile> list = filesRepository.findByUserId(user.getId()).orElseThrow(() -> new FileCloudException("Files not found"));
        return list.stream()
                .map(fileMapper::cloudFileToFileWebResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public void uploadFile(String authToken, String filename, MultipartFile file) {
        CloudFile cloudFile;
        String username = getUsername(authToken);
        User user = getUserFromToken(authToken).orElseThrow(() -> new UnauthorizedException(String.format("User '%s' not found", username)));
        Optional<CloudFile> fileIsHave = filesRepository.findByFileNameAndUserId(filename, user.getId());

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
                log.info("file '{}' successfully uploaded  by user '{}'", filename, username);
            } catch (IOException e) {
                throw new InputDataException(String.format("Upload file %s: Input data exception ", filename));
            }
        } else {
            try {
                cloudFile = fileIsHave.get();
                cloudFile.setFileData(file.getBytes());
                cloudFile.setSize(file.getSize());
                filesRepository.save(cloudFile);
                log.info("file '{}' successfully edit  by user '{}'", filename, username);
            } catch (IOException e) {
                throw new InputDataException(String.format("Upload file %s: Input data exception ", filename));
            }

        }

    }

    @Transactional
    public ResponseEntity<?> editFile(String authToken, String filename, FileEditNameRequest fileEditNameRequest) {
        String username = getUsername(authToken);
        User user = getUserFromToken(authToken).orElseThrow(() -> new UnauthorizedException(String.format("User '%s' not found", username)));
        Optional<CloudFile> fileIsHave = filesRepository.findByFileNameAndUserId(filename, user.getId());
        if (fileIsHave.isEmpty()) {
            throw new FileCloudException(String.format("File '%s' not found!", filename));
        } else {
            CloudFile cloudFile = fileIsHave.get();
            cloudFile.setFileName(fileEditNameRequest.getFilename());
            filesRepository.save(cloudFile);
            log.info("file '{}' was successfully renamed '{}'   by user '{}'", filename, fileEditNameRequest.getFilename(), username);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteFile(String authToken, String filename) {
        String username = getUsername(authToken);
        User user = getUserFromToken(authToken).orElseThrow(() -> new UnauthorizedException(String.format("User '%s' not found", username)));
        Optional<CloudFile> optionalCloudFile = filesRepository.findByFileNameAndUserId(filename, user.getId());
        if (optionalCloudFile.isEmpty()) {
            throw new FileCloudException(String.format("File '%s' not found!", filename));
        } else {
            filesRepository.delete(optionalCloudFile.get());
            log.info("file '{}' was successfully deleted  by user '{}'", filename, username);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<Resource> downloadFile(String authToken, String filename) {
        String username = getUsername(authToken);
        User user = getUserFromToken(authToken).orElseThrow(() -> new UnauthorizedException(String.format("User '%s' not found", username)));
        Optional<CloudFile> optionalCloudFile = filesRepository.findByFileNameAndUserId(filename, user.getId());
        if (optionalCloudFile.isEmpty()) {
            throw new FileCloudException(String.format("File '%s' not found!", filename));
        } else {
            log.info("file '{}' was successfully downloaded  by user '{}'", filename, username);
            return ResponseEntity.ok().body(new ByteArrayResource(optionalCloudFile.get().getFileData()));
        }
    }

    public Optional<User> getUserFromToken(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            final String authTokenWithoutBearer = authToken.split(" ")[1];
            final String username = authRepository.getUsernameByToken(authTokenWithoutBearer);
            Optional<User> user = userRepository.findByUsername(username);
            return user.isPresent() ? user : Optional.empty();
        }
        return Optional.empty();
    }

    public String getUsername(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            final String authTokenWithoutBearer = authToken.split(" ")[1];
            final String username = authRepository.getUsernameByToken(authTokenWithoutBearer);
//            return username.isEmpty() ? Optional.empty() : Optional.of(username);
            return username;
        }
        return null;
    }

    public Optional<Long> getUserIdFromToken(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            final String authTokenWithoutBearer = authToken.split(" ")[1];
            final String username = authRepository.getUsernameByToken(authTokenWithoutBearer);
            Optional<User> user = userRepository.findByUsername(username);
            return user.isPresent() ? ofNullable(user.get().getId()) : Optional.empty();
        }
        return Optional.empty();
    }
}
