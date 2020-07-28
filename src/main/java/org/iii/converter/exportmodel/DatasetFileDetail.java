package org.iii.converter.exportmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatasetFileDetail {
    private String id;

    @JsonProperty("file_name")
    private String fileName;

    private String description;

    @JsonProperty("file_size")
    private String fileSize;

    @JsonProperty("file_type")
    private String fileType;

    @JsonProperty("file_fields")
    private String[] fileFields;
}
