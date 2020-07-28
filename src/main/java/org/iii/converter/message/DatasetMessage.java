package org.iii.converter.message;

import com.google.common.collect.ImmutableBiMap;
import lombok.Data;

@Data
public class DatasetMessage {
    public static final String[] LICENSES = new String[]{"CC BY-SA 4.0", "MIT", "BSD", "Apache-2.0", "GPL", "商用授權"};
    public static final String[] METHODS = new String[]{"GET", "POST", "PUT", "DELETE", "其他"};
    public static final String[] FREQUENCIES = new String[]{"秒", "分", "時", "日"};
    public static final String[] FORMATS = new String[]{"JSON", "XML", "PLAIN"};
    public static final String[] QUERIES = new String[]{"ReST", "SOAP", "WebSocket", "HTTP", "TCP/UDP"};
    public static final String[] AUTHORITIES = new String[]{"有", "無"};
    // public static final String[] CATEGORIES = new String[]{"BusinessPromotion", "CommunityHealth", "UrbanGovernance", "Others"};
    public static final ImmutableBiMap<String, String> CATEGORIES =
            ImmutableBiMap.<String, String>builder()
                    .put("商業促進", "BusinessPromotion")
                    .put("社區健康", "CommunityHealth")
                    .put("城市治理", "UrbanGovernance")
                    .put("其他", "Others")
                    .build();

    private String titleMsg;
    private String imageSrcMsg;
    private String dataZipMsg;
    private String saveMsg;
    private String apiNameMsg;
    private String alertMsg;

    private String descPlaceholder;
    private String imgPlaceholder;

    public DatasetMessage() {
        this.descPlaceholder = "使用 markdown 格式進行編輯";
        this.imgPlaceholder = "使用 unsplash 圖庫";
        this.titleMsg = "";
        this.imageSrcMsg = "";
        this.dataZipMsg = "";
        this.saveMsg = "";
        this.apiNameMsg = "";
        this.alertMsg = "";
    }
}

