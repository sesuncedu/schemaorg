package com.criticollab.scowl.model
import org.openrdf.model.Model
import org.openrdf.model.Statement
import org.openrdf.model.URI
import org.openrdf.model.ValueFactory
import org.openrdf.model.impl.ValueFactoryImpl
import org.openrdf.model.vocabulary.OWL
import org.openrdf.model.vocabulary.RDFS

/**
 * Created by ses on 1/5/15.
 */
class SDOProperty extends SDOMetaObject{
    static ValueFactory vf = new ValueFactoryImpl()
    public static final SCHEMA_RANGEINCLUDES = vf.createURI("http://schema.org/", "rangeIncludes")
    public static final SCHEMA_DOMAININCLUDES = vf.createURI("http://schema.org/", "domainIncludes")
    public static final SCHEMA_INVERSEOF = vf.createURI("http://schema.org/", "inverseOf")
    Set<URI> rangeIncludesUris = new HashSet<>()
    Set<URI> domainIncludesUris = new HashSet<>()
    Set<URI> superPropertyUris = new HashSet<>()
    Set<URI> equivalentPropertyUris = new HashSet<>()
    Set<URI> inversePropertyUris = new HashSet<>()

    SDOProperty(URI uri,Model model) {
        super(uri,model)
        def doms=model.filter(uri,SCHEMA_DOMAININCLUDES ,null)
        for (Iterator<Statement> iterator = doms.iterator(); iterator.hasNext();) {
            Statement statement = iterator.next();
            domainIncludesUris.add(statement.object as URI)
            iterator.remove()
        }
        def ranges=model.filter(uri,SCHEMA_RANGEINCLUDES ,null)
        for (Iterator<Statement> iterator = ranges.iterator(); iterator.hasNext();) {
            Statement statement = iterator.next();
            rangeIncludesUris.add(statement.object as URI)
            iterator.remove()
        }
        def supers=model.filter(uri, RDFS.SUBPROPERTYOF ,null)
        for (Iterator<Statement> iterator = supers.iterator(); iterator.hasNext();) {
            Statement statement = iterator.next();
            superPropertyUris.add(statement.object as URI)
            iterator.remove()
        }
        def equivs=model.filter(uri, OWL.EQUIVALENTPROPERTY ,null)
        for (Iterator<Statement> iterator = equivs.iterator(); iterator.hasNext();) {
            Statement statement = iterator.next();
            equivalentPropertyUris.add(statement.object as URI)
            iterator.remove()
        }
        def inverses=model.filter(uri, SCHEMA_INVERSEOF ,null)
        for (Iterator<Statement> iterator = inverses.iterator(); iterator.hasNext();) {
            Statement statement = iterator.next();
            inversePropertyUris.add(statement.object as URI)
            iterator.remove()
        }


    }

    @Override
    public String toString() {
        return "SDOProperty{" +
                "rangeIncludesUris=" + rangeIncludesUris +
                ", domainIncludesUris=" + domainIncludesUris +
                ", superPropertyUris=" + superPropertyUris +
                ", equivalentPropertyUris=" + equivalentPropertyUris +
                ", inversePropertyUris=" + inversePropertyUris +
                "} " + super.toString();
    }
}
