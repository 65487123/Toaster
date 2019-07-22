/*
 * Copyright (c) 2018 UTStarcom, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.demo.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataTreeIdentifier;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.DemoService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.Main;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.main.User;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 初始化类.
 * @author luzeping
 * @version 创建时间：2018年6月10日 上午9:53:09
 */
public class DemoProvider {
    private static final Logger LOG = LoggerFactory.getLogger(DemoProvider.class);
    private final DataBroker dataBroker;
    private RpcRegistration<DemoService> serviceRegistration;
    private ListenerRegistration<?> dataTreeChangeListenerRegistration;
    private final DataTreeChangeListener<User> listener;

    public DemoProvider(final DataBroker dataBroker, final DemoListenerImpl listener) {
        this.dataBroker = dataBroker;
        this.listener = listener;
    }


    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        dataTreeChangeListenerRegistration =
            dataBroker.registerDataTreeChangeListener(new DataTreeIdentifier<User>(LogicalDatastoreType.CONFIGURATION,
                InstanceIdentifier.create(Main.class).child(User.class)), listener);
        LOG.info("DemoProvider Session Initiated");

    }

    /**
     * Method called when the blueprint container is destroyed.
     */
    public void close() {
        serviceRegistration.close();
        LOG.info("DemoProvider Closed");
        if (dataTreeChangeListenerRegistration != null) {
            dataTreeChangeListenerRegistration.close();
        }
    }
}
