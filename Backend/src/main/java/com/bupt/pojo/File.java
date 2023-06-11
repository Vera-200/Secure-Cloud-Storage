package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private int fileId;
    private String fileName;
    private String fileType;
    private double fileSize;
    private Date fileTime;
    private int fileOwner;
    private String fileHash;
    private String filePath;
    private int fileGroup;
}
