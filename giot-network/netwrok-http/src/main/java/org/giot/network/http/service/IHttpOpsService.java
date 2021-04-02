package org.giot.network.http.service;

import org.giot.core.service.Service;

/**
 * @author Created by gerry
 * @date 2021-03-31-21:31
 */
public interface IHttpOpsService extends Service {
    void start();

    void shutdown();
}
