/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.atgc.bio.domain;

import com.mongodb.BasicDBObject;
import org.apache.http.HttpException;
import org.atgc.bio.BioFields;
//import org.atgc.bio.UniProtAccess;
import org.atgc.bio.UniProtAccess;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.FullTextIndexed;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.meta.NodeLabel;
import org.atgc.bio.meta.NonIndexed;
import org.atgc.bio.meta.PartKey;
import org.atgc.bio.meta.RelatedTo;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniqueCompoundIndex;
import org.atgc.bio.meta.Visual;
import org.atgc.bio.repository.TemplateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.util.UniprotUtil;
import org.neo4j.graphdb.Direction;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

/**
 * A structure has chains.
 *
 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.CHAIN, field1=BioFields.STRUCTURE_ID, field2=BioFields.CHAIN_ID, field3=BioFields.NONE)
@BioEntity(bioType = BioTypes.CHAIN)
@SuppressWarnings("javadoc")
public class Chain {
    protected static Logger log = LogManager.getLogger(Chain.class);

    @GraphId
    private Long id;

    @Taxonomy(rbClass=TaxonomyTypes.CHAIN_ID, rbField=BioFields.CHAIN_ID)
    @PartKey
    private String chainId;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private final String nodeType = TemplateUtils.extractBioType(this).toString();

    @Visual
    @PartKey
    //@RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.PART_OF_STRUCTURE, elementClass = BioRelation.class)
    private String structureId;

    @NonIndexed
    private int atomLength;

    /**
     * Return the sequence of amino acids as it has been provided in the ATOM records.
     */
    @FullTextIndexed (indexName=IndexNames.CHAIN_ATOM_SEQUENCE)
    private String atomSequence;

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_A_PROTEIN, elementClass = BioRelation.class)
    private BioRelation relatedProtein;

    /**
     * This is the @start offset. We only consider Item 1 and not Item 0.
     */
    @NonIndexed
    private int start;

    /**
     * This is the @end offset. We only consider Item 1 and not Item 0.
     */
    @NonIndexed
    private int end;

    /**
     * This is the message label for a protein chain node that is auto generated.
     */
    @NodeLabel
    @Indexed (indexName=IndexNames.CHAIN_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.CHAIN_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * Get the message label for a protein chain that is auto-generated.
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the auto-generated message label for the protein chain.
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the protein on this chain.
     *
     * @return Protein
     */
    public Protein getProtein() {
        if (relatedProtein == null) {
            return null;
        }
        return (Protein)relatedProtein.getEndNode();
    }

    /**
     * Set the protein on this chain. Takes Uniprot and Swissprot.
     *
     * @param protein
     */
    public void setProtein(Protein protein) {
        relatedProtein = new BioRelation(this, protein, BioRelTypes.HAS_A_PROTEIN);
    }

    /**
     * The full code for generating the Protein model from Uniprot is not yet
     * ready. So we will just have some sample code.
     *
     * @param uniprotId
     * @throws Exception
     */
    public void setProtein(String uniprotId) throws ServiceException, IllegalAccessException, InterruptedException, HttpException, IOException, URISyntaxException, InvocationTargetException, NoSuchFieldException {
        setProtein(UniprotUtil.getProtein(uniprotId));
    }

    /**
     *
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the chainId for instance "A"
     *
     * @return String
     */
    public String getChainId() {
        return chainId;
    }

    /**
     *
     * @param chainId
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * Get the structureId for instance 4HHB
     *
     * @return String
     */
    public String getStructureId() {
        return structureId;
    }

    /**
     *
     * @param structureId
     */
    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    /**
     *
     * @return int
     */
    public int getAtomLength() {
        return atomLength;
    }

    /**
     *
     * @param atomLength
     */
    public void setAtomLength(int atomLength) {
        this.atomLength = atomLength;
    }

    /**
     *
     * @return String
     */
    public String getAtomSequence() {
        return atomSequence;
    }

    /**
     *
     * @param atomSequence
     */
    public void setAtomSequence(String atomSequence) {
        this.atomSequence = atomSequence;
    }

    /**
     * Get the start offset of the protein.
     *
     * @return int
     */
    public int getStart() {
        return start;
    }

    /**
     * Set the start offset of the protein.
     *
     * @param start
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Get the end offset of the protein.
     *
     * @return int
     */
    public int getEnd() {
        return end;
    }

    /**
     * Set the end offset of the protein.
     *
     * @param end
     */
    public void setEnd(int end) {
        this.end = end;
    }
}