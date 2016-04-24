/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xalan.templates.ElemValueOf;
import org.atgc.bio.BioFields;
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.meta.BioEntity;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;

import java.net.URLEncoder;
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
 *  reference example of subgraph or entire db from srcDb to destDb
 *      https://gist.github.com/kenahoo-windlogics/036639a7061877acc520
 *
 *  uniqueness example
 *  http://fooo.fr/~vjeux/github/github-recommandation/db/doc/manual/text/neo4j-manual.txt
 *
 */
@SuppressWarnings("javadoc")
public class KnowledgeEntityEvaluator implements Evaluator {

    List<Node> bioEntityList;
    List<BioTypes> bioTypes;

    protected static final Logger log = LogManager.getLogger(KnowledgeEntityEvaluator.class);

    public KnowledgeEntityEvaluator(List<Node> bioList, List<BioTypes> bTypes) {
        bioEntityList = bioList;
        bioTypes = bTypes;
        log.info("eval bioTypes " + bioTypes.get(0).name());
    }


    @Override
    public Evaluation evaluate(Path path) {
        log.info("evaluate(path)" + path);
        return Evaluation.INCLUDE_AND_CONTINUE;
    }


    public Evaluation test(Path path) {
         for (Node node : path.nodes()) {
             for (BioTypes bioType : bioTypes) {
                  String nodeType = (String) node.getProperty(BioFields.NODE_TYPE.toString());
                  if (nodeType.equals(bioType.toString())) {
                     return Evaluation.INCLUDE_AND_PRUNE;
                  }
             }
         }
        return Evaluation.INCLUDE_AND_CONTINUE;
    }

    private static String getLabel(Node node) {
        Object obj = node.getProperty(BioFields.MESSAGE.toString());
        String srcLabel = null;
        if (obj != null) {
            srcLabel = obj.toString();
        }
        return srcLabel;
    }

    /**
     * isNodeIncluded - checks if this bioEntity to be included
     * @param node
     * @param dNode
     * @return boolean
     */
    private static boolean isNodeIncluded(Node node, Node dNode) {
        if (node == null || dNode == null) {
            log.info("node or dNode is null, return false");
            return false;
        }
        Iterable<String> propertyKeys = node.getPropertyKeys();
            Iterable<String> destProps = dNode.getPropertyKeys();
            int cnt = 0;
            int dCnt = 0;
            StringBuffer sb = new StringBuffer();
            for (String s : propertyKeys) {
                cnt++;
                sb.append(s);
                sb.append(" , ");
                for (String d : destProps) {
                    if (s.equals(d)) {
                        dCnt++;
                    }
                }
            }

        if (cnt == dCnt)
            return true;
        return false;

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


    private static boolean isSpecifiTypesIncluded(Node node) {
        Iterator<Label> iterator = node.getLabels().iterator();
        Label label = iterator.next();
        if (label.toString().equalsIgnoreCase(BioTypes.NCBI_TAXONOMY.toString())) {
            return true;
        }
        if (label.toString().equalsIgnoreCase(BioTypes.PROTEIN.toString())) {
            return true;
        }
        if (label.toString().equalsIgnoreCase(BioTypes.GENE.toString())) {
            return true;
        }
        if (label.toString().equalsIgnoreCase(BioTypes.DRUG.toString())) {
            return true;
        }

        if (label.toString().equalsIgnoreCase(BioTypes.PUBMED.toString())) {
            return true;
        }

        if (label.toString().equalsIgnoreCase(BioTypes.INTACT_EXPERIMENT.toString())) {
            return true;
        }

        return (label.toString().equalsIgnoreCase("")) ? true : false;
    }

   /**
     * checks if relationship exists
     * @param pathEndNode
     * @param relType:1
     * @return boolean
     */
   private static boolean isRelationship(Node pathEndNode, BioRelTypes relType) {

        Iterable<Relationship> iterable = pathEndNode.getRelationships(
                DynamicRelationshipType.withName(URLEncoder.encode(
                        relType.toString())));
       // log.info("relType =" + relType.name());
        if (iterable.iterator().hasNext()) {
            Relationship rel = iterable.iterator().next();
            log.info("node rel type" + rel.getType().name());
        }
        return iterable.iterator().hasNext() ?  true :  false;
   }

}
