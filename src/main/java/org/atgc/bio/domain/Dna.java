package org.atgc.bio.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.BioFields;
import org.atgc.bio.meta.*;
import org.atgc.bio.repository.TemplateUtils;
import org.neo4j.graphdb.Direction;

import java.util.Collection;
import java.util.HashSet;

/**
 * {
 "_id": {
 "$oid": "5009e0210364add94f9cc499"
 },
 "@id": "358462",
 "names": {
 "shortLabel": "telodna",
 "fullName": "Human telomeric repeat DNA"
 },
 "xref": {
 "primaryRef": {
 "@refTypeAc": "MI:0356",
 "@refType": "identity",
 "@id": "EBI-1005914",
 "@dbAc": "MI:0469",
 "@db": "intact"
 }
 },
 "interactorType": {
 "names": {
 "shortLabel": "dna",
 "fullName": "deoxyribonucleic acid",
 "alias": [
 {
 "@typeAc": "MI:0303",
 "#text": "deoxyribonucleic acid"
 },
 {
 "@typeAc": "MI:0303",
 "#text": "DNA"
 }
 ]
 },
 "xref": {
 "primaryRef": {
 "@refTypeAc": "MI:0356",
 "@refType": "identity",
 "@id": "MI:0319",
 "@dbAc": "MI:0488",
 "@db": "psi-mi"
 },
 "secondaryRef": [
 {
 "@refTypeAc": "MI:0356",
 "@refType": "identity",
 "@id": "EBI-619647",
 "@dbAc": "MI:0469",
 "@db": "intact"
 },
 {
 "@refTypeAc": "MI:0358",
 "@refType": "primary-reference",
 "@id": "14755292",
 "@dbAc": "MI:0446",
 "@db": "pubmed"
 },
 {
 "@refTypeAc": "MI:0361",
 "@refType": "see-also",
 "@id": "SO:0000352",
 "@dbAc": "MI:0601",
 "@db": "so"
 }
 ]
 }
 },
 "organism": {
 "@ncbiTaxId": "9606",
 "names": {
 "shortLabel": "human",
 "fullName": "Homo sapiens"
 }
 },
 "intactId": "9391075.xml",
 "interactorId": "358462"
 }
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.DNA)
public class Dna {
    protected static Logger log = LogManager.getLogger(Drug.class);

    @GraphId
    private Long id;

    /**
     * Name of the intact XML file. Use Uniprot when null.
     */
    @Indexed( indexName=IndexNames.INTACT_ID)
    @Taxonomy(rbClass=TaxonomyTypes.INTACT_ID, rbField= BioFields.INTACT_ID)
    private String intactId;

    @Indexed (indexName=IndexNames.INTERACTOR_ID)
    @Taxonomy (rbClass=TaxonomyTypes.INTERACTOR_ID, rbField=BioFields.INTERACTOR_ID)
    private String interactorId;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();

    @Visual
    @Indexed (indexName=IndexNames.DNA_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.DNA_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)  // this is value
    private String shortLabel;

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Indexed (indexName=IndexNames.DNA_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.DNA_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;

    @FullTextIndexed (indexName = IndexNames.DNA_ALIASES)
    @Taxonomy (rbClass=TaxonomyTypes.DNA_ALIAS, rbField=BioFields.ALIASES)
    private String aliases;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed (indexName = IndexNames.DNA_INTACT_SECONDARY_REFS)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_ID, rbField=BioFields.INTACT_SECONDARY_REFS)
    private String intactSecondaryRefs;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed (indexName = IndexNames.DNA_PUBMED_SECONDARY_REFS)
    @Taxonomy (rbClass=TaxonomyTypes.PUBMED_ID, rbField=BioFields.PUBMED_SECONDARY_REFS)
    private String pubmedSecondaryRefs;

    @Indexed (indexName = IndexNames.DNA_ORGANISM_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.ORGANISM_SHORT_LABEL, rbField=BioFields.ORGANISM_SHORT_LABEL)
    private String organismShortLabel;

    @Indexed (indexName = IndexNames.DNA_ORGANISM_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.ORGANISM_FULL_NAME, rbField=BioFields.ORGANISM_FULL_NAME)
    private String organismFullName;

    /**
     * This is the species.
     */
    @Indexed (indexName=IndexNames.INTERACTOR_ID)
    @Taxonomy (rbClass=TaxonomyTypes.NCBI_TAX_ID, rbField=BioFields.NCBI_TAX_ID)
    private String ncbiTaxId;

    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.REFERENCES_PUBMED, elementClass=BioRelation.class)
    private Collection<BioRelation> pubmedRelations;

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.IN_ORGANISM, elementClass=BioRelation.class)
    private BioRelation ncbiTaxonomyRelation;

    /**
     * Name of the intact XML file. Use Uniprot when null.
     * <p>
     * {@link Indexed} {@link IndexNames#INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_ID} {@link BioFields#INTACT_ID}
     *
     * @return  String
     */
    public String getIntactId() {
        return intactId;
    }

    /**
     * Name of the intact XML file. Use Uniprot when null.
     * <p>
     * {@link Indexed} {@link IndexNames#INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_ID} {@link BioFields#INTACT_ID}
     *
     * @param intactId
     */
    public void setIntactId(String intactId) {
        this.intactId = intactId;
    }

    /**
     * When not known, this will be set to the uniprot.
     * <p>
     * {@link Indexed} {@link IndexNames#INTERACTOR_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTERACTOR_ID} {@link BioFields#INTERACTOR_ID}
     *
     * @return  String
     */
    public String getInteractorId() {
        return interactorId;
    }

    /**
     * When not known, this will be set to the uniprot.
     * <p>
     * {@link Indexed} {@link IndexNames#INTERACTOR_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTERACTOR_ID} {@link BioFields#INTERACTOR_ID}
     *
     * @param interactorId
     */
    public void setInteractorId(String interactorId) {
        this.interactorId = interactorId;
    }

    /**
     * {@link Indexed} {@link IndexNames#NODE_TYPE}
     *
     * @return String
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * {@link Indexed} {@link IndexNames#NODE_TYPE}
     *
     * @param nodeType
     */
    public void setNodeType(BioTypes nodeType) {
        this.nodeType = nodeType.toString();
    }

    /**
     * {@link Indexed} {@link IndexNames#DNA_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#DNA_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @param shortLabel
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#DNA_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#DNA_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @return String
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * {@link NonIndexed}
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link NonIndexed}
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * {@link Indexed} {@link IndexNames#DNA_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#DNA_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#DNA_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#DNA_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * A space separated list of aliases.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#DNA_ALIASES}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#DNA_ALIAS} {@link BioFields#ALIASES}
     *
     * @return String
     */
    public String getAliases() {
        return aliases;
    }

    /**
     * A space separated list of aliases.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#DNA_ALIASES}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#DNA_ALIAS} {@link BioFields#ALIASES}
     *
     * @param aliases
     */
    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#DNA_INTACT_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_ID} {@link BioFields#INTACT_SECONDARY_REFS}
     *
     * @return String
     */
    public String getIntactSecondaryRefs() {
        return intactSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#DNA_INTACT_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_ID} {@link BioFields#INTACT_SECONDARY_REFS}
     *
     * @param intactSecondaryRefs
     */
    public void setIntactSecondaryRefs(String intactSecondaryRefs) {
        this.intactSecondaryRefs = intactSecondaryRefs;
    }

    /**
     *
     * @return
     */
    public String getPubmedSecondaryRefs() {
        return pubmedSecondaryRefs;
    }

    /**
     *
     * @param pubmedSecondaryRefs
     */
    public void setPubmedSecondaryRefs(String pubmedSecondaryRefs) {
        this.pubmedSecondaryRefs = pubmedSecondaryRefs;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#NCBI_TAX_ID} {@link BioFields#NCBI_TAX_ID}
     *
     * @return String
     */
    public String getNcbiTaxId() {
        return ncbiTaxId;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#NCBI_TAX_ID} {@link BioFields#NCBI_TAX_ID}
     *
     * @param ncbiTaxId
     */
    public void setNcbiTaxId(String ncbiTaxId) {
        this.ncbiTaxId = ncbiTaxId;
    }

    /**
     * {@link Indexed} {@link IndexNames#DNA_ORGANISM_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_SHORT_LABEL} {@link BioFields#ORGANISM_SHORT_LABEL}
     *
     * @return String
     */
    public String getOrganismShortLabel() {
        return organismShortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#DNA_ORGANISM_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_SHORT_LABEL} {@link BioFields#ORGANISM_SHORT_LABEL}
     *
     * @param organismShortLabel
     */
    public void setOrganismShortLabel(String organismShortLabel) {
        this.organismShortLabel = organismShortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#DNA_ORGANISM_FULL_NAME)}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_FULL_NAME} {@link BioFields#ORGANISM_FULL_NAME}
     *
     * @return String
     */
    public String getOrganismFullName() {
        return organismFullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#DNA_ORGANISM_FULL_NAME)}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_FULL_NAME} {@link BioFields#ORGANISM_FULL_NAME}
     *
     * @param organismFullName
     */
    public void setOrganismFullName(String organismFullName) {
        this.organismFullName = organismFullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + shortLabel + "-" + interactorId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dna dna = (Dna) o;

        if (!id.equals(dna.id)) return false;
        if (!intactId.equals(dna.intactId)) return false;
        if (!interactorId.equals(dna.interactorId)) return false;
        if (!nodeType.equals(dna.nodeType)) return false;
        if (!shortLabel.equals(dna.shortLabel)) return false;
        if (!message.equals(dna.message)) return false;
        if (!fullName.equals(dna.fullName)) return false;
        if (!aliases.equals(dna.aliases)) return false;
        if (!intactSecondaryRefs.equals(dna.intactSecondaryRefs)) return false;
        if (!pubmedSecondaryRefs.equals(dna.pubmedSecondaryRefs)) return false;
        if (!organismShortLabel.equals(dna.organismShortLabel)) return false;
        if (!organismFullName.equals(dna.organismFullName)) return false;
        return ncbiTaxId.equals(dna.ncbiTaxId);

    }

    /**
     * In the following, <code>this<code> Dna object is related to other objects
     * with an outgoing relationship to Pubmed.
     * <p>
     * {@link RelatedTo} {@link Direction}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PUBMED}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PUBMED}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @param pubmed
     */
    public void addPubmedRelation(PubMed pubmed) {
        if (pubmedRelations == null)
            pubmedRelations = new HashSet<>();
        pubmedRelations.add(new BioRelation(this, pubmed, BioRelTypes.REFERENCES_PUBMED));
    }

    public void setNcbiTaxonomyRelation(NcbiTaxonomy ncbiTaxonomy) {
        ncbiTaxonomyRelation = new BioRelation(this, ncbiTaxonomy, BioRelTypes.IN_ORGANISM);
    }
}
