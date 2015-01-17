package com.criticollab.scowl.model

import org.openrdf.model.Model
import org.openrdf.model.URI

/**
 * Created by ses on 1/5/15.
 */
class SDOMetaObject extends SDOObject{
    SDOMetaObject(URI uri,Model model) {
        super(uri,model)
    }

    @Override
    public String toString() {
        return "SDOMetaObject{} " + super.toString();
    }
}
