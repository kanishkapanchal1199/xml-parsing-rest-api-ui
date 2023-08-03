package com.tatva.task.xmlparsingrestapi.exception;


import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.xml.sax.SAXException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleXmlProcessingException() {
        XmlProcessingException ex = new XmlProcessingException("Error message");
        ResponseEntity<String> response = exceptionHandler.handleXmlParserConfigurationException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error parsing the XML file. Please check the XML format.", response.getBody());
    }

    @Test
    void handleMultipartException() {
        MultipartException ex = new MultipartException("Invalid request format");
        ResponseEntity<String> response = exceptionHandler.handleMultipartException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request format. Please upload a valid file using multipart/form-data.", response.getBody());
    }

    @Test
    void handleMissingServletRequestPartException() {
        MissingServletRequestPartException ex = new MissingServletRequestPartException("file");
        ResponseEntity<String> response = exceptionHandler.handleMissingServletRequestPartException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Required file is missing in the request. Please upload a file.", response.getBody());
    }

    @Test
    void handleIOException() {
        IOException ex = new IOException("Parsing and Validation Of XML file has failed");
        ResponseEntity<String> response = exceptionHandler.handleIOException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Parsing and Validation Of XML file has failed , Please Upload the Valid Xml File.", response.getBody());
    }

    @Test
    void handleSAXException() {
        SAXException ex = new SAXException("Uploaded file is not a valid XML file or is empty");
        ResponseEntity<String> response = exceptionHandler.handleSAXException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Uploaded file is not a valid XML file or is empty. Please upload a valid XML file.", response.getBody());
    }


    @Test
    void handleConstraintViolationException() {
        Set<ConstraintViolation<?>> violations = new LinkedHashSet<>(); // Use LinkedHashSet


        ConstraintViolation<String> violation1 = mock(ConstraintViolation.class);



        when(violation1.getMessageTemplate()).thenReturn("Invalid input");



        violations.add(violation1);


        ConstraintViolationException ex = new ConstraintViolationException("Validation error", violations);
        ResponseEntity<String> response = exceptionHandler.handleConstraintViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());


        List<String> errorMessages = new ArrayList<>();
        for (ConstraintViolation<?> violation : violations) {
            errorMessages.add(violation.getMessageTemplate());
        }
        Collections.sort(errorMessages);
        String expectedErrorMessage = "Validation error: " + String.join(", ", errorMessages);

        assertEquals(expectedErrorMessage, response.getBody());
    }

    @Test
    void handleDataAccessException() {
        DataAccessException ex = new DataAccessException("Data access error") {};
        ResponseEntity<String> response = exceptionHandler.handleDataAccessException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error accessing data. Please try again later.", response.getBody());
    }

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid input parameters");
        ResponseEntity<String> response = exceptionHandler.handleIllegalArgumentException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input parameters. Please provide valid values.", response.getBody());
    }

    @Test
    void handlePersistenceException() {
        javax.persistence.PersistenceException ex = new javax.persistence.PersistenceException("Error in persistence layer");
        ResponseEntity<String> response = exceptionHandler.handlePersistenceException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error in persistence layer. Please try again later.", response.getBody());
    }

    @Test
    void handleHttpMessageNotReadableException() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Invalid request body");
        ResponseEntity<Object> response = exceptionHandler.handleHttpMessageNotReadableException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request body: Required request body is missing.", response.getBody());
    }


}
