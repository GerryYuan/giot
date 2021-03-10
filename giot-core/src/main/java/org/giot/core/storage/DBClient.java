package org.giot.core.storage;

import java.sql.SQLException;
import org.jooq.DSLContext;

/**
 * @author Created by gerry
 * @date 2021-03-10-10:31 PM
 */
public interface DBClient extends StorageClient {
    DSLContext getDSLContext() throws SQLException;
}
