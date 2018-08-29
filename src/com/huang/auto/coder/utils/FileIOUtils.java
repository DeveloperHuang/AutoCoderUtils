package com.huang.auto.coder.utils;

import java.io.*;

/**
 * Created by Joss on 2016/11/2.
 */
public class FileIOUtils {

    public static void writeJavaFile(File file,String message) throws IOException {
        writeFile(file,message,"UTF-8");
    }

    /**
     * 写文件(如果该文件不存在，创建该文件，如果该文件已经存在，则重写该文件)
     * @param file 待写文件
     * @param message 待写入信息
     * @param encoding 文件编码格式
     * @throws IOException
     */
    public static void writeFile(File file, String message, String encoding) throws IOException {
        BufferedWriter bufferedWriter = getBufferedWriter(file,encoding);
        bufferedWriter.write(message);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    /**
     * 得到文件对应的缓冲输出流
     * @param file 待输出文件
     * @param encoding 流的编码格式
     * @return
     * @throws IOException
     */
    private static BufferedWriter getBufferedWriter(File file,String encoding) throws IOException{
        return getBufferedWriter(file.getAbsolutePath(), encoding);
    }

    private static BufferedWriter getBufferedWriter(String filePath,String encoding) throws IOException{
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;

        fileOutputStream = new FileOutputStream(
                filePath);
        outputStreamWriter = new OutputStreamWriter(fileOutputStream,encoding);
        bufferedWriter = new BufferedWriter(
                outputStreamWriter);
        return bufferedWriter;
    }
}
