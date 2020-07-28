package org.iii.converter.datamodel;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Provider {
    private UUID id;
    private String code;
    private String name;
    private String officialTel;
    private String officialEmail;
    private String address;
    private String description;
    private String website;
    private String logo;

    public Provider buildNewOne() {
        return Provider.builder()
                .id(this.getId() != null ? this.getId() : UUID.randomUUID())
                .code(StringUtils.trimToEmpty(this.getCode()))
                .name(StringUtils.trimToEmpty(this.getName()))
                .officialTel(StringUtils.trimToEmpty(this.getOfficialTel()))
                .officialEmail(StringUtils.trimToEmpty(this.getOfficialEmail()))
                .address(StringUtils.trimToEmpty(this.getAddress()))
                .description(StringUtils.trimToEmpty(this.getDescription()))
                .website(StringUtils.trimToEmpty(this.getWebsite()))
                .logo(StringUtils.trimToEmpty(this.getLogo()))
                .build();
    }
}
