package net.tetris.web.controller;

import net.tetris.online.service.ServiceConfiguration;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class FileUploadServlet implements HttpRequestHandler {
    private static Logger logger = LoggerFactory.getLogger(FileUploadServlet.class);

    @Autowired
    private ServiceConfiguration configuration;

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, java.io.IOException {
        // Check that we have a file upload request
        boolean multipart = ServletFileUpload.isMultipartContent(request);
        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter();
        if (!multipart) {
            throw new UnsupportedOperationException("Not multipart request!");
        }
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

            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField()) {
                    File file = new File(configuration.getTetrisHomeDir(), request.getAttribute("logged.user")+".war");
                    fi.write(file);
                    logger.info("Uploaded Application {} ", file.getAbsolutePath());
                }
            }
            request.getRequestDispatcher("/view/uploaded.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}