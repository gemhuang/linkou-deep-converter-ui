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
public class DatasetAPI {

    public static final String FREQUENCY_UNCERTAIN = "";

    private UUID id;
    private UUID datasetId;
    private String apiName;
    private String method;
    private String url;
    private String format;
    private String queryMode;
    private String authentication;
    private String frequency;

    public static String buildFrequency(String freqNum, String freqUnit) {
        return String.format("%s,%s", freqNum, freqUnit);
    }

    public static String[] parseFrequency(String frequency) {
        return frequency.split(",");
    }
}
