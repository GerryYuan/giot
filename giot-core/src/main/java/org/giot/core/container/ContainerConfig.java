package org.giot.core.container;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 容器配置
 *
 * @author Created by gerry
 * @date 2021-02-27-11:22 PM
 */
@AllArgsConstructor
public abstract class ContainerConfig {
    /**
     * 容器名称
     */
    @Getter
    private String name;

}
