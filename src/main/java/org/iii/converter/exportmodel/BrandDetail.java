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
public class BrandDetail {
    private String id;
    private String code;
    private String name;
    private String imgsrc;

    @JsonProperty("official_tel")
    private String officialTel;

    @JsonProperty("official_email")
    private String officialEmail;

    private String address;
    private String website;
    private String description;
}
