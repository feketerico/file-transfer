package com.example.util;

/**
 * @author yangrui
 * @date 2021年09月30日 9:45
 */
import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
    /**
     * @Title: byteToFile
     * @Description: 把二进制数据转成指定后缀名的文件，例如PDF，PNG等
     * @param contents 二进制数据
     * @param filePath 文件存放目录，包括文件名及其后缀，如D:\file\bike.jpg
     * @Author: Wiener
     * @Time: 2018-08-26 08:43:36
     */
    public static void byteToFile(byte[] contents, String filePath) {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream output = null;
        try {
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(contents);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            // 获取文件的父路径字符串
            File path = file.getParentFile();
            if (!path.exists()) {
                logger.info("文件夹不存在，创建。path={}", path);
                boolean isCreated = path.mkdirs();
                if (!isCreated) {
                    logger.error("创建文件夹失败，path={}", path);
                }
            }
            fos = new FileOutputStream(file);
            // 实例化OutputString 对象
            output = new BufferedOutputStream(fos);
            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while (length != -1) {
                output.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            output.flush();
        } catch (Exception e) {
            logger.error("输出文件流时抛异常，filePath={}", filePath, e);
        } finally {
            try {
                bis.close();
                fos.close();
                output.close();
            } catch (IOException e0) {
                logger.error("文件处理失败，filePath={}", filePath, e0);
            }
        }
    }

    public static String fileToByte(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] b = new byte[1024];
        int len = -1;
        while((len = fis.read(b)) != -1) {
            bos.write(b, 0, len);
        }
        String str = bos.toString();

        return str;
    }


    public static void hexToApk(String hex, String filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append(hex);
        saveToApkFile(sb.toString().toUpperCase(), filePath);
    }

    public static void saveToApkFile(String src, String output) {
        if (src == null || src.length() == 0) {
            return;
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(output));
            byte[] bytes = src.getBytes();
            for (int i = 0; i < bytes.length; i+=2) {
                out.write(charToInt(bytes[i]) * 16 + charToInt(bytes[i + 1]));
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static int charToInt(byte ch) {
        int val = 0;
        if (ch >= 0x30 && ch <= 0x39) {
            val = ch - 0x30;
        } else if (ch >= 0x41 && ch <= 0x46) {
            val = ch - 0x41 + 10;
        }
        return val;
    }

    public static String fileToHex(String file) {
        try {
            StringBuffer sb = new StringBuffer();
            FileInputStream fis = new FileInputStream(file);
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 1024;
            int readSize = 1024;
            while (read == readSize) {
                read = fis.read(buffer, 0, readSize);
                bos.write(buffer, 0, read);
            }
            // 得到图片的字节数组
            System.out.println(bos.toString());
            byte[] result = bos.toByteArray();
            // 字节数组转成十六进制
//            String str = byte2HexStr(result);
            String str = bytesToHex(result);

            /*

             * 将十六进制串保存到txt文件中

             */

            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 实现字节数组向十六进制的转换方法
     */
    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

//加速版实现字节数组向十六进制的转换方法
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
}
