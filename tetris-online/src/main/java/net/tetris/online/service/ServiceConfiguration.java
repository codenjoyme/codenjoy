package net.tetris.online.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

@Service("configuration")
public class ServiceConfiguration {

    private File homeDir;
    private File archiveDir;
    private File tmpDir;
    private File logsDir;

    @PostConstruct
    public void init() {
        homeDir = new File(System.getProperty("user.home"), ".tetris");
        homeDir.mkdir();

        archiveDir = new File(getTetrisHomeDir(), "archive");
        archiveDir.mkdir();

        tmpDir = new File(getTetrisHomeDir(), "tmp");
        tmpDir.mkdir();

        logsDir = new File(getTetrisHomeDir(), "logs");
        logsDir.mkdir();
    }

    public File getTetrisHomeDir(){
        return homeDir;
    }

    public File getArchiveDir() {
        return archiveDir;
    }

    public File getTmpDir() {
        return tmpDir;
    }

    public File getLogsDir() {
        return logsDir;
    }
}
