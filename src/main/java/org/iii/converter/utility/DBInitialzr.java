package org.iii.converter.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.google.common.base.Strings;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.iii.converter.exception.DBInitErrorException;
import org.springframework.core.io.ClassPathResource;

public class DBInitialzr {
    private static final Logger logger = LoggerFactory.getLogger(DBInitialzr.class);

    public static final String DB_FILE_NAME = "datasets";
    public static final String CREATE_PROVIDERS = "create-providers.sql";
    public static final String CREATE_DATASETS = "create-datasets.sql";
    public static final String CREATE_APIS = "create-apis.sql";
    public static final String CREATE_FILES = "create-files.sql";


    private static String getDBPath() {
        return System.getProperty("user.dir") + "/" + DB_FILE_NAME;
    }

    private static Connection buildConnection(String dbPath) throws SQLException {
        String dbPass = buildDBPass(dbPath);
        Connection conn = DriverManager.getConnection("jdbc:h2:" + dbPath, "sa", dbPass);
        return conn;
    }

    private static String buildDBPass(String dbPath) {
        return HashUtils.sha256(dbPath);
    }

    public static String loadSQLFile(String sqlFileName) throws IOException {
        String sqlDir = "/sql/";
//        List<String> sqlLines = Files.readAllLines(
//                Paths.get(DBInitialzr.class.getResource(sqlDir + sqlFileName).toURI()),
//                Charset.defaultCharset());
//        List<String> sqlLines = Files.readAllLines(Paths.get(new ClassPathResource("sql/" + sqlFileName).getURI()));
//        String sql = String.join("\n", sqlLines);
        String sql = IOUtils.toString(new ClassPathResource("sql/" + sqlFileName).getInputStream());
        return sql;
    }

    private static void createTables(Connection conn, String sqlFile)
            throws SQLException, IOException {
        String sql = loadSQLFile(sqlFile);
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private static void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

    private static DBConfig buildDBConfig(String dbPath) {
        return DBConfig.builder()
                .connStr("jdbc:h2:" + dbPath)
                .user("sa")
                .password(buildDBPass(dbPath))
                .build();
    }

    public static DBConfig initDB() throws DBInitErrorException {
        try {
            String dbPath = getDBPath();
            checkCreateDB(dbPath);
            return buildDBConfig(dbPath);
        } catch (SQLException | URISyntaxException | IOException ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            throw new DBInitErrorException(ex);
        }
    }

    private static void checkCreateDB(String dbPath) throws SQLException, IOException, URISyntaxException {
        String fullPath = dbPath + ".mv.db";
        File dbFile = new File(fullPath);

        if (!dbFile.exists()) {
            logger.info("checking db not exists, build it at " + dbPath);
            Connection conn = buildConnection(dbPath);
            createTables(conn, CREATE_PROVIDERS);
            createTables(conn, CREATE_DATASETS);
            createTables(conn, CREATE_APIS);
            createTables(conn, CREATE_FILES);
            closeConnection(conn);
        }
    }

    @Data
    @Builder
    public static class DBConfig {
        private String connStr;
        private String user;
        private String password;
    }
}
