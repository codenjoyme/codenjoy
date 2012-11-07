package net.tetris.online.web.controller;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.path.json.JsonPath.from;
import static junit.framework.Assert.assertEquals;
import static net.tetris.online.web.controller.TestUtils.getCell;

/**
 * User: serhiy.zelenin
 * Date: 11/7/12
 * Time: 11:15 AM
 */
public class JacksonSerializerTest {

    private JacksonSerializer jacksonSerializer;
    private StringWriter writer;

    @Before
    public void setUp() throws Exception {
        jacksonSerializer = new JacksonSerializer();
        jacksonSerializer.init();
        writer = new StringWriter();
    }

    @Test
    public void shouldSerializeGameLogs() throws IOException {
        jacksonSerializer.serialize(writer, new GameLogsData(Arrays.asList("file1", "file2")));

        JsonPath jsonPath = from(writer.toString());
        List<List> dataList = jsonPath.getList("aaData");
        assertEquals("file1", getCell(dataList, 0, 0));
        assertEquals("file2", getCell(dataList, 1, 0));
    }


}
