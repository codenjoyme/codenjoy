package net.tetris.online.service;

import net.tetris.services.PlayerService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:36 PM
 */
@Service
public class ExecutionRequestChecker implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(ExecutionRequestChecker.class);

    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> future;

    @Autowired
    private ServiceConfiguration configuration;
    @Autowired
    private GameExecutorService gameExecutorService;

    private SimpleDateFormat timestampFormat;

    public ExecutionRequestChecker(ServiceConfiguration configuration, GameExecutorService gameExecutorService) {
        this.configuration = configuration;
        this.gameExecutorService = gameExecutorService;
    }

    public ExecutionRequestChecker() {
    }

    @PostConstruct
    public void init() {
        executor = new ScheduledThreadPoolExecutor(1);
        future = executor.scheduleAtFixedRate(this, 1, 10, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        Collection<File> files = FileUtils.listFiles(configuration.getTetrisHomeDir(), new String[]{"war"}, false);
        if (files.isEmpty()) {
            return;
        }
        File warFile = files.iterator().next();
        String userName = warFile.getName().replace(".war", "").split("@")[0];
        try {
            gameExecutorService.runGame(userName, warFile);
        } catch (Exception e) {
            logger.error("Unable to execute game for user: " + userName + ", app:" + warFile.getAbsolutePath(), e);
        } finally {
            try {
                FileUtils.moveFile(warFile, new File(configuration.getArchiveDir(), warFile.getName()));
            } catch (IOException e) {
                logger.error("Unable to archive app file for user: " + userName + ", app:" + warFile.getAbsolutePath(), e);
            }
        }
    }

    @Value("${timestamp.format}")
    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = new SimpleDateFormat(timestampFormat);
    }
}
