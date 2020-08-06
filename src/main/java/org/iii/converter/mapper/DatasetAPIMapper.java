package org.iii.converter.mapper;

import org.apache.ibatis.annotations.*;
import org.iii.converter.datamodel.DatasetAPI;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface DatasetAPIMapper {
    @SelectProvider(type = DatasetAPISQLBuilder.class, method = "getAll")
    List<DatasetAPI> getAll();

    @SelectProvider(type = DatasetAPISQLBuilder.class, method = "getById")
    Optional<DatasetAPI> getById(final @Param("id") UUID id);

    @SelectProvider(type = DatasetAPISQLBuilder.class, method = "getByDatasetId")
    List<DatasetAPI> getByDatasetId(final @Param("id") UUID datasetId);

    @InsertProvider(type = DatasetAPISQLBuilder.class, method = "insert")
    void insert(final @Param("datasetAPI") DatasetAPI datasetAPI);

    @UpdateProvider(type = DatasetAPISQLBuilder.class, method = "update")
    void update(final @Param("datasetAPI") DatasetAPI datasetAPI);

    @DeleteProvider(type = DatasetAPISQLBuilder.class, method = "deleteByDatasetId")
    void deleteByDatasetId(final @Param("id") UUID datasetId);

    @DeleteProvider(type = DatasetAPISQLBuilder.class, method = "deleteById")
    void deleteById(final @Param("id") UUID id);
}
