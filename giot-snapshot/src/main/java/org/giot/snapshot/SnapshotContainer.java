package org.giot.snapshot;

import org.giot.core.container.AbstractContainer;
import org.giot.core.container.Container;
import org.giot.core.container.ContainerConfig;
import org.giot.core.exception.ContainerStartException;
import org.giot.core.snapshot.SnapshotModule;
import org.giot.snapshot.service.IStorageSnapshotService;
import org.giot.snapshot.service.StorageSnapshotService;

/**
 * @author Created by gerry
 * @date 2021-03-08-10:34 PM
 */
public class SnapshotContainer extends AbstractContainer {

    private SnapshotConfig config;

    @Override
    public String name() {
        return Container.DEFAULT;
    }

    @Override
    public String module() {
        return SnapshotModule.NAME;
    }

    @Override
    public ContainerConfig createConfig() {
        this.config = new SnapshotConfig();
        return config;
    }

    @Override
    public void prepare() {
        super.register(IStorageSnapshotService.class, new StorageSnapshotService(config, getContainerManager()));
    }

    @Override
    public void start() throws ContainerStartException {
        System.out.println(SnapshotModule.NAME + "执行start方法");
    }

    @Override
    public void after() {
        System.out.println(SnapshotModule.NAME + "执行after方法");
    }
}
