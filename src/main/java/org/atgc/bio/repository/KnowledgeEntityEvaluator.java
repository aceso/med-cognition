/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    List<BioRelTypes> relTypes;

    protected static final Logger log = LogManager.getLogger(KnowledgeEntityEvaluator.class);

    public KnowledgeEntityEvaluator(List<Node> bioList, List<BioRelTypes> rTypes) {
        bioEntityList = bioList;
        log.info("bioEntityList.get(0)" + bioEntityList.get(0));
        relTypes = rTypes;
    }

    @Override
    public Evaluation evaluate(Path path)  {
        log.info("evaluate(path)" + path);

        int matches=0;
        //
        for(Node bio : bioEntityList) {
            // include the nodes of interest in the middle of the path too
            // for each depth, you get a path to expand.
            // each path can have one or more bioentities (nodes)
            for (Node n : path.nodes()) {
                // biochemical assay nodes which are relevant in the case study
                if (isNodeIncluded(n, bio)) {
                    ++matches;
                    for (BioRelTypes relType : relTypes) {
                        if (isRelationship(path.endNode(), relType)) {
                            return Evaluation.INCLUDE_AND_PRUNE;
                        }
                    }
                    return Evaluation.INCLUDE_AND_CONTINUE;
                }

            }
            if (isNodeIncluded(path.endNode(), bio)) {
                for (BioRelTypes relType : relTypes) {
                    if (isRelationship(path.endNode(), relType)) {
                        //System.out.println("relType =" + relType + " endNodetype" + path.endNode().getId());
                        return Evaluation.INCLUDE_AND_PRUNE;

                    }
                }
                // include and prune
                return Evaluation.INCLUDE_AND_CONTINUE;
            }
        }

        //  biochemical assays matched then continue to expand the path
        if (matches <= bioEntityList.size() && matches > 0) {
            return Evaluation.INCLUDE_AND_CONTINUE;
        }

        return Evaluation.EXCLUDE_AND_CONTINUE;
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
        return iterable.iterator().hasNext() ?  true :  false;
   }

}
