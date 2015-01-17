package com.criticollab.scowl.io
import com.criticollab.scowl.model.*
import org.openrdf.model.Model
import org.openrdf.model.Resource
import org.openrdf.model.Statement
import org.openrdf.model.URI
import org.openrdf.model.ValueFactory
import org.openrdf.model.impl.LinkedHashModel
import org.openrdf.model.impl.ValueFactoryImpl
import org.openrdf.model.vocabulary.RDF
import org.openrdf.model.vocabulary.RDFS
import org.openrdf.rio.RDFFormat
import org.openrdf.rio.RDFHandler
import org.openrdf.rio.RDFParser
import org.openrdf.rio.Rio
import org.openrdf.rio.helpers.BufferedGroupingRDFHandler
import org.openrdf.rio.helpers.StatementCollector
/**
 * Created by ses on 1/5/15.
 */
class SDOParser {
    Schema schema = new Schema()
    static ValueFactory vf = new ValueFactoryImpl()
    public static final SCHEMA_DATATYPE = vf.createURI("http://schema.org/", "DataType")

    Schema parse(String filename) {
        LinkedHashModel model = loadModelFromFile(filename)


        def subjects = new ArrayList<Resource>(model.subjects())
        subjects.each { subject ->
            def entityModel = model.filter(subject, null, null)
            processEntity(subject as URI, entityModel)
        }
        int n=1;
        for (Statement statement : model) {
            println "${n++}: ${statement}"
        }
        schema
    }

    def processEntity(URI uri, Model model) {
        def types = model.filter(null, RDF.TYPE, null).objects()
        if (RDF.PROPERTY in types) {
            schema.properties.put(uri,new SDOProperty(uri,model))
        } else if (RDFS.CLASS in types) {
            if (SCHEMA_DATATYPE in model.filter(uri, RDFS.SUBCLASSOF, null).objects()) {
                schema.datatypes.put(uri, new SDODataType(uri,model))
            } else {
                schema.types.put(uri, new SDOClass(uri,model))

            }
        } else {
            schema.individuals.put(uri, new SDOObject(uri,model))
        }
        model.remove(null,RDF.TYPE,null)
    }


    private LinkedHashModel loadModelFromFile(String filename) {
        Model model = new LinkedHashModel()
        def reader = new BufferedReader(new FileReader(filename))
        RDFParser parser = Rio.createParser(RDFFormat.RDFA)
        RDFHandler sc = new StatementCollector(model)
        sc = new BufferedGroupingRDFHandler(sc)
        parser.setRDFHandler(sc)
        parser.parse(reader, "http://schema.org/")
        model
    }
}
