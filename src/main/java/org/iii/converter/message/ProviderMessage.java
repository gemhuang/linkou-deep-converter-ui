package org.iii.converter.message;

import lombok.Data;

@Data
public class ProviderMessage {
    private String codeMsg;
    private String nameMsg;
    private String logoMsg;
    private String saveMsg;
    private String descPlaceholder;

    public ProviderMessage() {
        this.descPlaceholder = "使用 markdown 格式進行編輯";
        this.codeMsg = "";
        this.nameMsg = "";
        this.logoMsg = "";
        this.saveMsg = "";
    }
}
