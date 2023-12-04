package com.example.netologydiplom.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileWebResponse {
    @JsonProperty(value = "filename")
    private String filename;
    @JsonProperty(value = "size")
    private Integer size;

    public FileWebResponse(String filename, Integer size) {
        this.filename = filename;
        this.size = size;
    }

    public String getFileName() {
        return filename;
    }

    public void setFileName(String filename) {
        this.filename = filename;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
