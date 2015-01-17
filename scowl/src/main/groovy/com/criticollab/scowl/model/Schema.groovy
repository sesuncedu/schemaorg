package com.criticollab.scowl.model
import org.openrdf.model.URI

/**
 * Created by ses on 1/5/15.
 */
class Schema {
    Map<URI,SDOClass> types = [:]
    Map<URI,SDOProperty> properties = [:]
    Map<URI,SDOObject>  individuals = [:]
    Map<URI,SDODataType>  datatypes = [:]


    @Override
    public String toString() {
        return "Schema{" +
                "types=" + types +
                ", properties=" + properties +
                ", individuals=" + individuals +
                ", datatypes=" + datatypes +
                "} " + super.toString();
    }
}
