package net.tetris.online.service;

import org.springframework.stereotype.Service;

import java.io.File;

@Service("configuration")
public class ServiceConfiguration {

    public File getTetrisHomeDir(){
        File file = new File(System.getProperty("java.io.tmpdir"), ".tetris");
        file.mkdir();
        return file;
    }

    public File getArchiveDir() {
        File archive = new File(getTetrisHomeDir(), "archive");
        archive.mkdir();
        return archive;
    }

    public File getTmpDir() {
        File archive = new File(getTetrisHomeDir(), "tmp");
        archive.mkdir();
        return archive;
    }
}
