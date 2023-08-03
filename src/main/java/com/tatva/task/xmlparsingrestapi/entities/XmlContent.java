package com.tatva.task.xmlparsingrestapi.entities;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/*
 * @Entity Class to Populate and Map the data to database layer.
 */
@Entity
@Table(name="xml_content")
public class XmlContent {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Newspaper name must not be blank")
    private String newspaperName;

    @Min(value = 1, message = "Width must be greater than or equal to 1")
    private int width;

    @Min(value = 1, message = "Height must be greater than or equal to 1")
    private int height;

    @Min(value = 1, message = "DPI must be greater than or equal to 1")
    private int dpi;

    @NotNull(message = "Upload time must not be null")
    private LocalDateTime uploadTime;

    @NotBlank(message = "Filename must not be blank")
    private String filename;


    public XmlContent()
    {

    }

    //Argument Constructor to set the all values.
    public XmlContent(final String newspaperName, final int width, final int height, final int dpi, final LocalDateTime uploadTime, final String filename) {
        this.newspaperName = newspaperName;
        this.width = width;
        this.height = height;
        this.dpi = dpi;
        this.uploadTime = uploadTime;
        this.filename = filename;
    }

    //All Attributes Getters and Setters.
    public Long getId() {
        return id;
    }


    public void setId(final Long id) {
        this.id = id;
    }


    public String getNewspaperName() {
        return newspaperName;
    }


    public void setNewspaperName(final String newspaperName) {
        this.newspaperName = newspaperName;
    }


    public int getWidth() {
        return width;
    }


    public void setWidth(final int width) {
        this.width = width;
    }


    public int getHeight() {
        return height;
    }


    public void setHeight(final int height) {
        this.height = height;
    }


    public int getDpi() {
        return dpi;
    }


    public void setDpi(final int dpi) {
        this.dpi = dpi;
    }


    public LocalDateTime getUploadTime() {
        return uploadTime;
    }


    public void setUploadTime(final LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }


    public String getFilename() {
        return filename;
    }


    public void setFilename(final String filename) {
        this.filename = filename;
    }
}
