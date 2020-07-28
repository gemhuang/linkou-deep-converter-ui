package org.iii.converter.viewmodel;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.iii.converter.config.ApplicationContextProvider;
import org.iii.converter.exception.DataExportException;
import org.iii.converter.mapper.DBTimeMapper;
import org.iii.converter.service.DataExportService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import java.io.FileNotFoundException;
import java.nio.file.Path;

@Slf4j
public class IndexViewModel {

    private DBTimeMapper dbTimeMapper;

    @Getter
    @Setter
    private String exportMsg;


    private DataExportService exportService;

    public IndexViewModel() {
        dbTimeMapper = ApplicationContextProvider.getApplicationContext().getBean(DBTimeMapper.class);
        exportService = ApplicationContextProvider.getApplicationContext().getBean(DataExportService.class);
    }

    public String getDbTime() {
        return dbTimeMapper.getDBTime().getDbTime().toString();
    }

    @Command
    @NotifyChange({"exportMsg"})
    public void export() {
        try {
            Path workspace = exportService.exportWorkspace();
            log.info("exporting file: " + workspace.toAbsolutePath().toString());
            Filedownload.save(workspace.toFile(), "application/zip");
            setExportMsg("匯出完成");
        } catch (DataExportException | FileNotFoundException ex) {
            Messagebox.show("檔案匯出失敗: " + ex.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
}
