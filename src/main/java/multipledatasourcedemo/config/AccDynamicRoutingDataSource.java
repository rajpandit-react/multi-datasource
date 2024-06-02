package multipledatasourcedemo.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class AccDynamicRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceKey();
    }
}
