package org.giot.core.container;

import org.giot.core.service.ServiceHandler;

/**
 * @author Created by gerry
 * @date 2021-02-27-11:38 PM
 */
public interface ContainerHandler {

    /**
     * 模块是否存在
     *
     * @param moduleName
     * @return
     */
    boolean has(String moduleName);

    /**
     * 模块对应的容器是否存在
     *
     * @param moduleName
     * @param containerName
     * @return
     */
    boolean has(String moduleName, String containerName);

    /**
     * 根据模块找ServiceHandler
     *
     * @param moduleName
     * @return
     */
    ServiceHandler find(String moduleName);

    /**
     * 根据模块和容器找ServiceHandler
     *
     * @param moduleName
     * @param containerName
     * @return
     */
    ServiceHandler find(String moduleName, String containerName);

}
