package com.mininglamp.graph;
import java.io.File;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.logging.Log;
import org.neo4j.logging.LogProvider;
import org.neo4j.logging.NullLog;


public class CypherSearch {
	 private static class MyCustomLogProvider implements LogProvider
	    {
	        public MyCustomLogProvider( Object output )
	        {
	        }

	        @Override
	        public Log getLog( Class loggingClass )
	        {
	            return NullLog.getInstance();
	        }

	        @Override
	        public Log getLog( String context )
	        {
	            return NullLog.getInstance();
	        }
	    }
   public static void main(String[] args) {
       //String rule = "MATCH (n:node{name:'xxx'}) RETURN n";
	   //String rule = "MATCH (n:node{name:'taba'})--(m) RETURN n,m";
	   String rule = "Match (start:node{name:'xxx'})-[:join*1..2]->(end:node) return start,end";
       LogProvider logProvider = new MyCustomLogProvider(null);
       GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
       GraphDatabaseService graphDb = 
    		  graphDbFactory.newEmbeddedDatabase(new File("lineage-db"));

       ExecutionEngine execEngine = 
    		  new ExecutionEngine(graphDb,logProvider);
      
      ExecutionResult execResult = execEngine.execute(rule);
      
      String results = execResult.dumpToString();
      System.out.println(results);
   }
}