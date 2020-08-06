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
public class DatasetAPIDetail {
    private String id;

    @JsonProperty("api_name")
    private String apiName;

    private String url;
    private String method;
    private String format;
    private String[] frequency;
    private String authentication;
    private String license;

    @JsonProperty("query_mode")
    private String queryMode;

    @JsonProperty("swagger_file_name")
    private String swaggerFileName;

    @JsonProperty("check_time")
    private String checkTime;
}
