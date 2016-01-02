/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.FullTextIndexed;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.meta.NodeLabel;
import org.atgc.bio.meta.NonIndexed;
import org.atgc.bio.meta.RelatedTo;
import org.atgc.bio.meta.RelatedToVia;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.repository.TemplateUtils;
import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.Direction;

/**
 * In Protein Data Bank (PDB), information is represented using structures.
 * Structures are larger than molecules, and proteins. Each structure typically
 * has multiple chains, and each chain has multiple molecules or proteins on it.
 * The offsets of these proteins or molecules on the chain are mentioned in the
 * pdbuniprot collection. Each structure is identified by a @structureId.
 *
 * PDB Docs {@linkplain http://www.wwpdb.org/docs.html}
 *
 * {@linkplain http://www.wwpdb.org/documentation/format3.0.1-dif.pdf}
 *
 * PDB file format: 2012
 * {@linkplain  ftp://ftp.wwpdb.org/pub/pdb/doc/format_descriptions/Format_v33_Letter.pdf}
 * {@linkplain ftp://ftp.wwpdb.org/pub/pdb/doc/format_descriptions/Format_v33_A4.pdf}
 *
 * An old document: {@linkplain http://deposit.rcsb.org/format-faq-v1.html}
 *
 * <p>
 * The following is a record from the "pdb" collection.
 * </p>
 *
 * > db.pdb.find({"@structureId" : "3CDQ"}).pretty() { "_id" :
 * ObjectId("519d5849036436ad402c866a"), "@structureId" : "3CDQ",
 * "@bioAssemblies" : "1", "@release_date" : "Tue Feb 17 00:00:00 PST 2009",
 * "@resolution" : "1.68", "Method" : { "@name" : "xray" }, "Entity" : { "@id" :
 * "1", "@entityType" : "protein", "Chain" : { "@id" : "A" } } }
 *
 * <p>
 * The following is a record from the "pdbdesc" collection.
 * </p>
 *
 * <pre>
 * > db.pdbdesc.find({"@structureId" : "3CDQ"}).pretty() { "_id" :
 * ObjectId("519d58e5036436ad402d02f7"), "@structureId" : "3CDQ", "@title" :
 * "Contributions of all 20 amino acids at site 96 to the stability and
 * structure of T4 lysozyme", "@pubmedId" : "19384988", "@pubmedCentralId" :
 * "PMC2771291", "@expMethod" : "X-RAY DIFFRACTION", "@resolution" : "1.68",
 * "@keywords" : "HYDROLASE", "@nr_entities" : "4", "@nr_residues" : "164",
 * "@nr_atoms" : "1418", "@deposition_date" : "2008-02-27", "@release_date" :
 * "2009-02-17", "@last_modification_date" : "2009-05-05", "@structure_authors"
 * : "Mooers, B.H.M., Matthews, B.W.", "@citation_authors" : "Mooers, B.H.,
 * Baase, W.A., Wray, J.W., Matthews, B.W.", "@status" : "CURRENT" }
 * </pre>
 *
 * Let's look at an example below. The following shows the @nr_entities=3.
 *
 * <pre>
 * db.pdbdesc.find({"@structureId" : "3BB2"}).pretty()
 * {
	"_id" : ObjectId("519d5b86036436ad402d0b62"),
	"@structureId" : "3BB2",
	"@title" : "Crystal structure of L26D/D28N mutant of Human acidic fibroblast growth factor",
	"@pubmedId" : "18308335",
	"@expMethod" : "X-RAY DIFFRACTION",
	"@resolution" : "1.50",
	"@keywords" : "HORMONE",
	"@nr_entities" : "3",
	"@nr_residues" : "292",
	"@nr_atoms" : "2508",
	"@deposition_date" : "2007-11-09",
	"@release_date" : "2008-04-15",
	"@last_modification_date" : "2009-02-24",
	"@structure_authors" : "Lee, J., Blaber, M.",
	"@citation_authors" : "Lee, J., Dubey, V.K., Longo, L.M., Blaber, M.",
	"@status" : "CURRENT"
}

 * </pre>
 *
 * The pdbentity must show 3 entities, if not we only populate the ones we find, as this
 * may be a problem with pdb database or rest api.
 *
 * <pre>
 * db.pdbentity.find({"@structureId" : "3BB2"}).pretty()
{
	"_id" : ObjectId("51a104070364f6a2a359fa73"),
	"@structureId" : "3BB2",
	"@bioAssemblies" : "2",
	"@release_date" : "Tue Apr 15 00:00:00 PDT 2008",
	"@resolution" : "1.50",
	"Method" : {
		"@name" : "xray"
	},
	"Entity" : {
		"@id" : "1",
		"@entityType" : "protein",
		"Chain" : [
			{
				"@id" : "A"
			},
			{
				"@id" : "B"
			}
		]
	}
}

 * </pre>
 *
 * Also in pdbuniprot, we look for 3 entities.
 *
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.STRUCTURE)
public class Structure {

    /**
     *
     */
    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

    /**
     * The featureRange does not have a unique id of it's own. So we associate
     * that with a uniqueId such as featureId. Eg. 212408
     */
    @UniquelyIndexed(indexName = IndexNames.STRUCTURE_ID)
    @Taxonomy(rbClass = TaxonomyTypes.STRUCTURE_ID, rbField = BioFields.STRUCTURE_ID)
    private String structureId;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private final String nodeType = TemplateUtils.extractBioType(this).toString();

    @NonIndexed
    private String bioAssemblies;

    @NonIndexed
    private String releaseDate;

    @NonIndexed
    private String resolution;

    @Indexed(indexName = IndexNames.STRUCTURE_METHOD)
    @Taxonomy(rbClass = TaxonomyTypes.STRUCTURE_METHOD, rbField = BioFields.METHOD)
    private String method;

    @RelatedToVia(
            direction = Direction.OUTGOING,
            relType = BioRelTypes.HAS_CHAIN,
            elementClass = BioRelation.class)
    private HashSet<BioRelation> chainRelation;

    @NodeLabel
    @Indexed(indexName = IndexNames.STRUCTURE_MESSAGE)
    @Taxonomy(rbClass = TaxonomyTypes.STRUCTURE_MESSAGE, rbField = BioFields.MESSAGE)
    private String message;

    /**
     * The title comes from "pdbdesc" and as an example is: "@title" :
     * "Contributions of all 20 amino acids at site 96 to the stability and
     * structure of T4 lysozyme",
     *
     */
    @NonIndexed
    private String title;

    @RelatedTo(direction = Direction.OUTGOING,
               relType = BioRelTypes.REFERENCES_PUBMED,
               elementClass = BioRelation.class)
    private BioRelation pubmedRelation;

    /**
     * All articles added to PubMed Central are assigned a unique identifier a
     * PMCID that will need to be submitted to the NIH. Due to similar
     * terminology, there is confusion over where to find the correct number.
     * When looking at citations in PubMed, when displaying results in the
     * Summary format, you see a PMID number, a unique identifier assigned to
     * each article as it is added to PubMed. This is NOT the same number
     * assigned to articles in PubMed Central, and will not meet the NIH
     * guidelines. For eg. PMC2771291
     */
    @Indexed(indexName = IndexNames.STRUCTURE_PMCID)
    @Taxonomy(rbClass = TaxonomyTypes.PUBMED_CENTRAL_ID, rbField = BioFields.PUBMED_CENTRAL_ID)
    private String pubmedCentralId;

    @Indexed(indexName = IndexNames.STRUCTURE_PUBMED_ID)
    @Taxonomy(rbClass = TaxonomyTypes.STRUCTURE_PUBMED_ID, rbField = BioFields.PUBMED_ID)
    private String pubmedId;

    /**
     * The experimental method used. For eg. "X-RAY DIFFRACTION"
     *
     */
    @Indexed(indexName = IndexNames.STRUCTURE_EXP_METHOD)
    @Taxonomy(rbClass = TaxonomyTypes.STRUCTURE_EXP_METHOD, rbField = BioFields.EXP_METHOD)
    private String expMethod;

    @RelatedToVia(
            direction = Direction.OUTGOING,
            relType = BioRelTypes.HAS_LIGAND,
            elementClass = BioRelation.class)
    private HashSet<BioRelation> ligandRelations;

    /**
     *
     * @param ligands
     */
    public void setLigands(HashSet<Ligand> ligands) {
        if (ligandRelations == null) {
            ligandRelations = new HashSet<BioRelation>();
        }
        for (Ligand ligand : ligands) {
            ligandRelations.add(new BioRelation(this, ligand, BioRelTypes.HAS_LIGAND));
        }
    }

    /**
     *
     * @return HashSet<Ligand> null if there are no relations
     */
    public HashSet<Ligand> getLigands() {
        if (ligandRelations == null) {
            return null;
        }
        HashSet<Ligand> ligands = new HashSet<Ligand>();
        for (BioRelation bioRelation : ligandRelations) {
            ligands.add((Ligand) bioRelation.getEndNode());
        }
        return ligands;
    }

    /**
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
     * @return String
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     *
     * @param releaseDate
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Resolution is a measure of the quality of the data that has been
     * collected on the crystal containing the protein or nucleic acid. If all
     * of the proteins in the crystal are aligned in an identical way, forming a
     * very perfect crystal, then all of the proteins will scatter X-rays the
     * same way, and the diffraction pattern will show the fine details of
     * crystal. On the other hand, if the proteins in the crystal are all
     * slightly different, due to local flexibility or motion, the diffraction
     * pattern will not contain as much fine information. So resolution is a
     * measure of the level of detail present in the diffraction pattern and the
     * level of detail that will be seen when the electron density map is
     * calculated. High-resolution structures, with resolution values of 1 ?? or
     * so, are highly ordered and it is easy to see every atom in the electron
     * density map. Lower resolution structures, with resolution of 3 ?? or
     * higher, show only the basic contours of the protein chain, and the atomic
     * structure must be inferred. Most crystallographic-defined structures of
     * proteins fall in between these two extremes. As a general rule of thumb,
     * we have more confidence in the location of atoms in structures with
     * resolution values that are small, called "high-resolution structures".
     *
     * @return String
     */
    public String getResolution() {
        return resolution;
    }

    /**
     *
     * @param resolution
     */
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    /**
     *
     * @return String
     */
    public String getMethod() {
        return method;
    }

    /**
     *
     * @param method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return String
     */
    public String getPubmedCentralId() {
        return pubmedCentralId;
    }

    /**
     *
     * @param pubmedCentralId
     */
    public void setPubmedCentralId(String pubmedCentralId) {
        this.pubmedCentralId = pubmedCentralId;
    }

    /**
     *
     * @return String
     */
    public String getPubmedId() {
        return pubmedId;
    }

    /**
     *
     * @param pubmedId
     */
    public void setPubmedId(String pubmedId) {
        this.pubmedId = pubmedId;
    }

    /**
     *
     * @return String
     */
    public String getExpMethod() {
        return expMethod;
    }

    /**
     *
     * @param expMethod
     */
    public void setExpMethod(String expMethod) {
        this.expMethod = expMethod;
    }

    @Indexed(indexName = IndexNames.STRUCTURE_KEYWORDS)
    @Taxonomy(rbClass = TaxonomyTypes.STRUCTURE_KEYWORDS, rbField = BioFields.KEYWORDS)
    private String keywords;

    /**
     * Eg. HYDROLASE
     *
     * @return String
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     *
     * @param keywords
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @NonIndexed
    private String nrEntities;

    /**
     * Most likely non-redundant entities. Entities are the distinct chemical
     * components of structures in the PDB. Unlike chains, entities do not
     * include duplicate copies. In other words, each entity in a structure is
     * different from every other entity in the structure. 4HHB contains two
     * protein entities: hemoglobin alpha chain and hemoglobin beta chain. For
     * eg. 4
     *
     * @return String
     */
    public String getNrEntities() {
        return nrEntities;
    }

    @NonIndexed
    private String nrResidues;

    /**
     * A residue is a single molecular unit within a polymer. Residue is thus
     * another term for monomer. Although the term residue is most often used to
     * refer to a specific amino acid within a polypeptide , it is also used to
     * refer to sugars within a carbohydrate molecule and nucleotides within
     * deoxyribonucleic acid ( DNA ) or ribonucleic acid (RNA). A protein or a
     * polypeptide is composed of amino acids linked together by peptide bonds,
     * with amino acids as the monomeric units of the polypeptide. The order of
     * amino acids in a protein is known as the primary structure of that
     * protein. The specific sequence of amino acids in the protein determines
     * its three-dimensional structure and ultimately its function. The amino
     * acids are numbered sequentially, beginning at the amino terminus of the
     * polypeptide. For example, the 45th amino acid in the sequence would be
     * identified as residue 45. Most often, scientists refer to an individual
     * residue using both the name of the amino acid and its position.
     * Therefore, if residue 45 in a particular polypeptide sequence is serine,
     * that residue would be referred to as serine-45.
     *
     * <p>
     * Read more:
     * http://www.chemistryexplained.com/Pr-Ro/Residue.html#ixzz2iKaamKss
     *
     * In chemistry, residue is the material remaining after distillation or an
     * evaporation, or to a portion of a larger molecule, such as a methyl
     * group. It may also refer to the undesired byproducts of a reaction.
     * Residue is very important in the world of chemistry. Residues of
     * distillation are constantly analyzed for quantitative measures. In
     * biochemistry and molecular biology, a residue refers to a specific
     * monomer within the polymeric chain of a polysaccharide, protein or
     * nucleic acid. For example, one might say, "This protein consists of 118
     * amino acid residues" or "The histidine residue is considered to be basic
     * due to its imidazole ring." An alpha-helix is a right-handed coil of
     * amino-acid residues on a polypeptide chain, typically ranging between 4
     * and 40 residues.
     * </p>
     *
     * @return String
     */
    public String getNrResidues() {
        return nrResidues;
    }

    /**
     *
     * @param nrResidues
     */
    public void setNrResidues(String nrResidues) {
        this.nrResidues = nrResidues;
    }

    /**
     *
     * @param nrEntities
     */
    public void setNrEntities(String nrEntities) {
        this.nrEntities = nrEntities;
    }

    @NonIndexed
    private String nrAtoms;

    /**
     * Eg. 1418
     *
     * @return String
     */
    public String getNrAtoms() {
        return nrAtoms;
    }

    /**
     *
     * @param nrAtoms
     */
    public void setNrAtoms(String nrAtoms) {
        this.nrAtoms = nrAtoms;
    }

    /**
     *
     * @return String
     */
    public String getDepositionDate() {
        return depositionDate;
    }

    /**
     *
     * @param depositionDate
     */
    public void setDepositionDate(String depositionDate) {
        this.depositionDate = depositionDate;
    }

    /**
     * Eg. "2008-02-27"
     */
    @NonIndexed
    private String depositionDate;

    private String lastModificationDate;

    /**
     * Eg. "Mooers, B.H.M., Matthews, B.W."
     */
    @RelatedToVia(direction = Direction.BOTH, elementClass = BioRelation.class)
    private HashSet<BioRelation> structureAuthorRelations;

    /**
     * Eg. Mooers, B.H., Baase, W.A., Wray, J.W., Matthews, B.W.
     */
    @RelatedToVia(direction = Direction.BOTH, elementClass = BioRelation.class)
    private HashSet<BioRelation> citationAuthorRelations;

    /**
     * This is for situations where the author list in <code>structureAuthorRelations</code>
     * is not parseable. We are dealing with the following kinds of monstrosities.
     * <pre>
      	"@structure_authors" : "Rangarajan, E.S., Bhatia, S., Watson, D.C., Munger, C., Cygler, M., Matte, A., Young, N.M., Montreal-Kingston Bacterial Structural Genomics Initiative (BSGI)",
	"@citation_authors" : "Rangarajan, E.S., Bhatia, S., Watson, D.C., Munger, C., Cygler, M., Matte, A., Young, N.M.",
       </pre>
       * Notice the last component in structureAuthors above "Montreal-Kingston Bacterial Structural Genomics Initiative (BSGI)".
       * This is not even an author name. So we just dump the entire list into FullText index when we are not
       * able to parse out the author names.
     */
    @FullTextIndexed (indexName = IndexNames.AUTHOR_LIST)
    private String structureAuthors;

    /**
     * This is for situations where the author list in <code>structureAuthorRelations</code>
     * is not parseable. We are dealing with the following kinds of monstrosities.
     * <pre>
      	"@structure_authors" : "Rangarajan, E.S., Bhatia, S., Watson, D.C., Munger, C., Cygler, M., Matte, A., Young, N.M., Montreal-Kingston Bacterial Structural Genomics Initiative (BSGI)",
	"@citation_authors" : "Rangarajan, E.S., Bhatia, S., Watson, D.C., Munger, C., Cygler, M., Matte, A., Young, N.M.",
       </pre>
       * Notice the last component in structureAuthors above "Montreal-Kingston Bacterial Structural Genomics Initiative (BSGI)".
       * This is not even an author name. So we just dump the entire list into FullText index when we are not
       * able to parse out the author names.
     */
    @FullTextIndexed (indexName = IndexNames.AUTHOR_LIST)
    private String citationAuthors;

    @RelatedToVia(direction = Direction.BOTH, elementClass = BioRelation.class)
    private HashSet<BioRelation> chainRelations;

    /**
     * Fetch the structure authors list. Both the structure and citation authors are
     * related as <code>BioRelTypes.HAS_AUTHOR</code> to Structure.
     *
     * @return HashSet<Author> null if there are no authors.
     */
    public HashSet<Author> getStructureAuthorSet() {
        if (structureAuthorRelations == null) {
            return null;
        }
        HashSet<Author> authors = new HashSet<Author>();
        for (BioRelation bioRelation : structureAuthorRelations) {
            authors.add((Author)bioRelation.getEndNode());
        }
        return authors;
    }

    /**
     * Set the structure authors. Both the structure and citation authors are
     * related as <code>BioRelTypes.HAS_AUTHOR</code> to Structure.
     *
     * @param authors
     */
    public void setStructureAuthorSet(HashSet<Author> authors) {
        structureAuthorRelations = new HashSet<BioRelation>();
        for (Author author : authors) {
            structureAuthorRelations.add(new BioRelation(this, author, BioRelTypes.HAS_AUTHOR));
        }
    }

    /**
     * Fetch the citation authors list. Both the structure and citation authors are
     * related as <code>BioRelTypes.HAS_AUTHOR</code> to Structure.
     *
     * @return HashSet<Author> null if there are no authors.
     */
    public HashSet<Author> getCitationAuthorSet() {
        if (citationAuthorRelations == null) {
            return null;
        }
        HashSet<Author> authors = new HashSet<Author>();
        for (BioRelation bioRelation : citationAuthorRelations) {
            authors.add((Author)bioRelation.getEndNode());
        }
        return authors;
    }

    /**
     * Set the citation authors. Both the structure and citation authors are
     * related as <code>BioRelTypes.HAS_AUTHOR</code> to Structure.
     *
     * @param authors
     */
    public void setCitationAuthorSet(HashSet<Author> authors) {
        citationAuthorRelations = new HashSet<BioRelation>();
        for (Author author : authors) {
            citationAuthorRelations.add(new BioRelation(this, author, BioRelTypes.HAS_AUTHOR));
        }
    }

    /**
     * Get the chains for this structure.
     *
     * @return HashSet<Chain>
     */
    public HashSet<Chain> getChains() {
        if (chainRelations == null) {
            return null;
        }
        HashSet<Chain> chains = new HashSet<Chain>();
        for (BioRelation bioRelation : chainRelations) {
             chains.add((Chain)bioRelation.getEndNode());
        }
        return chains;
    }

    /**
     * Set the chains for this structure. This sets all the relationships.
     *
     * @param chains
     */
    public void setChains(HashSet<Chain> chains) {
        chainRelations = new HashSet<BioRelation>();
        for (Chain chain : chains) {
            chainRelations.add(new BioRelation(this, chain, BioRelTypes.HAS_CHAIN));
        }
    }

    /**
     * Eg. CURRENT
     */
    @NonIndexed
    private String status;

    /**
     * Get the last modification date for this structure.
     *
     * @return String
     */
    public String getLastModificationDate() {
        return lastModificationDate;
    }

    /**
     * Set the last modification date for this structure.
     *
     * @param lastModificationDate
     */
    public void setLastModificationDate(String lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    /**
     *
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @RelatedToVia(direction = Direction.OUTGOING, relType = BioRelTypes.HAS_PDBGO, elementClass = BioRelation.class)
    private HashSet<BioRelation> hasPdbGoRelations;

    /**
     *
     * @param pdbGoList
     */
    public void setPdbGo(HashSet<PdbGo> pdbGoList) {
        if (hasPdbGoRelations == null) {
            hasPdbGoRelations = new HashSet<BioRelation>();
        }
        for (PdbGo pdbGo : pdbGoList) {
            hasPdbGoRelations.add(new BioRelation(this, pdbGo, BioRelTypes.HAS_PDBGO));
        }
    }

    /**
     *
     * @return HashSet<PdbGo>
     */
    public HashSet<PdbGo> getPdbGo() {
        if (hasPdbGoRelations == null) {
            return null;
        }
        HashSet<PdbGo> pdbGoList = new HashSet<PdbGo>();
        for (BioRelation bioRelation : hasPdbGoRelations) {
            pdbGoList.add((PdbGo) bioRelation.getEndNode());
        }
        return pdbGoList;
    }

    public String getBioAssemblies() {
        return bioAssemblies;
    }

    public void setBioAssemblies(String bioAssemblies) {
        this.bioAssemblies = bioAssemblies;
    }

    /**
     * Get structure authors as a String value in cases where the authors are not parseable due to
     * unpredictable format.
     *
     * @return String
     */
    public String getStructureAuthors() {
        return structureAuthors;
    }

    /**
     * Set structure authors as a String value in cases where the authors are not parseable due to
     * unpredictable format.
     *
     * @param structureAuthors
     */
    public void setStructureAuthors(String structureAuthors) {
        this.structureAuthors = structureAuthors;
    }

    /**
     * Get citation authors as a String value in cases where the authors are not parseable due to
     * unpredictable format.
     *
     * @return String
     */
    public String getCitationAuthors() {
        return citationAuthors;
    }

    /**
     * Set citation authors as a String value in cases where the authors are not parseable due to
     * unpredictable format.
     *
     * @param citationAuthors
     */
    public void setCitationAuthors(String citationAuthors) {
        this.citationAuthors = citationAuthors;
    }
}