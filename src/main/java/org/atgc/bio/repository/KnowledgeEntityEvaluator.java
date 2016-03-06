/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.meta.BioEntity;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * KnowledgeGraph - Traversal
 * @author jtanisha-ee
 *         Use BioFields to fetch BioEntities and Nodes
 *
 *  References:
 *  https://github.com/neo4j/neo4j/blob/2.3.2/community/embedded-examples/src/main/java/org/neo4j/examples/NewMatrix.java
 *  http://www.informit.com/articles/article.aspx?p=2415371#articleDiscussion
 *
 *  copying subgraph or entire db from srcDb to destDb
 *      https://gist.github.com/kenahoo-windlogics/036639a7061877acc520
 *
 */
@SuppressWarnings("javadoc")
public class KnowledgeEntityEvaluator implements Evaluator {

    List<KnowledgeInteractor> knowledgeInteractorList;
    protected static final Logger log = LogManager.getLogger(PersistenceTemplate.class);

    public KnowledgeEntityEvaluator(List<KnowledgeInteractor> list) {
        knowledgeInteractorList = list;
    }

    @Override
    public Evaluation evaluate(Path path)  {
        System.out.println("evaluate path=" + path);
        Iterator<KnowledgeInteractor> iterator = knowledgeInteractorList.iterator();
        while (iterator.hasNext()) {
            KnowledgeInteractor ki = iterator.next();
            System.out.println("knowledge interactor =" + ki.bioEntity.bioType());
            if (isNodeIncluded(path.endNode(), ki.bioEntity)) {
                System.out.println("node exists " + path.endNode().getId() + " name " + path.endNode().getProperty("name"));
                System.out.println("path =" + path);
                return Evaluation.INCLUDE_AND_CONTINUE;
            }
        }
        return Evaluation.EXCLUDE_AND_CONTINUE;
    }


    /**
     * isNodeIncluded - checks if this Node to be included
     * @param node
     * @param bioEntity
     * @return boolean
     */
    private static boolean isNodeIncluded(Node node, BioEntity bioEntity) {
        Iterator<Label> iterator = node.getLabels().iterator();
        Label label = iterator.next();
        Node dNode = null;
        try {
            dNode = PersistenceTemplate.getNode(bioEntity);
        } catch(Exception e) {
            log.error("KnowledgeEntityEvaluator:isNodeIncluded(), exception in retrieving a node " + bioEntity.bioType() + ", error=" + e.getMessage(), e);
        }
        System.out.println("nodeId match check" + node.getId() +  "== " + dNode.getId());
        return (node.getId() == dNode.getId() && node.getLabels().toString().equalsIgnoreCase(dNode.getLabels().toString())) ? true : false;
    }

    /**
     * isPropertyIncluded  - checks for property name and value
      * @param node
     * @param ki
     * @return boolean
     */
    private static boolean isPropertyIncluded(Node node, KnowledgeInteractor ki) {
        return node.getProperty(ki.propertyName).toString().equals(ki.propertyValue) ? true : false;
    }

    /**
     *
      * @param node
     * @param dNodeType
     * @return
     */
    private static boolean isNodeTypeIncluded(Node node, BioTypes dNodeType) {
        System.out.println("nodeId match check" + node.getId() +  "== " + dNodeType);
        Iterator<Label> iterator = node.getLabels().iterator();
        Label label = iterator.next();
        return (label.toString().equalsIgnoreCase(dNodeType.toString())) ? true : false;
    }


   /**
     * checks if relationship exists
     * @param pathEndNode
     * @param relType
     * @return boolean
     */
   private static boolean isRelationship(Node pathEndNode, BioRelTypes relType) {
        Iterable<Relationship> iterable = pathEndNode.getRelationships(
                DynamicRelationshipType.withName(URLEncoder.encode(
                        relType.toString())));
        return iterable.iterator().hasNext() ?  true :  false;
   }

}
