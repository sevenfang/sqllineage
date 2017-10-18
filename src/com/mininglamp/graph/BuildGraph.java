package com.mininglamp.graph;

import com.mininglamp.common.tool.LNode;
import com.mininglamp.common.tool.LineageNode;
import com.mininglamp.common.tool.TreeGlass;
import com.mininglamp.graph.constant.LineageLabel;
import com.mininglamp.graph.constant.RelTypes;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author
 */
public class BuildGraph {
    private static final String DB_PATH = "lineage-db";
    private GraphDatabaseService graphDb;
    private Index<Node> index;
    private IndexQueryLineage query = null;

    public BuildGraph() {
        query = new IndexQueryLineage();
    }

    public void addNode(LineageNode linenode) {
        if (linenode.getParent() == null) {
            return;
        }
        Transaction tx = graphDb.beginTx();
        try {
            Node parentnode;
            index = graphDb.index().forNodes("nodes");
            parentnode = query.indexNode(linenode.getParent().getName());
            if (parentnode == null) {
                parentnode = graphDb.createNode(LineageLabel.node);
            }

            String bbb = linenode.getParent().getType();
            parentnode.setProperty("name", linenode.getParent().getName());
            index.add(parentnode, "name", linenode.getParent().getName());
            parentnode.setProperty("type", linenode.getParent().getType());
            parentnode.setProperty("filePath", linenode.getFilePath());

//    	int no = ai.
            for (LNode lnode : linenode.getTabs()) {
                Node childnode = query.indexNode(lnode.getName());
                if (childnode == null) {
                    childnode = graphDb.createNode(LineageLabel.node);
                }

                String aaa = lnode.getType();
                System.out.println("parent:" + bbb + "\tchild:" + aaa);
                childnode.setProperty("name", lnode.getName());
                index.add(childnode, "name", lnode.getName());
                childnode.setProperty("type", lnode.getType());
                childnode.setProperty("filePath", lnode.getFilePath());
                RelTypes type;
                if ((lnode.getType().equals(TreeGlass.TOK_LIKETABLE)) || (linenode.getParent().getType().equals(TreeGlass.TOK_INSERT_INTO) && lnode.getType().equals("test"))) {
                    type = RelTypes.LIKETABLE;
                } else if (lnode.getType().equals(TreeGlass.TOK_UNIONALL)) {
                    type = RelTypes.UNION;
                } else if (lnode.getType().equals(TreeGlass.TOK_LEFTOUTERJOIN)) {
                    type = RelTypes.LJOIN;
                } else if (lnode.getType().equals(TreeGlass.TOK_RIGHTOUTERJOIN)) {
                    type = RelTypes.RJOIN;
                } else {
                    type = RelTypes.ERR;
                }
                Relationship rship = parentnode.createRelationshipTo(childnode, type);

//                System.out.println("add name: " + lnode.getName());
            }
            tx.success();
        } catch (IOException e) {
            e.printStackTrace();
            tx.failure();
        } finally {
            tx.finish();
        }
    }

    public void openDb() throws IOException {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(DB_PATH));
        query.setGraphDb(graphDb);
        registerShutdownHook(graphDb);
    }

    public void initDb() throws IOException {
        FileUtils.deleteRecursively(new File(DB_PATH));
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(DB_PATH));
        query.setGraphDb(graphDb);
        registerShutdownHook(graphDb);
    }

    public void removeData() {
        try (Transaction tx = graphDb.beginTx()) {
            // START SNIPPET: removingData
            // let's remove the data
//            firstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING ).delete();
//            firstNode.delete();
//            secondNode.delete();
            // END SNIPPET: removingData

            tx.success();
        }
    }

    public void shutDown() {
        System.out.println("Shutting down database ...");
        graphDb.shutdown();
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
                System.out.println("executing VM hook!");
            }
        });
    }

}