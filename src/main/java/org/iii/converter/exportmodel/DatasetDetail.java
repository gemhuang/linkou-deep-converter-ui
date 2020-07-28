package org.iii.converter.exportmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatasetDetail {
    private String id;

    @JsonProperty("provider_code")
    private String providerCode;

    @JsonProperty("provider_name")
    private String providerName;

    private String title;
    private String subTitle;
    private String description;
    private String ranking;
    private String visibility;
    private String license;

    @JsonProperty("update_time")
    private String updateTime;

    private String imgsrc;

    @JsonProperty("data_zip_size")
    private String dataZipSize;
}
