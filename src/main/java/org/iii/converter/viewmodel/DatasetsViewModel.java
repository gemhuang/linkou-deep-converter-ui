package org.iii.converter.viewmodel;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.iii.converter.config.ApplicationContextProvider;
import org.iii.converter.datamodel.Dataset;
import org.iii.converter.datamodel.DatasetAPI;
import org.iii.converter.datamodel.DatasetFile;
import org.iii.converter.exception.DatasetFileException;
import org.iii.converter.message.DatasetMessage;
import org.iii.converter.service.DatasetService;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Messagebox;

import java.nio.file.Path;
import java.util.*;

@Slf4j
public class DatasetsViewModel {

    @Getter
    @Setter
    private DatasetMessage datasetMsg;


    @Getter
    @Setter
    private List<Dataset> ingDatasets;


    @Getter
    @Setter
    private Dataset ingDataset;

    @Getter
    @Setter
    private Integer ingDatasetIndex;

    @Getter
    @Setter
    private UUID ingId;

    @Getter
    @Setter
    private String ingTitle;

    @Getter
    @Setter
    private String ingSubTitle;

    @Getter
    @Setter
    private String ingDatasetDesc;

    @Getter
    @Setter
    private Map.Entry<String, String> ingCategoryEntry;

    @Getter
    @Setter
    private String ingImageSrc;

    @Getter
    @Setter
    private String ingDataZipName;


    @Getter
    @Setter
    private List<DatasetFile> ingDatasetFiles;


    @Getter
    @Setter
    private Optional<DatasetFile> ingFile;


    @Getter
    @Setter
    private Integer ingDatasetFileIndex;

    @Getter
    @Setter
    private String ingDatasetFileDesc;


    @Getter
    @Setter
    private List<DatasetAPI> ingDatasetAPIs;


    @Getter
    @Setter
    private Optional<DatasetAPI> ingAPI;


    @Getter
    @Setter
    private Integer ingDatasetAPIIndex;

    @Getter
    @Setter
    private String ingApiName;

    @Getter
    @Setter
    private String ingUrl;

    @Getter
    @Setter
    private Boolean ingHasFreq;

    @Getter
    @Setter
    private Boolean ingNoFreq;

    @Getter
    @Setter
    private String ingFreqNum;


    @Getter
    @Setter
    private List<String> licenseList;


    @Getter
    @Setter
    private List<String> methodList;


    @Getter
    @Setter
    private List<String> frequencyList;


    @Getter
    @Setter
    private List<String> formatList;


    @Getter
    @Setter
    private List<String> queryList;


    @Getter
    @Setter
    private List<String> authList;


    @Getter
    @Setter
    private Map<String, String> categoryMap;


    @Getter
    @Setter
    private Integer ingFileLicenseIndex;


    @Getter
    @Setter
    private Integer ingAPILicenseIndex;


    @Getter
    @Setter
    private Integer ingMethodIndex;


    @Getter
    @Setter
    private Integer ingFreqIndex;


    @Getter
    @Setter
    private Integer ingFormatIndex;


    @Getter
    @Setter
    private Integer ingQueryIndex;


    @Getter
    @Setter
    private Integer ingAuthIndex;


    @Getter
    @Setter
    private State state;


    @Getter
    @Setter
    private State fileState;


    @Getter
    @Setter
    private State apiState;


    private DatasetService service;

    public DatasetsViewModel() {
        service = ApplicationContextProvider.getApplicationContext().getBean(DatasetService.class);
    }

    // Init all fields and items

    @Init
    public void init() {
        initItems();
        initFields();
        diselectLists();
        diselectItems();
        initStates();
        loadDatasets();
    }

    private void initItems() {
        datasetMsg = new DatasetMessage();

        licenseList = Arrays.asList(DatasetMessage.LICENSES);
        methodList = Arrays.asList(DatasetMessage.METHODS);
        frequencyList = Arrays.asList(DatasetMessage.FREQUENCIES);
        formatList = Arrays.asList(DatasetMessage.FORMATS);
        queryList = Arrays.asList(DatasetMessage.QUERIES);
        authList = Arrays.asList(DatasetMessage.AUTHORITIES);
        categoryMap = DatasetMessage.CATEGORIES;
    }

    private void diselectLists() {
        ingDatasetIndex = -1;
    }

    private void diselectDatasetFileAndAPIList() {
        ingDatasetFileIndex = -1;
        ingDatasetAPIIndex = -1;
    }

    private void diselectItems() {
        diselectDatasetItems();
        diselectAPIItems();
    }

    private void diselectDatasetItems() {
        ingFileLicenseIndex = -1;
        ingAPILicenseIndex = -1;
        ingCategoryEntry = DatasetMessage.CATEGORIES.entrySet().asList().get(0);
    }

    private void diselectAPIItems() {
        ingMethodIndex = -1;
        ingFreqIndex = -1;
        ingFormatIndex = -1;
        ingAuthIndex = -1;
        ingQueryIndex = -1;
        ingHasFreq = false;
        ingNoFreq = false;
    }

    private void initStates() {
        state = State.INIT;
        fileState = State.INIT;
        apiState = State.INIT;
    }

    private void initFields() {
        initDatasetFields();
        initFileFields();
        initAPIFields();
    }

    private void initDatasetFields() {
        ingDatasets = new LinkedList<>();

        ingId = UUID.randomUUID();
        ingTitle = "";
        ingSubTitle = "";
        ingDatasetDesc = "";
        ingImageSrc = "";
        ingDataZipName = "";

        Dataset newDataset = new Dataset();
        newDataset.setId(getIngId());
        ingDataset = newDataset;
    }

    private void initFileFields() {
        ingDatasetFiles = new LinkedList<>();
        ingFile = Optional.empty();
        ingDatasetFileDesc = "";
    }

    private void initAPIFields() {
        ingDatasetAPIs = new LinkedList<>();
        ingAPI = Optional.empty();
        ingApiName = "";
        ingUrl = "";
    }

    private void loadDatasets() {
        List<Dataset> recent = service.getAll();

        if (CollectionUtils.isNotEmpty(recent)) {
            this.ingDatasets.addAll(recent);
        }
    }

    // Dataset commands.

    @Command
    @NotifyChange({"state"})
    public void editing() {
        setState(State.EDITING);
    }

    @Command
    @NotifyChange({"*"})
    public void initDataset() {
        initFields();
        diselectLists();
        diselectItems();
        initStates();
    }

    @Command
    @NotifyChange({"*"})
    public void editDataset() {
        diselectItems();
        diselectDatasetFileAndAPIList();

        Dataset ingDataset = this.ingDatasets.get(getIngDatasetIndex());
        setIngDataset(ingDataset);
        setDatasetFields(ingDataset);
    }

    private void setDatasetFields(Dataset ingDataset) {
        setIngId(ingDataset.getId());
        setIngTitle(ingDataset.getTitle());
        setIngSubTitle(ingDataset.getSubTitle());
        setIngDatasetDesc(ingDataset.getDescription());
        setIngImageSrc(ingDataset.getImageSrc());
        setIngCategoryEntry(
                DatasetMessage.CATEGORIES.entrySet()
                        .stream()
                        .filter(entry ->
                                ingDataset.getCategory().equals(entry.getValue()))
                        .findAny()
                        .get());
        setIngFileLicenseIndex(this.licenseList.indexOf(ingDataset.getFileLicense()));
        setIngAPILicenseIndex(this.licenseList.indexOf(ingDataset.getApiLicense()));

        setIngDatasetFiles(service.getFilesByDataset(ingDataset));
        setIngDatasetAPIs(service.getAPIsByDataset(ingDataset));
    }

    @Command
    @NotifyChange({"ingDatasetFiles",
            "ingDatasetFileIndex", "ingDatasetFileDesc",
            "ingDataZipName", "state"})
    public void uploadDataZip(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
        UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
        Media media = event.getMedia();
        Dataset ingDataset = getIngDataset();

        try {
            byte[] content = media.getByteData();
            service.saveDataZip(content, ingDataset);

            Path dataZip = service.getDataZip(ingDataset);
            getIngDatasetFiles().clear();
            getIngDatasetFiles().addAll(service.parseDataZip(ingDataset, dataZip));

            setIngDatasetFileIndex(0);
            setIngDatasetFileDesc(getIngDatasetFiles().get(0).getDescription());

            setIngDataZipName("上傳完成");
            editing();
        } catch (DatasetFileException ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
            Messagebox.show("檔案上傳失敗: " + media, "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    // DatasetFile commands.

    @Command
    @NotifyChange({"fileState", "state"})
    public void fileEditing() {
        setFileState(State.EDITING);
        setState(State.EDITING);
    }

    @Command
    @NotifyChange({"fileState", "ingFile", "ingDatasetFileDesc"})
    public void editFileDesc(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
        setFileState(State.INIT);
        setIngFile(Optional.of(getIngDatasetFiles().get(ingDatasetFileIndex)));
        setIngDatasetFileDesc(getIngFile().get().getDescription());
    }

    @Command
    @NotifyChange({"fileState", "ingFile", "ingDatasetFiles"})
    public void saveFileDesc() {
        if (State.EDITING == fileState) {
            setFileState(State.EDITED);

            if (getIngFile().isPresent()) {
                getIngFile().get().setDescription(getIngDatasetFileDesc());
            }

            if (getIngDatasetFileIndex() != -1) {
                DatasetFile selected = getIngDatasetFiles().get(getIngDatasetFileIndex());
                selected.setDescription(getIngDatasetFileDesc());
            }
        }
    }

    @Command
    @NotifyChange({"fileState", "ingDatasetFileDesc"})
    public void restoreFileDesc() {
        if (getIngFile().isPresent()) {
            setIngDatasetFileDesc(getIngFile().get().getDescription());
        } else {
            setIngDatasetFileDesc("");
        }

        setFileState(State.INIT);
    }

    // DatasetAPI commands.

    @Command
    @NotifyChange({"apiState", "ingAPI",
            "ingApiName", "ingUrl", "ingHasFreq", "ingNoFreq", "ingFreqNum",
            "ingMethodIndex", "ingFormatIndex", "ingQueryIndex", "ingAuthIndex", "ingFrequencyIndex"})
    public void initAPI() {
        setApiState(State.INIT);
        setIngAPI(Optional.empty());

        setIngApiName("");
        setIngUrl("");
        setIngHasFreq(false);
        setIngNoFreq(false);
        setIngFreqNum("");

        setIngMethodIndex(0);
        setIngFormatIndex(0);
        setIngQueryIndex(0);
        setIngAuthIndex(0);
        setIngFreqIndex(0);
    }

    @Command
    @NotifyChange({"apiState", "ingAPI",
            "ingApiName", "ingUrl",
            "ingMethodIndex", "ingFormatIndex", "ingQueryIndex", "ingAuthIndex",
            "ingHasFreq", "ingNoFreq", "ingFreqNum", "ingFreqIndex"})
    public void editAPI() {
        setApiState(State.INIT);

        DatasetAPI ingAPI = this.ingDatasetAPIs.get(getIngDatasetAPIIndex());
        setIngAPI(Optional.of(ingAPI));

        setAPIFields(ingAPI);
    }

    private void setAPIFields(DatasetAPI ingAPI) {
        setIngApiName(ingAPI.getApiName());
        setIngUrl(ingAPI.getUrl());

        setIngMethodIndex(this.methodList.indexOf(ingAPI.getMethod()));
        setIngFormatIndex(this.formatList.indexOf(ingAPI.getFormat()));
        setIngQueryIndex(this.queryList.indexOf(ingAPI.getQueryMode()));
        setIngAuthIndex(this.authList.indexOf(ingAPI.getAuthentication()));

        if (DatasetAPI.FREQUENCY_UNCERTAIN.equals(StringUtils.trim(ingAPI.getFrequency()))) {
            setIngNoFreq(true);
            setIngHasFreq(false);
        } else {
            setIngHasFreq(true);
            setIngNoFreq(false);

            String[] freqs = DatasetAPI.parseFrequency(ingAPI.getFrequency());

            setIngFreqNum(freqs[0]);
            setIngFreqIndex(this.frequencyList.indexOf(freqs[1]));
        }
    }

    @Command
    @NotifyChange({"apiState", "state"})
    public void apiEditing() {
        setApiState(State.EDITING);
        setState(State.EDITING);
    }

    @Command
    @NotifyChange({"ingAPI", "ingDatasetAPIs", "apiState"})
    public void saveAPI() {
        if (getIngAPI().isPresent()) {
            saveToIngAPI();
        } else if (State.EDITING == apiState) {
            saveNewAPI();
        }

        setApiState(State.EDITED);
    }

    private void saveNewAPI() {
        DatasetAPI datasetAPI = buildNewAPI();
        setIngAPI(Optional.of(datasetAPI));
        getIngDatasetAPIs().add(datasetAPI);
    }

    private DatasetAPI buildNewAPI() {
        return DatasetAPI.builder()
                .id(UUID.randomUUID())
                .datasetId(getIngDataset().getId())
                .apiName(StringUtils.trimToEmpty(getIngApiName()))
                .method(this.methodList.get(getIngMethodIndex()))
                .url(StringUtils.trimToEmpty(getIngUrl()))
                .format(this.formatList.get(getIngFormatIndex()))
                .authentication(this.authList.get(getIngAuthIndex()))
                .queryMode(this.queryList.get(getIngQueryIndex()))
                .frequency(getFrequency())
                .build();
    }

    private void saveToIngAPI() {
        DatasetAPI ingAPI = getIngAPI().get();

        ingAPI.setApiName(StringUtils.trimToEmpty(getIngApiName()));
        ingAPI.setMethod(this.methodList.get(getIngMethodIndex()));
        ingAPI.setUrl(StringUtils.trimToEmpty(getIngUrl()));
        ingAPI.setFormat(this.formatList.get(getIngFormatIndex()));
        ingAPI.setAuthentication(this.authList.get(getIngAuthIndex()));
        ingAPI.setQueryMode(this.queryList.get(getIngQueryIndex()));
        ingAPI.setFrequency(getFrequency());
    }

    private String getFrequency() {
        return getIngNoFreq() ?
                DatasetAPI.FREQUENCY_UNCERTAIN :
                DatasetAPI.buildFrequency(StringUtils.trimToEmpty(getIngFreqNum()), this.frequencyList.get(getIngFreqIndex()));
    }

    @Command
    @NotifyChange({"ingApiName", "ingUrl",
            "ingMethodIndex", "ingFormatIndex", "ingQueryIndex", "ingAuthIndex",
            "ingHasFreq", "ingNoFreq", "ingFreqNum", "ingFreqIndex",
            "apiState"})
    public void restoreAPI() {
        setApiState(State.INIT);

        if (!getIngAPI().isPresent()) {
            initAPI();
        } else {
            DatasetAPI ingAPI = getIngAPI().get();
            setAPIFields(ingAPI);
        }
    }

    @Command
    @NotifyChange({"ingHasFreq", "ingNoFreq", "state", "apiState"})
    public void hasFreq() {
        setIngHasFreq(true);
        setIngNoFreq(false);
        apiEditing();
    }

    // Save all commands.

    @Command
    @NotifyChange({"ingDataset", "ingDatasetFiles", "ingDatasetAPIs",
            "datasetMsg", "state", "fileState", "apiState"})
    public void saveAll() {
        saveDataset();
        Dataset ingDataset = getIngDataset();
        log.info("save dataset: " + ingDataset);
        service.saveDataset(ingDataset);

        saveFileDesc();
        List<DatasetFile> ingDatasetFiles = getIngDatasetFiles();
        ingDatasetFiles.forEach(datasetFile -> log.info("save dataset file: " + datasetFile));
        service.saveFiles(ingDataset, ingDatasetFiles);

        saveAPI();
        List<DatasetAPI> ingDatasetAPIs = getIngDatasetAPIs();
        ingDatasetAPIs.forEach(datasetAPI -> log.info("save dataset api: " + datasetAPI));
        service.saveAPIs(ingDataset, ingDatasetAPIs);

        getDatasetMsg().setSaveMsg("儲存成功");

        allEdited();
    }

    private void allEdited() {
        setState(State.EDITED);
        setFileState(State.EDITED);
        setApiState(State.EDITED);
    }

    private void saveDataset() {
        Dataset ingDataset = getIngDataset();
        ingDataset.setTitle(StringUtils.trimToEmpty(getIngTitle()));
        ingDataset.setSubTitle(StringUtils.trimToEmpty(getIngSubTitle()));
        ingDataset.setDescription(StringUtils.trimToEmpty(getIngDatasetDesc()));
        ingDataset.setFileLicense(getLicenseList().get(getIngFileLicenseIndex()));
        ingDataset.setApiLicense(getLicenseList().get(getIngAPILicenseIndex()));
        ingDataset.setImageSrc(StringUtils.trimToEmpty(getIngImageSrc()));
        ingDataset.setCategory(getIngCategoryEntry().getValue());
    }

    public enum State {
        INIT,
        EDITING,
        EDITED
    }
}
