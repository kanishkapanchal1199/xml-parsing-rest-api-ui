package com.tatva.task.xmlparsingrestapi.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.xml.sax.SAXException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
Global exception handler class for handling the exception occurring all over the project .
*
*/
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(XmlProcessingException.class)
    public ResponseEntity<String> handleXmlParserConfigurationException(XmlProcessingException ex) {
        // Handle the XmlParserConfigurationException and return an appropriate response or error message.
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error parsing the XML file. Please check the XML format.");
    }

    @ExceptionHandler(ParserConfigurationException.class)
    public ResponseEntity<String> handleParserConfigurationException(ParserConfigurationException ex) {
        // Handle the ParserConfigurationException and return an appropriate response or error message.
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error parsing the XML file. Please check the XML format.");
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> handleMultipartException(MultipartException ex) {
        // Handle the MultipartException and return an appropriate response or error message.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request format. Please upload a valid file using multipart/form-data.");
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<String> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        // Handle the MissingServletRequestPartException and return an appropriate response or error message.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Required file is missing in the request. Please upload a file.");
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        // Handle the IOException and return an appropriate response or error message.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parsing and Validation Of XML file has failed , Please Upload the Valid Xml File.");
    }

    @ExceptionHandler(SAXException.class)
    public ResponseEntity<String> handleSAXException(SAXException ex) {
        // Handle the SAXException and return an appropriate response or error message.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uploaded file is not a valid XML file or is empty. Please upload a valid XML file.");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errorMessages = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorMessages.add(violation.getMessageTemplate());
        }

        String errorMessage = "Validation error: " + String.join(", ", errorMessages);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
        // Handle the DataAccessException and return an appropriate response or error message.
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error accessing data. Please try again later.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Handle the IllegalArgumentException and return an appropriate response or error message.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input parameters. Please provide valid values.");
    }

    @ExceptionHandler(javax.persistence.PersistenceException.class)
    public ResponseEntity<String> handlePersistenceException(javax.persistence.PersistenceException ex) {
        // Handle the PersistenceException and return an appropriate response or error message.
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in persistence layer. Please try again later.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>("Invalid request body: Required request body is missing.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        String errorMessage = "Unsupported Media Type: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorMessage);
    }

}
