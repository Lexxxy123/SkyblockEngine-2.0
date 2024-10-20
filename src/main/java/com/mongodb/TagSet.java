/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.Tag;
import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Immutable
public final class TagSet
implements Iterable<Tag> {
    private final List<Tag> wrapped;

    public TagSet() {
        this.wrapped = Collections.emptyList();
    }

    public TagSet(Tag tag) {
        Assertions.notNull("tag", tag);
        this.wrapped = Collections.singletonList(tag);
    }

    public TagSet(List<Tag> tagList) {
        Assertions.notNull("tagList", tagList);
        HashSet<String> tagNames = new HashSet<String>();
        for (Tag tag : tagList) {
            if (tag == null) {
                throw new IllegalArgumentException("Null tags are not allowed");
            }
            if (tagNames.add(tag.getName())) continue;
            throw new IllegalArgumentException("Duplicate tag names not allowed in a tag set: " + tag.getName());
        }
        ArrayList<Tag> copy = new ArrayList<Tag>(tagList);
        Collections.sort(copy, new Comparator<Tag>(){

            @Override
            public int compare(Tag o1, Tag o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        this.wrapped = Collections.unmodifiableList(copy);
    }

    @Override
    public Iterator<Tag> iterator() {
        return this.wrapped.iterator();
    }

    public boolean containsAll(TagSet tagSet) {
        return this.wrapped.containsAll(tagSet.wrapped);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        TagSet tags = (TagSet)o2;
        return this.wrapped.equals(tags.wrapped);
    }

    public int hashCode() {
        return this.wrapped.hashCode();
    }

    public String toString() {
        return "TagSet{" + this.wrapped + '}';
    }
}

