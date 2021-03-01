package org.giot.core.container;

import org.giot.core.service.Service;

/**
 * 容器，提供生命周期的管理：准备、启动、后置等
 *
 * @author Created by gerry
 * @date 2021-02-27-11:34 PM
 */
public interface Container {
    String DEFAULT = "default";

    /**
     * 初始化容器之前进行准备操作
     */
    void prepare();

    /**
     * 启动容器，如果是storage模块，则初始化操作第三方存储的插件客户端（ESClient、MysqlClient、PGSQLClient等）
     */
    void start();

    /**
     * 容器启动后，进行后置操作，比如启异步线程处理独立任务等
     */
    void after();

    /**
     * 需要的服务
     *
     * @return
     */
    Service[] requireServices();

}
