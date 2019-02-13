package com.company.demo.core;

import com.company.demo.DemoTestContainer;
import com.company.demo.config.DigiKeyConfig;
import com.company.demo.service.DigiKeyService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DigiKeyRestTest {

    @ClassRule
    public static DemoTestContainer cont = DemoTestContainer.Common.INSTANCE;

    private DigiKeyService digiKeyService;
    private DigiKeyConfig digiKeyConfig;

    @Before
    public void setUp() throws Exception {
        digiKeyService = AppBeans.get(DigiKeyService.class);
        digiKeyConfig = AppBeans.get(Configuration.class).getConfig(DigiKeyConfig.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getGetOAuthToken() {
        digiKeyConfig.setClientId("---");
        digiKeyConfig.setClientSecret("----");

        String result = digiKeyService.obtainAccessTokenByCode("----");
        assertEquals("Success", result);
    }
}