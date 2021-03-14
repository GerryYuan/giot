package org.giot.core.container;

import org.giot.core.exception.ContainerStartException;
import org.giot.core.exception.ContainerStopException;

/**
 * 容器，提供生命周期的管理：准备、启动、后置等
 *
 * @author Created by gerry
 * @date 2021-02-27-11:34 PM
 */
public interface Container {

    String DEFAULT = "default";

    /**
     * 初始化容器之前进行准备操作 eg:注册当前模块下的容器所提供的服务{@link org.giot.core.service.Service}
     */
    void prepare();

    /**
     * 启动容器 eg:storage模块ESClient、MysqlClient、PGSQLClient
     */
    void start() throws ContainerStartException;

    /**
     * 容器启动后，进行后置操作 eg启异步线程处理独立任务等
     */
    void after();

    /**
     * 停止容器
     */
    void stop() throws ContainerStopException;
}
