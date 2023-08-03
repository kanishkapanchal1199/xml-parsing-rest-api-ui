package com.tatva.task.xmlparsingrestapi.entities;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class XmlContentTest {

    private XmlContent xmlContent;

    @Before
    public void setUp() {
        String newspaperName = "Newspaper X";
        int width = 800;
        int height = 600;
        int dpi = 300;
        LocalDateTime uploadTime = LocalDateTime.now();
        String filename = "example.xml";

        xmlContent = new XmlContent(newspaperName, width, height, dpi, uploadTime, filename);
    }



    @Test
    public void testXmlContentGetterSetter() {
        String newspaperName = "Newspaper Y";
        int width = 1024;
        int height = 768;
        int dpi = 600;
        LocalDateTime uploadTime = LocalDateTime.of(2023, 7, 28, 12, 0);
        String filename = "sample.xml";


        xmlContent.setNewspaperName(newspaperName);
        xmlContent.setWidth(width);
        xmlContent.setHeight(height);
        xmlContent.setDpi(dpi);
        xmlContent.setUploadTime(uploadTime);
        xmlContent.setFilename(filename);

        assertEquals(newspaperName, xmlContent.getNewspaperName());
        assertEquals(width, xmlContent.getWidth());
        assertEquals(height, xmlContent.getHeight());
        assertEquals(dpi, xmlContent.getDpi());
        assertEquals(uploadTime, xmlContent.getUploadTime());
        assertEquals(filename, xmlContent.getFilename());
    }
}
