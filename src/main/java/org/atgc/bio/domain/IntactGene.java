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
 "$oid": "5009dfed0364add94f9c8b59"
 },
 "@id": "761202",
 "names": {
 "shortLabel": "ada10_mouse_gene",
 "fullName": "Mouse Adam10 gene"
 },
 "xref": {
 "primaryRef": {
 "@refTypeAc": "MI:0356",
 "@refType": "identity",
 "@id": "ENSMUSG00000054693",
 "@dbAc": "MI:0476",
 "@db": "ensembl"
 },
 "secondaryRef": {
 "@refTypeAc": "MI:0356",
 "@refType": "identity",
 "@id": "EBI-2903226",
 "@dbAc": "MI:0469",
 "@db": "intact"
 }
 },
 "interactorType": {
 "names": {
 "shortLabel": "gene",
 "fullName": "gene"
 },
 "xref": {
 "primaryRef": {
 "@refTypeAc": "MI:0356",
 "@refType": "identity",
 "@id": "MI:0250",
 "@dbAc": "MI:0488",
 "@db": "psi-mi"
 },
 "secondaryRef": [
 {
 "@refTypeAc": "MI:0361",
 "@refType": "see-also",
 "@id": "SO:0000704",
 "@dbAc": "MI:0601",
 "@db": "so"
 },
 {
 "@refTypeAc": "MI:0358",
 "@refType": "primary-reference",
 "@id": "14755292",
 "@dbAc": "MI:0446",
 "@db": "pubmed"
 },
 {
 "@refTypeAc": "MI:0356",
 "@refType": "identity",
 "@id": "EBI-706030",
 "@dbAc": "MI:0469",
 "@db": "intact"
 }
 ]
 }
 },
 "organism": {
 "@ncbiTaxId": "10090",
 "names": {
 "shortLabel": "mouse",
 "fullName": "Mus musculus"
 }
 },
 "intactId": "20655472.xml",
 "interactorId": "761202"
 }
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.INTACT_GENE)
public class IntactGene {
    protected static Logger log = LogManager.getLogger(IntactGene.class);

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
    @Indexed (indexName=IndexNames.GENE_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)  // this is value
    private String shortLabel;

    @UniquelyIndexed (indexName=IndexNames.GENE_SYMBOL)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_SYMBOL, rbField=BioFields.GENE_SYMBOL)  // this is value
    private String geneSymbol;

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Indexed (indexName=IndexNames.GENE_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;

    @FullTextIndexed (indexName = IndexNames.GENE_ALIASES)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_ALIAS, rbField=BioFields.ALIASES)
    private String aliases;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed (indexName = IndexNames.GENE_INTACT_SECONDARY_REFS)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_ID, rbField=BioFields.INTACT_SECONDARY_REFS)
    private String intactSecondaryRefs;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed (indexName = IndexNames.DNA_PUBMED_SECONDARY_REFS)
    @Taxonomy (rbClass=TaxonomyTypes.PUBMED_ID, rbField=BioFields.PUBMED_SECONDARY_REFS)
    private String pubmedSecondaryRefs;

    @Indexed (indexName = IndexNames.GENE_ORGANISM_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.ORGANISM_SHORT_LABEL, rbField=BioFields.ORGANISM_SHORT_LABEL)
    private String organismShortLabel;

    @Indexed (indexName = IndexNames.GENE_ORGANISM_FULL_NAME)
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

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.GENE_RELATION, elementClass=BioRelation.class)
    private BioRelation geneRelation;

    /**
     * Name of the gene symbol.
     * <p>
     * {@link Indexed} {@link IndexNames#GENE_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#GENE_SYMBOL} {@link BioFields#GENE_SYMBOL}
     *
     * @return  String
     */
    public String getGeneSymbol() {
        return geneSymbol;
    }

    /**
     * Name of the gene symbol.
     * <p>
     * {@link Indexed} {@link IndexNames#GENE_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#GENE_SYMBOL} {@link BioFields#GENE_SYMBOL}
     *
     * @param geneSymbol
     */
    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

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
     * {@link Indexed} {@link IndexNames#GENE_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#GENE_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @param shortLabel
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#GENE_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#GENE_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
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
     * {@link Indexed} {@link IndexNames#GENE_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#GENE_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#GENE_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#GENE_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * A space separated list of aliases.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#GENE_ALIASES}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#GENE_ALIAS} {@link BioFields#ALIASES}
     *
     * @return String
     */
    public String getAliases() {
        return aliases;
    }

    /**
     * A space separated list of aliases.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#GENE_ALIASES}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#GENE_ALIAS} {@link BioFields#ALIASES}
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
     * {@link FullTextIndexed} {@link IndexNames#GENE_INTACT_SECONDARY_REFS}
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
     * {@link FullTextIndexed} {@link IndexNames#GENE_INTACT_SECONDARY_REFS}
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
     * {@link Indexed} {@link IndexNames#GENE_ORGANISM_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_SHORT_LABEL} {@link BioFields#ORGANISM_SHORT_LABEL}
     *
     * @return String
     */
    public String getOrganismShortLabel() {
        return organismShortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#GENE_ORGANISM_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_SHORT_LABEL} {@link BioFields#ORGANISM_SHORT_LABEL}
     *
     * @param organismShortLabel
     */
    public void setOrganismShortLabel(String organismShortLabel) {
        this.organismShortLabel = organismShortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#GENE_ORGANISM_FULL_NAME)}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_FULL_NAME} {@link BioFields#ORGANISM_FULL_NAME}
     *
     * @return String
     */
    public String getOrganismFullName() {
        return organismFullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#GENE_ORGANISM_FULL_NAME)}
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

        IntactGene intactGene = (IntactGene) o;

        if (!id.equals(intactGene.id)) return false;
        if (!intactId.equals(intactGene.intactId)) return false;
        if (!interactorId.equals(intactGene.interactorId)) return false;
        if (!nodeType.equals(intactGene.nodeType)) return false;
        if (!shortLabel.equals(intactGene.shortLabel)) return false;
        if (!message.equals(intactGene.message)) return false;
        if (!fullName.equals(intactGene.fullName)) return false;
        if (!aliases.equals(intactGene.aliases)) return false;
        if (!intactSecondaryRefs.equals(intactGene.intactSecondaryRefs)) return false;
        if (!pubmedSecondaryRefs.equals(intactGene.pubmedSecondaryRefs)) return false;
        if (!organismShortLabel.equals(intactGene.organismShortLabel)) return false;
        if (!organismFullName.equals(intactGene.organismFullName)) return false;
        return ncbiTaxId.equals(intactGene.ncbiTaxId);

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

    public void setGeneRelation(Gene gene) {
        geneRelation = new BioRelation(this, gene, BioRelTypes.GENE_RELATION);
    }
}

/*

{
  "_id": "51806af503642ce7bf5748e0",
  "LocusLinkID": "79155",
  "gene": [
    {
      "Entrezgene_track-info": [
        {
          "Gene-track_geneid": "79155",
          "Gene-track_status": {
            "@value": "live",
            "#text": "0"
          },
          "Gene-track_create-date": [
            [
              [
                {
                  "Date-std_year": "2001",
                  "Date-std_month": "3",
                  "Date-std_day": "7"
                }
              ]
            ]
          ],
          "Gene-track_update-date": [
            [
              [
                {
                  "Date-std_year": "2013",
                  "Date-std_month": "4",
                  "Date-std_day": "7",
                  "Date-std_hour": "10",
                  "Date-std_minute": "55",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        }
      ],
      "Entrezgene_type": {
        "@value": "protein-coding",
        "#text": "6"
      },
      "Entrezgene_source": [
        {
          "BioSource_genome": {
            "@value": "genomic",
            "#text": "1"
          },
          "BioSource_origin": {
            "@value": "natural",
            "#text": "1"
          },
          "BioSource_org": [
            {
              "Org-ref_taxname": "Homo sapiens",
              "Org-ref_common": "human",
              "Org-ref_db": [
                {
                  "Dbtag_db": "taxon",
                  "Dbtag_tag": [
                    [
                      "9606"
                    ]
                  ]
                }
              ],
              "Org-ref_syn": [
                "man"
              ],
              "Org-ref_orgname": [
                {
                  "OrgName_name": [
                    [
                      {
                        "BinomialOrgName_genus": "Homo",
                        "BinomialOrgName_species": "sapiens"
                      }
                    ]
                  ],
                  "OrgName_lineage": "Eukaryota; Metazoa; Chordata; Craniata; Vertebrata; Euteleostomi; Mammalia; Eutheria; Euarchontoglires; Primates; Haplorrhini; Catarrhini; Hominidae; Homo",
                  "OrgName_gcode": "1",
                  "OrgName_mgcode": "2",
                  "OrgName_div": "PRI",
                  "OrgName_pgcode": "0"
                }
              ]
            }
          ],
          "BioSource_subtype": [
            {
              "SubSource_subtype": {
                "@value": "chromosome",
                "#text": "1"
              },
              "SubSource_name": "4"
            }
          ]
        }
      ],
      "Entrezgene_gene": [
        {
          "Gene-ref_locus": "TNIP2",
          "Gene-ref_desc": "TNFAIP3 interacting protein 2",
          "Gene-ref_maploc": "4p16.3",
          "Gene-ref_db": [
            {
              "Dbtag_db": "HGNC",
              "Dbtag_tag": [
                [
                  "19118"
                ]
              ]
            },
            {
              "Dbtag_db": "Ensembl",
              "Dbtag_tag": [
                [
                  "ENSG00000168884"
                ]
              ]
            },
            {
              "Dbtag_db": "HPRD",
              "Dbtag_tag": [
                [
                  "18207"
                ]
              ]
            },
            {
              "Dbtag_db": "MIM",
              "Dbtag_tag": [
                [
                  "610669"
                ]
              ]
            },
            {
              "Dbtag_db": "Vega",
              "Dbtag_tag": [
                [
                  "OTTHUMG00000090267"
                ]
              ]
            }
          ],
          "Gene-ref_syn": [
            "KLIP",
            "ABIN2",
            "FLIP1"
          ],
          "Gene-ref_locus-tag": "RP3-474M20.1"
        }
      ],
      "Entrezgene_prot": [
        {
          "Prot-ref_name": [
            "TNFAIP3-interacting protein 2",
            "fetal liver LKB1-interacting protein",
            "A20-binding inhibitor of NF-kappaB activation-2",
            "A20-binding inhibitor of NF-kappa-B activation 2"
          ],
          "Prot-ref_desc": "TNFAIP3-interacting protein 2"
        }
      ],
      "Entrezgene_summary": "TNIP2 binds to the C-terminal zinc finger domain of A20 (TNFAIP3; MIM 191163) and is involved in activation of the ERK (see MAPK3; MIM 601795) MAP kinase pathway in various cell types (Van Huffel et al., 2001 [PubMed 11390377]; Papoutsopoulou et al., 2006 [PubMed 16633345]).[supplied by OMIM, Mar 2008]",
      "Entrezgene_location": [
        {
          "Maps_display-str": "4p16.3",
          "Maps_method": [
            {
              "@value": "cyto"
            }
          ]
        }
      ],
      "Entrezgene_gene-source": [
        {
          "Gene-source_src": "LocusLink",
          "Gene-source_src-int": "79155",
          "Gene-source_src-str2": "79155"
        }
      ],
      "Entrezgene_locus": [
        {
          "Gene-commentary_type": {
            "@value": "genomic",
            "#text": "1"
          },
          "Gene-commentary_heading": "Reference GRCh37.p10 Primary Assembly",
          "Gene-commentary_label": "chromosome 4 reference GRCh37.p10 Primary Assembly",
          "Gene-commentary_accession": "NC_000004",
          "Gene-commentary_version": "11",
          "Gene-commentary_seqs": [
            [
              [
                {
                  "Seq-interval_from": "2743386",
                  "Seq-interval_to": "2758102",
                  "Seq-interval_strand": [
                    {
                      "@value": "minus"
                    }
                  ],
                  "Seq-interval_id": [
                    [
                      "224589816"
                    ]
                  ]
                }
              ]
            ]
          ],
          "Gene-commentary_products": [
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "Reference",
              "Gene-commentary_label": "transcript variant 1",
              "Gene-commentary_accession": "NM_024309",
              "Gene-commentary_version": "3",
              "Gene-commentary_genomic-coords": [
                [
                  [
                    [
                      [
                        [
                          {
                            "Seq-interval_from": "2757740",
                            "Seq-interval_to": "2758102",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "224589816"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2749381",
                            "Seq-interval_to": "2749671",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "224589816"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2747172",
                            "Seq-interval_to": "2747261",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "224589816"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2746423",
                            "Seq-interval_to": "2746671",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "224589816"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2746113",
                            "Seq-interval_to": "2746232",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "224589816"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2743386",
                            "Seq-interval_to": "2744246",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "224589816"
                              ]
                            ]
                          }
                        ]
                      ]
                    ]
                  ]
                ]
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "239787091"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_heading": "Reference",
                  "Gene-commentary_label": "isoform 1",
                  "Gene-commentary_accession": "NP_077285",
                  "Gene-commentary_version": "3",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "CCDS",
                          "Dbtag_tag": [
                            [
                              "CCDS3362.1"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "CCDS3362.1"
                    }
                  ],
                  "Gene-commentary_genomic-coords": [
                    [
                      [
                        [
                          [
                            [
                              {
                                "Seq-interval_from": "2757740",
                                "Seq-interval_to": "2758015",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "224589816"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2749381",
                                "Seq-interval_to": "2749671",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "224589816"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2747172",
                                "Seq-interval_to": "2747261",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "224589816"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2746423",
                                "Seq-interval_to": "2746671",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "224589816"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2746113",
                                "Seq-interval_to": "2746232",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "224589816"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2743983",
                                "Seq-interval_to": "2744246",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "224589816"
                                  ]
                                ]
                              }
                            ]
                          ]
                        ]
                      ]
                    ]
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "239787092"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "Reference",
              "Gene-commentary_label": "transcript variant 2",
              "Gene-commentary_accession": "NM_001161527",
              "Gene-commentary_version": "1",
              "Gene-commentary_genomic-coords": [
                [
                  [
                    [
                      [
                        [
                          {
                            "Seq-interval_from": "2757675",
                            "Seq-interval_to": "2757751",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "224589816"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2749381",
                            "Seq-interval_to": "2749671",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "224589816"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2747172",
                            "Seq-interval_to": "2747261",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "224589816"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2746423",
                            "Seq-interval_to": "2746671",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "224589816"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2746113",
                            "Seq-interval_to": "2746232",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "224589816"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2743386",
                            "Seq-interval_to": "2744246",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "224589816"
                              ]
                            ]
                          }
                        ]
                      ]
                    ]
                  ]
                ]
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "239787093"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_heading": "Reference",
                  "Gene-commentary_label": "isoform 2",
                  "Gene-commentary_accession": "NP_001154999",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "CCDS",
                          "Dbtag_tag": [
                            [
                              "CCDS54714.1"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "CCDS54714.1"
                    }
                  ],
                  "Gene-commentary_genomic-coords": [
                    [
                      [
                        [
                          [
                            [
                              {
                                "Seq-interval_from": "2749381",
                                "Seq-interval_to": "2749626",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "224589816"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2747172",
                                "Seq-interval_to": "2747261",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "224589816"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2746423",
                                "Seq-interval_to": "2746671",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "224589816"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2746113",
                                "Seq-interval_to": "2746232",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "224589816"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2743983",
                                "Seq-interval_to": "2744246",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "224589816"
                                  ]
                                ]
                              }
                            ]
                          ]
                        ]
                      ]
                    ]
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "239787094"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            }
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "genomic",
            "#text": "1"
          },
          "Gene-commentary_heading": "Alternate CHM1_1.0",
          "Gene-commentary_label": "chromosome 4 alternate CHM1_1.0",
          "Gene-commentary_accession": "NC_018915",
          "Gene-commentary_version": "1",
          "Gene-commentary_seqs": [
            [
              [
                {
                  "Seq-interval_from": "2724306",
                  "Seq-interval_to": "2739001",
                  "Seq-interval_strand": [
                    {
                      "@value": "minus"
                    }
                  ],
                  "Seq-interval_id": [
                    [
                      "409253460"
                    ]
                  ],
                  "Seq-interval_fuzz-to": [
                    [
                      {
                        "@value": "gt"
                      }
                    ]
                  ]
                }
              ]
            ]
          ],
          "Gene-commentary_products": [
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "Reference",
              "Gene-commentary_label": "transcript variant 1",
              "Gene-commentary_accession": "NM_024309",
              "Gene-commentary_version": "3",
              "Gene-commentary_genomic-coords": [
                [
                  [
                    [
                      [
                        [
                          {
                            "Seq-interval_from": "2738666",
                            "Seq-interval_to": "2739001",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "409253460"
                              ]
                            ],
                            "Seq-interval_fuzz-to": [
                              [
                                {
                                  "@value": "gt"
                                }
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2730301",
                            "Seq-interval_to": "2730591",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "409253460"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2728092",
                            "Seq-interval_to": "2728181",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "409253460"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2727343",
                            "Seq-interval_to": "2727591",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "409253460"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2727033",
                            "Seq-interval_to": "2727152",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "409253460"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2724306",
                            "Seq-interval_to": "2725166",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "409253460"
                              ]
                            ]
                          }
                        ]
                      ]
                    ]
                  ]
                ]
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "239787091"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_heading": "Reference",
                  "Gene-commentary_label": "isoform 1",
                  "Gene-commentary_accession": "NP_077285",
                  "Gene-commentary_version": "3",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "CCDS",
                          "Dbtag_tag": [
                            [
                              "CCDS3362.1"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "CCDS3362.1"
                    }
                  ],
                  "Gene-commentary_genomic-coords": [
                    [
                      [
                        [
                          [
                            [
                              {
                                "Seq-interval_from": "2738666",
                                "Seq-interval_to": "2738941",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "409253460"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2730301",
                                "Seq-interval_to": "2730591",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "409253460"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2728092",
                                "Seq-interval_to": "2728181",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "409253460"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2727343",
                                "Seq-interval_to": "2727591",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "409253460"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2727033",
                                "Seq-interval_to": "2727152",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "409253460"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2724903",
                                "Seq-interval_to": "2725166",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "409253460"
                                  ]
                                ]
                              }
                            ]
                          ]
                        ]
                      ]
                    ]
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "239787092"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "Reference",
              "Gene-commentary_label": "transcript variant 2",
              "Gene-commentary_accession": "NM_001161527",
              "Gene-commentary_version": "1",
              "Gene-commentary_genomic-coords": [
                [
                  [
                    [
                      [
                        [
                          {
                            "Seq-interval_from": "2738601",
                            "Seq-interval_to": "2738677",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "409253460"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2730301",
                            "Seq-interval_to": "2730591",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "409253460"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2728092",
                            "Seq-interval_to": "2728181",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "409253460"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2727343",
                            "Seq-interval_to": "2727591",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "409253460"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2727033",
                            "Seq-interval_to": "2727152",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "409253460"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2724306",
                            "Seq-interval_to": "2725166",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "409253460"
                              ]
                            ]
                          }
                        ]
                      ]
                    ]
                  ]
                ]
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "239787093"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_heading": "Reference",
                  "Gene-commentary_label": "isoform 2",
                  "Gene-commentary_accession": "NP_001154999",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "CCDS",
                          "Dbtag_tag": [
                            [
                              "CCDS54714.1"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "CCDS54714.1"
                    }
                  ],
                  "Gene-commentary_genomic-coords": [
                    [
                      [
                        [
                          [
                            [
                              {
                                "Seq-interval_from": "2730301",
                                "Seq-interval_to": "2730546",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "409253460"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2728092",
                                "Seq-interval_to": "2728181",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "409253460"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2727343",
                                "Seq-interval_to": "2727591",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "409253460"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2727033",
                                "Seq-interval_to": "2727152",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "409253460"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2724903",
                                "Seq-interval_to": "2725166",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "409253460"
                                  ]
                                ]
                              }
                            ]
                          ]
                        ]
                      ]
                    ]
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "239787094"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            }
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "genomic",
            "#text": "1"
          },
          "Gene-commentary_heading": "Alternate HuRef",
          "Gene-commentary_label": "chromosome 4 alternate HuRef",
          "Gene-commentary_accession": "AC_000136",
          "Gene-commentary_version": "1",
          "Gene-commentary_seqs": [
            [
              [
                {
                  "Seq-interval_from": "2682482",
                  "Seq-interval_to": "2696861",
                  "Seq-interval_strand": [
                    {
                      "@value": "minus"
                    }
                  ],
                  "Seq-interval_id": [
                    [
                      "157734150"
                    ]
                  ]
                }
              ]
            ]
          ],
          "Gene-commentary_products": [
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "Reference",
              "Gene-commentary_label": "transcript variant 1",
              "Gene-commentary_accession": "NM_024309",
              "Gene-commentary_version": "3",
              "Gene-commentary_genomic-coords": [
                [
                  [
                    [
                      [
                        [
                          {
                            "Seq-interval_from": "2696499",
                            "Seq-interval_to": "2696861",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "157734150"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2688139",
                            "Seq-interval_to": "2688429",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "157734150"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2685930",
                            "Seq-interval_to": "2686019",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "157734150"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2685181",
                            "Seq-interval_to": "2685429",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "157734150"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2684871",
                            "Seq-interval_to": "2684990",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "157734150"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2682482",
                            "Seq-interval_to": "2683342",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "157734150"
                              ]
                            ]
                          }
                        ]
                      ]
                    ]
                  ]
                ]
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "239787091"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_heading": "Reference",
                  "Gene-commentary_label": "isoform 1",
                  "Gene-commentary_accession": "NP_077285",
                  "Gene-commentary_version": "3",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "CCDS",
                          "Dbtag_tag": [
                            [
                              "CCDS3362.1"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "CCDS3362.1"
                    }
                  ],
                  "Gene-commentary_genomic-coords": [
                    [
                      [
                        [
                          [
                            [
                              {
                                "Seq-interval_from": "2696499",
                                "Seq-interval_to": "2696774",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "157734150"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2688139",
                                "Seq-interval_to": "2688429",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "157734150"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2685930",
                                "Seq-interval_to": "2686019",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "157734150"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2685181",
                                "Seq-interval_to": "2685429",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "157734150"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2684871",
                                "Seq-interval_to": "2684990",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "157734150"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2683079",
                                "Seq-interval_to": "2683342",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "157734150"
                                  ]
                                ]
                              }
                            ]
                          ]
                        ]
                      ]
                    ]
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "239787092"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "Reference",
              "Gene-commentary_label": "transcript variant 2",
              "Gene-commentary_accession": "NM_001161527",
              "Gene-commentary_version": "1",
              "Gene-commentary_genomic-coords": [
                [
                  [
                    [
                      [
                        [
                          {
                            "Seq-interval_from": "2696434",
                            "Seq-interval_to": "2696510",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "157734150"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2688139",
                            "Seq-interval_to": "2688429",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "157734150"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2685930",
                            "Seq-interval_to": "2686019",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "157734150"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2685181",
                            "Seq-interval_to": "2685429",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "157734150"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2684871",
                            "Seq-interval_to": "2684990",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "157734150"
                              ]
                            ]
                          }
                        ]
                      ],
                      [
                        [
                          {
                            "Seq-interval_from": "2682482",
                            "Seq-interval_to": "2683342",
                            "Seq-interval_strand": [
                              {
                                "@value": "minus"
                              }
                            ],
                            "Seq-interval_id": [
                              [
                                "157734150"
                              ]
                            ]
                          }
                        ]
                      ]
                    ]
                  ]
                ]
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "239787093"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_heading": "Reference",
                  "Gene-commentary_label": "isoform 2",
                  "Gene-commentary_accession": "NP_001154999",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "CCDS",
                          "Dbtag_tag": [
                            [
                              "CCDS54714.1"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "CCDS54714.1"
                    }
                  ],
                  "Gene-commentary_genomic-coords": [
                    [
                      [
                        [
                          [
                            [
                              {
                                "Seq-interval_from": "2688139",
                                "Seq-interval_to": "2688384",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "157734150"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2685930",
                                "Seq-interval_to": "2686019",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "157734150"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2685181",
                                "Seq-interval_to": "2685429",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "157734150"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2684871",
                                "Seq-interval_to": "2684990",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "157734150"
                                  ]
                                ]
                              }
                            ]
                          ],
                          [
                            [
                              {
                                "Seq-interval_from": "2683079",
                                "Seq-interval_to": "2683342",
                                "Seq-interval_strand": [
                                  {
                                    "@value": "minus"
                                  }
                                ],
                                "Seq-interval_id": [
                                  [
                                    "157734150"
                                  ]
                                ]
                              }
                            ]
                          ]
                        ]
                      ]
                    ]
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "239787094"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            }
          ]
        }
      ],
      "Entrezgene_properties": [
        {
          "Gene-commentary_type": {
            "@value": "comment",
            "#text": "254"
          },
          "Gene-commentary_label": "Nomenclature",
          "Gene-commentary_source": [
            [
              "HUGO Gene Nomenclature Committee"
            ]
          ],
          "Gene-commentary_properties": [
            {
              "Gene-commentary_type": {
                "@value": "property",
                "#text": "16"
              },
              "Gene-commentary_label": "Official Symbol",
              "Gene-commentary_text": "TNIP2"
            },
            {
              "Gene-commentary_type": {
                "@value": "property",
                "#text": "16"
              },
              "Gene-commentary_label": "Official Full Name",
              "Gene-commentary_text": "TNFAIP3 interacting protein 2"
            }
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "comment",
            "#text": "254"
          },
          "Gene-commentary_heading": "GeneOntology",
          "Gene-commentary_source": [
            {
              "Other-source_pre-text": "Provided by",
              "Other-source_anchor": "GOA",
              "Other-source_url": "http://www.ebi.ac.uk/GOA/"
            }
          ],
          "Gene-commentary_comment": [
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_label": "Function",
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_refs": [
                    [
                      [
                        "18212736"
                      ]
                    ]
                  ],
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "31593"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "polyubiquitin binding",
                      "Other-source_post-text": "evidence: IDA"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_refs": [
                    [
                      [
                        "12595760"
                      ]
                    ]
                  ],
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "5515"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "protein binding",
                      "Other-source_post-text": "evidence: IPI"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "19901"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "protein kinase binding",
                      "Other-source_post-text": "evidence: IDA"
                    }
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_label": "Process",
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "23035"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "CD40 signaling pathway",
                      "Other-source_post-text": "evidence: ISS"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "7249"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "I-kappaB kinase/NF-kappaB cascade",
                      "Other-source_post-text": "evidence: IEA"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "6915"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "apoptotic process",
                      "Other-source_post-text": "evidence: IEA"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "71222"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "cellular response to lipopolysaccharide",
                      "Other-source_post-text": "evidence: ISS"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "6954"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "inflammatory response",
                      "Other-source_post-text": "evidence: IEA"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "70498"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "interleukin-1-mediated signaling pathway",
                      "Other-source_post-text": "evidence: ISS"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_refs": [
                    [
                      [
                        "12933576"
                      ]
                    ]
                  ],
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "2000352"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "negative regulation of endothelial cell apoptotic process",
                      "Other-source_post-text": "evidence: IDA"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "50871"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "positive regulation of B cell activation",
                      "Other-source_post-text": "evidence: ISS"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "43123"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "positive regulation of I-kappaB kinase/NF-kappaB cascade",
                      "Other-source_post-text": "evidence: IDA"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "43032"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "positive regulation of macrophage activation",
                      "Other-source_post-text": "evidence: ISS"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_refs": [
                    [
                      [
                        "12753905"
                      ]
                    ]
                  ],
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "45944"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "positive regulation of transcription from RNA polymerase II promoter",
                      "Other-source_post-text": "evidence: IDA"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "50821"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "protein stabilization",
                      "Other-source_post-text": "evidence: ISS"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "34134"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "toll-like receptor 2 signaling pathway",
                      "Other-source_post-text": "evidence: ISS"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "34138"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "toll-like receptor 3 signaling pathway",
                      "Other-source_post-text": "evidence: ISS"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "34162"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "toll-like receptor 9 signaling pathway",
                      "Other-source_post-text": "evidence: ISS"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "6351"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "transcription, DNA-dependent",
                      "Other-source_post-text": "evidence: IEA"
                    }
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_label": "Component",
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "5829"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "cytosol",
                      "Other-source_post-text": "evidence: TAS"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "comment",
                    "#text": "254"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GO",
                          "Dbtag_tag": [
                            [
                              "5634"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "nucleus",
                      "Other-source_post-text": "evidence: IEA"
                    }
                  ]
                }
              ]
            }
          ]
        }
      ],
      "Entrezgene_homology": [
        {
          "Gene-commentary_type": {
            "@value": "comment",
            "#text": "254"
          },
          "Gene-commentary_heading": "Mouse, Rat",
          "Gene-commentary_source": [
            {
              "Other-source_src": [
                {
                  "Dbtag_db": "HomoloGene",
                  "Dbtag_tag": [
                    [
                      "11515"
                    ]
                  ]
                }
              ],
              "Other-source_anchor": "Map Viewer",
              "Other-source_url": "http://www.ncbi.nlm.nih.gov/mapview/maps.cgi?taxid=9606&chr=4&MAPS=genes-r-org/rat-chr/human%3A4,genes-r-org/mouse-chr/human%3A4,genes-r-org/human-chr4&query=e%3A79155[egene_id]+AND+gene[obj_type]&QSTR=tnip2&cmd=focus&fill=10"
            }
          ]
        }
      ],
      "Entrezgene_comments": [
        {
          "Gene-commentary_type": {
            "@value": "comment",
            "#text": "254"
          },
          "Gene-commentary_heading": "RefSeq Status",
          "Gene-commentary_label": "VALIDATED"
        },
        {
          "Gene-commentary_type": {
            "@value": "comment",
            "#text": "254"
          },
          "Gene-commentary_heading": "Clone Names",
          "Gene-commentary_text": "MGC4289, DKFZp434J1313"
        },
        {
          "Gene-commentary_type": {
            "@value": "gene-group",
            "#text": "23"
          },
          "Gene-commentary_heading": "Related pseudogene(s)",
          "Gene-commentary_source": [
            {
              "Other-source_pre-text": "1 found",
              "Other-source_anchor": "Review record(s) in Gene"
            }
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "comment",
            "#text": "254"
          },
          "Gene-commentary_refs": [
            [
              [
                "21784860"
              ]
            ],
            [
              [
                "21266526"
              ]
            ],
            [
              [
                "19898481"
              ]
            ],
            [
              [
                "19754427"
              ]
            ],
            [
              [
                "19464428"
              ]
            ],
            [
              [
                "18622428"
              ]
            ],
            [
              [
                "18212736"
              ]
            ],
            [
              [
                "17038805"
              ]
            ],
            [
              [
                "16633345"
              ]
            ],
            [
              [
                "16554025"
              ]
            ],
            [
              [
                "16480954"
              ]
            ],
            [
              [
                "16344560"
              ]
            ],
            [
              [
                "15474016"
              ]
            ],
            [
              [
                "15169888"
              ]
            ],
            [
              [
                "14743216"
              ]
            ],
            [
              [
                "14702039"
              ]
            ],
            [
              [
                "14653779"
              ]
            ],
            [
              [
                "12933576"
              ]
            ],
            [
              [
                "12753905"
              ]
            ],
            [
              [
                "12609966"
              ]
            ],
            [
              [
                "12595760"
              ]
            ],
            [
              [
                "12477932"
              ]
            ],
            [
              [
                "12080044"
              ]
            ],
            [
              [
                "11390377"
              ]
            ],
            [
              [
                "11389905"
              ]
            ],
            [
              [
                "10385526"
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "comment",
            "#text": "254"
          },
          "Gene-commentary_heading": "NCBI Reference Sequences (RefSeq)",
          "Gene-commentary_comment": [
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_heading": "RefSeqs maintained independently of Annotated Genomes",
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "mRNA",
                    "#text": "3"
                  },
                  "Gene-commentary_heading": "mRNA Sequence",
                  "Gene-commentary_accession": "NM_001161527",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Nucleotide",
                          "Dbtag_tag": [
                            [
                              "239787093"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "NM_001161527"
                    }
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "239787093"
                        ]
                      ]
                    ]
                  ],
                  "Gene-commentary_products": [
                    {
                      "Gene-commentary_type": {
                        "@value": "peptide",
                        "#text": "8"
                      },
                      "Gene-commentary_heading": "Product",
                      "Gene-commentary_accession": "NP_001154999",
                      "Gene-commentary_version": "1",
                      "Gene-commentary_source": [
                        {
                          "Other-source_src": [
                            {
                              "Dbtag_db": "Protein",
                              "Dbtag_tag": [
                                [
                                  "239787094"
                                ]
                              ]
                            }
                          ],
                          "Other-source_anchor": "NP_001154999",
                          "Other-source_post-text": "TNFAIP3-interacting protein 2 isoform 2"
                        }
                      ],
                      "Gene-commentary_seqs": [
                        [
                          [
                            [
                              "239787094"
                            ]
                          ]
                        ]
                      ],
                      "Gene-commentary_comment": [
                        {
                          "Gene-commentary_type": {
                            "@value": "other",
                            "#text": "255"
                          },
                          "Gene-commentary_heading": "Conserved Domains",
                          "Gene-commentary_source": [
                            {
                              "Other-source_pre-text": "(1)",
                              "Other-source_anchor": "summary"
                            }
                          ],
                          "Gene-commentary_comment": [
                            {
                              "Gene-commentary_type": {
                                "@value": "other",
                                "#text": "255"
                              },
                              "Gene-commentary_source": [
                                {
                                  "Other-source_src": [
                                    {
                                      "Dbtag_db": "CDD",
                                      "Dbtag_tag": [
                                        [
                                          "152615"
                                        ]
                                      ]
                                    }
                                  ],
                                  "Other-source_anchor": "pfam12180: EABR; TSG101 and ALIX binding domain of CEP55"
                                }
                              ],
                              "Gene-commentary_comment": [
                                {
                                  "Gene-commentary_type": {
                                    "@value": "other",
                                    "#text": "255"
                                  },
                                  "Gene-commentary_text": "Location: 107 - 141  Blast Score: 108"
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "Gene-commentary_type": {
                            "@value": "other",
                            "#text": "255"
                          },
                          "Gene-commentary_heading": "Related",
                          "Gene-commentary_source": [
                            [
                              [
                                {
                                  "Dbtag_db": "Ensembl",
                                  "Dbtag_tag": [
                                    [
                                      "ENSP00000427613"
                                    ]
                                  ]
                                }
                              ]
                            ]
                          ]
                        },
                        {
                          "Gene-commentary_type": {
                            "@value": "other",
                            "#text": "255"
                          },
                          "Gene-commentary_heading": "UniProtKB",
                          "Gene-commentary_source": [],
                          "Gene-commentary_comment": [
                            {
                              "Gene-commentary_type": {
                                "@value": "other",
                                "#text": "255"
                              },
                              "Gene-commentary_source": [
                                {
                                  "Other-source_src": [
                                    {
                                      "Dbtag_db": "UniProtKB/Swiss-Prot",
                                      "Dbtag_tag": [
                                        [
                                          "Q8NFZ5"
                                        ]
                                      ]
                                    }
                                  ],
                                  "Other-source_anchor": "Q8NFZ5"
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "Gene-commentary_type": {
                            "@value": "other",
                            "#text": "255"
                          },
                          "Gene-commentary_heading": "Consensus CDS (CCDS)",
                          "Gene-commentary_source": [
                            {
                              "Other-source_src": [
                                {
                                  "Dbtag_db": "CCDS",
                                  "Dbtag_tag": [
                                    [
                                      "CCDS54714.1"
                                    ]
                                  ]
                                }
                              ],
                              "Other-source_anchor": "CCDS54714.1"
                            }
                          ]
                        },
                        {
                          "Gene-commentary_type": {
                            "@value": "other",
                            "#text": "255"
                          },
                          "Gene-commentary_heading": "Related",
                          "Gene-commentary_source": [
                            [
                              [
                                {
                                  "Dbtag_db": "Vega",
                                  "Dbtag_tag": [
                                    [
                                      "OTTHUMP00000219012"
                                    ]
                                  ]
                                }
                              ]
                            ]
                          ]
                        }
                      ]
                    }
                  ],
                  "Gene-commentary_comment": [
                    {
                      "Gene-commentary_type": {
                        "@value": "comment",
                        "#text": "254"
                      },
                      "Gene-commentary_label": "RefSeq Status",
                      "Gene-commentary_text": "VALIDATED"
                    },
                    {
                      "Gene-commentary_type": {
                        "@value": "comment",
                        "#text": "254"
                      },
                      "Gene-commentary_heading": "Transcriptional Variant",
                      "Gene-commentary_comment": [
                        {
                          "Gene-commentary_type": {
                            "@value": "comment",
                            "#text": "254"
                          },
                          "Gene-commentary_text": "Transcript Variant: This variant (2) differs in the 5' UTR, lacks a portion of the 5' coding region, and initiates translation at a downstream start codon, compared to variant 1. The encoded isoform (2) has a shorter N-terminus compared to isoform 1."
                        }
                      ]
                    },
                    {
                      "Gene-commentary_type": {
                        "@value": "other",
                        "#text": "255"
                      },
                      "Gene-commentary_heading": "Source Sequence",
                      "Gene-commentary_source": [
                        {
                          "Other-source_src": [
                            {
                              "Dbtag_db": "Nucleotide",
                              "Dbtag_tag": [
                                [
                                  "AK026176,AL110117,BC002740,DA421921"
                                ]
                              ]
                            }
                          ],
                          "Other-source_anchor": "AK026176,AL110117,BC002740,DA421921"
                        }
                      ]
                    },
                    {
                      "Gene-commentary_type": {
                        "@value": "other",
                        "#text": "255"
                      },
                      "Gene-commentary_heading": "Related",
                      "Gene-commentary_source": [
                        [
                          [
                            {
                              "Dbtag_db": "Ensembl",
                              "Dbtag_tag": [
                                [
                                  "ENST00000510267"
                                ]
                              ]
                            }
                          ]
                        ]
                      ]
                    },
                    {
                      "Gene-commentary_type": {
                        "@value": "other",
                        "#text": "255"
                      },
                      "Gene-commentary_heading": "Related",
                      "Gene-commentary_source": [
                        [
                          [
                            {
                              "Dbtag_db": "Vega",
                              "Dbtag_tag": [
                                [
                                  "OTTHUMT00000362368"
                                ]
                              ]
                            }
                          ]
                        ]
                      ]
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "mRNA",
                    "#text": "3"
                  },
                  "Gene-commentary_heading": "mRNA Sequence",
                  "Gene-commentary_accession": "NM_024309",
                  "Gene-commentary_version": "3",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Nucleotide",
                          "Dbtag_tag": [
                            [
                              "239787091"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "NM_024309"
                    }
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "239787091"
                        ]
                      ]
                    ]
                  ],
                  "Gene-commentary_products": [
                    {
                      "Gene-commentary_type": {
                        "@value": "peptide",
                        "#text": "8"
                      },
                      "Gene-commentary_heading": "Product",
                      "Gene-commentary_accession": "NP_077285",
                      "Gene-commentary_version": "3",
                      "Gene-commentary_source": [
                        {
                          "Other-source_src": [
                            {
                              "Dbtag_db": "Protein",
                              "Dbtag_tag": [
                                [
                                  "239787092"
                                ]
                              ]
                            }
                          ],
                          "Other-source_anchor": "NP_077285",
                          "Other-source_post-text": "TNFAIP3-interacting protein 2 isoform 1"
                        }
                      ],
                      "Gene-commentary_seqs": [
                        [
                          [
                            [
                              "239787092"
                            ]
                          ]
                        ]
                      ],
                      "Gene-commentary_comment": [
                        {
                          "Gene-commentary_type": {
                            "@value": "other",
                            "#text": "255"
                          },
                          "Gene-commentary_heading": "Conserved Domains",
                          "Gene-commentary_source": [
                            {
                              "Other-source_pre-text": "(1)",
                              "Other-source_anchor": "summary"
                            }
                          ],
                          "Gene-commentary_comment": [
                            {
                              "Gene-commentary_type": {
                                "@value": "other",
                                "#text": "255"
                              },
                              "Gene-commentary_source": [
                                {
                                  "Other-source_src": [
                                    {
                                      "Dbtag_db": "CDD",
                                      "Dbtag_tag": [
                                        [
                                          "152615"
                                        ]
                                      ]
                                    }
                                  ],
                                  "Other-source_anchor": "pfam12180: EABR; TSG101 and ALIX binding domain of CEP55"
                                }
                              ],
                              "Gene-commentary_comment": [
                                {
                                  "Gene-commentary_type": {
                                    "@value": "other",
                                    "#text": "255"
                                  },
                                  "Gene-commentary_text": "Location: 214 - 248  Blast Score: 112"
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "Gene-commentary_type": {
                            "@value": "other",
                            "#text": "255"
                          },
                          "Gene-commentary_heading": "Related",
                          "Gene-commentary_source": [
                            [
                              [
                                {
                                  "Dbtag_db": "Ensembl",
                                  "Dbtag_tag": [
                                    [
                                      "ENSP00000321203"
                                    ]
                                  ]
                                }
                              ]
                            ]
                          ]
                        },
                        {
                          "Gene-commentary_type": {
                            "@value": "other",
                            "#text": "255"
                          },
                          "Gene-commentary_heading": "UniProtKB",
                          "Gene-commentary_source": [],
                          "Gene-commentary_comment": [
                            {
                              "Gene-commentary_type": {
                                "@value": "other",
                                "#text": "255"
                              },
                              "Gene-commentary_source": [
                                {
                                  "Other-source_src": [
                                    {
                                      "Dbtag_db": "UniProtKB/Swiss-Prot",
                                      "Dbtag_tag": [
                                        [
                                          "Q8NFZ5"
                                        ]
                                      ]
                                    }
                                  ],
                                  "Other-source_anchor": "Q8NFZ5"
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "Gene-commentary_type": {
                            "@value": "other",
                            "#text": "255"
                          },
                          "Gene-commentary_heading": "Consensus CDS (CCDS)",
                          "Gene-commentary_source": [
                            {
                              "Other-source_src": [
                                {
                                  "Dbtag_db": "CCDS",
                                  "Dbtag_tag": [
                                    [
                                      "CCDS3362.1"
                                    ]
                                  ]
                                }
                              ],
                              "Other-source_anchor": "CCDS3362.1"
                            }
                          ]
                        },
                        {
                          "Gene-commentary_type": {
                            "@value": "other",
                            "#text": "255"
                          },
                          "Gene-commentary_heading": "Related",
                          "Gene-commentary_source": [
                            [
                              [
                                {
                                  "Dbtag_db": "Vega",
                                  "Dbtag_tag": [
                                    [
                                      "OTTHUMP00000115298"
                                    ]
                                  ]
                                }
                              ]
                            ]
                          ]
                        }
                      ]
                    }
                  ],
                  "Gene-commentary_comment": [
                    {
                      "Gene-commentary_type": {
                        "@value": "comment",
                        "#text": "254"
                      },
                      "Gene-commentary_label": "RefSeq Status",
                      "Gene-commentary_text": "VALIDATED"
                    },
                    {
                      "Gene-commentary_type": {
                        "@value": "comment",
                        "#text": "254"
                      },
                      "Gene-commentary_heading": "Transcriptional Variant",
                      "Gene-commentary_comment": [
                        {
                          "Gene-commentary_type": {
                            "@value": "comment",
                            "#text": "254"
                          },
                          "Gene-commentary_text": "Transcript Variant: This variant (1) represents the longer transcript and encodes the longer isoform (1)."
                        }
                      ]
                    },
                    {
                      "Gene-commentary_type": {
                        "@value": "other",
                        "#text": "255"
                      },
                      "Gene-commentary_heading": "Source Sequence",
                      "Gene-commentary_source": [
                        {
                          "Other-source_src": [
                            {
                              "Dbtag_db": "Nucleotide",
                              "Dbtag_tag": [
                                [
                                  "AF372839,AL110117,BC002740"
                                ]
                              ]
                            }
                          ],
                          "Other-source_anchor": "AF372839,AL110117,BC002740"
                        }
                      ]
                    },
                    {
                      "Gene-commentary_type": {
                        "@value": "other",
                        "#text": "255"
                      },
                      "Gene-commentary_heading": "Related",
                      "Gene-commentary_source": [
                        [
                          [
                            {
                              "Dbtag_db": "Ensembl",
                              "Dbtag_tag": [
                                [
                                  "ENST00000315423"
                                ]
                              ]
                            }
                          ]
                        ]
                      ]
                    },
                    {
                      "Gene-commentary_type": {
                        "@value": "other",
                        "#text": "255"
                      },
                      "Gene-commentary_heading": "Related",
                      "Gene-commentary_source": [
                        [
                          [
                            {
                              "Dbtag_db": "Vega",
                              "Dbtag_tag": [
                                [
                                  "OTTHUMT00000206589"
                                ]
                              ]
                            }
                          ]
                        ]
                      ]
                    }
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_heading": "RefSeqs of Annotated Genomes: Homo sapiens Annotation Release 104",
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "genomic",
                    "#text": "1"
                  },
                  "Gene-commentary_heading": "Reference GRCh37.p10 Primary Assembly",
                  "Gene-commentary_accession": "NC_000004",
                  "Gene-commentary_version": "11",
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "Nucleotide",
                          "Dbtag_tag": [
                            [
                              "224589816"
                            ]
                          ]
                        }
                      ]
                    ]
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        {
                          "Seq-interval_from": "2743386",
                          "Seq-interval_to": "2758102",
                          "Seq-interval_strand": [
                            {
                              "@value": "minus"
                            }
                          ],
                          "Seq-interval_id": [
                            [
                              "224589816"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "genomic",
                    "#text": "1"
                  },
                  "Gene-commentary_heading": "Alternate HuRef",
                  "Gene-commentary_accession": "AC_000136",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "Nucleotide",
                          "Dbtag_tag": [
                            [
                              "157734150"
                            ]
                          ]
                        }
                      ]
                    ]
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        {
                          "Seq-interval_from": "2682482",
                          "Seq-interval_to": "2696861",
                          "Seq-interval_strand": [
                            {
                              "@value": "minus"
                            }
                          ],
                          "Seq-interval_id": [
                            [
                              "157734150"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "genomic",
                    "#text": "1"
                  },
                  "Gene-commentary_heading": "Alternate CHM1_1.0",
                  "Gene-commentary_accession": "NC_018915",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "Nucleotide",
                          "Dbtag_tag": [
                            [
                              "409253460"
                            ]
                          ]
                        }
                      ]
                    ]
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        {
                          "Seq-interval_from": "2724306",
                          "Seq-interval_to": "2739001",
                          "Seq-interval_strand": [
                            {
                              "@value": "minus"
                            }
                          ],
                          "Seq-interval_id": [
                            [
                              "409253460"
                            ]
                          ],
                          "Seq-interval_fuzz-to": [
                            [
                              {
                                "@value": "gt"
                              }
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ]
            }
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "comment",
            "#text": "254"
          },
          "Gene-commentary_heading": "Related Sequences",
          "Gene-commentary_products": [
            {
              "Gene-commentary_type": {
                "@value": "genomic",
                "#text": "1"
              },
              "Gene-commentary_heading": "Genomic",
              "Gene-commentary_accession": "ABBA01029490",
              "Gene-commentary_version": "1",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "148162673"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "ABBA01029490"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    {
                      "Seq-interval_from": "23362",
                      "Seq-interval_to": "36798",
                      "Seq-interval_strand": [
                        {
                          "@value": "plus"
                        }
                      ],
                      "Seq-interval_id": [
                        [
                          "148162673"
                        ]
                      ]
                    }
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_text": "None"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "genomic",
                "#text": "1"
              },
              "Gene-commentary_heading": "Genomic",
              "Gene-commentary_accession": "ABBA01029491",
              "Gene-commentary_version": "1",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "148162672"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "ABBA01029491"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    {
                      "Seq-interval_from": "29994",
                      "Seq-interval_to": "30916",
                      "Seq-interval_strand": [
                        {
                          "@value": "minus"
                        }
                      ],
                      "Seq-interval_id": [
                        [
                          "148162672"
                        ]
                      ]
                    }
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_text": "None"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "genomic",
                "#text": "1"
              },
              "Gene-commentary_heading": "Genomic",
              "Gene-commentary_accession": "AL110117",
              "Gene-commentary_version": "3",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "50234160"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "AL110117"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    {
                      "Seq-interval_from": "1999",
                      "Seq-interval_to": "7371",
                      "Seq-interval_strand": [
                        {
                          "@value": "plus"
                        }
                      ],
                      "Seq-interval_id": [
                        [
                          "50234160"
                        ]
                      ]
                    }
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_text": "None"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "genomic",
                "#text": "1"
              },
              "Gene-commentary_heading": "Genomic",
              "Gene-commentary_accession": "AL121750",
              "Gene-commentary_version": "22",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "50250927"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "AL121750"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "50250927"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_accession": "CAM28233",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "123234960"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "CAM28233"
                    }
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "123234960"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "genomic",
                "#text": "1"
              },
              "Gene-commentary_heading": "Genomic",
              "Gene-commentary_accession": "AMYH01013883",
              "Gene-commentary_version": "1",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "409152793"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "AMYH01013883"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    {
                      "Seq-interval_from": "10451",
                      "Seq-interval_to": "25146",
                      "Seq-interval_strand": [
                        {
                          "@value": "minus"
                        }
                      ],
                      "Seq-interval_id": [
                        [
                          "409152793"
                        ]
                      ]
                    }
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_text": "None"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "genomic",
                "#text": "1"
              },
              "Gene-commentary_heading": "Genomic",
              "Gene-commentary_accession": "CH471131",
              "Gene-commentary_version": "2",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "74230020"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "CH471131"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "74230020"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_accession": "EAW82514",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "119602920"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "EAW82514"
                    }
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "119602920"
                        ]
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_accession": "EAW82515",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "119602921"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "EAW82515"
                    }
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "119602921"
                        ]
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_accession": "EAW82516",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "119602922"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "EAW82516"
                    }
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "119602922"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "mRNA",
              "Gene-commentary_accession": "AF372839",
              "Gene-commentary_version": "1",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "20385503"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "AF372839"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "20385503"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_accession": "AAM21315",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "20385504"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "AAM21315"
                    }
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "20385504"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "mRNA",
              "Gene-commentary_accession": "AJ304866",
              "Gene-commentary_version": "1",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "13445187"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "AJ304866"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "13445187"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_accession": "CAC34835",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "13445188"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "CAC34835"
                    }
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "13445188"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "mRNA",
              "Gene-commentary_accession": "AK026176",
              "Gene-commentary_version": "1",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "10438939"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "AK026176"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "10438939"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_accession": "BAB15382",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "10438940"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "BAB15382"
                    }
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "10438940"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "mRNA",
              "Gene-commentary_accession": "AK096296",
              "Gene-commentary_version": "1",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "21755755"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "AK096296"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "21755755"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_accession": "BAG53250",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "193788356"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "BAG53250"
                    }
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "193788356"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "mRNA",
              "Gene-commentary_accession": "AL137262",
              "Gene-commentary_version": "1",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "6807691"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "AL137262"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "6807691"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_text": "None"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "mRNA",
              "Gene-commentary_accession": "BC002740",
              "Gene-commentary_version": "2",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "33990984"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BC002740"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "33990984"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_accession": "AAH02740",
                  "Gene-commentary_version": "2",
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "31455243"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "AAH02740"
                    }
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "31455243"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "mRNA",
                "#text": "3"
              },
              "Gene-commentary_heading": "mRNA",
              "Gene-commentary_accession": "DA421921",
              "Gene-commentary_version": "1",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Nucleotide",
                      "Dbtag_tag": [
                        [
                          "81136207"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "DA421921"
                }
              ],
              "Gene-commentary_seqs": [
                [
                  [
                    [
                      "81136207"
                    ]
                  ]
                ]
              ],
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_text": "None"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "other",
                "#text": "255"
              },
              "Gene-commentary_text": "None",
              "Gene-commentary_products": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_accession": "Q8NFZ5",
                  "Gene-commentary_version": "1",
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "74715616"
                            ]
                          ]
                        }
                      ]
                    ],
                    [
                      [
                        {
                          "Dbtag_db": "UniProtKB/Swiss-Prot",
                          "Dbtag_tag": [
                            [
                              "Q8NFZ5"
                            ]
                          ]
                        }
                      ]
                    ]
                  ],
                  "Gene-commentary_seqs": [
                    [
                      [
                        [
                          "74715616"
                        ]
                      ]
                    ]
                  ]
                }
              ]
            }
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "comment",
            "#text": "254"
          },
          "Gene-commentary_heading": "Additional Links",
          "Gene-commentary_comment": [
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Evidence Viewer",
                      "Dbtag_tag": [
                        [
                          "79155"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "Evidence Viewer",
                  "Other-source_url": "http://www.ncbi.nlm.nih.gov/sutils/evv.cgi?taxid=9606&contig=NT_006051.18&gene=TNIP2&lid=79155&from=1264740&to=1279456"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_text": "MIM",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "MIM",
                      "Dbtag_tag": [
                        [
                          "610669"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "610669"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "AceView",
                      "Dbtag_tag": [
                        [
                          "79155"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "AceView",
                  "Other-source_url": "http://www.ncbi.nlm.nih.gov/IEB/Research/Acembly/av.cgi?c=geneid&org=9606&l=79155"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_text": "UCSC",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "UCSC",
                      "Dbtag_tag": [
                        [
                          "UCSC"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "UCSC",
                  "Other-source_url": "http://genome.ucsc.edu/cgi-bin/hgTracks?org=human&position=NM_024309"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "MGC",
                      "Dbtag_tag": [
                        [
                          "BC002740"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "MGC",
                  "Other-source_url": "http://mgc.nci.nih.gov/Genes/CloneList?ORG=Hs&LIST=BC002740"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "HuGE Navigator",
                      "Dbtag_tag": [
                        [
                          "79155"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "HuGE Navigator",
                  "Other-source_url": "http://hugenavigator.net/HuGENavigator/huGEPedia.do?firstQuery=TNIP2&geneID=79155&typeOption=gene&which=2&pubOrderType=pubD&typeSubmit=GO&check=y"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "KEGG",
                      "Dbtag_tag": [
                        [
                          "hsa:79155"
                        ]
                      ]
                    }
                  ],
                  "Other-source_url": "http://www.genome.jp/dbget-bin/www_bget?hsa:79155"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Reactome",
                      "Dbtag_tag": [
                        [
                          []
                        ]
                      ]
                    }
                  ],
                  "Other-source_url": "http://www.reactome.org/cgi-bin/link?SOURCE=UniProt&ID=Q8NFZ5"
                }
              ]
            }
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "ABIN-2 acts as a positive regulator of NF-kappaB-dependent transcription by activating IKKalpha.",
          "Gene-commentary_refs": [
            [
              [
                "21784860"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2011",
                  "Date-std_month": "11",
                  "Date-std_day": "26",
                  "Date-std_hour": "10",
                  "Date-std_minute": "1",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2011",
                  "Date-std_month": "11",
                  "Date-std_day": "26",
                  "Date-std_hour": "11",
                  "Date-std_minute": "25",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "A20, ABIN-1/2, and CARD11 mutations have prognostic value in gastrointestinal diffuse large B-cell lymphoma",
          "Gene-commentary_refs": [
            [
              [
                "21266526"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2011",
                  "Date-std_month": "7",
                  "Date-std_day": "23",
                  "Date-std_hour": "10",
                  "Date-std_minute": "1",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2011",
                  "Date-std_month": "7",
                  "Date-std_day": "23",
                  "Date-std_hour": "10",
                  "Date-std_minute": "52",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "Observational study of gene-disease association. (HuGE Navigator)",
          "Gene-commentary_refs": [
            [
              [
                "19898481"
              ]
            ]
          ],
          "Gene-commentary_source": [
            {
              "Other-source_src": [
                {
                  "Dbtag_db": "HuGENet",
                  "Dbtag_tag": [
                    [
                      "79155.19898481"
                    ]
                  ]
                }
              ],
              "Other-source_anchor": "HuGENet"
            }
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2009",
                  "Date-std_month": "12",
                  "Date-std_day": "2",
                  "Date-std_hour": "21",
                  "Date-std_minute": "40",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2009",
                  "Date-std_month": "12",
                  "Date-std_day": "2",
                  "Date-std_hour": "21",
                  "Date-std_minute": "41",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "The IRAK1-, IKKbeta- and PP2-independent dissociation of Tpl2 from ABIN2 is one of the two signalling events involved in activation of the native Tpl2 complex by IL-1.",
          "Gene-commentary_refs": [
            [
              [
                "19754427"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2009",
                  "Date-std_month": "11",
                  "Date-std_day": "23",
                  "Date-std_hour": "11",
                  "Date-std_minute": "3",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2010",
                  "Date-std_month": "1",
                  "Date-std_day": "21",
                  "Date-std_hour": "0",
                  "Date-std_minute": "0",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "This review defines ABIN-2 based on three different parameters: ability to bind A20; ability to inhibit NF-kappaB activation upon overexpression; the presence of specific short amino acid regions of strong homology, designated ABIN homology domains.",
          "Gene-commentary_refs": [
            [
              [
                "19464428"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2009",
                  "Date-std_month": "6",
                  "Date-std_day": "22",
                  "Date-std_hour": "11",
                  "Date-std_minute": "2",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2010",
                  "Date-std_month": "1",
                  "Date-std_day": "21",
                  "Date-std_hour": "0",
                  "Date-std_minute": "0",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "role for an ABIN-sensitive non-classical NF-kappaB signalling pathway in the proliferation of EGFR-overexpressing tumour cells.",
          "Gene-commentary_refs": [
            [
              [
                "18622428"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2008",
                  "Date-std_month": "12",
                  "Date-std_day": "15",
                  "Date-std_hour": "11",
                  "Date-std_minute": "36",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2010",
                  "Date-std_month": "1",
                  "Date-std_day": "21",
                  "Date-std_hour": "0",
                  "Date-std_minute": "0",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "transcription repressed by promyelocytic leukemia protein expression",
          "Gene-commentary_refs": [
            [
              [
                "12080044"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2007",
                  "Date-std_month": "8",
                  "Date-std_day": "25",
                  "Date-std_hour": "10",
                  "Date-std_minute": "2",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2010",
                  "Date-std_month": "1",
                  "Date-std_day": "21",
                  "Date-std_hour": "0",
                  "Date-std_minute": "0",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "optimal TPL-2 stability in vivo requires interaction with ABIN-2 as well as p105",
          "Gene-commentary_refs": [
            [
              [
                "15169888"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2007",
                  "Date-std_month": "8",
                  "Date-std_day": "25",
                  "Date-std_hour": "10",
                  "Date-std_minute": "2",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2010",
                  "Date-std_month": "1",
                  "Date-std_day": "21",
                  "Date-std_hour": "0",
                  "Date-std_minute": "0",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "Endothelial tyrosine kinase Tie2 interacts with ABIN-2.",
          "Gene-commentary_refs": [
            [
              [
                "12609966"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2007",
                  "Date-std_month": "8",
                  "Date-std_day": "25",
                  "Date-std_hour": "10",
                  "Date-std_minute": "2",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2010",
                  "Date-std_month": "1",
                  "Date-std_day": "21",
                  "Date-std_hour": "0",
                  "Date-std_minute": "0",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "Thus, CIH-mediated NF-kappaB activation may be a molecular mechanism linking OSA and cardiovascular pathologies seen in OSA patients.",
          "Gene-commentary_refs": [
            [
              [
                "16554025"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2007",
                  "Date-std_month": "8",
                  "Date-std_day": "25",
                  "Date-std_hour": "10",
                  "Date-std_minute": "2",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2010",
                  "Date-std_month": "1",
                  "Date-std_day": "21",
                  "Date-std_hour": "0",
                  "Date-std_minute": "0",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "FLIP1 has a role in the regulation of NF-kappaB activity related to the role of LKB1 in tumor suppression",
          "Gene-commentary_refs": [
            [
              [
                "12595760"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2007",
                  "Date-std_month": "8",
                  "Date-std_day": "25",
                  "Date-std_hour": "10",
                  "Date-std_minute": "2",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2010",
                  "Date-std_month": "1",
                  "Date-std_day": "21",
                  "Date-std_hour": "0",
                  "Date-std_minute": "0",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "ABIN-2 exerts unexpected function in mediating transcriptional coactivation.",
          "Gene-commentary_refs": [
            [
              [
                "12753905"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2007",
                  "Date-std_month": "8",
                  "Date-std_day": "25",
                  "Date-std_hour": "10",
                  "Date-std_minute": "2",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2010",
                  "Date-std_month": "1",
                  "Date-std_day": "21",
                  "Date-std_hour": "0",
                  "Date-std_minute": "0",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "ABIN-2 may function as a negative regulator that downregulates NF-kappaB activation during liver regeneration",
          "Gene-commentary_refs": [
            [
              [
                "16480954"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2007",
                  "Date-std_month": "8",
                  "Date-std_day": "25",
                  "Date-std_hour": "10",
                  "Date-std_minute": "2",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2010",
                  "Date-std_month": "1",
                  "Date-std_day": "21",
                  "Date-std_hour": "0",
                  "Date-std_minute": "0",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "ABIN-2 was found to inhibit endothelial apoptosis and rescue cells from death following growth factor deprivation. Deletion of the carboxy-terminus of ABIN-2 removed its ability to inhibit apoptosis.",
          "Gene-commentary_refs": [
            [
              [
                "12933576"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2007",
                  "Date-std_month": "8",
                  "Date-std_day": "25",
                  "Date-std_hour": "10",
                  "Date-std_minute": "2",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2010",
                  "Date-std_month": "1",
                  "Date-std_day": "21",
                  "Date-std_hour": "0",
                  "Date-std_minute": "0",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_text": "Thus the present results showed a correlation between NF-kappaB activation and the repair of sublethal damage in split-dose irradiation.",
          "Gene-commentary_refs": [
            [
              [
                "17038805"
              ]
            ]
          ],
          "Gene-commentary_create-date": [
            [
              [
                {
                  "Date-std_year": "2007",
                  "Date-std_month": "8",
                  "Date-std_day": "25",
                  "Date-std_hour": "10",
                  "Date-std_minute": "2",
                  "Date-std_second": "0"
                }
              ]
            ]
          ],
          "Gene-commentary_update-date": [
            [
              [
                {
                  "Date-std_year": "2010",
                  "Date-std_month": "1",
                  "Date-std_day": "21",
                  "Date-std_hour": "0",
                  "Date-std_minute": "0",
                  "Date-std_second": "0"
                }
              ]
            ]
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "generif",
            "#text": "18"
          },
          "Gene-commentary_heading": "Interactions",
          "Gene-commentary_comment": [
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "21988832"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "10015"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "PDCD6IP"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "115332"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "21988832"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "8569"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "MKNK1"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "114138"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Affinity Capture-Western; Co-localization; Reconstituted Complex; Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "21988832"
                  ]
                ],
                [
                  [
                    "14653779"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "8517"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "IKBKG"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "114089"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "21988832"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "5715"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "PSMD9"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "111687"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "21988832"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "5594"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "MAPK1"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "111580"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Affinity Capture-MS; Affinity Capture-Western; Reconstituted Complex; Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "21988832"
                  ]
                ],
                [
                  [
                    "15169888"
                  ]
                ],
                [
                  [
                    "14743216"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "4790"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "NFKB1"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "110857"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "21988832"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "2033"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "EP300"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "108347"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "21988832"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "1026"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "CDKN1A"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "107460"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Reconstituted Complex",
              "Gene-commentary_refs": [
                [
                  [
                    "21832049"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "351"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "APP"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "106848"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "1",
                      "Date-std_day": "6",
                      "Date-std_hour": "10",
                      "Date-std_minute": "28",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Affinity Capture-Western; Reconstituted Complex; Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "12753905"
                  ]
                ],
                [
                  [
                    "12595760"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "6794"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "STK11"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "112670"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2012",
                      "Date-std_month": "12",
                      "Date-std_day": "9",
                      "Date-std_hour": "10",
                      "Date-std_minute": "21",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Affinity Capture-Western; Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "12609966"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "7010"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "TEK"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "112869"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2012",
                      "Date-std_month": "11",
                      "Date-std_day": "4",
                      "Date-std_hour": "9",
                      "Date-std_minute": "21",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Affinity Capture-Western; Biochemical Activity; Protein-peptide; Reconstituted Complex",
              "Gene-commentary_refs": [
                [
                  [
                    "21784860"
                  ]
                ],
                [
                  [
                    "19763089"
                  ]
                ],
                [
                  [
                    "19373254"
                  ]
                ],
                [
                  [
                    "19285159"
                  ]
                ],
                [
                  [
                    "18212736"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "7316"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "UBC"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "113164"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2012",
                      "Date-std_month": "10",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "20",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Affinity Capture-Western; Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "21266526"
                  ]
                ],
                [
                  [
                    "18029035"
                  ]
                ],
                [
                  [
                    "11390377"
                  ]
                ],
                [
                  [
                    "11389905"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "7128"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "TNFAIP3"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "112983"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2012",
                      "Date-std_month": "10",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "20",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "12753905"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "6602"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "SMARCD1"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "112486"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2012",
                      "Date-std_month": "10",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "20",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Affinity Capture-Western",
              "Gene-commentary_refs": [
                [
                  [
                    "21784860"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "3551"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "IKBKB"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "109767"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2012",
                      "Date-std_month": "10",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "20",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Affinity Capture-MS; Affinity Capture-Western; Reconstituted Complex",
              "Gene-commentary_refs": [
                [
                  [
                    "15169888"
                  ]
                ],
                [
                  [
                    "14743216"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "1326"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "MAP3K8"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "107719"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2012",
                      "Date-std_month": "10",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "20",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Affinity Capture-Western; Biochemical Activity",
              "Gene-commentary_refs": [
                [
                  [
                    "21784860"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "1147"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "CHUK"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "107569"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2012",
                      "Date-std_month": "10",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "20",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Two-hybrid",
              "Gene-commentary_refs": [
                [
                  [
                    "15604093"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "9612"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "NCOR2"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "114974"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2011",
                      "Date-std_month": "12",
                      "Date-std_day": "4",
                      "Date-std_hour": "10",
                      "Date-std_minute": "21",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_refs": [
                [
                  [
                    "12595760"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "HPRD",
                      "Dbtag_tag": [
                        [
                          "18207"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "HPRD"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      "TNFAIP3 interacting protein 2"
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "6794"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "STK11"
                    },
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "3024670"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "Q15831"
                    }
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "28",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_refs": [
                [
                  [
                    "12753905"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "HPRD",
                      "Dbtag_tag": [
                        [
                          "18207"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "HPRD"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      "TNFAIP3 interacting protein 2"
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "6602"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "SMARCD1"
                    },
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "238054318"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "Q96GM5"
                    }
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "28",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_refs": [
                [
                  [
                    "12609966"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "HPRD",
                      "Dbtag_tag": [
                        [
                          "18207"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "HPRD"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      "TNFAIP3 interacting protein 2"
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "7010"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "TEK"
                    },
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "218511853"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "Q02763"
                    }
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "28",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_refs": [
                [
                  [
                    "14653779"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "HPRD",
                      "Dbtag_tag": [
                        [
                          "18207"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "HPRD"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      "TNFAIP3 interacting protein 2"
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "8517"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "IKBKG"
                    },
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "6685695"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "Q9Y6K9"
                    }
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "28",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_refs": [
                [
                  [
                    "12753905"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "HPRD",
                      "Dbtag_tag": [
                        [
                          "18207"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "HPRD"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      "TNFAIP3 interacting protein 2"
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "2316"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "FLNA"
                    },
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "116241365"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "P21333"
                    }
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "28",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_refs": [
                [
                  [
                    "15169888"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "HPRD",
                      "Dbtag_tag": [
                        [
                          "18207"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "HPRD"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      "TNFAIP3 interacting protein 2"
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "1326"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "MAP3K8"
                    },
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "50403742"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "P41279"
                    }
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "28",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_refs": [
                [
                  [
                    "11390377"
                  ]
                ],
                [
                  [
                    "11389905"
                  ]
                ],
                [
                  [
                    "10385526"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "HPRD",
                      "Dbtag_tag": [
                        [
                          "18207"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "HPRD"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      "TNFAIP3 interacting protein 2"
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "7128"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "TNFAIP3"
                    },
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "112894"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "P21580"
                    }
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "28",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_refs": [
                [
                  [
                    "14743216"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "HPRD",
                      "Dbtag_tag": [
                        [
                          "18207"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "HPRD"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      "TNFAIP3 interacting protein 2"
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "5966"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "REL"
                    },
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "548720"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "Q04864"
                    }
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "28",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_refs": [
                [
                  [
                    "14743216"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "HPRD",
                      "Dbtag_tag": [
                        [
                          "18207"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "HPRD"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      "TNFAIP3 interacting protein 2"
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "5970"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "RELA"
                    },
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "62906901"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "Q04206"
                    }
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "28",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_refs": [
                [
                  [
                    "14743216"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "HPRD",
                      "Dbtag_tag": [
                        [
                          "18207"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "HPRD"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      "TNFAIP3 interacting protein 2"
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "4791"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "NFKB2"
                    },
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "116242678"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "Q00653"
                    }
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "28",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_refs": [
                [
                  [
                    "15169888"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "HPRD",
                      "Dbtag_tag": [
                        [
                          "18207"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "HPRD"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      "TNFAIP3 interacting protein 2"
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "4790"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "NFKB1"
                    },
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "21542418"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "P19838"
                    }
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "28",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2009",
                      "Date-std_month": "12",
                      "Date-std_day": "17",
                      "Date-std_hour": "15",
                      "Date-std_minute": "36",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Affinity Capture-MS",
              "Gene-commentary_refs": [
                [
                  [
                    "14743216"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "5966"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "REL"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "111898"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2007",
                      "Date-std_month": "11",
                      "Date-std_day": "5",
                      "Date-std_hour": "17",
                      "Date-std_minute": "9",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Affinity Capture-MS",
              "Gene-commentary_refs": [
                [
                  [
                    "14743216"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "5970"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "RELA"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "111902"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2007",
                      "Date-std_month": "10",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "5",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "Affinity Capture-MS",
              "Gene-commentary_refs": [
                [
                  [
                    "14743216"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BioGRID",
                      "Dbtag_tag": [
                        [
                          "122573"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BioGRID"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "122573"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "4791"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "NFKB2"
                    },
                    [
                      [
                        {
                          "Dbtag_db": "BioGRID",
                          "Dbtag_tag": [
                            [
                              "110858"
                            ]
                          ]
                        }
                      ]
                    ]
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2007",
                      "Date-std_month": "10",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "5",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2013",
                      "Date-std_month": "4",
                      "Date-std_day": "7",
                      "Date-std_hour": "10",
                      "Date-std_minute": "55",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "generif",
                "#text": "18"
              },
              "Gene-commentary_text": "ABIN-2 interacts with BAF60a.",
              "Gene-commentary_refs": [
                [
                  [
                    "12753905"
                  ]
                ]
              ],
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "BIND",
                      "Dbtag_tag": [
                        [
                          "183448"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "BIND"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "34147370"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "NP_077285.2"
                    }
                  ]
                },
                {
                  "Gene-commentary_type": {
                    "@value": "peptide",
                    "#text": "8"
                  },
                  "Gene-commentary_source": [
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "GeneID",
                          "Dbtag_tag": [
                            [
                              "6602"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "SMARCD1"
                    },
                    {
                      "Other-source_src": [
                        {
                          "Dbtag_db": "Protein",
                          "Dbtag_tag": [
                            [
                              "20070148"
                            ]
                          ]
                        }
                      ],
                      "Other-source_anchor": "NP_003067.2"
                    }
                  ]
                }
              ],
              "Gene-commentary_create-date": [
                [
                  [
                    {
                      "Date-std_year": "2005",
                      "Date-std_month": "5",
                      "Date-std_day": "16",
                      "Date-std_hour": "15",
                      "Date-std_minute": "49",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ],
              "Gene-commentary_update-date": [
                [
                  [
                    {
                      "Date-std_year": "2005",
                      "Date-std_month": "5",
                      "Date-std_day": "16",
                      "Date-std_hour": "16",
                      "Date-std_minute": "12",
                      "Date-std_second": "0"
                    }
                  ]
                ]
              ]
            }
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "comment",
            "#text": "254"
          },
          "Gene-commentary_heading": "Pathways",
          "Gene-commentary_comment": [
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_text": "Reactome Event:Immune System",
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "Reactome",
                      "Dbtag_tag": [
                        [
                          "REACT_6900"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "REACT_6900",
                  "Other-source_url": "http://www.reactome.org/cgi-bin/eventbrowser_st_id?ST_ID=REACT_6900"
                }
              ]
            }
          ]
        },
        {
          "Gene-commentary_type": {
            "@value": "comment",
            "#text": "254"
          },
          "Gene-commentary_heading": "Markers (Sequence Tagged Sites/STS)",
          "Gene-commentary_comment": [
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "UniSTS",
                      "Dbtag_tag": [
                        [
                          "5182"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "SHGC-68050",
                  "Other-source_post-text": "(e-PCR)"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_label": "Alternate name",
                  "Gene-commentary_text": "RH75272"
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_label": "Alternate name",
                  "Gene-commentary_text": "RH95267"
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_label": "Alternate name",
                  "Gene-commentary_text": "STS-AA034025"
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_label": "Alternate name",
                  "Gene-commentary_text": "sts-AA034025"
                }
              ]
            },
            {
              "Gene-commentary_type": {
                "@value": "comment",
                "#text": "254"
              },
              "Gene-commentary_source": [
                {
                  "Other-source_src": [
                    {
                      "Dbtag_db": "UniSTS",
                      "Dbtag_tag": [
                        [
                          "39233"
                        ]
                      ]
                    }
                  ],
                  "Other-source_anchor": "SHGC-67741",
                  "Other-source_post-text": "(e-PCR)"
                }
              ],
              "Gene-commentary_comment": [
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_label": "Alternate name",
                  "Gene-commentary_text": "RH48566"
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_label": "Alternate name",
                  "Gene-commentary_text": "RH95179"
                },
                {
                  "Gene-commentary_type": {
                    "@value": "other",
                    "#text": "255"
                  },
                  "Gene-commentary_label": "Alternate name",
                  "Gene-commentary_text": "stSG30447"
                }
              ]
            }
          ]
        }
      ],
      "Entrezgene_unique-keys": [
        {
          "Dbtag_db": "HGNC",
          "Dbtag_tag": [
            [
              "19118"
            ]
          ]
        },
        {
          "Dbtag_db": "LocusID",
          "Dbtag_tag": [
            [
              "79155"
            ]
          ]
        },
        {
          "Dbtag_db": "MIM",
          "Dbtag_tag": [
            [
              "610669"
            ]
          ]
        }
      ],
      "Entrezgene_xtra-index-terms": [
        "LOC79155"
      ]
    }
  ]
}

 */
