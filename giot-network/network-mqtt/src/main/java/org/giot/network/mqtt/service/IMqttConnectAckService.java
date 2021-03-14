package org.giot.network.mqtt.service;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import org.giot.core.service.Service;
import org.giot.network.mqtt.exception.MqttStartException;

/**
 * @author Created by gerry
 * @date 2021-03-14-9:00 PM
 */
public interface IMqttConnectAckService extends Service {

    void ack(Channel channel, MqttConnAckMessage msg) throws MqttStartException;
}
