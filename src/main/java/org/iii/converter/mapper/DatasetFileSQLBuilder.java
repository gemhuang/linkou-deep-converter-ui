package org.iii.converter.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.iii.converter.datamodel.DatasetFile;

import java.util.UUID;

public class DatasetFileSQLBuilder {
    public String getAll() {
        return new SQL() {{
            SELECT("id",
                    "dataset_id as datasetId",
                    "file_name as fileName",
                    "file_size as fileSize",
                    "file_type as fileType",
                    "spec",
                    "description");
            FROM("files");
        }}.toString();
    }

    public String getById(final @Param("id") UUID id) {
        return new SQL() {{
            SELECT("id",
                    "dataset_id as datasetId",
                    "file_name as fileName",
                    "file_size as fileSize",
                    "file_type as fileType",
                    "spec",
                    "description");
            FROM("files");
            WHERE("id=#{id}");
        }}.toString();
    }

    public String getByDatasetId(final @Param("id") UUID datasetId) {
        return new SQL() {{
            SELECT("id",
                    "dataset_id as datasetId",
                    "file_name as fileName",
                    "file_size as fileSize",
                    "file_type as fileType",
                    "spec",
                    "description");
            FROM("files");
            WHERE("dataset_id=#{id}");
        }}.toString();
    }

    public String insert(final @Param("datasetFile") DatasetFile datasetFile) {
        return new SQL() {{
            INSERT_INTO("files");
            VALUES("id", "#{datasetFile.id}");
            VALUES("dataset_id", "#{datasetFile.datasetId}");
            VALUES("file_name", "#{datasetFile.fileName}");
            VALUES("file_size", "#{datasetFile.fileSize}");
            VALUES("file_type", "#{datasetFile.fileType}");
            VALUES("spec", "#{datasetFile.spec}");
            VALUES("description", "#{datasetFile.description}");
        }}.toString();
    }

    public String update(final @Param("datasetFile") DatasetFile datasetFile) {
        return new SQL() {{
            UPDATE("files");
            SET("file_name", "#{datasetFile.fileName}");
            SET("file_size", "#{datasetFile.fileSize}");
            SET("file_type", "#{datasetFile.fileType}");
            SET("spec", "#{datasetFile.spec}");
            SET("description", "#{datasetFile.description}");
            WHERE("id=#{datasetFile.id}");
        }}.toString();
    }

    public String deleteByDatasetId(final @Param("id") UUID datasetId) {
        return new SQL() {{
            DELETE_FROM("files");
            WHERE("dataset_id=#{id}");
        }}.toString();
    }

    public String deleteById(final @Param("id") UUID id) {
        return new SQL() {{
            DELETE_FROM("files");
            WHERE("id=#{id}");
        }}.toString();
    }
}
