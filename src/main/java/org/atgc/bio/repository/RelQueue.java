/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import org.atgc.bio.BioFields;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.RelProperty;
import org.atgc.bio.meta.RelationshipEntity;
import org.atgc.bio.meta.StartNode;

/**
 *
 * Use {@link BioFields} to fetch {@link BioEntity}  and Nodes
 * @author jtanisha-ee
 * 
 */
public class RelQueue<T> {
   
    protected static Logger log = LogManager.getLogger(RelQueue.class);
    
    /**
     * The start node must be a @BioEntity class. And it cannot be null, 
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     */
    @StartNode
    private Object startNode;
    
  
    /**
     * All properties that need to be saved for the relationship must
     * have a @RelProperty annotation. 
     * "@protein" : "P31751" 
     */
    @RelProperty
    private String endNodeId;
    
    @RelProperty
    private BioFields endNodeKey; 
    
    /**
     * repository of relationship
     * BasicDBObject
     */
    @RelationshipEntity
    private Object relInfo;
  
   
    /**
     * Relationship class name
     */
    private Class relClassName;
   
    /**
     * This method is implemented yet.
     * 
     * @return 
     */
    @Override
    public String toString() {
        return String.format("%s interacts %s in %s", startNode,  endNodeId);
    }
    
    public RelQueue() {}
    
    public void add(Object startEntity, String moleculeIdRef, BioFields bioField, Object molecule, Class entityClass) {
       this.startNode = startEntity;
       this.endNodeId = moleculeIdRef;
       this.endNodeKey = bioField;
       this.relInfo = molecule;
       this.relClassName = entityClass;     
    }
    
    public Object getStartNode() {
        return this.startNode;
    }
    
    public String getEndNodeId() {
        return this.endNodeId;
    }
    
    public Object getRelInfo() {
        return this.relInfo;
    }
    
    public Class getRelClassName() {
        return this.relClassName;
    }
    
    public BioFields getEndNodeKey() {
        return this.endNodeKey;
    }
    /*
    
    public RelQueue copy() {
        RelQueue rel = new RelQueue();
        rel.endNodeId = this.endNodeId;
        rel.endNodeKey = this.endNodeKey;
        rel.relClassName = this.relClassName;      
     }
     * 
     */
    
}
   

