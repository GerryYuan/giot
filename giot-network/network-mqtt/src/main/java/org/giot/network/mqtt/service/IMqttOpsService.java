package org.giot.network.mqtt.service;

import org.giot.core.service.Service;

/**
 * @author Created by gerry
 * @date 2021-03-14-6:31 PM
 */
public interface IMqttOpsService extends Service {
    /**
     * 连接mqtt broker
     */
    void connect() throws InterruptedException;

    /**
     * 关闭处理
     */
    void shutdown() throws InterruptedException;
}
