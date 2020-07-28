package org.iii.converter.datamodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatasetFile {
    private UUID id;
    private UUID datasetId;
    private String fileName;
    private String fileSize;
    private String fileType;
    private String spec;
    private String description;
}
