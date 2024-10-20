/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.event.ClusterClosedEvent;
import com.mongodb.event.ClusterDescriptionChangedEvent;
import com.mongodb.event.ClusterOpeningEvent;
import java.util.EventListener;

public interface ClusterListener
extends EventListener {
    public void clusterOpening(ClusterOpeningEvent var1);

    public void clusterClosed(ClusterClosedEvent var1);

    public void clusterDescriptionChanged(ClusterDescriptionChangedEvent var1);
}

