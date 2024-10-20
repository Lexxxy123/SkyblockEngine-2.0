/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.ClusterClosedEvent;
import com.mongodb.event.ClusterDescriptionChangedEvent;
import com.mongodb.event.ClusterListener;
import com.mongodb.event.ClusterOpeningEvent;
import java.util.ArrayList;
import java.util.List;

final class ClusterListenerMulticaster
implements ClusterListener {
    private static final Logger LOGGER = Loggers.getLogger("cluster.event");
    private final List<ClusterListener> clusterListeners;

    ClusterListenerMulticaster(List<ClusterListener> clusterListeners) {
        Assertions.isTrue("All ClusterListener instances are non-null", !clusterListeners.contains(null));
        this.clusterListeners = new ArrayList<ClusterListener>(clusterListeners);
    }

    @Override
    public void clusterOpening(ClusterOpeningEvent event) {
        for (ClusterListener cur : this.clusterListeners) {
            try {
                cur.clusterOpening(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising cluster opening event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void clusterClosed(ClusterClosedEvent event) {
        for (ClusterListener cur : this.clusterListeners) {
            try {
                cur.clusterClosed(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising cluster closed event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void clusterDescriptionChanged(ClusterDescriptionChangedEvent event) {
        for (ClusterListener cur : this.clusterListeners) {
            try {
                cur.clusterDescriptionChanged(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising cluster description changed event to listener %s", cur), e2);
            }
        }
    }
}

