package ru.diploma.cloudstor;

import com.example.netologydiplom.dto.request.AuthRequest;
import com.example.netologydiplom.dto.response.AuthResponse;
import com.example.netologydiplom.entyties.CloudFile;
import com.example.netologydiplom.entyties.Role;
import com.example.netologydiplom.entyties.User;
import com.example.netologydiplom.model.EnumRoles;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataTest {
    public static final String USERNAME = "user@gmail.com";
    public static final String USER_UNAUTHORIZED = "unauthorized@gmail.com";
    public static final String PASSWORD = "100";
    public static final Long USER_ID = 1L;
    public static final String BEARER_TOKEN = "Bearer Token";
    public static final String FILENAME_1 = "file";
    public static final String FILENAME_2 = "file2";
    public static final String FILENAME_NEW = "file_new";
    public static final String TOKEN = "Token";
    public static final Set<Role> ROLES = new HashSet<>(List.of(new Role(EnumRoles.ROLE_ADMIN)));
    public static final UserDetails USER_DETAILS = new User(USER_ID, USERNAME, PASSWORD, ROLES);
    public static final AuthResponse AUTH_RESPONSE = new AuthResponse(TOKEN);
    public static final AuthRequest AUTH_REQUEST = new AuthRequest(USERNAME, PASSWORD);
    public static final MockMultipartFile MULTIPART_FILE = new MockMultipartFile(
            FILENAME_1,
            FILENAME_1.getBytes()
    );
    public static CloudFile TEST_FILE_1 = null;
    public static CloudFile TEST_FILE_2 = null;

    static {
        try {
            TEST_FILE_1 = new CloudFile(
                    FILENAME_1,
                    LocalDateTime.now(),
                    MULTIPART_FILE.getBytes(),
                    MULTIPART_FILE.getSize(),
                    new User(USER_ID, USERNAME, PASSWORD, ROLES)
            );
            TEST_FILE_2 = new CloudFile(
                    FILENAME_2,
                    LocalDateTime.now(),
                    MULTIPART_FILE.getBytes(),
                    MULTIPART_FILE.getSize(),
                    new User(USER_ID, USERNAME, PASSWORD, ROLES));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CloudFile FILE_EDIT_NAME = new CloudFile(FILENAME_NEW,
            TEST_FILE_1.getDate(),
            TEST_FILE_1.getFileData(),
            TEST_FILE_1.getSize(),
            TEST_FILE_1.getUser()
    );

    public static final List<CloudFile> CLOUD_FILES = List.of(
            TEST_FILE_1,
            TEST_FILE_2
    );
}
