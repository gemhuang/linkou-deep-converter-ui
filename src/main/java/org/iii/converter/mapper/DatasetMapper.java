package org.iii.converter.mapper;

import org.apache.ibatis.annotations.*;
import org.iii.converter.datamodel.Dataset;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface DatasetMapper {
    @SelectProvider(type = DatasetSQLBuilder.class, method = "getAll")
    List<Dataset> getAll();

    @SelectProvider(type = DatasetSQLBuilder.class, method = "getById")
    Optional<Dataset> getById(final @Param("id") UUID id);

    @InsertProvider(type = DatasetSQLBuilder.class, method = "insert")
    void insert(final @Param("dataset") Dataset dataset);

    @UpdateProvider(type = DatasetSQLBuilder.class, method = "update")
    void update(final @Param("dataset") Dataset dataset);

    @DeleteProvider(type = DatasetSQLBuilder.class, method = "delete")
    void delete(final @Param("dataset") Dataset dataset);
}
