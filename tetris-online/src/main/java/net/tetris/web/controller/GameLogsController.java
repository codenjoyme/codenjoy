package net.tetris.web.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.tetris.online.service.SecurityFilter;
import net.tetris.online.service.ServiceConfiguration;
import net.tetris.services.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * User: serhiy.zelenin
 * Date: 10/29/12
 * Time: 8:33 PM
 */
@Controller
public class GameLogsController {
    private ServiceConfiguration configuration;
    private final ObjectMapper objectMapper;
    private SimpleDateFormat timestampFormat;

    @Autowired
    public GameLogsController(ServiceConfiguration configuration) {
        this.configuration = configuration;
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new StdSerializer<GameLogsData>(GameLogsData.class) {
            @Override
            public void serialize(GameLogsData gameLog, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeStartObject();
                jgen.writeArrayFieldStart("aaData");
                if (gameLog.getFileNames() != null && gameLog.getFileNames().length > 0) {
                    jgen.writeObject(gameLog.getFileNames());
                }
                jgen.writeEndArray();
                jgen.writeEndObject();
            }
        });
        objectMapper.registerModule(module);
    }

    @RequestMapping(value = "/logs")
    @ResponseBody
    public String gameLogs(HttpServletRequest request) {
        try {
            StringWriter writer = new StringWriter();
            String loggedUser = (String) request.getAttribute(SecurityFilter.LOGGED_USER);
            String[] logFiles = null;
            if (loggedUser != null) {
                File userLogDir = new File(configuration.getLogsDir(), loggedUser);
                logFiles = userLogDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        try {
                            timestampFormat.parse(name);
                            return true;
                        } catch (ParseException e) {
                            return false;
                        }
                    }
                });
            }
            GameLogsData data = new GameLogsData(logFiles);
            objectMapper.writeValue(writer, data);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Value("${timestamp.format}")
    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = new SimpleDateFormat(timestampFormat);
    }
}
