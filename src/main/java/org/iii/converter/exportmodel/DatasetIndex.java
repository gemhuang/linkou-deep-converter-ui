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
public class DatasetIndex {
    private String id;

    @JsonProperty("provider_code")
    private String providerCode;

    @JsonProperty("provider_name")
    private String providerName;

    private String title;

    @JsonProperty("update_time")
    private String updateTime;

    @JsonProperty("data_zip_size")
    private String dataZipSize;

    private String ranking;

    @JsonProperty("file_type")
    private String fileType;

    @JsonProperty("viewer_count")
    private Integer viewerCount;

    private String imgsrc;
}
