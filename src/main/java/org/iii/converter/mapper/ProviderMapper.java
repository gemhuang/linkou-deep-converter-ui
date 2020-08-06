package org.iii.converter.mapper;

import org.apache.ibatis.annotations.*;
import org.iii.converter.datamodel.Provider;

@Mapper
public interface ProviderMapper {
    @SelectProvider(type = ProviderSQLBuilder.class,
            method = "getTop1")
    Provider getTop1();

    @InsertProvider(type = ProviderSQLBuilder.class,
            method = "insert")
    void insert(final @Param("provider") Provider provider);

    @UpdateProvider(type = ProviderSQLBuilder.class,
            method = "update")
    void update(final @Param("provider") Provider provider);

    @DeleteProvider(type = ProviderSQLBuilder.class,
            method = "delete")
    void delete();
}
