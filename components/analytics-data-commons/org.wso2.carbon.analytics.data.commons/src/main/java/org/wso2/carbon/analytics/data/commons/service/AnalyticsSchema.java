/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.analytics.data.commons.service;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents a schema for the analytics tables.
 */
public class AnalyticsSchema implements Serializable {

    private static final long serialVersionUID = -4696693702436657326L;

    private Map<String, ColumnDefinition> columns;

    private transient Map<String, ColumnDefinition> indexedColumns = null;

    /**
     * primary keys are set separately rather than having in {@link ColumnDefinition}
     * to optimize the lookup of just the primary keys when required
     */
    private List<String> primaryKeys;

    public AnalyticsSchema() {
    }

    public AnalyticsSchema(List<ColumnDefinition> columns, List<String> primaryKeys) {
        if (columns != null) {
            this.columns = new LinkedHashMap<>(columns.size());
            for (ColumnDefinition column : columns) {
                this.columns.put(column.getName(), column);
            }
        }
        this.primaryKeys = primaryKeys;
        /* we should always have the same primary key order, or order to
         * not to depend on the order of the primary key list in generating
         * the hashcode */
        if (this.primaryKeys != null) {
            Collections.sort(this.primaryKeys);
        }
    }

    public Map<String, ColumnDefinition> getColumns() {
        return columns;
    }

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public static enum ColumnType {
        STRING,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        BOOLEAN,
        BINARY,
        FACET
    }

    @Override
    public int hashCode() {
        int hash1 = 0, hash2 = 0;
        if (this.columns != null) {
            hash1 = this.columns.hashCode();
        }
        if (this.primaryKeys != null) {
            hash2 = this.primaryKeys.hashCode();
        }
        return hash1 * hash2;
    }

    @Override
    public boolean equals(Object rhs) {
        if (!(rhs instanceof AnalyticsSchema)) {
            return false;
        }
        AnalyticsSchema other = (AnalyticsSchema) rhs;
        if (this.getColumns() != null && !this.getColumns().equals(other.getColumns())) {
            return false;
        }
        if (this.getPrimaryKeys() != null && !this.getPrimaryKeys().equals(other.getPrimaryKeys())) {
            return false;
        }
        return true;
    }
}
