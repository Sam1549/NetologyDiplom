package com.example.netologydiplom.repositories;

import com.example.netologydiplom.entyties.CloudFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface FilesRepository extends JpaRepository<CloudFile, Long> {
    Optional<List<CloudFile>> findByUserId(Long id);

    Optional<CloudFile> findByFileNameAndUserId(String filename, Long id);


}
