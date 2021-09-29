package com.example.service;

import com.example.model.FileMessage;
import com.example.model.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface StorageService {

    Result storeFile(MultipartFile file, String id);

    Result download(String id, HttpServletResponse response) throws Exception;

    Result<FileMessage> sendMessageToServer();

    Result recieveMessageFromServer(String id,String fileName,String afterPath,String description);

}
