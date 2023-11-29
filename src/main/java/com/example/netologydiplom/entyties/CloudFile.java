package com.example.netologydiplom.entyties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "files")
@Data
public class CloudFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(nullable = false, name = "filename")
    private String fileName;

    @Column(nullable = false)
    private LocalDateTime date;

    @Lob
    @Column(nullable = false)
    private byte[] fileData;

    @Column(nullable = false)
    private Long size;


    @ManyToOne()
    @JoinColumn(nullable = false)
    private User user;

    public CloudFile(String fileName, LocalDateTime date, byte[] fileData, Long size, User user) {
        this.fileName = fileName;
        this.date = date;
        this.fileData = fileData;
        this.size = size;
        this.user = user;
    }

}
