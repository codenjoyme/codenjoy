package net.tetris.online.web.controller;

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
    private SimpleDateFormat timestampFormat;
    private JsonSerializer serializer;

    @Autowired
    public GameLogsController(ServiceConfiguration configuration, JsonSerializer serializer) {
        this.configuration = configuration;
        this.serializer = serializer;
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
            serializer.serialize(writer, new GameLogData(logFiles));
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
