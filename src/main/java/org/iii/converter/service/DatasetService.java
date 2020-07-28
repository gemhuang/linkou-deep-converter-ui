package org.iii.converter.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.iii.converter.datamodel.Dataset;
import org.iii.converter.datamodel.DatasetAPI;
import org.iii.converter.datamodel.DatasetFile;
import org.iii.converter.exception.DatasetFileException;
import org.iii.converter.mapper.DatasetAPIMapper;
import org.iii.converter.mapper.DatasetFileMapper;
import org.iii.converter.mapper.DatasetMapper;
import org.iii.converter.utility.ZipUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DatasetService {
    private static final Logger logger = LoggerFactory.getLogger(DatasetService.class);

    @Autowired
    private DatasetMapper datasetMapper;

    @Autowired
    private DatasetFileMapper datasetFileMapper;

    @Autowired
    private DatasetAPIMapper datasetAPIMapper;

    public synchronized void saveDataZip(byte[] fileContent, Dataset dataset) {
        try {
            Path fileDir = buildDataZipDir(dataset);
            Path file = fileDir.resolve("data.zip");
            Files.deleteIfExists(file);
            Files.write(file, fileContent);
        } catch (IOException ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            throw new DatasetFileException(ex);
        }
    }

    public Path getDataZip(Dataset dataset) {
        return Paths.get(System.getProperty("user.dir") + "/workspace/files/" + dataset.getId().toString() + "/data.zip");
    }

    private Path buildDataZipDir(Dataset dataset) throws IOException {
        Path dataZipDir = Paths.get(System.getProperty("user.dir") + "/workspace/files/" + dataset.getId());
        Files.createDirectories(dataZipDir);
        return dataZipDir;
    }


    public synchronized List<DatasetFile> parseDataZip(Dataset dataset, Path dataZip) {
        try {
            String tempDir = createTempDirByDatasetId(dataset.getId());
            ZipUtils.extractAll(dataZip.toFile(), tempDir);

            return Files.walk(Paths.get(tempDir))
                    .filter(Files::isRegularFile)
                    .map(path -> parseDataFile(dataset, path))
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            throw new DatasetFileException(ex);
        } finally {
            safeDelete(Paths.get(getTempDirByDatasetId(dataset.getId())));
        }
    }

    private void safeDelete(Path path) {
        try {
            FileUtils.deleteDirectory(path.toFile());
        } catch (IOException ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    private String createTempDirByDatasetId(UUID id) throws IOException {
        String tempDir = getTempDirByDatasetId(id);
        Files.createDirectories(Paths.get(tempDir));
        return tempDir;
    }

    private String getTempDirByDatasetId(UUID id) {
        return System.getProperty("user.dir") + "/workspace/upload/" + id.toString();
    }

    private DatasetFile parseDataFile(Dataset dataset, Path path) {
        try {
            String fileName = path.getFileName().toString();
            String size = Long.valueOf(Files.size(path)).toString();
            String type = FilenameUtils.getExtension(fileName).toUpperCase();
            String spec = parseCSVFile(path, type);
            String desc = generateDefaultDescription(spec, type, fileName);

            return DatasetFile.builder()
                    .id(UUID.randomUUID())
                    .datasetId(dataset.getId())
                    .fileName(fileName)
                    .fileType(type)
                    .fileSize(size)
                    .spec(spec)
                    .description(desc)
                    .build();
        } catch (IOException ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            throw new DatasetFileException(ex);
        }
    }

    private String generateDefaultDescription(String spec, String type, String name) {
        if (!"CSV".equals(type)) {
            return "";
        } else {
            String[] columns = spec.split(",");
            StringBuilder builder = new StringBuilder();

            builder.append(String.format("本檔案 %1$s 為 CSV 檔案, 其欄位如下:%n", name, type));
            for (String column : columns) {
                builder.append(String.format("* %1$s%n", column));
            }

            return builder.toString();
        }
    }

    private String parseCSVFile(Path file, String type) throws IOException {
        if (!"CSV".equals(type)) {
            return "";
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
                String headerLine = String.format("%s%n", br.readLine()).trim();
                List<String> headers =
                        Arrays.stream(headerLine.split(","))
                                .map(header -> StringUtils.isEmpty(header) ? "\"EMPTY\"" : header)
                                .collect(Collectors.toList());
                return String.join(",", headers);
            }
        }
    }

    public void saveDataset(Dataset dataset) {
        Optional<Dataset> exist = datasetMapper.getById(dataset.getId());

        if (exist.isPresent()) {
            datasetMapper.update(dataset);
        } else {
            datasetMapper.insert(dataset);
        }
    }

    public void saveFiles(Dataset dataset, List<DatasetFile> datasetFiles) {
        Optional<Dataset> exist = datasetMapper.getById(dataset.getId());

        if (exist.isPresent()) {
            datasetFileMapper.deleteByDatasetId(dataset.getId());
        }

        datasetFiles.forEach(datasetFileMapper::insert);
    }

    public void saveAPIs(Dataset dataset, List<DatasetAPI> datasetAPIs) {
        Optional<Dataset> exist = datasetMapper.getById(dataset.getId());

        if (exist.isPresent()) {
            datasetAPIMapper.deleteByDatasetId(dataset.getId());
        }

        datasetAPIs.forEach(datasetAPIMapper::insert);
    }

    public List<Dataset> getAll() {
        return datasetMapper.getAll();
    }

    public List<DatasetFile> getFilesByDataset(Dataset dataset) {
        return datasetFileMapper.getByDatasetId(dataset.getId());
    }

    public List<DatasetAPI> getAPIsByDataset(Dataset dataset) {
        return datasetAPIMapper.getByDatasetId(dataset.getId());
    }

    public void removeAPIs(Dataset dataset) {
        datasetMapper.delete(dataset);
    }

    public void removeFiles(Dataset dataset) {
        datasetFileMapper.deleteByDatasetId(dataset.getId());
    }


    public void removeDataset(Dataset dataset) {
        datasetAPIMapper.deleteByDatasetId(dataset.getId());
    }
}
