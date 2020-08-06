package org.iii.converter.service;

import org.apache.commons.collections4.CollectionUtils;
import org.iii.converter.datamodel.Dataset;
import org.iii.converter.datamodel.DatasetAPI;
import org.iii.converter.datamodel.DatasetFile;
import org.iii.converter.message.DatasetMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class DatasetServiceTest {

    @Autowired
    private DatasetService service;

    private Dataset dataset;

    private Path dataZip;

    private List<DatasetFile> datasetFiles;

    private List<DatasetAPI> datasetAPIs;

    @BeforeEach
    public void init() throws URISyntaxException {
        dataset = initDatasetMain();
        dataZip = loadDataZip();
        datasetAPIs = initDatasetAPIs(dataset);
    }

    private Dataset initDatasetMain() {
        return Dataset.builder()
                .id(UUID.randomUUID())
                .title("test dataset")
                .subTitle("test sub title")
                .description("**description**")
                .fileLicense(DatasetMessage.LICENSES[0])
                .apiLicense(DatasetMessage.LICENSES[0])
                .imageSrc("https://images.unsplash.com/photo-1559319895-7c3deca286cf?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80")
                .category(DatasetMessage.CATEGORIES.get("其他"))
                .build();
    }

    private Path loadDataZip() throws URISyntaxException {
        return Paths.get(this.getClass().getResource("/data.zip").toURI());
    }

    private List<DatasetAPI> initDatasetAPIs(Dataset dataset) {
        DatasetAPI api =
                DatasetAPI.builder()
                        .id(UUID.randomUUID())
                        .datasetId(dataset.getId())
                        .apiName("GET test API")
                        .method(DatasetMessage.METHODS[0])
                        .url("http://test-domain.iii.org.tw/api")
                        .format(DatasetMessage.FORMATS[0])
                        .authentication(DatasetMessage.AUTHORITIES[1])
                        .queryMode(DatasetMessage.QUERIES[0])
                        .frequency(DatasetAPI.FREQUENCY_UNCERTAIN)
                        .build();

        return Arrays.asList(api);
    }

//    @Test
    public void testCreateDataset() {
        // Test Insert
        datasetFiles = service.parseDataZip(dataset, dataZip);
        addFileDesc(datasetFiles);
        service.saveDataset(dataset);
        service.saveFiles(dataset, datasetFiles);
        service.saveAPIs(dataset, datasetAPIs);

        // Test Query
        List<Dataset> datasetList = service.getAll();
        Assertions.assertTrue(CollectionUtils.isNotEmpty(datasetList));

        Dataset saved = datasetList.get(0);

        List<DatasetFile> savedFiles = service.getFilesByDataset(saved);
        Assertions.assertTrue(CollectionUtils.isNotEmpty(savedFiles));

        List<DatasetAPI> savedAPIs = service.getAPIsByDataset(saved);
        Assertions.assertTrue(CollectionUtils.isNotEmpty(savedAPIs));

        // Test Update
        saved.setApiLicense(DatasetMessage.LICENSES[1]);

        DatasetFile savedFile = savedFiles.get(0);
        savedFile.setDescription("_a new desc_");

        DatasetAPI savedAPI = savedAPIs.get(0);
        savedAPI.setAuthentication(DatasetMessage.AUTHORITIES[0]);

        service.saveFiles(saved, savedFiles);
        service.saveAPIs(saved, savedAPIs);
        service.saveDataset(saved);

        // Test Delete
//        service.removeAPIs(saved);
//        service.removeFiles(saved);
//        service.removeDataset(saved);
    }

    private void addFileDesc(List<DatasetFile> datasetFiles) {
        datasetFiles.forEach(file -> file.setDescription("###test file description"));
    }

    @AfterEach
    public void finish() {

    }
}
