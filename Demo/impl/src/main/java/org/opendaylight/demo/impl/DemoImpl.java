/*
 * Copyright (c) 2018 UTStarcom, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.demo.impl;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import java.util.concurrent.Future;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.DemoInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.DemoOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.DemoOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.DemoService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.Main;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.main.User;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.main.UserBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.main.UserKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现类.
 * @author luzeping
 * @version 创建时间：2019年6月10日 上午10:09:41
 */
public class DemoImpl implements DemoService {

    private static final Logger LOG = LoggerFactory.getLogger(DemoImpl.class);
    private final DataBroker db;

    public DemoImpl(final DataBroker db) {
        this.db = db;
    }

    @Override
    public Future<RpcResult<DemoOutput>> demo(final DemoInput input) {
        final DemoOutputBuilder demoBuilder = new DemoOutputBuilder();
        demoBuilder.setGreeting("demo " + input.getName());
        final UserBuilder userBuilder = new UserBuilder();
        userBuilder.setName(input.getName());
        final User user = userBuilder.build();
        final WriteTransaction tx = db.newWriteOnlyTransaction();
        tx.put(LogicalDatastoreType.CONFIGURATION,
            InstanceIdentifier.<Main>create(Main.class).child(User.class, new UserKey(input.getName())), user);
        Futures.addCallback(tx.submit(), new FutureCallback<Void>() {

            @Override
            public void onSuccess(final Void result) {
                LOG.info("Success to write Config/DS");
            }

            @Override
            public void onFailure(final Throwable error) {
                LOG.error("Failed to write Config/DS", error);
            }

        });
        return RpcResultBuilder.success(demoBuilder.build()).buildFuture();
    }
}

