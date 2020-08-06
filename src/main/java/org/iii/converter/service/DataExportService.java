package org.iii.converter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableBiMap;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.iii.converter.datamodel.*;
import org.iii.converter.exception.DataExportException;
import org.iii.converter.exportmodel.*;
import org.iii.converter.message.DatasetMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataExportService {

    private static final Logger logger = LoggerFactory.getLogger(DataExportService.class);

    private static final ImmutableBiMap<String, String> CATEGORY_FILES =
            ImmutableBiMap.<String, String>builder()
                    .put("BusinessPromotion", "businessPromotionData.json")
                    .put("CommunityHealth", "communityHealthData.json")
                    .put("UrbanGovernance", "urbanGovernanceData.json")
                    .put("Others", "othersData.json")
                    .build();

    @Autowired
    private ProviderService providerService;

    @Autowired
    private DatasetService datasetService;

    public synchronized Path exportWorkspace() throws DataExportException {
        TwoTuples<Provider, List<Dataset>> totalData = checkData();
        Provider provider = totalData.left;
        List<Dataset> datasets = totalData.right;

        Path exportDir = prepareExportDir(provider);

        buildProvider(provider, exportDir);
        buildDatasets(datasets, provider, exportDir);

        Path exportZip = exportZip(provider, exportDir);

        cleanExportDir(exportDir);

        return exportZip;
    }

    private void cleanExportDir(Path exportDir) throws DataExportException {
        try {
            FileUtils.deleteDirectory(exportDir.toFile());
        } catch (IOException ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            throw new DataExportException("清除匯出暫存目錄失敗");
        }
    }

    private TwoTuples<Provider, List<Dataset>> checkData() throws DataExportException {
        Optional<Provider> provider = providerService.getTop1();
        List<Dataset> datasets = datasetService.getAll();

        if (!provider.isPresent()) {
            throw new DataExportException("必須先建立品牌資料");
        } else if (CollectionUtils.isEmpty(datasets)) {
            throw new DataExportException("必須先建立數據集資料");
        }

        return new TwoTuples<>(provider.get(), datasets);
    }

    private Path prepareExportDir(Provider provider) throws DataExportException {
        try {
            Path exportDir = buildExportDirByProviderCode(provider.getCode());

            FileUtils.deleteDirectory(exportDir.toFile());

            Files.createDirectories(exportDir);

            Files.createDirectories(resolveDataZipDir(exportDir));

            Files.createDirectories(resolveImageDir(exportDir));
            Files.createDirectories(resolveBrandImageDir(exportDir));

            Files.createDirectories(resolveJsonIndexDir(exportDir));
            Files.createDirectories(resolveBrandDetailDir(exportDir));
            Files.createDirectories(resolveDatasetDetailDir(exportDir));

            return exportDir;
        } catch (IOException ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            throw new DataExportException("匯出目錄建立失敗");
        }
    }

    private Path resolveDataZipDir(Path exportDir) {
        return exportDir.resolve("files");
    }

    private Path resolveJsonIndexDir(Path exportDir) {
        return exportDir.resolve("json");
    }

    private Path resolveBrandDetailDir(Path exportDir) {
        return resolveJsonIndexDir(exportDir).resolve("brandDetail");
    }

    private Path resolveDatasetDetailDir(Path exportDir) {
        return resolveJsonIndexDir(exportDir).resolve("detailData");
    }

    private Path resolveImageDir(Path exportDir) {
        return exportDir.resolve("images");
    }

    private Path resolveBrandImageDir(Path exportDir) {
        return resolveImageDir(exportDir).resolve("brands");
    }

    private Path buildExportDirByProviderCode(String code) {
        String exportDir = System.getProperty("user.dir") + "/workspace/" + code;
        return Paths.get(exportDir);
    }

    private void buildProvider(Provider provider, Path exportDir) throws DataExportException {
        BrandIndex index = buildBrandIndex(provider);
        exportBrandIndex(index, exportDir);

        BrandDetail detail = buildBrandDetail(provider);
        exportBrandDetail(detail, exportDir);

        prepareBrandLogo(provider, exportDir);
    }

    private void prepareBrandLogo(Provider provider, Path exportDir) throws DataExportException {
        Path source = providerService.getImageFile(provider.getLogo());
        Path target = resolveBrandImageDir(exportDir).resolve(provider.getLogo());

        try {
            Files.copy(source, target);
        } catch (IOException ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            throw new DataExportException("品牌圖片匯出錯誤");
        }
    }

    private BrandIndex buildBrandIndex(Provider provider) {
        return BrandIndex.builder()
                .id(provider.getId().toString())
                .code(provider.getCode())
                .name(provider.getName())
                .imgsrc(provider.getLogo())
                .build();
    }

    private void exportBrandIndex(BrandIndex index, Path exportDir) throws DataExportException {
        try {
            Path brandDataFile = resolveJsonIndexDir(exportDir).resolve("brandData.json");

            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.writeValue(brandDataFile.toFile(), new Object[]{index});
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new DataExportException("brandData.json 匯出失敗");
        }
    }

    private BrandDetail buildBrandDetail(Provider provider) {
        return BrandDetail.builder()
                .id(provider.getId().toString())
                .code(provider.getCode())
                .name(provider.getName())
                .address(provider.getAddress())
                .officialEmail(provider.getOfficialEmail())
                .officialTel(provider.getOfficialTel())
                .website(provider.getWebsite())
                .description(parseDescription(provider.getDescription()))
                .imgsrc(provider.getLogo())
                .build();
    }

    private String parseDescription(String description) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(description);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    private void exportBrandDetail(BrandDetail detail, Path exportDir) throws DataExportException {
        try {
            Path brandDetailFile = resolveBrandDetailDir(exportDir).resolve(detail.getCode() + ".json");

            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.writeValue(brandDetailFile.toFile(), Collections.singletonMap("provider", detail));
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new DataExportException("brandDetail/ 匯出失敗");
        }
    }

    private void buildDatasets(List<Dataset> datasets, Provider provider, Path exportDir) throws DataExportException {
        List<DatasetIndex> mainIndexes = new LinkedList<>();
        Map<String, List<DatasetIndex>> cateIndexes = buildCategoryMap();

        for (Dataset dataset : datasets) {
            List<DatasetFile> datasetFiles = datasetService.getFilesByDataset(dataset);
            List<DatasetAPI> datasetAPIs = datasetService.getAPIsByDataset(dataset);

            DatasetDetailCompositor detailCompo = buildDatasetDetailCompositor(dataset, provider, datasetFiles, datasetAPIs, exportDir);
            exportDatasetDetail(detailCompo, exportDir);

            DatasetIndex index = buildDatasetIndex(detailCompo);
            mainIndexes.add(index);
            cateIndexes.get(dataset.getCategory()).add(index);

            prepareDatasetFile(dataset, datasetFiles, exportDir);
        }

        exportDatasetIndex(mainIndexes, exportDir);
        exportDatasetCategories(cateIndexes, exportDir);
    }

    private Map<String, List<DatasetIndex>> buildCategoryMap() {
        Map<String, List<DatasetIndex>> categoryMap = new HashMap<>();

        for (String category : DatasetMessage.CATEGORIES.values()) {
            categoryMap.put(category, new LinkedList<>());
        }

        return categoryMap;
    }

    private DatasetIndex buildDatasetIndex(DatasetDetailCompositor detailCompo) {
        DatasetDetail datasetDetail = detailCompo.getDataset();
        String fileTypes = buildFileTypes(detailCompo.getDatasetfiles());

        return DatasetIndex.builder()
                .id(datasetDetail.getId())
                .providerCode(datasetDetail.getProviderCode())
                .providerName(datasetDetail.getProviderName())
                .title(datasetDetail.getTitle())
                .updateTime(datasetDetail.getUpdateTime())
                .dataZipSize(datasetDetail.getDataZipSize())
                .ranking("10")
                .fileType(fileTypes)
                .viewerCount(0)
                .imgsrc(datasetDetail.getImgsrc())
                .build();
    }

    private String buildFileTypes(List<DatasetFileDetail> datasetFiles) {
        Set<String> fileTypes = new HashSet<>();

        datasetFiles.forEach(datasetFile -> fileTypes.add(datasetFile.getFileType()));

        return String.join(",", fileTypes);
    }

    private DatasetDetailCompositor buildDatasetDetailCompositor(Dataset dataset, Provider provider, List<DatasetFile> datasetFiles, List<DatasetAPI> datasetAPIs, Path exportDir) throws DataExportException {
        DatasetDetail datasetDetail = builDatasetDetail(dataset, provider, datasetFiles);
        List<DatasetFileDetail> datasetFileDetails = buildDatasetFileDetails(datasetFiles);
        List<DatasetAPIDetail> datasetAPIDetails = buildDatasetAPIDetails(datasetAPIs, dataset);

        return DatasetDetailCompositor.builder()
                .dataset(datasetDetail)
                .datasetfiles(CollectionUtils.isEmpty(datasetFileDetails) ? Collections.emptyList() : datasetFileDetails)
                .api(CollectionUtils.isEmpty(datasetAPIDetails) ? Collections.emptyList() : datasetAPIDetails)
                .build();
    }

    private List<DatasetAPIDetail> buildDatasetAPIDetails(List<DatasetAPI> datasetAPIs, Dataset dataset) {
        if (CollectionUtils.isEmpty(datasetAPIs)) {
            return Collections.emptyList();
        } else {
            return datasetAPIs.stream()
                    .map(datasetAPI ->
                            DatasetAPIDetail.builder()
                                    .id(datasetAPI.getId().toString())
                                    .apiName(datasetAPI.getApiName())
                                    .url(datasetAPI.getUrl())
                                    .method(datasetAPI.getMethod())
                                    .format(datasetAPI.getFormat())
                                    .frequency(split(datasetAPI.getFrequency(), ","))
                                    .authentication(datasetAPI.getAuthentication())
                                    .license(dataset.getApiLicense())
                                    .queryMode(datasetAPI.getQueryMode())
                                    .swaggerFileName("")
                                    .checkTime(getNow())
                                    .build())
                    .collect(Collectors.toList());
        }
    }

    private String[] split(String text, String sep) {
        if (StringUtils.isEmpty(text)) {
            return new String[0];
        } else {
            return text.split(sep);
        }
    }

    private List<DatasetFileDetail> buildDatasetFileDetails(List<DatasetFile> datasetFiles) {
        if (CollectionUtils.isEmpty(datasetFiles)) {
            return Collections.emptyList();
        } else {
            return datasetFiles.stream()
                    .map(datasetFile ->
                            DatasetFileDetail.builder()
                                    .id(datasetFile.getId().toString())
                                    .fileName(datasetFile.getFileName())
                                    .fileSize(datasetFile.getFileSize())
                                    .fileType(datasetFile.getFileType())
                                    .description(parseDescription(datasetFile.getDescription()))
                                    .fileFields(split(datasetFile.getSpec(), ","))
                                    .build())
                    .collect(Collectors.toList());
        }
    }

    private DatasetDetail builDatasetDetail(Dataset dataset, Provider provider, List<DatasetFile> datasetFiles) throws DataExportException {
        return DatasetDetail.builder()
                .id(dataset.getId().toString())
                .providerCode(provider.getCode())
                .providerName(provider.getName())
                .title(dataset.getTitle())
                .subTitle(dataset.getSubTitle())
                .description(parseDescription(dataset.getDescription()))
                .ranking("10")
                .visibility("0")
                .license(dataset.getFileLicense())
                .updateTime(getNow())
                .imgsrc(dataset.getImageSrc())
                .dataZipSize(calDataZipSize(dataset, datasetFiles))
                .build();
    }

    private String getNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String calDataZipSize(Dataset dataset, List<DatasetFile> datasetFiles) throws DataExportException {
        if (CollectionUtils.isEmpty(datasetFiles)) {
            return "0";
        } else {
            Path dataZipFile = datasetService.getDataZip(dataset);

            try {
                return String.valueOf(Files.size(dataZipFile));
            } catch (IOException ex) {
                logger.error(ExceptionUtils.getStackTrace(ex));
                throw new DataExportException("數據集檔案讀取錯誤");
            }
        }
    }

    private void exportDatasetDetail(DatasetDetailCompositor compo, Path exportDir) throws DataExportException {
        Path datasetDetailDir = resolveDatasetDetailDir(exportDir);

        DatasetDetail detail = compo.getDataset();
        Path datasetDetailFile = datasetDetailDir.resolve(detail.getId() + ".json");

        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.writeValue(datasetDetailFile.toFile(), compo);
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new DataExportException(String.format("detaiData/%s.json 匯出失敗", detail.getId()));
        }
    }

    private void exportDatasetIndex(List<DatasetIndex> mainIndexes, Path exportDir) throws DataExportException {
        Path jsonIndexDir = resolveJsonIndexDir(exportDir);
        Path indexFile = jsonIndexDir.resolve("homeData.json");
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.writeValue(indexFile.toFile(), Collections.singletonMap("data", mainIndexes));
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new DataExportException("homeData.json 匯出失敗");
        }
    }

    private void exportDatasetCategories(Map<String, List<DatasetIndex>> cateIndexes, Path exportDir) throws DataExportException {
        Path jsonIndexDir = resolveJsonIndexDir(exportDir);
        String fileName = "";

        try {
            ObjectMapper jsonMapper = new ObjectMapper();

            for (String key : cateIndexes.keySet()) {
                fileName = CATEGORY_FILES.get(key);
                Path categoryFile = jsonIndexDir.resolve(fileName);
                jsonMapper.writeValue(categoryFile.toFile(), Collections.singletonMap("data", cateIndexes.get(key)));
            }
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new DataExportException(String.format("%s 匯出失敗", fileName));
        }
    }

    private void prepareDatasetFile(Dataset dataset, List<DatasetFile> datasetFiles, Path exportDir) throws DataExportException {
        if (CollectionUtils.isEmpty(datasetFiles)) {
            return;
        } else {
            Path dataZipDir = resolveDataZipDir(exportDir);
            Path datasetIdDir = dataZipDir.resolve(dataset.getId().toString());
            Path dataZip = datasetService.getDataZip(dataset);

            try {
                Files.createDirectories(datasetIdDir);
                Files.copy(dataZip, datasetIdDir.resolve("data.zip"));
            } catch (IOException ex) {
                logger.error(ExceptionUtils.getStackTrace(ex));
                throw new DataExportException(String.format("files/%s 之 data.zip 匯出錯誤", dataset.getId().toString()));
            }
        }
    }

    private Path exportZip(Provider provider, Path exportDir) throws DataExportException {
        String fileName = provider.getCode() + ".zip";
        Path exportFile = exportDir.getParent().resolve(fileName);
        try {
            Files.deleteIfExists(exportFile);

            new ZipFile(exportFile.toFile()).addFolder(exportDir.toFile());
            return exportFile;
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new DataExportException("zip 檔匯出錯誤");
        }
    }
}
