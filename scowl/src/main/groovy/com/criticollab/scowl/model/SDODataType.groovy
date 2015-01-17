package com.criticollab.scowl.model

import org.openrdf.model.Model
import org.openrdf.model.Statement
import org.openrdf.model.URI
import org.openrdf.model.vocabulary.RDFS

/**
 * Created by ses on 1/5/15.
 */
class SDODataType extends SDOMetaObject {
    Set<URI> superclassUris = new HashSet<URI>()

    SDODataType(URI uri, Model model) {
        super(uri, model)
        def supers=model.filter(uri,RDFS.SUBCLASSOF ,null)
        for (Iterator<Statement> iterator = supers.iterator(); iterator.hasNext();) {
            Statement statement = iterator.next()
            superclassUris.add(statement.object as URI)
            iterator.remove()
        }

    }

    @Override
    public String toString() {
        return "SDODataType{" +
                "superclassUris=" + superclassUris +
                "} " + super.toString();
    }
}
