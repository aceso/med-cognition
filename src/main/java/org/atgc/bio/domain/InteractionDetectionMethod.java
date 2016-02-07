/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.FullTextIndexed;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.meta.NodeLabel;
import org.atgc.bio.meta.RelatedToVia;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.meta.Visual;
import org.atgc.bio.repository.TemplateUtils;
import java.util.Collection;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 * Interaction Detection Method in an Intact Experiment. For example:
 * 
 * "interactionDetectionMethod" : {
                "names" : {
                        "shortLabel" : "2 hybrid",
                        "fullName" : "two hybrid",
                        "alias" : [
                                {
                                        "@typeAc" : "MI:0303",
                                        "#text" : "classical two hybrid"
                                },
                                {
                                        "@typeAc" : "MI:0303",
                                        "#text" : "Gal4 transcription regeneration"
                                },
                                {
                                        "@typeAc" : "MI:0303",
                                        "#text" : "two-hybrid"
                                },
                                {
                                        "@typeAc" : "MI:0303",
                                        "#text" : "yeast two hybrid"
                                },
                                {
                                        "@typeAc" : "MI:0303",
                                        "#text" : "2-hybrid"
                                },
                                {
                                        "@typeAc" : "MI:1041",
                                        "#text" : "Y2H"
                                },
                                {
                                        "@typeAc" : "MI:0303",
                                        "#text" : "2H"
                                },
                                {
                                        "@typeAc" : "MI:0303",
                                        "#text" : "2h"
                                }
                        ]
                },
                "xref" : {
                        "primaryRef" : {
                                "@refTypeAc" : "MI:0356",
                                "@refType" : "identity",
                                "@id" : "MI:0018",
                                "@dbAc" : "MI:0488",
                                "@db" : "psi-mi"
                        },
                        "secondaryRef" : [
                                {
                                        "@refTypeAc" : "MI:0357",
                                        "@refType" : "method reference",
                                        "@id" : "1946372",
                                        "@dbAc" : "MI:0446",
                                        "@db" : "pubmed"
                                },
                                {
                                        "@refTypeAc" : "MI:0357",
                                        "@refType" : "method reference",
                                        "@id" : "10967325",
                                        "@dbAc" : "MI:0446",
                                        "@db" : "pubmed"
                                },
                                {
                                        "@refTypeAc" : "MI:0356",
                                        "@refType" : "identity",
                                        "@id" : "EBI-94",
                                        "@dbAc" : "MI:0469",
                                        "@db" : "intact"
                                },
                                {
                                        "@refTypeAc" : "MI:0358",
                                        "@refType" : "primary-reference",
                                        "@id" : "12634794",
                                        "@dbAc" : "MI:0446",
                                        "@db" : "pubmed"
                                }
                        ]
                }
        },

 * We define this object model to store the above type of data. In most
 * cases, we skip many fields.
 *
 * JSON tidyed up output of the following query:
 *
 * db.intactexperiment.find({}, {"interactionDetectionMethod" : ""});
 *
 * added an array, and removed the ObjectId that mongo generates.
 * This is useful to check if the interactionDetectionMethod have a lot of overlap. I am trying to figure out what's the UniquelyIndexed key that we should use. Should it be the experimentRef, in which the InteractionDetectionMethod is found or should it be something else. If the InteractionDetectionMethod of multiple experimentRef instances is the same, then it does not make sense duplicating that data inside neo4J as separate nodes. In that case, we can use a different key instead of experimentRef.
 * 
 * {
  "list": [
    {
      "_id": "5009dfe30364add94f9c8153",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8154",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8155",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8156",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c814e",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c814f",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8150",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8151",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8152",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009e0900364add94f9d235c",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "pull down",
          "fullName": "pull down"
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0096",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-1223",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "14755292",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c812d",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c812e",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c812f",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8130",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8131",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8132",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8134",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8135",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8136",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    },
    {
      "_id": "5009dfe30364add94f9c8137",
      "interactionDetectionMethod": {
        "names": {
          "shortLabel": "2 hybrid",
          "fullName": "two hybrid",
          "alias": [
            {
              "@typeAc": "MI:0303",
              "#text": "classical two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "Gal4 transcription regeneration"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "two-hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "yeast two hybrid"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2-hybrid"
            },
            {
              "@typeAc": "MI:1041",
              "#text": "Y2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2H"
            },
            {
              "@typeAc": "MI:0303",
              "#text": "2h"
            }
          ]
        },
        "xref": {
          "primaryRef": {
            "@refTypeAc": "MI:0356",
            "@refType": "identity",
            "@id": "MI:0018",
            "@dbAc": "MI:0488",
            "@db": "psi-mi"
          },
          "secondaryRef": [
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "1946372",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0357",
              "@refType": "method reference",
              "@id": "10967325",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            },
            {
              "@refTypeAc": "MI:0356",
              "@refType": "identity",
              "@id": "EBI-94",
              "@dbAc": "MI:0469",
              "@db": "intact"
            },
            {
              "@refTypeAc": "MI:0358",
              "@refType": "primary-reference",
              "@id": "12634794",
              "@dbAc": "MI:0446",
              "@db": "pubmed"
            }
          ]
        }
      }
    }
  ]
}
 * 
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.INTERACTION_DETECTION_METHOD)
public class InteractionDetectionMethod {
    
    protected static Logger log = LogManager.getLogger(InteractionDetectionMethod.class);
    
    /**
     * This is required for the graph database.
     */
    @GraphId
    private Long id;
    
    @Indexed(indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy(rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getExperimentRef() {
        return experimentRef;
    }

    public void setExperimentRef(String experimentRef) {
        this.experimentRef = experimentRef;
    }
    
    /**
     * The interaction detection method seems to be specific to an experiment.
     * What's methods were used in this experiment to detect the interaction.
     * It would be confusing to draw commonality between different experiments.
     * We are better off using experimentRef as the unique index key in Neo4J.
     */
    @UniquelyIndexed (indexName=IndexNames.IDM_EXPERIMENT_REF)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_EXPERIMENT_REF, rbField=BioFields.EXPERIMENT_REF)  // this is value
    private String experimentRef;

    /**
     * Interaction detection method short label as in Intact experiment.
     * For example: 2 hybrid
     */
    @Visual
    @Indexed (indexName=IndexNames.INTERACTION_DETECTION_METHOD_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.INTERACTION_DETECTION_METHOD_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)  
    private String shortLabel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortLabel() {
        return shortLabel;
    }

    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAliases() {
        return aliases;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    public Collection<BioRelation> getReferencesPubMed() {
        return referencesPubMed;
    }

    public void setReferencesPubMed(Collection<PubMed> pubMedList) {
        HashSet<BioRelation> bioRelations = new HashSet<BioRelation>();
        for (PubMed pubMed : pubMedList) {
            BioRelation bioRelation = new BioRelation(this, pubMed, BioRelTypes.REFERENCES_PUBMED);
            bioRelations.add(bioRelation);
        }
        this.referencesPubMed = bioRelations;
    }
    
    /**
     * Interaction detection method full name as in Intact experiment.
     * For example: two hybrid
     */
    @Indexed (indexName=IndexNames.INTERACTION_DETECTION_METHOD_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.INTERACTION_DETECTION_METHOD_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;
    
    /**
     * Interaction detection method aliases as in Intact experiment.
     * 
     * For eg. 
     * classical two hybrid
     * Gal4 transcription regeneration
     * two-hybrid
     */
    @FullTextIndexed(indexName = IndexNames.INTERACTION_DETECTION_METHOD_ALIAS)
    @Taxonomy (rbClass=TaxonomyTypes.INTERACTION_DETECTION_METHOD_ALIAS, rbField=BioFields.METHOD_ALIAS)
    private String aliases;
    
    /**
     * List of secondary pubMed articles.
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> referencesPubMed;
    
    public String getExperimentId() {
        return experimentRef;
    }

    public void setExperimentId(String experimentId) {
        this.experimentRef = experimentId;
    }
}