package com.ruijc;

import org.junit.Assert;
import org.junit.Test;

/**
 * 测试用例
 * @author Storezhang
 */
public class IdObjectTest {

    @Test
    public void testToString() {
        IdObject one = new IdObject();
        one.setId(1);
        Assert.assertEquals("{\"id\":1,\"key\":\"IdObject-1\"}", one.toString());
    }

    @Test
    public void testEqual() {
        IdObject one = new IdObject();
        one.setId(1);

        IdObject two = new IdObject();
        two.setId(1);

        Assert.assertEquals(one, two);
    }
}
