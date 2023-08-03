package com.tatva.task.xmlparsingrestapi.service;

import com.tatva.task.xmlparsingrestapi.dto.XmlContentDTO;
import com.tatva.task.xmlparsingrestapi.entities.XmlContent;
import com.tatva.task.xmlparsingrestapi.exception.XmlProcessingException;
import com.tatva.task.xmlparsingrestapi.repository.XmlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

/*
*@service Implementation class for upload and getting xml-content with sorting and pagination and filtering.
*/
@Component
public class XmlServiceImpl implements XmlService{

    @Autowired
    private XmlRepository xmlRepository;


    /* Filtering ,Sorting, pagination functionality to get list of xml-content*/
    public Page<XmlContent> getXmlContentWithFilteringAndPagination(XmlContentDTO filterDTO, Pageable pageable) {


        Specification<XmlContent> spec = Specification.where(null);
        if (filterDTO != null) {

            if (filterDTO.getNewspaperName() != null && !filterDTO.getNewspaperName().isEmpty()) {
                spec = spec.and((root, query, builder) -> builder.like(builder.lower(root.get("newspaperName")), "%" + filterDTO.getNewspaperName().toLowerCase() + "%"));
            }
            if (filterDTO.getWidth() != null) {
                spec = spec.and((root, query, builder) -> builder.equal(root.get("width"), filterDTO.getWidth()));
            }
            if (filterDTO.getHeight() != null) {
                spec = spec.and((root, query, builder) -> builder.equal(root.get("height"), filterDTO.getHeight()));
            }
            if (filterDTO.getDpi() != null) {
                spec = spec.and((root, query, builder) -> builder.equal(root.get("dpi"), filterDTO.getDpi()));
            }
            if (filterDTO.getUploadTime() != null) {
                spec = spec.and((root, query, builder) -> builder.equal(root.get("uploadTime"), filterDTO.getUploadTime()));
            }
            if (filterDTO.getFilename() != null && !filterDTO.getFilename().isEmpty()) {
                spec = spec.and((root, query, builder) -> builder.like(builder.lower(root.get("filename")), "%" + filterDTO.getFilename().toLowerCase() + "%"));
            }
        }
        return xmlRepository.findAll(spec, pageable);
    }


    /*
    * Save the processed xml file into database
    */
    public void processXmlFile(final MultipartFile file) throws IOException, XmlProcessingException, SAXException, ParserConfigurationException {

            validateXmlAgainstXsd(file);
            final XmlContent xmlContent = parseXml(file);
            xmlRepository.save(xmlContent);

    }


    /*
    * validate the xml file against xsd
    */
    public void validateXmlAgainstXsd(final MultipartFile file) throws IOException, SAXException {

            final SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            final InputStream xsdStream = getClass().getClassLoader().getResourceAsStream("epaperRequest.xsd");
            final Schema schema = schemaFactory.newSchema(new StreamSource(xsdStream));
            final Validator validator = schema.newValidator();
            validator.validate(new StreamSource(file.getInputStream()));


    }

    /*
    *parse the uploaded xml file
    * */
    public XmlContent parseXml(final MultipartFile file) throws IOException, XmlProcessingException, SAXException, ParserConfigurationException {

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document doc = builder.parse(file.getInputStream());

            final Element deviceInfoElement = (Element) doc.getElementsByTagName("deviceInfo").item(0);
            final String newspaperName = getElementText(deviceInfoElement, "newspaperName");

            final Element screenInfoElement = (Element) deviceInfoElement.getElementsByTagName("screenInfo").item(0);
            final int width = Integer.parseInt(screenInfoElement.getAttribute("width"));
            final int height = Integer.parseInt(screenInfoElement.getAttribute("height"));
            final int dpi = Integer.parseInt(screenInfoElement.getAttribute("dpi"));

            final LocalDateTime uploadTime = LocalDateTime.now();
            final String filename = file.getOriginalFilename();

            return new XmlContent(newspaperName, width, height, dpi, uploadTime, filename);

    }


    /* get the element text from tag */
    public String getElementText(Element element, String tagName) throws XmlProcessingException {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        } else {
            throw new XmlProcessingException("Tag '" + tagName + "' not found in the XML.");
        }
    }




}


