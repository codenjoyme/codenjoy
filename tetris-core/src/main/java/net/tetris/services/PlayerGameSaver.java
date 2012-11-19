package net.tetris.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tetris.services.Player;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 11/15/12
 * Time: 2:08 AM
 */
@Component("playerGameSaver")
public class PlayerGameSaver implements GameSaver {

    private ObjectMapper mapper = new ObjectMapper();
    private static final String EXT = ".game";

    @Override
    public void saveGame(Player player) {
        try {
            mapper.writeValue(new FileWriter(getFileName(player.getName())), player.new PlayerReader());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(String name) {
        return name + EXT;
    }

    @Override
    public Player.PlayerBuilder loadGame(String userName) {
        try {
            Player.PlayerBuilder playerBuilder = mapper.readValue(new FileReader(getFileName(userName)), Player.PlayerBuilder.class);
            playerBuilder.getPlayer();
            return playerBuilder;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getSavedList() {
        FilenameFilter filter = new GameFile();
        File dir = new File(".");
        String[] files = dir.list(filter);

        List<String> result = new LinkedList<>();
        for (String file : files) {
            result.add(file.replaceAll(EXT, ""));
        }
        return result;
    }

    class GameFile implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(EXT);
        }
    }

    @Override
    public void delete(String name) {
        FilenameFilter filter = new GameFile();
        File dir = new File(".");
        String[] files = dir.list(filter);

        for (String file : files) {
            if (name == null || file.equals(getFileName(name))) {
                new File(file).delete();
            }
        }
    }
}
