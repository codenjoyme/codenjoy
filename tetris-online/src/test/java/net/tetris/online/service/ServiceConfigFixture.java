package net.tetris.online.service;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.when;

/**
 * User: serhiy.zelenin
 * Date: 10/23/12
 * Time: 10:30 PM
 */
public class ServiceConfigFixture {

    private File homeFolder;
    private File archiveFolder;

    private File setupHomeFolder() {
        homeFolder = new File(FileUtils.getTempDirectory(), "test");
        homeFolder.mkdirs();
        return homeFolder;
    }

    private File setupArchiveFolder() {
        archiveFolder = new File(homeFolder, "archive");
        archiveFolder.mkdirs();
        return archiveFolder;
    }

    public void setupFolders() {
        setupHomeFolder();
        setupArchiveFolder();
    }

    public void setupConfiguration(ServiceConfiguration configuration) {
        setupFolders();
        when(configuration.getTetrisHomeDir()).thenReturn(setupHomeFolder());
        when(configuration.getArchiveDir()).thenReturn(setupArchiveFolder());
    }

    public File getHomeFolder() {
        return homeFolder;
    }

    public File getArchiveFolder() {
        return archiveFolder;
    }

    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(homeFolder);
    }
}
