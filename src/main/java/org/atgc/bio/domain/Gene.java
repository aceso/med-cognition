/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to. Currently not supporting NON_INDEXED.
 * Connect this to the disease bioentity
 * A gene is uniquely identified by two part keys ncbiTaxId and geneSymbol.
 * For HUGOGenes, use ncbiTaxId=9606 (Homo Sapiens), as found in collection
 * ncbitaxonomy.
 * 
 * Use an ENUM for human and RAT for ncidisease.
 *
 * @author jtanisha-ee
 */

@UniqueCompoundIndex(indexName=IndexNames.GENE_ID, field1=BioFields.NCBI_TAX_ID_RELATION, field2=BioFields.GENE_SYMBOL, field3=BioFields.NONE)
@BioEntity(bioType = BioTypes.GENE)
public class Gene {

    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

    /**
     * We will use the Symbol field in ncbigene collection as equivalent to
     * hugoGeneSymbol below.
     */
    @PartKey
    @Visual
    @Taxonomy(rbClass=TaxonomyTypes.GENE_SYMBOL, rbField=BioFields.GENE_SYMBOL)
    private String geneSymbol;

    @Indexed(indexName=IndexNames.HGNC_ID)
    @Taxonomy (rbClass=TaxonomyTypes.HGNC_ID, rbField=BioFields.HGNC_ID)
    private String hgncId;

    /**
     * The ncbiGeneId is actually unique in the NCBI world. We just not called
     * it unique here. But it's a very good way to search in Neo4J too, except
     * unless there are genes beyond NCBI genes that are not in the NCBI database.
     */
    @Indexed (indexName=IndexNames.GENE_NCBI_GENE_ID)
    @Taxonomy (rbClass=TaxonomyTypes.NCBI_GENE_ID, rbField=BioFields.NCBI_GENE_ID)
    private String ncbiGeneId;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     *  <GeneAliasCollection>
     * <GeneAlias>agxt-i</GeneAlias>
     *           <GeneAlias>agxt i</GeneAlias>
     *           <GeneAlias>the spat gene</GeneAlias>
     *           <GeneAlias>ph-i</GeneAlias>
     *           <GeneAlias>alanine-glyoxylate aminotransferase</GeneAlias>
     *           <GeneAlias>the spat protein</GeneAlias>
     */
    @FullTextIndexed (indexName = IndexNames.GENE_ALIASES)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_ALIASES, rbField=BioFields.GENE_ALIASES)
    private String geneAliases;

    @Indexed (indexName = IndexNames.GENE_STATUS_FLAG)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_STATUS_FLAG, rbField=BioFields.GENE_STATUS_FLAG)
    private String geneStatusFlag;

    /**
     *  <SequenceIdentificationCollection>
     *           <HgncID>341</HgncID>
     *           <LocusLinkID>189</LocusLinkID>
     *           <GenbankAccession></GenbankAccession>
     *           <RefSeqID>NM_000030</RefSeqID>
     *           <UniProtID>P21549</UniProtID>
     *  </SequenceIdentificationCollection>
     *
     */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IDENTIFIED_IN_SEQUENCE, elementClass=SequenceIdentificationRelation.class)
    private Collection<SequenceIdentificationRelation> sequenceIdentificationRelation;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.GENE.toString();

    @Indexed (indexName=IndexNames.GENE_LOCUS_TAG)
    @Taxonomy (rbClass=TaxonomyTypes.LOCUS_TAG, rbField=BioFields.LOCUS_TAG)
    private String locusTag;

    @FullTextIndexed (indexName = IndexNames.GENE_DESC)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_DESC, rbField=BioFields.DESCRIPTION)
    private String description;

    @Indexed (indexName=IndexNames.GENE_TYPE_OF_GENE)
    @Taxonomy (rbClass=TaxonomyTypes.TYPE_OF_GENE, rbField=BioFields.TYPE_OF_GENE)
    private String typeOfGene;

    @Indexed (indexName=IndexNames.GENE_MOD_DATE)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_MOD_DATE, rbField=BioFields.MOD_DATE)
    private String modDate;

    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.REFERENCES_PUBMED, elementClass = BioRelation.class)
    private Collection<BioRelation> pubmedRelations;

    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.HAS_GENE_TO_GO, elementClass = BioRelation.class)
    private Collection<BioRelation> geneToGoRelations;

    @NonIndexed
    private String relationship;

    @NonIndexed
    private String genomicAccessionVersion;

    @NonIndexed
    private String genomicGi;

    @NonIndexed
    private String startPosition;

    @NonIndexed
    private String distanceToRight;

    @NonIndexed
    private String distanceToLeft;

    @PartRelation (field=BioFields.NCBI_TAX_ID)
    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_TAXONOMY, elementClass = BioRelation.class)
    private BioRelation ncbiTaxIdRelation;

    public NcbiTaxonomy getNcbiTaxonomy() {
        return (NcbiTaxonomy)(ncbiTaxIdRelation.getEndNode());
    }

    public void setNcbiTaxonomy(NcbiTaxonomy ncbiTaxonomy) {
        ncbiTaxIdRelation = new BioRelation(this, ncbiTaxonomy, BioRelTypes.HAS_TAXONOMY);
    }

    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.GENE_ON_LEFT, elementClass = BioRelation.class)
    private HashSet<BioRelation> leftGeneRelations;

    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.GENE_ON_RIGHT, elementClass = BioRelation.class)
    private HashSet<BioRelation> rightGeneRelations;

    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.OVERLAPPING_GENE, elementClass = BioRelation.class)
    private HashSet<BioRelation> overlappingGeneRelations;

    public void setLeftGenes(HashSet<Gene> leftGenes) {
        HashSet<BioRelation> leftRelations = new HashSet<BioRelation>();
        for (Gene gene : leftGenes) {
            leftRelations.add(new BioRelation(this, gene, BioRelTypes.GENE_ON_LEFT));
        }
        this.leftGeneRelations = leftRelations;
    }

    public void setOverlappingGenes(HashSet<Gene> overlappingGenes) {
        HashSet<BioRelation> overlappingRelations = new HashSet<BioRelation>();
        for (Gene gene : overlappingGenes) {
            overlappingRelations.add(new BioRelation(this, gene, BioRelTypes.OVERLAPPING_GENE));
        }
        this.overlappingGeneRelations = overlappingRelations;
    }

    public void setRightGenes(HashSet<Gene> rightGenes) {
        HashSet<BioRelation> rightRelations = new HashSet<BioRelation>();
        for (Gene gene : rightGenes) {
            rightRelations.add(new BioRelation(this, gene, BioRelTypes.GENE_ON_RIGHT));
        }
        this.rightGeneRelations = rightRelations;
    }

    public HashSet<Gene> getLeftGenes() {
        HashSet<Gene> leftGenes = new HashSet<Gene>();
        for (BioRelation bioRelation : leftGeneRelations) {
            leftGenes.add((Gene)bioRelation.getEndNode());
        }
        return leftGenes;
    }

    public HashSet<Gene> getRightGenes() {
        HashSet<Gene> rightGenes = new HashSet<Gene>();
        for (BioRelation bioRelation : rightGeneRelations) {
            rightGenes.add((Gene)bioRelation.getEndNode());
        }
        return rightGenes;
    }

    public HashSet<Gene> getOverlappingGenes() {
        HashSet<Gene> overlappingGenes = new HashSet<Gene>();
        for (BioRelation bioRelation : overlappingGeneRelations) {
            overlappingGenes.add((Gene)bioRelation.getEndNode());
        }
        return overlappingGenes;
    }

    /**
     * This is from the ncbigeneneighbors.
     *
     * @return String
     */
    public String getDistanceToLeft() {
        return distanceToLeft;
    }

    public void setDistanceToLeft(String distanceToLeft) {
        this.distanceToLeft = distanceToLeft;
    }

    /**
     * This is from the ncbigeneneighbors.
     *
     * @return String
     */
    public String getDistanceToRight() {
        return distanceToRight;
    }

    public void setDistanceToRight(String distanceToRight) {
        this.distanceToRight = distanceToRight;
    }

    /**
     * This is from the ncbigeneneighbors.
     *
     * @return String
     */
    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * This is from the ncbigeneneighbors.
     *
     * @return String
     */
    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    /**
     * Orientation of the gene feature on the genomic accession.
     * '?' if not applicable
     *
     * <p>
     * {@linkplain GeneAndTranscriptionOrientation http://sandwalk.blogspot.com/2007/03/gene-and-transcription-orientation.html}
     * </p>
     *
     * This is from the ncbigeneneighbors.
     *
     * @return String
     */
    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    /**
     * This is from the ncbigeneneighbors.
     *
     * @return String
     */
    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    @NonIndexed
    private String endPosition;

    @NonIndexed
    private String orientation;

    @NonIndexed
    private String chromosome;

    /**
     * Why are there two types of sequence identification numbers (GI and VERSION),
     * and what is the difference between them? This is from the ncbigeneneighbors.
     *
     * <p>
     * The two types of sequence identification numbers, GI and VERSION, have
     * different formats and were implemented at different points in time.
     * </p>
     *
     * <DL>
     *
     * <LI>
     * GI number (sometimes written in lower case, "gi") is simply a series of
     * digits that are assigned consecutively to each sequence record processed
     * by NCBI. The GI number bears no resemblance to the Accession number of
     * the sequence record.
     * </LI>
     *
     * <LI>
     * VERSION is made of the accession number of the database record followed
     * by a dot and a version number (and is therefore sometimes referred to as
     * the "accession.version")
     * </LI>
     *
     * </DL>
     *
     * <P>
     * The GI number has been used for many years by NCBI to track sequence
     * histories in GenBank and the other sequence databases it maintains.
     * The VERSION system of identifiers was adopted in February 1999 by the
     * International Nucleotide Sequence Database Collaboration (GenBank,
     * EMBL, and DDBJ). More details are given in the historical note, below.
     * </p>
     *
     * <p>
     * The two systems of identifiers run in parallel to each other. That is,
     * when any change is made to a sequence, it receives a new GI number AND an
     * increase to its version number.
     * </p>
     *
     * <p>
     * A Sequence Revision History tool is available to track the various gi
     * numbers, version numbers, and update dates for sequences that appeared
     * in a specific GenBank record (more information and example).
     * </p>
     *
     * <p>
     * The first type of sequence identification number was GI, which stands for
     * "GenInfo Identifier." GenInfo was an early system used to access GenBank
     * and related databases. A GI number was assigned to each nucleotide and
     * protein sequence accessible through the NCBI search systems, and was a
     * means of tracking changes to the sequence. However, GI numbers were not
     * used uniformly across the collaborating databases (GenBank, EMBL, DDBJ).
     * They instead served as an internal tracking system for the databases that
     * chose to implement them. In addition, the gi number for a nucleotide
     * sequence originally appeared in the "Comment" field of a record. There was
     * no separate field for sequence identification numbers.
     * </p>
     *
     * <p>
     * When the collaborating databases began to formalize use of sequence
     * identifiers, they created a new, separate field called NID (nucleotide
     * identifier) in the database record, which contained the GI number of the
     * nucleotide sequence. Similarly, the GI number for each protein sequence
     * was named PID, and placed above each amino acid translation in the field:
     * FEATURES/CDS/db_xref="PID:gNNNNNN". Hence, there became two types of gi
     * numbers: NID and PID. In December 1999, the use of the abbreviations "NID"
     * and "PID" was discontinued. Both are now just shown as "GI".
     * </p>
     *
     * <p>
     * In February 1999, GenBank/EMBL/DDBJ implemented a new "accession.version"
     * system of sequence identifiers that runs parallel to the gi number system.
     * (See section 1.3.2 of the GenBank 111.0 release notes for details.)
     * </p>
     *
     * <p>
     * Unlike the gi number system, in which sequence identification numbers were
     * not necessarily consistent across the databases (e.g., GenBank and EMBL
     * could each assign their own gi number to a sequence), the new system is
     * designed to ensure consistency. It is also designed to show a relationship
     * between a sequence identification number and the accession number of the
     * record in which it is found. In contrast, GI numbers are assigned
     * consecutively and bear no resemblance to the accession number. Finally, the
     * new system allows the assignment of alphanumeric protein IDs to proteins
     * translations within nucleotide sequence records. The protein IDs contain
     * three letters followed by five digits, a period, and a version number.
     * </p>
     *
     * <p>
     * As of December 1999 (GenBank release 115.0): the NID field and
     * /db_xref="PID:xxxxxxx" qualifer have been removed, and both are now simply
     * shown as "GI" numbers the VERSION field of nucleotide records will continue
     * to contain both an accession.version and a GI number for the nucleotide
     * sequence each amino acid translation will continue to be labeled with
     * an accession.version sequence identifier (in the "/protein_id" field) and
     * a GI number (in the "/db_xref=GI:xxxxxxx" qualifier), under the CDS feature
     * of a GenBank record the accession.version and GI systems of sequence
     * identifiers will run in parellel to each other. Therefore, when any
     * change is made to a sequence, it receives a new GI number AND an increase
     * to its version number.
     * </p>
     *
     * @return String
     */
    public String getGenomicAccessionVersion() {
        return genomicAccessionVersion;
    }

    public void setGenomicAccessionVersion(String genomicAccessionVersion) {
        this.genomicAccessionVersion = genomicAccessionVersion;
    }

    /**
     * Why are there two types of sequence identification numbers (GI and VERSION),
     * and what is the difference between them? This is from the ncbigeneneighbors.
     *
     * <p>
     * The two types of sequence identification numbers, GI and VERSION, have
     * different formats and were implemented at different points in time.
     * </p>
     *
     * <DL>
     *
     * <LI>
     * GI number (sometimes written in lower case, "gi") is simply a series of
     * digits that are assigned consecutively to each sequence record processed
     * by NCBI. The GI number bears no resemblance to the Accession number of
     * the sequence record.
     * </LI>
     *
     * <LI>
     * VERSION is made of the accession number of the database record followed
     * by a dot and a version number (and is therefore sometimes referred to as
     * the "accession.version")
     * </LI>
     *
     * </DL>
     *
     * <P>
     * The GI number has been used for many years by NCBI to track sequence
     * histories in GenBank and the other sequence databases it maintains.
     * The VERSION system of identifiers was adopted in February 1999 by the
     * International Nucleotide Sequence Database Collaboration (GenBank,
     * EMBL, and DDBJ). More details are given in the historical note, below.
     * </p>
     *
     * <p>
     * The two systems of identifiers run in parallel to each other. That is,
     * when any change is made to a sequence, it receives a new GI number AND an
     * increase to its version number.
     * </p>
     *
     * <p>
     * A Sequence Revision History tool is available to track the various gi
     * numbers, version numbers, and update dates for sequences that appeared
     * in a specific GenBank record (more information and example).
     * </p>
     *
     * <p>
     * The first type of sequence identification number was GI, which stands for
     * "GenInfo Identifier." GenInfo was an early system used to access GenBank
     * and related databases. A GI number was assigned to each nucleotide and
     * protein sequence accessible through the NCBI search systems, and was a
     * means of tracking changes to the sequence. However, GI numbers were not
     * used uniformly across the collaborating databases (GenBank, EMBL, DDBJ).
     * They instead served as an internal tracking system for the databases that
     * chose to implement them. In addition, the gi number for a nucleotide
     * sequence originally appeared in the "Comment" field of a record. There was
     * no separate field for sequence identification numbers.
     * </p>
     *
     * <p>
     * When the collaborating databases began to formalize use of sequence
     * identifiers, they created a new, separate field called NID (nucleotide
     * identifier) in the database record, which contained the GI number of the
     * nucleotide sequence. Similarly, the GI number for each protein sequence
     * was named PID, and placed above each amino acid translation in the field:
     * FEATURES/CDS/db_xref="PID:gNNNNNN". Hence, there became two types of gi
     * numbers: NID and PID. In December 1999, the use of the abbreviations "NID"
     * and "PID" was discontinued. Both are now just shown as "GI".
     * </p>
     *
     * <p>
     * In February 1999, GenBank/EMBL/DDBJ implemented a new "accession.version"
     * system of sequence identifiers that runs parallel to the gi number system.
     * (See section 1.3.2 of the GenBank 111.0 release notes for details.)
     * </p>
     *
     * <p>
     * Unlike the gi number system, in which sequence identification numbers were
     * not necessarily consistent across the databases (e.g., GenBank and EMBL
     * could each assign their own gi number to a sequence), the new system is
     * designed to ensure consistency. It is also designed to show a relationship
     * between a sequence identification number and the accession number of the
     * record in which it is found. In contrast, GI numbers are assigned
     * consecutively and bear no resemblance to the accession number. Finally, the
     * new system allows the assignment of alphanumeric protein IDs to proteins
     * translations within nucleotide sequence records. The protein IDs contain
     * three letters followed by five digits, a period, and a version number.
     * </p>
     *
     * <p>
     * As of December 1999 (GenBank release 115.0): the NID field and
     * /db_xref="PID:xxxxxxx" qualifer have been removed, and both are now simply
     * shown as "GI" numbers the VERSION field of nucleotide records will continue
     * to contain both an accession.version and a GI number for the nucleotide
     * sequence each amino acid translation will continue to be labeled with
     * an accession.version sequence identifier (in the "/protein_id" field) and
     * a GI number (in the "/db_xref=GI:xxxxxxx" qualifier), under the CDS feature
     * of a GenBank record the accession.version and GI systems of sequence
     * identifiers will run in parellel to each other. Therefore, when any
     * change is made to a sequence, it receives a new GI number AND an increase
     * to its version number.
     * </p>
     *
     * <p>
     * A possible related note, this needs to be verified.
     * </p>
     *
     * <p>
     * A genomic island (GI) is part of a genome that has evidence of horizontal
     * origins. [1] The term is usually used in microbiology, especially with
     * regard to bacteria. A GI can code for many functions, can be involved in
     * symbiosis or pathogenesis, and may help an organism's adaptation. Many
     * sub-classes of GIs exist that are based on the function that they confer.
     * For example a GI associated with pathogenesis is often called a pathogenicity
     * island (PAIs), while GIs that contain many antibiotic resistant genes are
     * referred to as antibiotic resistance islands. The same GI can occur in
     * distantly related species as a result of various types of lateral gene
     * transfer (transformation, conjugation, transduction). This can be
     * determined by base composition analysis, as well as phylogeny estimations.
     * </p>
     *
     * <p>
     * In bacteria, many type 3 secretion systems and type 4 secretion systems
     * are located on regions of DNA called genomic islands. These "islands"
     * are characterised by their large size(>10 Kb), their frequent association
     * with tRNA-encoding genes and a different G+C content compared with the
     * rest of the genome. Many genomic islands are flanked by repeat structures
     * and carry fragments of other mobile elements such as phages and plasmids.
     * Some genomic islands can excise themselves spontaneously from the
     * chromosome and can be transferred to other suitable recipients.
     * </p>
     *
     * <p>
     * Various genomic island predictions programs have been developed. These tools
     * can be broadly grouped into sequence based methods and comparative
     * genomics/phylogeny based methods. Sequence based methods depend on the
     * naturally occurring variation that exists between the genome sequence
     * composition of different species. Genomic regions that show abnormal
     * sequence composition (such as nucleotide bias or codon bias) suggests
     * that these regions may have been horizontally transferred. Two major
     * problems with these methods are that false predictions can occur due to
     * natural variation in the genome (sometimes due to highly expressed genes)
     * and that horizontally transferred DNA will ameliorate (change to the host
     * genome) over time; therefore, limiting predictions to only recently acquired GIs.
     * </p>
     *
     * <p>
     * Comparative genomics based methods try to identify regions that show signs
     * that they have been horizontally transferred using information from several
     * related species. For example, a genomic region that is present in one
     * species, but is not present in several other related species suggests
     * that the region may have been horizontally transferred. The alternative
     * explanations are (i) that the region was present in the common ancestor
     * but has been lost in all the other species being compared, or (ii) that
     * the region was absent in the common ancestor but was acquired through
     * mutation and selection in the species in which it is still found. The
     * argument for multiple deletions of the region would be strengthened if
     * there is evidence from outgroups that the region was present in the
     * common ancestor, or if the phylogeny implies relatively few actual
     * deletion events would be required. The argument for acquisition via
     * mutation would be strengthened if the species with the region is known
     * to have diverged substantially from the other species, or if the region
     * in question is small. The plausibility of either (i) or (ii) would be
     * modified if taxon sampling omitted many extinct species that may have
     * possessed the region, and particularly if extinction was correlated
     * with the presence of the region.
     * </p>
     *
     * <p>
     * One example of a method that integrates several of the most accurate
     * GI prediction methods is IslandViewer.
     * </p>
     *
     * @return String
     */
    public String getGenomicGi() {
        return genomicGi;
    }

    public void setGenomicGi(String genomicGi) {
        this.genomicGi = genomicGi;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getOtherTaxTd() {
        return otherTaxTd;
    }

    public void setOtherTaxTd(String otherTaxTd) {
        this.otherTaxTd = otherTaxTd;
    }

    public String getOtherGeneId() {
        return otherGeneId;
    }

    public void setOtherGeneId(String otherGeneId) {
        this.otherGeneId = otherGeneId;
    }

    @NonIndexed
    private String otherTaxTd;

    @NonIndexed
    private String otherGeneId;

    public Collection<BioRelation> getGeneToGoRelations() {
        return geneToGoRelations;
    }

    public void setGeneToGoList(HashSet<GeneToGo> geneToGoList) {

    }

    public Collection<BioRelation> getPubmedRelations() {
        return pubmedRelations;
    }

    /**
     * Take a list of pubmed Ids.
     *
     * @param pubmedList
     */
    public void setPubmedList(List<PubMed> pubmedList) {
        pubmedRelations = new HashSet<BioRelation>();
        for (PubMed pubmed : pubmedList) {
            BioRelation bioRelation = new BioRelation();
            bioRelation.setStartNode(this);
            bioRelation.setEndNode(pubmed);
            bioRelation.setRelType(BioRelTypes.REFERENCED_BY_PUBMED);
            bioRelation.setMessage(BioRelTypes.REFERENCED_BY_PUBMED.toString());
            pubmedRelations.add(bioRelation);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNcbiGeneId() {
        return ncbiGeneId;
    }

    public void setNcbiGeneId(String ncbiGeneId) {
        this.ncbiGeneId = ncbiGeneId;
    }

    /*
    public void setSequenceIdentificationRelation(Collection<SequenceIdentificationRelation> sequenceIdentificationRelation) {
        this.sequenceIdentificationRelation = sequenceIdentificationRelation;
    } */

    public String getLocusTag() {
        return locusTag;
    }

    public void setLocusTag(String locusTag) {
        this.locusTag = locusTag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeOfGene() {
        return typeOfGene;
    }

    public void setTypeOfGene(String typeOfGene) {
        this.typeOfGene = typeOfGene;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public void setOrganismComponent(Collection<BioRelation> organismComponent) {
        this.organismComponent = organismComponent;
    }

    /* organism */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_OBSERVED_IN, elementClass = BioRelation.class)
    private Collection<BioRelation> organismComponent = new HashSet<BioRelation>();

    @NodeLabel
    @Indexed (indexName=IndexNames.GENE_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Indexed (indexName=IndexNames.GENE_NAME_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_NAME_LABEL, rbField=BioFields.GENE_NAME_LABEL)  // this is value
    private String geneNameLabel;

    @NonIndexed
    private String otherDesignations;

    public String getOtherDesignations() {
        return otherDesignations;
    }

    public void setOtherDesignations(String otherDesignations) {
        this.otherDesignations = otherDesignations;
    }

    public String getHgncId() {
        return this.hgncId;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    /**
     *
     * @param geneSymbol
     * @deprecated
     */
    public void setHugoGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    /**
     *
     * @return String
     * @deprecated
     */
    public String getHugoGeneSymbol() {
        return this.geneSymbol;
    }

    public void setHgncId(String hgncId) {
        this.hgncId = hgncId;
    }

    public String getNodeType() {
        return this.nodeType;
    }

    public String geneStatusFlag() {
        return this.geneStatusFlag;
    }

    public void setGeneStatusFlag(String flag) {
        this.geneStatusFlag = flag;
    }

    public Iterable<BioRelation> getOrganismComponent() {
        log.info("length = " + organismComponent.size());
        return organismComponent;
        //return IteratorUtil.asCollection(proteinInteractions);
    }

    public void setOrganismComponent(Organism organism) {
        final BioRelation organismRelation = new BioRelation(this, organism, BioRelTypes.IS_OBSERVED_IN);
       // log.info("organism.shortLabel =" + organism.getShortLabel());
        organismRelation.setName(organism.getShortLabel());
        this.organismComponent.add(organismRelation);
       // log.info("organismComponent size = " + organismComponent.size());
    }


    public Iterable<SequenceIdentificationRelation> getSequenceIdentificationRelation() {
        return sequenceIdentificationRelation;
    }

    public void setSequenceIdentificationRelation(Protein protein, String hgncId, String locusLinkId,
            String genBankAccession, String refSeqId) {
        
        if (sequenceIdentificationRelation == null)  { 
            sequenceIdentificationRelation = new HashSet<SequenceIdentificationRelation>();
        }
        final SequenceIdentificationRelation seqRelation = new SequenceIdentificationRelation(
                hgncId, locusLinkId,  genBankAccession, refSeqId, this, protein, BioRelTypes.IDENTIFIED_IN_SEQUENCE);
        seqRelation.setMessage(BioRelTypes.IDENTIFIED_IN_SEQUENCE.toString());
        this.sequenceIdentificationRelation.add(seqRelation);
    }

    public void setGeneAliases(String aliases) {
        this.geneAliases = aliases;
    }

    public String getGeneAliases() {
        return geneAliases;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String getMessage() {
        return this.message;
    }

    public String getGeneNameLabel() {
        return this.geneNameLabel;
    }

    public void setGeneNameLabel(String s) {
        this.geneNameLabel = s;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "Gene{" + "geneSymbol=" + geneSymbol + ", hgncId=" + hgncId + ", ncbiGeneId=" + ncbiGeneId + ", geneAliases=" + geneAliases + ", geneStatusFlag=" + geneStatusFlag + ", nodeType=" + nodeType + ", locusTag=" + locusTag + ", description=" + description + ", typeOfGene=" + typeOfGene + ", modDate=" + modDate + ", relationship=" + relationship + ", genomicAccessionVersion=" + genomicAccessionVersion + ", genomicGi=" + genomicGi + ", startPosition=" + startPosition + ", distanceToRight=" + distanceToRight + ", distanceToLeft=" + distanceToLeft + ", endPosition=" + endPosition + ", orientation=" + orientation + ", chromosome=" + chromosome + ", otherTaxTd=" + otherTaxTd + ", otherGeneId=" + otherGeneId + ", message=" + message + ", geneNameLabel=" + geneNameLabel + ", otherDesignations=" + otherDesignations + '}';
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Gene other = (Gene) obj;
        if ((this.hgncId == null) ? (other.hgncId != null) : !this.hgncId.equals(other.hgncId)) {
            return false;
        }
        return !((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType));
    }


}
