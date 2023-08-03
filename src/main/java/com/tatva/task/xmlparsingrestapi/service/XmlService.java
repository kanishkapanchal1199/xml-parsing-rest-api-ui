package com.tatva.task.xmlparsingrestapi.service;

import com.tatva.task.xmlparsingrestapi.dto.XmlContentDTO;
import com.tatva.task.xmlparsingrestapi.entities.XmlContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


/* @Service interface layer*/
@Service
public interface XmlService {

    public Page<XmlContent> getXmlContentWithFilteringAndPagination(XmlContentDTO filterDTO, Pageable pageable);

    public void processXmlFile(final MultipartFile file) throws IOException, SAXException, ParserConfigurationException;

    public XmlContent parseXml(final MultipartFile file) throws IOException, ParserConfigurationException, SAXException;

    public void validateXmlAgainstXsd(final MultipartFile file) throws IOException, SAXException;

}

