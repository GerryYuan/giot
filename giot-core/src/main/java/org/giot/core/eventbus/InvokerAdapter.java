package org.giot.core.eventbus;

import java.util.List;
import org.giot.core.service.Service;

/**
 * @author Created by gerry
 * @date 2021-04-17-21:48
 */
public interface InvokerAdapter extends Service {

    List<BusInvoker> adapters();

    boolean supports(BusInvoker busInvoker);
}
