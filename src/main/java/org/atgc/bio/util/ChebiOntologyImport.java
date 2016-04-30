package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.atgc.bio.BioFields;
import org.atgc.bio.ChebiOntologyFields;
import org.atgc.bio.domain.*;
import org.atgc.bio.repository.PersistenceTemplate;
import org.atgc.bio.repository.Subgraph;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.http.HttpException;
import org.neo4j.graphdb.NotFoundException;

/**
 * for chemical entities of biological interest (CHEBI)
 * http://www.ebi.ac.uk/chebi/chebiOntology.do?chebiId=CHEBI:16411
 * obo file :  ftp://ftp.ebi.ac.uk/pub/databases/chebi/ontology/chebi.obo
 * @author jtanisha-ee
 */
public class ChebiOntologyImport {

    private static final Logger log = LogManager.getLogger(ChebiOntologyImport.class);
    /**
     * 
     * @param id
     * @param result
     * @throws UnknownHostException 
     */
    public static void processOntologyDoc(String id, BasicDBObject result) throws UnknownHostException {
        if (OntologyStrUtil.isChebiOntology(id)) {
            log.info("chebi Id =" + id);
            if (!StatusUtil.idExists(BioTypes.CHEBI_ONTOLOGY.toString(), BioFields.CHEBI_ONTOLOGY_ID.toString(), id)) {
                log.info("******* chebiOntologyId =" + id);
                try {
                    processChebiOntology(id, result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }     
            }
         }  else if (OntologyStrUtil.isProteinOntology(id)) {
            ProteinOntologyImport.processOntologyDoc(id, result);
         }
    }
    
    public static ChebiOntology getChebi(String id, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
        if (id != null) {
               log.info("getChebiOntology(), id =" + id);
               ChebiOntology chebiOnto = (ChebiOntology)subGraph.search(BioTypes.CHEBI_ONTOLOGY, BioFields.CHEBI_ONTOLOGY_ID, id);
               if (chebiOnto == null) {
                   chebiOnto = new ChebiOntology();
                   chebiOnto.setChebiOntologyId(id);
                   subGraph.add(chebiOnto);
               } 
               return chebiOnto;
        } else {
            return null;
        }
    }
    
    public static String getName(BasicDBObject dbObj) {
        return (String)dbObj.get(ChebiOntologyFields.NAME.toString());
    }
    
    public static String getDef(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ChebiOntologyFields.DEF);
    }
    
    public static String getPropertyValue(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ChebiOntologyFields.PROPERTY_VALUE);
    }
    
    public static String getNamespace(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ChebiOntologyFields.NAME_SPACE);
    }
    
    /**
     * <pre>
     * "altList" : [
		{
			"alt_id" : "CHEBI:25700"
		},
		{
			"alt_id" : "CHEBI:33244"
		}
	],
     * </pre>
     * @param dbObj
     * @return 
     */
    public static String getAlternateIds(BasicDBObject dbObj) {
        BasicDBList list = OntologyStrUtil.getList(dbObj, ChebiOntologyFields.ALT_ID_LIST);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            str.append(OntologyStrUtil.getString((BasicDBObject)obj, ChebiOntologyFields.ALT_ID));
            str.append(" ");       
        }
        return str.toString();
    }
    
    public static String getSynonym(BasicDBList list, ChebiOntologyFields enumField) {
        List synList = new ArrayList();
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, ChebiOntologyFields.SYNONYM);
            if (str != null && str.contains(enumField.toString())) {
                switch(enumField) {
                    case EXACT:
                        synList.add(OntologyStrUtil.getCleanSyn(str, " EXACT"));
                        break;
                    case RELATED:
                        synList.add(OntologyStrUtil.getCleanSyn(str, " RELATED"));
                        break;
                    case NARROW:
                        synList.add(OntologyStrUtil.getCleanSyn(str," NARROW"));
                }                  
            }
        } 
        if (synList.isEmpty()) {
            return null;
        } else {
            return synList.toString();
        }
    }
   
    
    /**
     * setSynonyms
     * @param chebi
     * @param dbObj 
     */
    public static void setSynonyms(ChebiOntology chebi, BasicDBObject dbObj) {
        BasicDBList list = OntologyStrUtil.getList(dbObj, ChebiOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, ChebiOntologyFields.EXACT)) != null) {
            chebi.setChebiExactSynonyms(synStr);
        } 
        if ((synStr = getSynonym(list, ChebiOntologyFields.RELATED)) != null) {
           chebi.setChebiRelatedSynonyms(synStr);
        }
        if ((synStr = getSynonym(list, ChebiOntologyFields.NARROW)) != null) {
           chebi.setChebiNarrowSynonyms(synStr);
        }
    }
    
    /*
    public static void setCellTypeIsARelationship(String str, ChebiOntology chebiOnto, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
        String cellId = OntologyStrUtil.getId(str, OntologyStrUtil.cellIdPattern);
        //log.info("id = " + goId + ", name = " + name);
        if (cellId != null) {
            CellTypeOntology cellEntity = CellTypeOntologyImport.getCellTypeOntology(cellId, subGraph);
            if (cellEntity != null) {
                chebiOnto.setIsARelationship(cellEntity, BioRelTypes.IS_A);
            }
        }
    }
    * 
    */
    
    /**
     * setGoIsARelationship
     * @param str
     * @param cellOnto
     * @param subGraph
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception 
     */
    /*
    public static void setGoIsARelationship(String str, ChebiOntology cellOnto, Subgraph subGraph) throws UnknownHostException, RuntimeException, Exception {
        String goId = OntologyStrUtil.getId(str, OntologyStrUtil.goIdPattern);
        GeneOntology go = GeneOntologyObo.getGeneOntology(goId, subGraph);
        cellOnto.setIsARelationship(go, BioRelTypes.IS_A);
    }
    * 
    */
    
    
    /**
     * "relationship" : "part_of PR:000018263 ! amino acid chain"
     * setProteinRelationship
     * @param str
     * @param chebiOnto
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void setProteinRelationship(String str, ChebiOntology chebiOnto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String prId = OntologyStrUtil.getId(str, OntologyStrUtil.prIdPattern);
        log.info("proteinOntology id = " + prId);
        if (prId != null) {
            ProteinOntology entity = ProteinOntologyImport.getProteinOntology(prId, subGraph);
            if (entity != null) {
                BioRelTypes relType = OntologyStrUtil.getRelationshipType(str, OntologyStrUtil.prIdPattern);
                if (relType == null) {
                    
                   log.info("relType is null, " + str);  
                } 
                chebiOnto.setRelationship(entity, relType); 
            }
        }
    }
    
    /**
     * relationship: has_functional_parent RNAO:0000026
     * setRNAORelationship
     * @param str
     * @param chebiOnto
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void setRNAORelationship(String str, ChebiOntology chebiOnto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String id = OntologyStrUtil.getId(str, OntologyStrUtil.RNAOPattern);
        log.info("RNAO id = " + id);
        if (id != null) {
            RNAOntology entity = RNAOntologyUtil.getRNAOntology(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = OntologyStrUtil.getRelationshipType(str, OntologyStrUtil.RNAOPattern);
                if (relType != null) {
                    chebiOnto.setRelationship(entity, relType);
                }
                
            }
        }
    }
      
    /**
     * 
     * relationship: has_functional_parent RNAO:0000026
     * processRelationshipList
     * @param chebiOnto
     * @param dbObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception 
     */
    public static void processRelationshipList(ChebiOntology chebiOnto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, NotFoundException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
        if (OntologyStrUtil.listExists(dbObj, ChebiOntologyFields.RELATIONSHIP_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = OntologyStrUtil.getList(dbObj, ChebiOntologyFields.RELATIONSHIP_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, ChebiOntologyFields.RELATIONSHIP);
                if (OntologyStrUtil.isProteinOntology(str)) {
                    setProteinRelationship(str, chebiOnto, subGraph);
                } else if (OntologyStrUtil.isRNAO(str)) {
                    setRNAORelationship(str, chebiOnto, subGraph);
                }
            }
         }
    } 
   
    /**
     * 
     * setIsARelationship between CellTypeOntology <->CellTypeOntology
     * @param chebiOnto
     * @param dbObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException
     * @throws UnknownHostException
     */
    public static void setIsARelationship(ChebiOntology chebiOnto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException {
         if (OntologyStrUtil.listExists(dbObj, ChebiOntologyFields.IS_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = OntologyStrUtil.getList(dbObj, ChebiOntologyFields.IS_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, ChebiOntologyFields.IS_A);
                if (OntologyStrUtil.isChebiOntology(str)) { 
                    String id = OntologyStrUtil.getId(str, OntologyStrUtil.chebiIdPattern);
                    if (id != null) { 
                        ChebiOntology chebiEntity = getChebi(id, subGraph);
                        if (chebiEntity != null) {
                            chebiOnto.setIsARelationship(chebiEntity, BioRelTypes.IS_A);
                        }
                    }
                } else if (OntologyStrUtil.isRNAO(str)) {
                    String id = OntologyStrUtil.getId(str, OntologyStrUtil.RNAOPattern);
                    if (id != null) {
                       RNAOntology rnao = RNAOntologyUtil.getRNAOntology(id, subGraph);
                       if (rnao != null) {
                           chebiOnto.setIsARelationship(rnao, BioRelTypes.IS_A);
                       }
                    }
                }
                
                /*
                if (OntologyStrUtil.isCellTypeOntology(str)) {
                   setCellTypeIsARelationship(str, chebiOnto, subGraph);
                } else if (OntologyStrUtil.isGeneOntology(str)) {
                   setGoIsARelationship(str, chebiOnto, subGraph);
                } else if (OntologyStrUtil.isProteinOntology(str)) {
                    setProteinOntoIsARelationship(str, chebiOnto, subGraph);
                }
                * 
                */
            }
         }
    }

    /**
     * No pubmed relationships in Chebi
     * processChebiOntology
     * @param id
     * @param obj
     * @throws IllegalAccessException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws HttpException
     * @throws UnknownHostException
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws NotFoundException
     * @throws InvocationTargetException
     * @throws RuntimeException
     * @throws Exception
     */
    public static void processChebiOntology(String id, BasicDBObject obj) throws IllegalAccessException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, HttpException, UnknownHostException, IllegalArgumentException, NoSuchFieldException, NotFoundException, InvocationTargetException, RuntimeException, Exception {          
        Subgraph subGraph  = new Subgraph();
        ChebiOntology chebi = getChebi(id, subGraph);

        if (chebi != null) {
            if (OntologyStrUtil.objectExists(obj, ChebiOntologyFields.NAME)) { 
                chebi.setName(getName(obj));
            }

            if (OntologyStrUtil.objectExists(obj, ChebiOntologyFields.ALT_ID_LIST)) {
                chebi.setChebiAlternateIds(getAlternateIds(obj));
            }

            if (OntologyStrUtil.objectExists(obj, ChebiOntologyFields.NAME_SPACE)) {
                chebi.setChebiNameSpace(getNamespace(obj));
            }

            if (OntologyStrUtil.objectExists(obj, ChebiOntologyFields.PROPERTY_VALUE)) {
                chebi.setChebiPropertyValue(getPropertyValue(obj));
            }

            if (getDef(obj) != null) {
                chebi.setDef(getDef(obj));
            }

            if (OntologyStrUtil.objectExists(obj, ChebiOntologyFields.SYNONYM_LIST)) {
                setSynonyms(chebi, obj);
            }

            if (OntologyStrUtil.objectExists(obj, ChebiOntologyFields.XREF_LIST)) {
                setPubMedReferences(chebi, obj, subGraph);
            }

            setIsARelationship(chebi, obj, subGraph);
            processRelationshipList(chebi, obj, subGraph);

            PersistenceTemplate.saveSubgraph(subGraph);
        } else {
            log.info("chebi is null");
        }
    }

    /**
     * xref: CiteXplore:16228119 "PubMed citation"
     * @param chebi
     * @param pubMedId
     * @param subGraph
     */
    public static void setPubMedRelation(ChebiOntology chebi, String pubMedId, Subgraph subGraph) throws Exception {
        if (pubMedId != null) {
           PubMed pubMed = CellTypeOntologyImport.getPubMed(pubMedId, subGraph);
           pubMed.setPubMedRelation(chebi);
        }
    }

    /**
     * xRefList
     * @param:
     */
    private static void setPubMedReferences(ChebiOntology chebi, BasicDBObject dbObj, Subgraph subGraph) throws Exception {
        BasicDBList list = OntologyStrUtil.getList(dbObj, ChebiOntologyFields.XREF_LIST);
        List<String> pubMedList = getCiteExplore(list, ChebiOntologyFields.CITEXPLORE);
        for (String pMed : pubMedList) {
            setPubMedRelation(chebi, pMed, subGraph);
        }

    }

    private static List<String> getCiteExplore(BasicDBList list, ChebiOntologyFields enumField) {
        List<String> xRefList = new ArrayList<>();
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject) obj, ChebiOntologyFields.XREF);
            if (str != null && str.contains(enumField.toString())) {
                switch (enumField) {
                    case CITEXPLORE:
                        String sList[] = str.split(ChebiOntologyFields.PUBMED_CITATION.toString());
                        xRefList.add(sList[0]);
                }
            }
        }
        return xRefList;
    }



}
        

