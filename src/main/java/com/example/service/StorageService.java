package com.example.service;

import com.example.model.FileMessage;
import com.example.model.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

public interface StorageService {

    Result storeFile(String str,String filename, String id);

    Result<String> download(String id, HttpServletResponse response) throws Exception;

    Result<FileMessage> sendMessageToServer();

    Result recieveMessageFromServer(String id,String fileName,String afterPath,String description);

}
