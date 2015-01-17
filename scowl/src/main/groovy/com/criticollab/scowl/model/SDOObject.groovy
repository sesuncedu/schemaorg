package com.criticollab.scowl.model
import com.criticollab.scowl.io.SDOParseException
import org.openrdf.model.*
import org.openrdf.model.impl.ValueFactoryImpl
import org.openrdf.model.vocabulary.RDF
import org.openrdf.model.vocabulary.RDFS
import org.openrdf.model.vocabulary.XMLSchema
/**
 * Created by ses on 1/5/15.
 */
class SDOObject {
    public static final URI DC_SOURCE = ValueFactoryImpl.instance.createURI("http://purl.org/dc/terms/source")
    public static final URI SCHEMA_SUPERSEDEDBY = ValueFactoryImpl.instance.createURI("http://schema.org/supersededBy")
    URI uri
    String label
    String comment
    Resource source
    Set<URI> rdfTypes = []
    Set<URI> supersededBys = []

    SDOObject(URI uri, Model model) {
        this.uri = uri
        this.label = getSingleValuedString(model, RDFS.LABEL)
        model.remove(uri,RDFS.LABEL,null)
        this.comment = getSingleValuedString(model,RDFS.COMMENT)
        model.remove(uri,RDFS.COMMENT,null)
        this.source = getSingleValuedResource(model, DC_SOURCE)
        model.remove(uri,DC_SOURCE,null)

        Model typeStatements = model.filter(null, RDF.TYPE, null)
        for (Iterator<Statement> iterator = typeStatements.iterator(); iterator.hasNext();) {
            Statement statement = iterator.next()
            if(statement.object instanceof URI) {
                rdfTypes.add((URI)statement.object)
                iterator.remove();
            }
        }
        Model supersededByStatements = model.filter(null, SCHEMA_SUPERSEDEDBY, null)
        for (Iterator<Statement> iterator = supersededByStatements.iterator(); iterator.hasNext();) {
            Statement statement = iterator.next()
            if(statement.object instanceof URI) {
                supersededBys.add((URI)statement.object)
                iterator.remove();
            }
        }
    }

    String getSingleValuedString(Model model, URI predicate) {
        Set<Value> values = model.filter(null, predicate, null).objects()
        if(values.isEmpty()) {
            return null
        }
        if (values.size() > 1) {
            throw new SDOParseException("More than one value for ${predicate} : ${values}")
        }
        Value value = values.first()
        if (!(value in Literal)) {
            throw new SDOParseException("Value of ${predicate} is not a literal : ${value}")
        }
        if (value.datatype != null && !value.datatype.equals(XMLSchema.STRING)) {
            throw new SDOParseException("Value of ${predicate} is not a string : ${value.datatype}")
        }
        return value.stringValue()
    }

    Resource getSingleValuedResource(Model model, URI predicate) {
        Set<Value> values = model.filter(null, predicate, null).objects()
        if(values.isEmpty()) {
            return null
        }
        if (values.size() > 1) {
            throw new SDOParseException("More than one value for ${predicate} : ${values}")
        }
        Value value = values.first()
        if (!(value in Resource)) {
            throw new SDOParseException("Value of ${predicate} is not a resource : ${value}")
        }

        return value as Resource;
    }

    @Override
    public String toString() {
        return "SDOObject{" +
                "uri=" + uri +
                ", label='" + label + '\'' +
                ", comment='" + comment + '\'' +
                ", source=" + source +
                ", rdfTypes=" + rdfTypes +
                ", supersededBys=" + supersededBys +
                "} " + super.toString();
    }
}
