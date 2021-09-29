package com.example.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.config.FileStorageProperties;
import com.example.exception.FileStorageException;
import com.example.model.FileMessage;
import com.example.model.Result;
import com.example.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path fileStorageLocation;


    /**
     * 文件排序表获取id
     */
    private String fileId = "fileId";


    @Autowired
    private RedisTemplate redisTemplate;



    @Autowired
    public StorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create directory where the uploaded files will be stored");
        }
    }

    @Override
    public Result storeFile(MultipartFile file, String id ) {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        FileMessage fileMessage = new FileMessage();
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("File name contains invalid path sequence");
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            fileMessage.setFileName(fileName);
            fileMessage.setFilePath(targetLocation.toString());
            fileMessage.setTag(0);

            if (null == redisTemplate.opsForValue().get(fileId)){

                String fileIds = "";
                redisTemplate.opsForValue().set(fileId,fileIds);

            }

            String fileIds = redisTemplate.opsForValue().get(fileId).toString();

            //队列放入redis
            redisTemplate.opsForValue().set(fileId,fileIds.concat(id).concat(","));
            redisTemplate.opsForValue().set(id,fileMessage);

            return ResultUtil.success();

        } catch (IOException ex) {
            System.out.println("Could not store file " + fileName + ". Please try again later!");
            ex.printStackTrace();
            return ResultUtil.error("500","上传失败");
        }
    }

    @Override
    public Result download(String id, HttpServletResponse response) throws Exception {
        //id为空不允许保存
        if (StringUtils.isEmpty(id)) {
            throw new Exception("id不能为空");
        }

        Object value = redisTemplate.opsForValue().get(id);
        JSONObject json = (JSONObject) JSON.toJSON(value);
        String fileName = json.get("fileName")+"";
        String afterPath = json.get("afterPath")+"";
        Integer tag = (Integer) json.get("tag");
        if (tag == 1){

            File file = new File(afterPath + "/" + fileName);
            if(file.exists()){
                response.setContentType("application/octet-stream");
                response.setHeader("content-type", "application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName,"utf8"));
                byte[] buffer = new byte[1024];
                //输出流
                OutputStream os = null;
                try(FileInputStream fis= new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);) {
                    os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while(i != -1){
                        os.write(buffer);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            HttpRequest get = HttpUtil.createGet(afterPath);
//            HttpResponse execute = get.execute();
//            //获取输入流对象（用于读文件）
//            BufferedInputStream bis = new BufferedInputStream(execute.bodyStream());
//            //获取文件后缀
//            String fileName = afterPath.substring(afterPath.lastIndexOf("/"));
//            response.setContentType("application/octet-stream");
//            //设置响应头,attachment表示以附件的形式下载，inline表示在线打开
//            response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
//            //获取输出流对象（用于写文件）
//            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
//            //下载文件,使用spring框架中的FileCopyUtils工具(内部自动关闭流，释放资源)
//            FileCopyUtils.copy(bis, bos);
            return ResultUtil.success();
        }else {
            return ResultUtil.error("500","文件正在处理中");
        }
    }

    @Override
    public Result recieveMessageFromServer(String id,String fileName,String afterPath,String description) {

        if (!StringUtils.isEmpty(id)){
            FileMessage fileMessage = new FileMessage();
            fileMessage.setId(id);
            fileMessage.setFileName(fileName);
            fileMessage.setAfterPath(afterPath);
            fileMessage.setDescription(description);
            fileMessage.setTag(1);

            //更新redis文件信息为处理后的信息
            redisTemplate.opsForValue().set(id,fileMessage);

            return ResultUtil.success();
        }else {
            return ResultUtil.paramError();
        }


    }

    @Override
    public Result<FileMessage> sendMessageToServer(){
        FileMessage fileMessage = new FileMessage();
        if (null != redisTemplate.opsForValue().get(fileId) && !"".equals(redisTemplate.opsForValue().get(fileId))){
            String fileIds1 = redisTemplate.opsForValue().get(fileId).toString();
            //获取文件列表中第一个文件id，获取后从列表里删除

            String[] ids = fileIds1.split(",");
            String id = "";
            String fileIds = "";

            for (int i = 0; i<ids.length;i++){

                if (i == 0){
                    id = ids[0];
                }else{

                    fileIds += ids[i]+",";

                }

            }
            System.out.println(id);
            System.out.println(fileIds);
            //更新修改后的队列
            redisTemplate.opsForValue().set(fileId,fileIds);
            Object value = redisTemplate.opsForValue().get(id);
            JSONObject json = (JSONObject) JSON.toJSON(value);
            String fileName = json.get("fileName")+"";
            String filePath = json.get("filePath")+"";

            fileMessage.setId(id);
            fileMessage.setFileName(fileName);
            fileMessage.setFilePath(filePath);
            return ResultUtil.success(fileMessage);
        } else {
            return ResultUtil.error("500","无需要处理的文件");
        }


    }
}
