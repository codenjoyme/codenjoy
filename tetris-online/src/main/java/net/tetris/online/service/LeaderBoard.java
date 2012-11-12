package net.tetris.online.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: serhiy.zelenin
 * Date: 10/26/12
 * Time: 2:08 PM
 */
@Service
public class LeaderBoard {
    private List<Score> scores = new ArrayList<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static Logger logger = LoggerFactory.getLogger(LeaderBoard.class);

    @Autowired
    private ServiceConfiguration serviceConfiguration;

    public LeaderBoard() {
    }

    public LeaderBoard(ServiceConfiguration serviceConfiguration) {
        this.serviceConfiguration = serviceConfiguration;
    }

    public void addScore(String playerName, int score, String levelsClass, String timestamp, int level) {
        logger.debug("Adding score {} for player {}", score, playerName);
        lock.writeLock().lock();
        try {
            int maxScore = score;
            Iterator iterator = scores.iterator();
            while (iterator.hasNext()) {
                Score next = (Score) iterator.next();
                if (next.getPlayerName().equals(playerName)) {
                    if (next.getScore() <= score) {
                        iterator.remove();
                    } else {
                        maxScore = next.getScore();
                    }
                }
            }
            if (maxScore <= score) {
                scores.add(new Score(playerName, score, levelsClass, timestamp, level));
                logger.debug("Adding best score {} for player {}", score, playerName);
            }
            Collections.sort(scores);
            store();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void store() {
        List<String> lines = new ArrayList<>(scores.size());
        for (Score score : scores) {
            lines.add(score.getPlayerName() + "|" + score.getScore() +
                    "|" + score.getLevelsClass() + "|" + score.getTimestamp() + "|" + score.getLevel());
        }
        File scoresFile = getScoresFile();
        try {
            FileUtils.writeLines(scoresFile, "UTF-8", lines, false);
        } catch (IOException e) {
            logger.error("Unable to store leaderboard!", e);
        }
    }

    public List<Score> getScores() {
        return scores;
    }

    @PostConstruct
    public void init() throws IOException {
        File scoresFile = getScoresFile();
        if (!scoresFile.exists()) {
            return;
        }
        List<String> lines = FileUtils.readLines(scoresFile, "UTF-8");
        for (String line : lines) {
            String[] columns = line.split("\\|");
            scores.add(new Score(columns[0], Integer.parseInt(columns[1]), columns[2], columns[3], Integer.parseInt(columns[4])));
        }
    }

    private File getScoresFile() {
        return new File(serviceConfiguration.getTetrisHomeDir(), "scores.txt");
    }
}
