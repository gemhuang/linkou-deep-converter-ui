package org.iii.converter.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import org.iii.converter.datamodel.Provider;

public class ProviderSQLBuilder {
    public String getTop1() {
        return new SQL() {{
            SELECT("top 1 " +
                    "id, " +
                    "code, " +
                    "name, " +
                    "official_tel as officialTel, " +
                    "official_email as officialEmail, " +
                    "address, " +
                    "description, " +
                    "website, " +
                    "logo");
            FROM("providers");
        }}.toString();
    }

    public String insert(final @Param("provider") Provider provider) {
        return new SQL() {{
            INSERT_INTO("providers");
            VALUES("id", "#{provider.id}");
            VALUES("code", "#{provider.code}");
            VALUES("name", "#{provider.name}");
            VALUES("official_tel", "#{provider.officialTel}");
            VALUES("official_email", "#{provider.officialEmail}");
            VALUES("address", "#{provider.address}");
            VALUES("description", "#{provider.description}");
            VALUES("website", "#{provider.website}");
            VALUES("logo", "#{provider.logo}");
        }}.toString();
    }

    public String update(final @Param("provider") Provider provider) {
        return new SQL() {{
            UPDATE("providers");
            SET("code=#{provider.code}");
            SET("name=#{provider.name}");
            SET("official_tel=#{provider.officialTel}");
            SET("official_email=#{provider.officialEmail}");
            SET("address=#{provider.address}");
            SET("description=#{provider.description}");
            SET("website=#{provider.website}");
            SET("logo=#{provider.logo}");

            WHERE("id=#{provider.id}");
        }}.toString();
    }

    public String delete() {
        return new SQL() {{
            DELETE_FROM("providers");
        }}.toString();
    }
}
