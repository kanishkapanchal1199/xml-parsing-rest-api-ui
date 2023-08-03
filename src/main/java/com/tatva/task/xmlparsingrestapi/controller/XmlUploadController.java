package com.tatva.task.xmlparsingrestapi.controller;

import com.tatva.task.xmlparsingrestapi.dto.PaginationRequestDTO;
import com.tatva.task.xmlparsingrestapi.dto.XmlContentDTO;
import com.tatva.task.xmlparsingrestapi.entities.XmlContent;
import com.tatva.task.xmlparsingrestapi.service.XmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/* @Controller Class to for Request Mapping
 * which is containing get and post mapping.
 */
@RestController
@RequestMapping("/xml")
public class XmlUploadController {
    @Autowired
    private XmlService xmlService;


    /*
    * This Method is For uploading the xml file through xml/upload endpoints.
    */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadXmlFile(final @RequestParam("file") MultipartFile file) throws IOException, ParserConfigurationException, SAXException {
            xmlService.processXmlFile(file);
            return ResponseEntity.ok("XML file uploaded and processed successfully.");

    }


    /*
    *This Method is for getting the data by pagination, filtering and sorting.
    */
    @PostMapping("/xml-content")
    public ResponseEntity<Page<XmlContent>> getXmlContent(@RequestBody XmlContentDTO filterDTO) {

        int page = (filterDTO.getPage() > 0) ? filterDTO.getPage() : 0;
        int size = (filterDTO.getSize() > 0) ? filterDTO.getSize() : 10;
        String sortBy = (filterDTO.getSortBy() != null && !filterDTO.getSortBy().isEmpty()) ? filterDTO.getSortBy():"id";
        Pageable pageable = PageRequest.of(page, size, PaginationRequestDTO.createSortFromSortBy(sortBy));
        Page<XmlContent> xmlContentPage = xmlService.getXmlContentWithFilteringAndPagination(filterDTO, pageable);
        return ResponseEntity.ok(xmlContentPage);
    }

}
