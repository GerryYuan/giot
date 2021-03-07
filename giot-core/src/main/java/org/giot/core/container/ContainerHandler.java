package org.giot.core.container;

import org.giot.core.service.ServiceHandler;

/**
 * @author Created by gerry
 * @date 2021-02-27-11:38 PM
 */
public interface ContainerHandler {

    boolean has(String moduleName);

    ServiceHandler find(String moduleName);

}
