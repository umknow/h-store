/* This file is part of VoltDB.
 * Copyright (C) 2008-2010 VoltDB L.L.C.
 *
 * VoltDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VoltDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VoltDB.  If not, see <http://www.gnu.org/licenses/>.
 */

/* WARNING: THIS FILE IS AUTO-GENERATED
            DO NOT MODIFY THIS SOURCE
            ALL CHANGES MUST BE MADE IN THE CATALOG GENERATOR */

package org.voltdb.catalog;

/**
 * A index structure on a database table's columns
 */
public class Index extends CatalogType {

    boolean m_unique;
    int m_type;
    CatalogMap<ColumnRef> m_columns;

    void setBaseValues(Catalog catalog, CatalogType parent, String path, String name) {
        super.setBaseValues(catalog, parent, path, name);
        m_fields.put("unique", m_unique);
        m_fields.put("type", m_type);
        m_columns = new CatalogMap<ColumnRef>(catalog, this, path + "/" + "columns", ColumnRef.class);
        m_childCollections.put("columns", m_columns);
    }

    void update() {
        m_unique = (Boolean) m_fields.get("unique");
        m_type = (Integer) m_fields.get("type");
    }

    /** GETTER: May the index contain duplicate keys? */
    public boolean getUnique() {
        return m_unique;
    }

    /** GETTER: What data structure is the index using and what kinds of keys does it support? */
    public int getType() {
        return m_type;
    }

    /** GETTER: Columns referenced by the index */
    public CatalogMap<ColumnRef> getColumns() {
        return m_columns;
    }

    /** SETTER: May the index contain duplicate keys? */
    public void setUnique(boolean value) {
        m_unique = value; m_fields.put("unique", value);
    }

    /** SETTER: What data structure is the index using and what kinds of keys does it support? */
    public void setType(int value) {
        m_type = value; m_fields.put("type", value);
    }

}
