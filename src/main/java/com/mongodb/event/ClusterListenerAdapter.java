/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.event.ClusterClosedEvent;
import com.mongodb.event.ClusterDescriptionChangedEvent;
import com.mongodb.event.ClusterListener;
import com.mongodb.event.ClusterOpeningEvent;

public abstract class ClusterListenerAdapter
implements ClusterListener {
    @Override
    public void clusterOpening(ClusterOpeningEvent event) {
    }

    @Override
    public void clusterClosed(ClusterClosedEvent event) {
    }

    @Override
    public void clusterDescriptionChanged(ClusterDescriptionChangedEvent event) {
    }
}

