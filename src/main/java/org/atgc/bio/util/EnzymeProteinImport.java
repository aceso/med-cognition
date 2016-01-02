package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.*;
import org.atgc.bio.*;
import org.atgc.bio.repository.RedbasinTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.domain.BioEntityClasses;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.domain.Enzyme;
import org.atgc.bio.domain.Protein;
import org.neo4j.graphdb.NotFoundException;

/**
 * Uses: enzymeprotein mongo collection
 * The ID line - EC_NUM: 1.1.1.1,  2.1.1.3
          3.2  The DE line - description
          3.3  The AN line - alternate name
          3.4  The CA line - catalytic activity
          3.5  The CF line - cofactors
          3.6  The CC line - comments
          3.7  The DI line - diseaseindicator (does not exist) 
          3.8  The PR line - references to prosite 
          3.9  The DR line - references to uniprotid/swissprot
          3.10 The // line - comment
 * 
 * @author jtanisha-ee
 */
public class EnzymeProteinImport {
    
     protected static Log log = LogFactory.getLog(EnzymeProteinImport.class);
     
     private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
     
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.ENZYME_PROTEIN).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                //String ecNum = (String)result.get(EnzymeFields.EC_NUM.toString());
                String ecNum = getEnzymeId(result);
                if (!StatusUtil.idExists(ImportCollectionNames.ENZYME_PROTEIN.toString(), BioFields.ENZYME_ID.toString(), ecNum)) {
                    log.info("******* ecNum =" + ecNum);
                    try {
                        processOntologyDoc(ecNum, result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } finally {
            dbCursor.close();
        }   
    }
   
    /**
     * processOntologyDoc
     * @param str
     * @param dbObj 
     */
    public static void processOntologyDoc(String id, BasicDBObject doc) throws UnknownHostException, RuntimeException, Exception {
         if (!StatusUtil.idExists(BioTypes.ENZYME.toString(), BioFields.ENZYME_ID.toString(), id)) {
              processEnzyme(id, doc);
         }
    }
    
    public static String getEnzymeId(BasicDBObject obj) {
          return "EC:" + (String)obj.get(EnzymeProteinFields.ID.toString());
    }
    
    public static boolean isObj(BasicDBObject obj) {
           if (obj.get(EnzymeFields.TEXT.toString()) != null) {
              // log.info(field.toString() + " value exists");
              return true;
           }
        return false;
    }
    
    /**
     * <pre>
     * 	"CA_LIST" : [
		{
			"CA" : "L-threo-3-methylaspartate = mesaconate + NH(3)."
		}
	],
       </pre>
     * @param obj - reactions
     * @return 
     */
    public static String getCatalyticActivity(BasicDBObject obj) {
         return getListAsString(obj, EnzymeProteinListFields.CA_LIST, EnzymeProteinFields.CA);
    }
    
    /**
     * 
     * @param obj
     * @return 
     */
    public static String getAlterateName(BasicDBObject obj) {
         return getListAsString(obj, EnzymeProteinListFields.AN_LIST, EnzymeProteinFields.AN);    
    }
    
    /**
     * <pre>
     * 	"CF_LIST" : [
		{
			"CF" : "Cobalamin."
		}
	],
     * </pre>
     */
    public static String getCofactor(BasicDBObject obj) {
        return getListAsString(obj, EnzymeProteinListFields.CF_LIST, EnzymeProteinFields.CF);
    }
    
    public static String getComments(BasicDBObject obj) {
        return getListAsString(obj, EnzymeProteinListFields.CC_LIST, EnzymeProteinFields.CC);
    }
    
    
    public static Protein getProtein(String id, Subgraph subGraph) throws Exception {
        Protein protein = UniprotUtil.getProtein(id, subGraph);
        return protein;
    }
    
    /**
     * Protein list (uniprot/swissprot)
     * @param enzyme
     * @param dbObj
     * @param subGraph 
     */
    public static void setProteinRelationship(Enzyme enzyme, BasicDBObject dbObj, Subgraph subGraph) throws Exception {
         BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, EnzymeProteinListFields.DR_LIST);
         for (Object obj : list) {
             if (obj != null) {
                 String id = OntologyStrUtil.getString((BasicDBObject)obj, EnzymeProteinFields.DR);
                 if (id != null) {
                     Protein entity = getProtein(id, subGraph);
                     //entity.setEnzymeRelationship(enzyme);
                 }
             }
         }
    }
    
    public static String getAlternateName(BasicDBObject dbObj) {
        if (dbObj != null) {
            return getListAsString(dbObj, EnzymeProteinListFields.AN_LIST, EnzymeProteinFields.AN);
        } else {
            return null;
        }
    }
    
    public static String getName(BasicDBObject dbObj) {
        if (dbObj != null) {
            return getListAsString(dbObj, EnzymeProteinListFields.DE_LIST, EnzymeProteinFields.DE);
        } else {
            return null;
        }
    }
   
    public static void setOtherNames(Enzyme enzyme, BasicDBObject dbObj) {
        String alternateName = enzyme.getEnzymeOtherNames();
        String an = getAlternateName(dbObj);
        if (an != null && an.length() > 0) { 
            if (alternateName == null) {
                enzyme.setEnzymeOtherNames(an);
            } else {
                if (alternateName.equalsIgnoreCase(an)) {
                    enzyme.setEnzymeOtherNames(alternateName + "," + an);
                }
            }
        }
    }
    
    
    public static String getListAsString(BasicDBObject dbObj, Enum fieldList, Enum field) {
         BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, field);
         StringBuilder str = new StringBuilder();
         for (Object obj : list) {
            str.append(OntologyStrUtil.getString((BasicDBObject)obj, field));
            str.append(" ");       
         }
         return str.toString();
    }
   
    public static Enzyme getEnzyme(String ecNum, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
         Enzyme enzyme = (Enzyme)subGraph.search(BioTypes.ENZYME, BioFields.ENZYME_ID, ecNum);              
         if (enzyme == null) {
            enzyme = new Enzyme();
            enzyme.setEnzymeId(ecNum);  
        }
        return enzyme;
    }
    
    public static Enzyme getEnzyme(String ecNum, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
       Enzyme enzyme = (Enzyme)subGraph.search(BioTypes.ENZYME, BioFields.ENZYME_ID, ecNum);              
         if (enzyme == null) {
            enzyme = new Enzyme();
            enzyme.setEnzymeId(ecNum);  
            //setEnzymeProperties(enzyme, dbObj, subGraph);
        }  else {
            if (dbObj != null) { 
                if (OntologyStrUtil.objectExists(dbObj, EnzymeProteinListFields.CA_LIST)) {
                    String reaction = enzyme.getEnzymeReaction();
                    if (reaction == null) {
                        enzyme.setEnzymeReaction(getCatalyticActivity(dbObj));
                    } else {
                        enzyme.setEnzymeReaction(reaction + "," + getCatalyticActivity(dbObj));
                    }
                } 

                if (OntologyStrUtil.objectExists(dbObj, EnzymeProteinListFields.AN_LIST)) {
                    setOtherNames(enzyme, dbObj);
                }
            }
        }
        
        if (enzyme != null) {
            subGraph.add(enzyme);
        }
        return enzyme;
        
    }
  
    public static void setEnzymeProperties(Enzyme enzyme, BasicDBObject dbObj, Subgraph subGraph) throws Exception {
        if (OntologyStrUtil.objectExists(dbObj, EnzymeProteinListFields.DE_LIST)) {
            enzyme.setEnzymeAcceptedNames(getName(dbObj));
        }
      
        if (OntologyStrUtil.objectExists(dbObj, EnzymeProteinListFields.AN_LIST)) {
            enzyme.setEnzymeOtherNames(getAlternateName(dbObj));
        }
        
        if (OntologyStrUtil.objectExists(dbObj, EnzymeProteinListFields.CA_LIST)) {
            enzyme.setEnzymeReaction(getCatalyticActivity(dbObj));
        }
        
        /**
         * cofactor - A nonprotein component of enzymes is called the cofactor.
         */
        if (OntologyStrUtil.objectExists(dbObj, EnzymeProteinListFields.CF_LIST)) {
            enzyme.setEnzymeCofactor(getCofactor(dbObj));
        }
        
        if (OntologyStrUtil.objectExists(dbObj, EnzymeProteinListFields.CC_LIST)) {
            enzyme.setEnzymeComments(getComments(dbObj));
        }
        
        if (OntologyStrUtil.objectExists(dbObj, EnzymeProteinListFields.DR_LIST)) {
            setProteinRelationship(enzyme, dbObj, subGraph);
        }
           
    } 
    
   
    /**
     * 
     * @param ecNum
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception 
     */
    public static void processEnzyme(String ecNum, BasicDBObject result) throws UnknownHostException, RuntimeException, Exception {    
        Subgraph subGraph = new Subgraph();
        Enzyme enzyme = getEnzyme(ecNum, subGraph);
      /*  
        for (Object obj : row) {
             BasicDBObject dbObj = (BasicDBObject)obj;
             log.info("dbObj =" + dbObj.toString());
             if (dbObj.containsField(EnzymeFields.ACCEPTED_NAME.toString())) {
                 log.info("acceptedName");
             }
             log.info("dbObj key = " + dbObj.getString(EnzymeFields.NAME.toString()));
             log.info("value =" + dbObj.getString(EnzymeFields.TEXT.toString()));
         } */
         RedbasinTemplate.saveSubgraph(subGraph);
    }
    
    
    /**
     * Given enzymeid or ec_num, return {@link ENZYME} {@link BioEntityClasses#ENZYME}
     * Fetch information from enzyme collection and create enzyme, if it does not exist
     * @param ecNum
     * @param subGraph
     * @return 
     */
    /*
   public static Enzyme getEnzyme(String ecNum, Subgraph subGraph) throws UnknownHostException, RuntimeException, Exception {
        return fetchEnzymeFromObject(ecNum, subGraph);
    } */
    
}