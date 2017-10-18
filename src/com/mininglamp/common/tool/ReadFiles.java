package com.mininglamp.common.tool;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 读取文件夹中指定扩展名的文件文件列表
 * 读取指定文件的文件内容
 * Created by anke on 2017/10/16.
 */
public class ReadFiles {
    private static int con = 1;
    private static List filelist = new ArrayList();

    /***输入文件夹路径，及文件扩展名
     *
     * @param path 文件夹路径
     * @param format 文件扩展名
     * @return filelist list数组，装载文件绝对路径
     */
    public static List<String> getFileNameList(String path, String format) {
        File dir = new File(path);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileNameList(files[i].getAbsolutePath(), format); // 获取文件绝对路径
                } else if (fileName.endsWith(format)) { // 判断文件名的结尾
                    String strFileName = files[i].getAbsolutePath();
//                    System.out.println(format + con + "\t" + strFileName);
                    filelist.add(strFileName);
                    con++;
                } else {
                    continue;
                }
            }

        }
        return filelist;
    }


    /**
     * 读取txt文件的内容
     *
     * @param filePath 想要读取的文件路径
     * @return 返回文件内容
     */
    public static List<String> getFileContent(String filePath) {
        File file = new File(filePath);
        List resuList = new ArrayList();
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s;
            //使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {
                if (s.trim().startsWith("--")
                        || s.trim().startsWith("!/var/lib/hive/bjw/sqoop/bin/sqoop")
                        || s.trim().startsWith("!sqoop export")
                        || s.trim().startsWith("invalidate metadata")) {
                    continue;
                }
                result.append(System.lineSeparator() + s);
            }
            br.close();
            resuList.add(filePath);
            resuList.add(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resuList;
    }

    /**
     * 读取txt文件的内容,使用 byte 方式读取
     *
     * @param filePath 想要读取的文件路径
     * @return 返回文件内容
     */
    public static List<String> getFileContent2(String filePath) {
        File file = new File(filePath);
        FileInputStream fileInputStream = null;
        List resuList = new ArrayList();
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] b = new byte[1024];
        byte[] B = new byte[0];
        int read;
        try {
            while ((read = fileInputStream.read(b)) > -1) {
                int i = B.length;
                B = Arrays.copyOf(B, B.length + read);
                for (int j = 0; j < read; j++) {
                    B[i + j] = b[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            resuList.add(filePath);
            resuList.add(new String(B, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resuList;
    }

}
