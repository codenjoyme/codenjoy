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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FileUploadServlet implements HttpRequestHandler {
    private static Logger logger = LoggerFactory.getLogger(FileUploadServlet.class);
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceConfiguration configuration;
    private SimpleDateFormat timestampFormat;

    public FileUploadServlet() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule());

    }

    public void doUploadFile(HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, java.io.IOException {
        // Check that we have a file upload request
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // maximum size that will be stored in memory
        factory.setSizeThreshold(4 * 1024);
        // Location to save data that is larger than maxMemSize.
        File tmpDir = new File(configuration.getTetrisHomeDir(), "tmp");
        tmpDir.mkdirs();
        factory.setRepository(tmpDir);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum file size to be uploaded.
        upload.setSizeMax(50 * 1024 * 1024);

        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            if (i.hasNext()) {
                String timestamp = null;
                String userName = (String) request.getAttribute(SecurityFilter.LOGGED_USER);
                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField()) {
                    timestamp = timestampFormat.format(new Date());
                    File file = new File(configuration.getTetrisHomeDir(), userName + "@" + timestamp + ".war");
                    fi.write(file);
                    request.setAttribute("warFileName", file.getName());
                    logger.info("Uploaded Application {} ", file.getAbsolutePath());
                }
                objectMapper.writeValue(response.getOutputStream(), Collections.singletonMap("file", new FileData(timestamp, fi.getName())));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean multipart = ServletFileUpload.isMultipartContent(request);
        if (!multipart) {
            throw new ServletException("Only POST with multipart content is allowed for this path!");
        } else {
            doUploadFile(request, response);
        }
    }

    @Value("${timestamp.format}")
    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = new SimpleDateFormat(timestampFormat);
    }
}