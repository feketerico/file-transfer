package com.example.controller;

import com.example.model.Result;
import com.example.service.StorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FileController {


    private final StorageService storageService;


    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }


    /**
     * 功能描述: 客户端文件上传到本地
     * @param:
     * @return:
     * @auther: yangrui
     * @date: 2021/9/27 17:57
     */
    @PostMapping("/uploadFile")
    public Result uploadFile(@RequestParam("str")String str,
                             @RequestParam("fileName")String fileName,
                             @RequestParam("id")String id,
                             @RequestParam("description")String description){

        return storageService.storeFile(str, fileName, id, description);
    }

    /**
     * 功能描述: 客户端通用文件下载
     * @param:
     * @return:
     * @auther: yangrui
     * @date: 2021/9/27 17:56
     */
    @PostMapping("/downloadFile")
    public Result download(@RequestParam("id") String id) throws Exception {

        return storageService.download(id);

    }

    /**
     * 功能描述: 文件信息传送服务端
     * @param:
     * @return:
     * @auther: yangrui
     * @date: 2021/9/27 17:53
     */
    @PostMapping("/sendMessageToServer")
    public Result sendMessageToServer(){

        return storageService.sendMessageToServer();

    }

    /**
     * 功能描述: 接收服务端处理文件后的信息
     * @param: params
     * @return:
     * @auther: yangrui
     * @date: 2021/9/27 17:56
     */
    @PostMapping("/recieveMessageFromServer")
    public Result recieveMessageFromServer(@RequestParam("id") String id,
                                           @RequestParam("fileName") String fileName,
                                           @RequestParam("afterPath") String afterPath,
                                           @RequestParam("description") String description
                                           ){

        return storageService.recieveMessageFromServer(id,fileName,afterPath,description);

    }
}
