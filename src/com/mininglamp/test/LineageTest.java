package com.mininglamp.test;

import com.mininglamp.common.tool.LineageNode;
import com.mininglamp.common.tool.ParseDriver;
import com.mininglamp.common.tool.ReadFiles;
import com.mininglamp.graph.BuildGraph;
import com.mininglamp.graph.exception.ParseException;
import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class LineageTest {
    private ParseDriver pd;

    @Test
    public void testCreatedb() {

        String command = "create table staff.tb_ml_1 as select a.col1,a.col2 from staff.tb_ml_2 a join " +
                "(select col3 from staff.tb_ml_3) c on a.col1 = c.col3";
        String command2 = "create table staff.tb_ml_4 as " +
                "select * from staff.tb_ml_1 c join " +
                "staff.tb_ml_6 d on c.id = d.id where c.age > 25";
        //String command = "create table staff.tab1 like staff.tab2";
        //String command = "insert into staff.tab3 select c.col1,c.col2 from staff.tb4 c";

        ParseDriver pd = new ParseDriver();
        try {
            ASTNode tree = pd.parse(command);
            System.out.println(tree.dump());
            System.out.println(tree.toStringTree());
            LineageNode lin = new LineageNode();
            pd.travelTree(lin, tree, "");

            BuildGraph graph = new BuildGraph();
            graph.initDb();
            graph.addNode(lin);

            lin = new LineageNode();
            ASTNode tree2 = pd.parse(command2);
            pd.travelTree(lin, tree2, "");
            graph.addNode(lin);
            graph.shutDown();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreatedb2() {
//        String dirPath = "/Users/anke/Downloads/【工作文件】mininglamp/【20171012】hive大sql脚本优化/jobscheduler2.0";
//        String dirPath = "/Users/anke/data/test/test2";
        String dirPath = "/Users/anke/data/test/test3";
        List<String> filePathLists = ReadFiles.getFileNameList(dirPath, "sql");
        int con = 1;
        BuildGraph graph = new BuildGraph();
        try {
            graph.initDb();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String val : filePathLists) {
            //当前解析的 sql 文件绝对路径
            String sqlFilePath = val;
            List<String> sqlLists = ReadFiles.getFileContent(sqlFilePath);
            String[] command = sqlLists.get(1).trim().split(";");

            for (String sql : command) {
                System.out.println("=======================================================");
                System.out.println("正在解析的文件路径" + con + ":\t" + sqlLists.get(0));
                System.out.println("即将解析的 sql:\n" + sql);
                ParseDriver pd = new ParseDriver();
                try {
                    ASTNode tree = pd.parse(sql);
                    System.out.println(tree.dump());
//                System.out.println(tree.toStringTree());
                    LineageNode lin = new LineageNode();
                    pd.travelTree(lin, tree, sqlFilePath);
                    graph.addNode(lin);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            con++;
        }
    }
}
