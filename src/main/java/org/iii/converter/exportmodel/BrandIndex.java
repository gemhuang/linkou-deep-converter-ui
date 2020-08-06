package org.iii.converter.exportmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandIndex {
    private String id;
    private String code;
    private String name;
    private String imgsrc;
}
