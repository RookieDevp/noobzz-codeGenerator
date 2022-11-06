import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author: ZZJ
 * @date: 2022/10/30
 * @desc:
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }
}
