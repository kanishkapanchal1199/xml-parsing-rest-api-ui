package com.tatva.task.xmlparsingrestapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tatva.task.xmlparsingrestapi.dto.XmlContentDTO;
import com.tatva.task.xmlparsingrestapi.entities.XmlContent;
import com.tatva.task.xmlparsingrestapi.service.XmlService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class XmlUploadControllerTest {

    @Mock
    private XmlService xmlService;

    @InjectMocks
    private XmlUploadController xmlUploadController;

    @Test
    public void uploadXmlFile_Success() throws IOException, ParserConfigurationException, SAXException {

        MultipartFile file = mock(MultipartFile.class);


        doNothing().when(xmlService).processXmlFile(any(MultipartFile.class));


        ResponseEntity<String> response = xmlUploadController.uploadXmlFile(file);


        verify(xmlService, times(1)).processXmlFile(any(MultipartFile.class));
        assertSame(HttpStatus.OK, response.getStatusCode());
        assertEquals("XML file uploaded and processed successfully.", response.getBody());
    }

    @Test
    public void getXmlContent_Success() {
        List<XmlContent> xmlContents = new ArrayList<>();
        xmlContents.add(new XmlContent("Newspaper A", 800, 600, 300, LocalDateTime.now(), "file1.xml"));
        xmlContents.add(new XmlContent("Newspaper B", 1024, 768, 200, LocalDateTime.now(), "file2.xml"));
        Page<XmlContent> xmlContentPage = new PageImpl<>(xmlContents);


        XmlContentDTO filterDTO = new XmlContentDTO();
        filterDTO.setPage(0);
        filterDTO.setSize(10);
        filterDTO.setSortBy("id");


        when(xmlService.getXmlContentWithFilteringAndPagination(any(XmlContentDTO.class), any(Pageable.class)))
                .thenReturn(xmlContentPage);

        ResponseEntity<Page<XmlContent>> response = xmlUploadController.getXmlContent(filterDTO);


        verify(xmlService, times(1)).getXmlContentWithFilteringAndPagination(any(XmlContentDTO.class), any(Pageable.class));
        assertSame(HttpStatus.OK, response.getStatusCode());
        assertSame(xmlContentPage, response.getBody());
    }
}
