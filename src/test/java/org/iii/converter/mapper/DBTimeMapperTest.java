package org.iii.converter.mapper;

import org.iii.converter.datamodel.DBTime;
import org.iii.converter.exception.DBInitErrorException;
import org.iii.converter.utility.DBInitialzr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DBTimeMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(DBTimeMapperTest.class);

    @Autowired
    private DBTimeMapper mapper;

    // @Test
    public void testDBTime() {
        DBTime dbTime = mapper.getDBTime();
        Assertions.assertNotNull(dbTime);
        logger.info(dbTime.toString());
    }
}
