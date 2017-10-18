package com.mininglamp.graph.constant;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType {
    UNION, LJOIN, RJOIN, LIKETABLE, ERR
}