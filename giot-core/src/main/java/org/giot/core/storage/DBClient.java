package org.giot.core.storage;

import java.sql.Connection;
import java.sql.SQLException;
import org.giot.core.service.Service;

/**
 * @author Created by gerry
 * @date 2021-03-09-11:06 PM
 */
public interface DBClient extends Service {

    Connection getConnection() throws SQLException;
}
