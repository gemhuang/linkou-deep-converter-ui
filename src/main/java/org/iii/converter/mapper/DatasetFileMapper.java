package org.iii.converter.mapper;

import org.apache.ibatis.annotations.*;
import org.iii.converter.datamodel.DatasetFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface DatasetFileMapper {
    @SelectProvider(type = DatasetFileSQLBuilder.class, method = "getAll")
    List<DatasetFile> getAll();

    @SelectProvider(type = DatasetFileSQLBuilder.class, method = "getById")
    Optional<DatasetFile> getById(final @Param("id") UUID id);

    @SelectProvider(type = DatasetFileSQLBuilder.class, method = "getByDatasetId")
    List<DatasetFile> getByDatasetId(final @Param("id") UUID datasetId);

    @InsertProvider(type = DatasetFileSQLBuilder.class, method = "insert")
    void insert(final @Param("datasetFile") DatasetFile datasetFile);

    @UpdateProvider(type = DatasetFileSQLBuilder.class, method = "update")
    void update(final @Param("datasetFile") DatasetFile datasetFile);

    @DeleteProvider(type = DatasetFileSQLBuilder.class, method = "deleteByDatasetId")
    void deleteByDatasetId(final @Param("id") UUID datasetId);

    @DeleteProvider(type = DatasetFileSQLBuilder.class, method = "deleteById")
    void deleteById(final @Param("id") UUID id);
}
