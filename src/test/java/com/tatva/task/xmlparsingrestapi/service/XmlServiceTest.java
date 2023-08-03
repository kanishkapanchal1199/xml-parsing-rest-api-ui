package com.tatva.task.xmlparsingrestapi.service;

import com.tatva.task.xmlparsingrestapi.dto.XmlContentDTO;
import com.tatva.task.xmlparsingrestapi.entities.XmlContent;
import com.tatva.task.xmlparsingrestapi.exception.XmlProcessingException;
import com.tatva.task.xmlparsingrestapi.repository.XmlRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class XmlServiceTest {

    @Mock
    private XmlRepository xmlRepository;

    @InjectMocks
    private XmlServiceImpl xmlService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetXmlContentWithFilteringAndPagination_WithFilter() {
        // Test data
        String filterBy = "Newspaper";
        XmlContentDTO filterDTO = new XmlContentDTO();
        filterDTO.setNewspaperName(filterBy);
        List<XmlContent> xmlContents = new ArrayList<>();
        xmlContents.add(new XmlContent("Newspaper A", 800, 600, 300, LocalDateTime.now(), "file1.xml"));
        xmlContents.add(new XmlContent("Newspaper B", 1024, 768, 200, LocalDateTime.now(), "file2.xml"));
        Page<XmlContent> page = new PageImpl<>(xmlContents);

        // Mocking the repository
        Pageable pageable = PageRequest.of(0, 10);
        when(xmlRepository.findAll(ArgumentMatchers.<Specification<XmlContent>>any(), any(Pageable.class))).thenReturn(page);

        // Test the method
        Page<XmlContent> result = xmlService.getXmlContentWithFilteringAndPagination(filterDTO, pageable);

        // Verify the result
        assertEquals(xmlContents.size(), result.getContent().size());
        assertEquals(xmlContents.get(0).getNewspaperName(), result.getContent().get(0).getNewspaperName());
        assertEquals(xmlContents.get(1).getNewspaperName(), result.getContent().get(1).getNewspaperName());

        verify(xmlRepository, times(1)).findAll(ArgumentMatchers.<Specification<XmlContent>>any(), any(Pageable.class));
        verifyNoMoreInteractions(xmlRepository);
    }

    @Test
    public void testProcessXmlFile_ValidXmlFile() throws IOException, XmlProcessingException, SAXException, ParserConfigurationException {
        // Test data
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<epaperRequest>\n" +
                "    <deviceInfo name=\"someName\" id=\"someId\">\n" +
                "        <screenInfo width=\"800\" height=\"600\" dpi=\"300\" />\n" +
                "        <osInfo name=\"OS_Name\" version=\"1.0\" />\n" +
                "        <appInfo>\n" +
                "            <newspaperName>Newspaper X</newspaperName>\n" +
                "            <version>1.0</version>\n" +
                "        </appInfo>\n" +
                "    </deviceInfo>\n" +
                "    <getPages editionDefId=\"123\" publicationDate=\"2023-07-28\" />\n" +
                "</epaperRequest>";

        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "text/xml", xmlData.getBytes());


        XmlContent savedXmlContent = new XmlContent("Newspaper X", 800, 600, 300, LocalDateTime.now(), "test.xml");
        when(xmlRepository.save(any(XmlContent.class))).thenReturn(savedXmlContent);


        xmlService.processXmlFile(file);


        ArgumentCaptor<XmlContent> xmlContentCaptor = ArgumentCaptor.forClass(XmlContent.class);
        verify(xmlRepository, times(1)).save(xmlContentCaptor.capture());
        verifyNoMoreInteractions(xmlRepository);

        XmlContent capturedXmlContent = xmlContentCaptor.getValue();
        assertEquals("Newspaper X", capturedXmlContent.getNewspaperName());
        assertEquals(800, capturedXmlContent.getWidth());
        assertEquals(600, capturedXmlContent.getHeight());
        assertEquals(300, capturedXmlContent.getDpi());
        assertNotNull(capturedXmlContent.getUploadTime());
        assertEquals("test.xml", capturedXmlContent.getFilename());
    }



    @Test
    public void testValidateXmlAgainstXsd_ValidXmlFile() throws IOException, SAXException {

        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<epaperRequest>\n" +
                "    <deviceInfo name=\"someName\" id=\"someId\">\n" +
                "        <screenInfo width=\"800\" height=\"600\" dpi=\"300\" />\n" +
                "        <osInfo name=\"OS_Name\" version=\"1.0\" />\n" +
                "        <appInfo>\n" +
                "            <newspaperName>Newspaper X</newspaperName>\n" +
                "            <version>1.0</version>\n" +
                "        </appInfo>\n" +
                "    </deviceInfo>\n" +
                "    <getPages editionDefId=\"123\" publicationDate=\"2023-07-28\" />\n" +
                "</epaperRequest>";


        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "text/xml", xmlData.getBytes());
        xmlService.validateXmlAgainstXsd(file);
    }



    @Test
    public void testParseXml_ValidXmlFile() throws IOException, XmlProcessingException, SAXException, ParserConfigurationException {

        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<deviceInfo>\n" +
                "    <newspaperName>Newspaper X</newspaperName>\n" +
                "    <screenInfo width=\"800\" height=\"600\" dpi=\"300\"/>\n" +
                "</deviceInfo>";

        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "text/xml", xmlData.getBytes());


        XmlContent xmlContent = xmlService.parseXml(file);


        assertEquals("Newspaper X", xmlContent.getNewspaperName());
        assertEquals(800, xmlContent.getWidth());
        assertEquals(600, xmlContent.getHeight());
        assertEquals(300, xmlContent.getDpi());
        assertNotNull(xmlContent.getUploadTime());
        assertEquals("test.xml", xmlContent.getFilename());
    }


}
