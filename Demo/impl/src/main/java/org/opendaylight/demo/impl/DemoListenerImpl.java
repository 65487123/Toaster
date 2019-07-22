/*
 * Copyright (c) 2018 UTStarcom, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.demo.impl;

import java.util.Collection;
import org.opendaylight.controller.md.sal.binding.api.DataObjectModification;
import org.opendaylight.controller.md.sal.binding.api.DataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataTreeModification;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.SayBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.main.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据树监听类.
 * @author luzeping
 * @version 创建时间：2018年6月10日 上午10:02:06
 */
public class DemoListenerImpl implements DataTreeChangeListener<User> {
    private static final Logger LOG = LoggerFactory.getLogger(DemoListenerImpl.class);
    private final NotificationPublishService publishService;

    public DemoListenerImpl(final NotificationPublishService publishService) {
        this.publishService = publishService;
    }

    @Override
    public void onDataTreeChanged(final Collection<DataTreeModification<User>> changes) {
        for (final DataTreeModification<User> change : changes) {
            final DataObjectModification<User> rootNode = change.getRootNode();
            switch (rootNode.getModificationType()) {
                case WRITE:
                    try {
                        publishService
                            .putNotification(new SayBuilder().setMessage(rootNode.getDataAfter().getName()).build());
                        LOG.info("{}publish success", rootNode.getDataAfter().getName());
                    } catch (final InterruptedException e) {
                        LOG.error("notification write happens a Exception", e);
                    }
                    break;
                case SUBTREE_MODIFIED:
                    try {
                        publishService
                            .putNotification(new SayBuilder().setMessage(rootNode.getDataAfter().getName()).build());
                        LOG.info("{}publish success", rootNode.getDataAfter().getName());
                    } catch (final InterruptedException e) {
                        LOG.error("notification modified happens a Exception", e);
                    }
                    break;
                case DELETE:
                    LOG.info("Delete - before : {} after : {}", rootNode.getDataBefore(), rootNode.getDataAfter());
                    break;
                default:
                    break;
            }
        }

    }

}

