package com.tatva.task.xmlparsingrestapi.dto;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class XmlContentDTOTest {

    private XmlContentDTO xmlContentDTO;

    @Before
    public void setUp() {
        int page = 2;
        int size = 20;
        String sortBy = "newspaperName";
        String newspaperName = "Newspaper X";
        Integer width = 800;
        Integer height = 600;
        Integer dpi = 300;
        String filename = "example.xml";
        LocalDateTime uploadTime = LocalDateTime.now();

        xmlContentDTO = new XmlContentDTO(page, size, sortBy, newspaperName, width, height, dpi, filename, uploadTime);
    }

    @Test
    public void testXmlContentDTOGetterSetter() {
        int page = 1;
        int size = 15;
        String sortBy = "uploadTime";
        String newspaperName = "Newspaper Y";
        Integer width = 1024;
        Integer height = 768;
        Integer dpi = 600;
        String filename = "sample.xml";
        LocalDateTime uploadTime = LocalDateTime.of(2023, 7, 28, 12, 0);


        xmlContentDTO.setPage(page);
        xmlContentDTO.setSize(size);
        xmlContentDTO.setSortBy(sortBy);
        xmlContentDTO.setNewspaperName(newspaperName);
        xmlContentDTO.setWidth(width);
        xmlContentDTO.setHeight(height);
        xmlContentDTO.setDpi(dpi);
        xmlContentDTO.setFilename(filename);
        xmlContentDTO.setUploadTime(uploadTime);


        assertEquals(page, xmlContentDTO.getPage());
        assertEquals(size, xmlContentDTO.getSize());
        assertEquals(sortBy, xmlContentDTO.getSortBy());
        assertEquals(newspaperName, xmlContentDTO.getNewspaperName());
        assertEquals(width, xmlContentDTO.getWidth());
        assertEquals(height, xmlContentDTO.getHeight());
        assertEquals(dpi, xmlContentDTO.getDpi());
        assertEquals(filename, xmlContentDTO.getFilename());
        assertEquals(uploadTime, xmlContentDTO.getUploadTime());
    }
}
