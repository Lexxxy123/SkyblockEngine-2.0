/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import java.util.Arrays;
import java.util.List;
import org.bson.conversions.Bson;

public class Facet {
    private final String name;
    private final List<? extends Bson> pipeline;

    public Facet(String name, List<? extends Bson> pipeline) {
        this.name = name;
        this.pipeline = pipeline;
    }

    public Facet(String name, Bson ... pipeline) {
        this(name, Arrays.asList(pipeline));
    }

    public String getName() {
        return this.name;
    }

    public List<? extends Bson> getPipeline() {
        return this.pipeline;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        Facet facet = (Facet)o2;
        if (this.name != null ? !this.name.equals(facet.name) : facet.name != null) {
            return false;
        }
        return this.pipeline != null ? this.pipeline.equals(facet.pipeline) : facet.pipeline == null;
    }

    public int hashCode() {
        int result = this.name != null ? this.name.hashCode() : 0;
        result = 31 * result + (this.pipeline != null ? this.pipeline.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Facet{name='" + this.name + '\'' + ", pipeline=" + this.pipeline + '}';
    }
}

