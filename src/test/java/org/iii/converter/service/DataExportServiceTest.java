package org.iii.converter.service;

import org.apache.commons.io.FileUtils;
import org.iii.converter.datamodel.Dataset;
import org.iii.converter.datamodel.DatasetAPI;
import org.iii.converter.datamodel.DatasetFile;
import org.iii.converter.datamodel.Provider;
import org.iii.converter.exception.DataExportException;
import org.iii.converter.message.DatasetMessage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class DataExportServiceTest {

    @Autowired
    private DataExportService exportService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private DatasetService datasetService;

    @BeforeEach
    public void init() throws URISyntaxException, IOException {
        buildProvider();
        buildDataset();
    }

    private void buildProvider() throws IOException, URISyntaxException {
        Provider provider =
                Provider.builder()
                        .id(UUID.randomUUID())
                        .code("III")
                        .name("財團法人資訊工業策進會")
                        .address("10622台北市大安區和平東路二段106號11樓")
                        .officialTel("02-6631-8168")
                        .officialEmail("")
                        .website("https://www.iii.org.tw/")
                        .description("以「數位轉型的化育者( Digital Transformation Enabler)」為定位，" +
                                "主要任務為整合智庫、人培與資通訊技術研發及推動之能量，發展符合產業需求的解決方案" +
                                "與應用服務，促進政府與產業的數位轉型。")
                        .logo("III.png")
                        .build();

        providerService.save(provider);

        prepareLogoFile(provider);
    }

    private void prepareLogoFile(Provider provider) throws URISyntaxException, IOException {
        Path sourceFile = Paths.get(this.getClass().getResource("/" + provider.getLogo()).toURI());
        byte[] image = Files.readAllBytes(sourceFile);
        providerService.saveImage(image, provider.getLogo());
    }

    private void buildDataset() throws URISyntaxException, IOException {
        Dataset dataset =
                Dataset.builder()
                        .id(UUID.randomUUID())
                        .title("test dataset")
                        .subTitle("test sub title")
                        .description("**description**")
                        .fileLicense(DatasetMessage.LICENSES[0])
                        .apiLicense(DatasetMessage.LICENSES[0])
                        .imageSrc("https://images.unsplash.com/photo-1559319895-7c3deca286cf?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80")
                        .category(DatasetMessage.CATEGORIES.get("其他"))
                        .build();
        datasetService.saveDataset(dataset);

        Path dataZip = prepareDataZip(dataset.getId());
        List<DatasetFile> datasetFiles = datasetService.parseDataZip(dataset, dataZip);
        datasetService.saveFiles(dataset, datasetFiles);

//        List<DatasetAPI> datasetAPIS = initDatasetAPIs(dataset);
//        datasetService.saveAPIs(dataset, datasetAPIS);
    }

    private Path prepareDataZip(UUID datasetId) throws URISyntaxException, IOException {
        String workDir = System.getProperty("user.dir") + "/workspace/files/" + datasetId.toString();
        Files.createDirectories(Paths.get(workDir));

        Path sourceFile = Paths.get(this.getClass().getResource("/data.zip").toURI());
        Path targetFile = Paths.get(workDir + "/data.zip");
        Files.copy(sourceFile, targetFile);

        return targetFile;
    }

    private List<DatasetAPI> initDatasetAPIs(Dataset dataset) {
        DatasetAPI api =
                DatasetAPI.builder()
                        .id(UUID.randomUUID())
                        .datasetId(dataset.getId())
                        .apiName("GET test API")
                        .url("http://test-domain.iii.org.tw/api")
                        .format(DatasetMessage.FORMATS[0])
                        .authentication(DatasetMessage.AUTHORITIES[1])
                        .method(DatasetMessage.METHODS[0])
                        .queryMode(DatasetMessage.QUERIES[0])
                        .frequency("5," + DatasetMessage.FREQUENCIES[1])
                        .build();

        return Arrays.asList(api);
    }

    //    @Test
    public void testExportFile() throws IOException, DataExportException {
        Path workspace = exportService.exportWorkspace();

        Assertions.assertTrue(workspace.toFile().exists());

        FileUtils.forceDelete(workspace.toFile());
    }

    @AfterAll
    public static void finish() throws IOException {
        Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "datasets.mv.db"));
        Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "datasets.track.db"));
    }

}
