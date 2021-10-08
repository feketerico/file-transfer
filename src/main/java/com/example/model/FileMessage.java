package com.example.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangrui
 * @date 2021年09月26日 18:39
 */
@Data
public class FileMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String fileName;

    private String filePath;

    private String afterPath;

    //
    private String tag;

    private String description;


}
