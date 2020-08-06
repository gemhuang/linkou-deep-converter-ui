package org.iii.converter.utility;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DBInitializrTest {
    private static final Logger logger = LoggerFactory.getLogger(DBInitializrTest.class);

    @Test
    public void testLoadSQLFile() throws Exception {
        String sql = DBInitialzr.loadSQLFile(DBInitialzr.CREATE_APIS);
        assertNotNull(sql);
        logger.info(sql);
    }

    // @Test
    public void testInitDB() throws Exception {
        DBInitialzr.DBConfig config = DBInitialzr.initDB();
        assertNotNull(config);
        logger.info(config.toString());
    }
}
