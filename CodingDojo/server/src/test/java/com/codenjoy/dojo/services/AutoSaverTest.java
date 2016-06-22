package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.mocks.MockSaveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 21:14
 */
@ContextConfiguration(classes = {
        AutoSaver.class,
        MockSaveService.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class AutoSaverTest {

    @Autowired private AutoSaver autoSaver;
    @Autowired private SaveService saveService;

    @Test
    public void testSaveEachNTicks() throws InterruptedException {
        verifyNoMoreInteractions(saveService);

        autoSaver.tick();

        verify(saveService, only()).loadAll();
        reset(saveService);

        for (int count = 0; count < AutoSaver.TICKS - 2; count++) {
            autoSaver.tick();
        }

        verifyNoMoreInteractions(saveService);

        autoSaver.tick();

        Thread.sleep(1000);

        verify(saveService, only()).saveAll();
    }

}
