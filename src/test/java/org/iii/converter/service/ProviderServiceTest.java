package org.iii.converter.service;

import org.iii.converter.datamodel.Provider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

@SpringBootTest
public class ProviderServiceTest {

    @Autowired
    private ProviderService service;

//    @Test
    public void testBuildProvider() throws IOException, URISyntaxException {
        Assertions.assertFalse(service.getTop1().isPresent());
        Provider provider = buildProvider();
        service.save(provider);
        Provider top1 = service.getTop1().get();
        Assertions.assertEquals(provider, top1);

        top1.setOfficialEmail("gemhuang@iii.org.tw");
        service.save(top1);

//         service.delete();
    }

    private Provider buildProvider() throws IOException, URISyntaxException {
        return Provider.builder()
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
    }

}
