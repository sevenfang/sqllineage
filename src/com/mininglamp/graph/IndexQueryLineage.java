package com.mininglamp.graph;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

import com.mininglamp.graph.constant.LineageLabel;

//import n1.EmbeddedNeo4j.RelTypes;

public class IndexQueryLineage {
     private static final String DB_PATH = "target/lineage-db";
     GraphDatabaseService graphDb;
     
     public GraphDatabaseService getGraphDb() {
		return graphDb;
	}


	public void setGraphDb(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
	}


	public Node indexNode(String tabname) throws IOException
        {
         // graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("lineage-db")) ;
          /***
            Iterator<Node> ir = 
            		graphDb.findNodes(DynamicLabel.label("xxx"));
            while(ir.hasNext()){
            	System.out.println(ir.next().getProperty("name"));
            }
            ***/
          Node node = 
        		  graphDb.findNode(LineageLabel.node, "name", tabname);
          return node;
        }
      
      /****
      public static void main(String[] args) throws IOException{
          IndexQueryLineage hello = new IndexQueryLineage();
           
      }
      ****/
}