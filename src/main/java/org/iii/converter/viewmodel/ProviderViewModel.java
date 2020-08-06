package org.iii.converter.viewmodel;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.iii.converter.config.ApplicationContextProvider;
import org.iii.converter.datamodel.Provider;
import org.iii.converter.exception.FieldNotInputException;
import org.iii.converter.message.ProviderMessage;
import org.iii.converter.service.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Messagebox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class ProviderViewModel {
    private static final Logger logger = LoggerFactory.getLogger(ProviderViewModel.class);

    private ProviderService providerService;

    @Getter
    @Setter
    private Provider provider;

    @Getter
    @Setter
    private Image providerCI;

    @Getter
    @Setter
    private ProviderMessage providerMsg;

    private boolean updateCI;

    private boolean updateProvider;

    @Init
    public void init() throws IOException {
        providerService = ApplicationContextProvider.getApplicationContext().getBean(ProviderService.class);
        updateCI = false;
        providerMsg = new ProviderMessage();

        Optional<Provider> top1 = providerService.getTop1();
        if (top1.isPresent()) {
            provider = top1.get();
            loadCI();
        } else {
            provider = new Provider();
        }
    }

    private void loadCI() throws IOException {
        String fileName = provider.getLogo();
        providerCI = new AImage(System.getProperty("user.dir") + "/workspace/images/brands/" + fileName);
    }


    @Command
    @NotifyChange({"providerCI"})
    public void uploadCI(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
        UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
        Media media = event.getMedia();

        if (media instanceof Image) {
            setProviderCI((Image) media);
            updateCI = true;
        } else {
            Messagebox.show("不是圖片檔: " + media, "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"providerMsg"})
    public void saveProvider() {
        try {
            checkProviderFields();
            saveImage();
            Provider saving = provider.buildNewOne();
            logger.info("save provider: " + saving);
            providerService.save(saving);
            getProviderMsg().setSaveMsg("更新成功");
        } catch (FieldNotInputException ex) {
            getProviderMsg().setSaveMsg(ex.getMessage());
        } catch (IOException ex) {
            getProviderMsg().setSaveMsg("Logo 檔案寫入失敗");
            logger.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    private void checkProviderFields() throws FieldNotInputException {
        boolean failed = true;

        if (StringUtils.isEmpty(provider.getName())) {
            getProviderMsg().setNameMsg("品牌名稱必填");
        } else if (StringUtils.isEmpty(provider.getCode())) {
            getProviderMsg().setCodeMsg("品牌代碼必填");
        } else if (providerCI == null) {
            getProviderMsg().setLogoMsg("品牌識別 Logo 必須提供");
        } else {
            failed = false;
        }

        if (failed) {
            throw new FieldNotInputException("請檢查必輸入之欄位");
        }
    }

    private void saveImage() throws IOException {
        if (updateCI) {
            String[] names = providerCI.getName().split("\\.");
            String code = provider.getCode();
            String extension = names[names.length - 1];
            String fileName = code + "." + extension;

            providerService.saveImage(providerCI.getByteData(), fileName);

            provider.setLogo(fileName);
        }
    }
}
