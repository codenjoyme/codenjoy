package com.epam.dojo.icancode.model.items;

import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.enums.FeatureItem;
import com.epam.dojo.icancode.model.interfaces.IField;

/**
 * Created by Mikhail_Udalyi on 05.07.2016.
 */
public class FieldItem extends BaseItem {

    protected IField field;

    public FieldItem(Elements element) {
        super(element);
    }

    public FieldItem(Elements element, FeatureItem[] features) {
        super(element, features);
    }

    public void setField(IField value) {
        field = value;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
