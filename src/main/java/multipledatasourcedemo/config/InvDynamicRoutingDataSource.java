package multipledatasourcedemo.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class InvDynamicRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceKey();
    }
}
