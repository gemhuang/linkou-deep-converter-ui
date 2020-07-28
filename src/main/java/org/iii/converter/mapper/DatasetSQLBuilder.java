package org.iii.converter.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.iii.converter.datamodel.Dataset;

import java.util.UUID;

public class DatasetSQLBuilder {
    public String getAll() {
        return new SQL() {{
            SELECT("id, " +
                    "title, " +
                    "sub_title as subTitle, " +
                    "description, " +
                    "image_src as imageSrc, " +
                    "file_license as fileLicense, " +
                    "api_license as apiLicense, " +
                    "category");
            FROM("datasets");
        }}.toString();
    }

    public String getById(final @Param("id") UUID id) {
        return new SQL() {{
            SELECT("id, " +
                    "title, " +
                    "sub_title as subTitle, " +
                    "description, " +
                    "image_src as imageSrc, " +
                    "file_license as fileLicense, " +
                    "api_license as apiLicense, " +
                    "category");
            FROM("datasets");
            WHERE("id=#{id}");
        }}.toString();
    }

    public String insert(final @Param("dataset") Dataset dataset) {
        return new SQL() {{
            INSERT_INTO("datasets");
            VALUES("id", "#{dataset.id}");
            VALUES("title", "#{dataset.title}");
            VALUES("sub_title", "#{dataset.subTitle}");
            VALUES("description", "#{dataset.description}");
            VALUES("image_src", "#{dataset.imageSrc}");
            VALUES("file_license", "#{dataset.fileLicense}");
            VALUES("api_license", "#{dataset.apiLicense}");
            VALUES("category", "#{dataset.category}");
        }}.toString();
    }

    public String update(final @Param("dataset") Dataset dataset) {
        return new SQL() {{
            UPDATE("datasets");
            SET("title=#{dataset.title}");
            SET("sub_title=#{dataset.subTitle}");
            SET("description=#{dataset.description}");
            SET("image_src=#{dataset.imageSrc}");
            SET("file_license=#{dataset.fileLicense}");
            SET("api_license=#{dataset.apiLicense}");
            SET("category=#{dataset.category}");
            WHERE("id=#{dataset.id}");
        }}.toString();
    }

    public String delete(final @Param("dataset") Dataset dataset) {
        return new SQL() {{
            DELETE_FROM("datasets");
            WHERE("id=#{dataset.id}");
        }}.toString();
    }
}
