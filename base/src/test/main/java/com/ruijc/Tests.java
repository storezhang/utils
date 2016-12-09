package com.ruijc;

import org.junit.Assert;
import org.junit.Test;

/**
 * 测试用例
 * @author Storezhang
 */
public class Tests {

    @Test
    public void testToString() {
        IdObject id = new IdObject();
        id.setId(1);
        Assert.assertEquals("{\"id\":1,\"key\":\"IdObject-1\"}", id.toString());
    }
}
