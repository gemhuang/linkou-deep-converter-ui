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
public class Dataset {
    private UUID id;
    private String title;
    private String subTitle;
    private String description;
    private String imageSrc;
    private String fileLicense;
    private String apiLicense;
    private String category;
}
