package org.iii.converter.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.iii.converter.datamodel.DatasetAPI;

import java.util.UUID;

public class DatasetAPISQLBuilder {
    public String getAll() {
        return new SQL() {{
            SELECT("id, " +
                    "dataset_id as datasetId, " +
                    "api_name as apiName, " +
                    "method, " +
                    "url, " +
                    "format, " +
                    "query_mode as queryMode, " +
                    "authentication, " +
                    "frequency");
            FROM("apis");
        }}.toString();
    }

    public String getById(final @Param("id") UUID id) {
        return new SQL() {{
            SELECT("id, " +
                    "dataset_id as datasetId, " +
                    "api_name as apiName, " +
                    "method, " +
                    "url, " +
                    "format, " +
                    "query_mode as queryMode, " +
                    "authentication, " +
                    "frequency");
            FROM("apis");
            WHERE("id=#{id}");
        }}.toString();
    }

    public String getByDatasetId(final @Param("id") UUID datasetId) {
        return new SQL() {{
            SELECT("id, " +
                    "dataset_id as datasetId, " +
                    "api_name as apiName, " +
                    "method, " +
                    "url, " +
                    "format, " +
                    "query_mode as queryMode, " +
                    "authentication, " +
                    "frequency");
            FROM("apis");
            WHERE("dataset_id=#{id}");
        }}.toString();
    }

    public String insert(final @Param("datasetAPI") DatasetAPI datasetAPI) {
        return new SQL() {{
            INSERT_INTO("apis");
            VALUES("id", "#{datasetAPI.id}");
            VALUES("dataset_id", "#{datasetAPI.datasetId}");
            VALUES("api_name", "#{datasetAPI.apiName}");
            VALUES("method", "#{datasetAPI.method}");
            VALUES("url", "#{datasetAPI.url}");
            VALUES("format", "#{datasetAPI.format}");
            VALUES("query_mode", "#{datasetAPI.queryMode}");
            VALUES("authentication", "#{datasetAPI.authentication}");
            VALUES("frequency", "#{datasetAPI.frequency}");
        }}.toString();
    }

    public String update(final @Param("datasetAPI") DatasetAPI datasetAPI) {
        return new SQL() {{
            UPDATE("apis");
            SET("api_name", "#{datasetAPI.apiName}");
            SET("method", "#{datasetAPI.method}");
            SET("url", "#{datasetAPI.url}");
            SET("format", "#{datasetAPI.format}");
            SET("query_mode", "#{datasetAPI.queryMode}");
            SET("authentication", "#{datasetAPI.authentication}");
            SET("frequency", "#{datasetAPI.frequency}");
            WHERE("id=#{datasetAPI.id}");
        }}.toString();
    }

    public String deleteByDatasetId(final @Param("id") UUID datasetId) {
        return new SQL() {{
            DELETE_FROM("apis");
            WHERE("dataset_id=#{id}");
        }}.toString();
    }

    public String deleteById(final @Param("id") UUID id) {
        return new SQL() {{
            DELETE_FROM("apis");
            WHERE("id=#{id}");
        }}.toString();
    }
}
