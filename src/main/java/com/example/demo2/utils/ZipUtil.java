package com.example.demo2.utils;
import java.io.*;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 *
 * @author
 *
 */
public class ZipUtil {

    private static final int BUFFERED_SIZE = 1024;

    /**
     * 压缩文件
     *
     * @param zipFileName 保存的压缩包文件路径
     * @param filePath    需要压缩的文件夹或者文件路径
     * @param isDelete    是否删除源文件
     * @throws Exception
     */
    public static void zip(String zipFileName, String filePath, boolean isDelete) throws Exception {
        zip(new File(zipFileName), new File(filePath), isDelete);
    }


    public static void zip(File zipFile, File inputFile, boolean isDelete) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        if (!inputFile.exists()) {
            throw new FileNotFoundException("在指定路径未找到需要压缩的文件！");
        }
        try {
            zip(out, inputFile, "", isDelete);
        } catch (Exception e) {
            throw e;
        }finally {
            if(out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static void zip(ZipOutputStream out, File inputFile, String base, boolean isDelete) throws Exception {

        FileInputStream in = null;
        try {
            if (inputFile.isDirectory()) { // 如果是目录
                File[] inputFiles = inputFile.listFiles();
                out.putNextEntry(new ZipEntry(base + "/"));
                base = base.length() == 0 ? "" : base + "/";
                for (int i = 0; i < inputFiles.length; i++) {
                    zip(out, inputFiles[i], base + inputFiles[i].getName(), isDelete);
                }
            } else { // 如果是文件
                if (base.length() > 0) {
                    out.putNextEntry(new ZipEntry(base));
                } else {
                    out.putNextEntry(new ZipEntry(inputFile.getName()));
                }

                int len;
                byte[] buff = new byte[BUFFERED_SIZE];
                in = new FileInputStream(inputFile);
                while ((len = in.read(buff)) != -1) {
                    out.write(buff, 0, len);
                }
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }finally {
            if(in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (isDelete) {
            inputFile.delete();
        }
    }

    /**
     * 解压缩
     *
     * @param zipFilePath  压缩包路径
     * @param fileSavePath 解压路径
     * @param isDelete     是否删除源文件
     * @throws Exception
     */
    public static void unZip(String zipFilePath, String fileSavePath, boolean isDelete) throws Exception {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            (new File(fileSavePath)).mkdirs();
            File zFile = new File(zipFilePath);
            if ((!zFile.exists()) && (zFile.length() <= 0)) {
                throw new Exception("要解压的文件不存在!");
            }
            ZipFile zipFile = new ZipFile(zFile);
            String tempSavePathStr, zipEntryNameStr, tempEntryPathStr;
            File tempSaveFile = new File(fileSavePath);// 从当前目录开始
            tempSavePathStr = tempSaveFile.getAbsolutePath();// 输出的绝对位置
            Enumeration<ZipEntry> enume = zipFile.getEntries();
            while (enume.hasMoreElements()) {
                ZipEntry zipEntry = enume.nextElement();
                zipEntryNameStr = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    tempEntryPathStr = tempSavePathStr + File.separator + zipEntryNameStr;
                    File dir = new File(tempEntryPathStr);
                    dir.mkdirs();
                    continue;
                } else {
                    // 读写文件
                    bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                    zipEntryNameStr = zipEntry.getName();
                    tempEntryPathStr = tempSavePathStr + File.separator + zipEntryNameStr;
                    // 建目录
                    for (int i = 0; i < zipEntryNameStr.length(); i++) {
                        if (zipEntryNameStr.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String subTemp = tempSavePathStr + File.separator + zipEntryNameStr.substring(0, i);
                            File subdir = new File(subTemp);
                            if (!subdir.exists())
                                subdir.mkdir();
                        }
                    }
                    bos = new BufferedOutputStream(new FileOutputStream(tempEntryPathStr));
                    int len;
                    byte[] buff = new byte[BUFFERED_SIZE];
                    while ((len = bis.read(buff)) != -1) {
                        bos.write(buff, 0, len);
                    }
                    bos.flush();
                }
            }
            zipFile.close();

            if (isDelete) {
                zFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            if(bis != null) {
                try {
                    bis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String unGZip(byte[] compressedString) {


        byte[] compressed;
        try {
            compressed = compressedString;
            try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                 GZIPInputStream unGZipStream = new GZIPInputStream(new ByteArrayInputStream(compressed))) {

                byte[] buffer = new byte[1024];
                int offset;
                while ((offset = unGZipStream.read(buffer)) != -1) {
                    out.write(buffer, 0, offset);
                }

                return out.toString();
            }
        } catch (IOException e) {
            return null;
        }
    }
//	public static void main(String[] args) throws Exception {
//		ZipUtil.zip("D:/tmp/yc.zip", "D:/tmp/yc", true);
//		ZipUtil.unZip("D:/tmp/yc.zip", "D:/tmp/yc", true);
//	}
}

