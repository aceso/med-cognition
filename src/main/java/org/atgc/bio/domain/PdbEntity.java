/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.domain.types.StructureEntityTypes;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.meta.NodeLabel;
import org.atgc.bio.meta.PartKey;
import org.atgc.bio.meta.RelatedTo;
import org.atgc.bio.meta.RelatedToVia;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniqueCompoundIndex;
import org.atgc.bio.meta.Visual;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 * Each structure has one or more entities or molecules. The entities are of different
 * entity types. {@link StructureEntityTypes}
 * Each entity is present or found on one or more chains.
 *
 * <pre>
   > db.pdbentity.findOne({"@structureId" : "4HHB"})
{
	"_id" : ObjectId("51a1494f0364ef1b44f04904"),
	"@structureId" : "4HHB",
	"@bioAssemblies" : "1",
	"@release_date" : "Tue Jul 17 00:00:00 PDT 1984",
	"@resolution" : "1.74",
	"Method" : {
		"@name" : "xray"
	},
	"Entity" : [
		{
			"@id" : "1",
			"@entityType" : "protein",
			"Chain" : [
				{
					"@id" : "A"
				},
				{
					"@id" : "C"
				}
			]
		},
		{
			"@id" : "2",
			"@entityType" : "protein",
			"Chain" : [
				{
					"@id" : "B"
				},
				{
					"@id" : "D"
				}
			]
		}
	]
}

 * </pre>
 *
 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.PDB_ENTITY, field1=BioFields.STRUCTURE_ID, field2=BioFields.ENTITY_ID, field3=BioFields.ENTITY_TYPE)
@BioEntity(bioType = BioTypes.PDB_ENTITY)
public class PdbEntity {
    protected static Logger log = LogManager.getLogger(PdbEntity.class);

    @GraphId
    private Long id;

    /**
     * The entityId is @id in the pdbentity collection.
     */
    @Taxonomy(rbClass=TaxonomyTypes.PDB_ENTITY_ENTITY_ID, rbField=BioFields.ENTITY_ID)
    @PartKey
    private String entityId;

    @Taxonomy(rbClass=TaxonomyTypes.PDB_ENTITY_STRUCTURE_ID, rbField=BioFields.STRUCTURE_ID)
    @PartKey
    private String structureId;

    @Visual
    @Indexed(indexName=IndexNames.PDB_ENTITY_ENTITY_TYPE)
    private StructureEntityTypes entityType;

    @NodeLabel
    @Indexed(indexName=IndexNames.PDB_ENTITY_MESSAGE)
    @Taxonomy(rbClass=TaxonomyTypes.PDB_ENTITY_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.IS_A_PROTEIN, elementClass = BioRelation.class)
    private BioRelation proteinRelation;

    @RelatedToVia(direction = Direction.OUTGOING, relType = BioRelTypes.HAS_CHAIN, elementClass = BioRelation.class)
    private HashSet<BioRelation> chainRelations;

    public Protein getProtein() {
        if (proteinRelation == null) {
            return null;
        }
        return (Protein)proteinRelation.getEndNode();
    }

    /**
     * A PDB entity (usually a protein) is found on one or more chains. As far as we
     * know, each chain has only one entity. At least that's how the API for <code>Chain</code>
     * works.
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
     * A PDB entity (usually a protein) is found on one or more chains. As far as we
     * know, each chain has only one entity. At least that's how the API for <code>Chain</code>
     * works.
     *
     * @param chains
     */
    public void setChains(HashSet<Chain> chains) {
        if (chainRelations == null) {
            chainRelations = new HashSet<BioRelation>();
        }
        for (Chain chain : chains) {
            chainRelations.add(new BioRelation(this, chain, BioRelTypes.HAS_CHAIN));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * The entityId is usually 1, 2, 3,.... Each entity is a model and corresponds to
     * a protein, dna or other entity types. {@link StructureEntityTypes}
     *
     * @return String
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * The entityId is usually 1, 2, 3,.... Each entity is a model and corresponds to
     * a protein, dna or other entity types. {@link StructureEntityTypes}
     *
     * @param entityId
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    /**
     * Each structure has a structureId and contains entities.
     *
     * @return String
     */
    public String getStructureId() {
        return structureId;
    }

    /**
     * Each structure has a structureId and contains entities.
     *
     * @param structureId
     */
    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    /**
     * Return an entity type has mentioned in {@link StructureEntityTypes}
     *
     * @return StructureEntityTypes
     */
    public StructureEntityTypes getEntityType() {
        return entityType;
    }

    /**
     * Return an entity type has mentioned in {@link StructureEntityTypes}
     *
     * @param entityType
     */
    public void setEntityType(StructureEntityTypes entityType) {
        this.entityType = entityType;
    }

    /**
     * Get the auto generated message label.
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message label to be displayed which is auto-generated.
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}