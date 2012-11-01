package net.tetris.online.web.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.tetris.online.service.SecurityFilter;
import net.tetris.online.service.ServiceConfiguration;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
                List<String> fileNames = gameLog.getFileNames();
                if (fileNames != null && !fileNames.isEmpty()) {
                    for (String fileName : fileNames) {
                        jgen.writeStartArray();
                        jgen.writeObject(fileName);
                        jgen.writeObject(fileName);
                        jgen.writeEndArray();
                    }
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
            List<String> logFiles = null;
            if (loggedUser != null) {
                File userLogDir = new File(configuration.getLogsDir(), loggedUser);
                String[] fileNames = userLogDir.list(new TimestampFilter());
                if (fileNames != null) {
                    logFiles = Arrays.asList(fileNames);
                }
            }
            if (logFiles != null) {
                Collections.sort(logFiles, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o2.compareTo(o1);
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

    private class TimestampFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            try {
                timestampFormat.parse(name);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
    }
}
