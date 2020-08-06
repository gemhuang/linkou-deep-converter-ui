package org.iii.converter.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashUtilsTest {
    @Test
    public void testSha256() {
        String content = "";
        String expect = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

        Assertions.assertEquals(expect, HashUtils.sha256(content));
    }
}
