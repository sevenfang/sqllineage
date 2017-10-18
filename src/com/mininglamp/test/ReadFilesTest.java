package com.mininglamp.test;


import com.mininglamp.common.tool.ReadFiles;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by anke on 2017/10/16.
 */
public class ReadFilesTest {
    @Test
    public void getFileNameList() throws Exception {
        System.out.println(ReadFiles.getFileNameList("/Users/anke/data/test", "sql"));
    }

    @Test
    public void getFileContent() throws Exception {

        List<String> fileContent = ReadFiles.getFileContent("/Users/anke/data/test/07_04_ml_jnl_acc_chg_final.sql");
        System.out.println(fileContent.size());
        for (String val : fileContent) {
            System.out.println(val);
            System.out.println("---------------------------------");
        }
    }

    @Test
    public void getFileContent2() throws IOException {
        ReadFiles.getFileContent2("/Users/anke/data/test/test1.sql");
    }

}