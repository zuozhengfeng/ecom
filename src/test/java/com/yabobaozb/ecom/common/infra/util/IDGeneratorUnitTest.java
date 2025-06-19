package com.yabobaozb.ecom.common.infra.util;

import org.junit.jupiter.api.Test;

public class IDGeneratorUnitTest {


    @Test
    public void testGenerateId() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            Thread.sleep(1L);
            System.out.println(IDGenerator.generateId());
        }
    }
}
