package multipledatasourcedemo.config;

public class DataSourceContextHolder {

    private static final ThreadLocal<String> dataSourceKey = new ThreadLocal<>();

    public static String getDataSourceKey(){
        return dataSourceKey.get();
    }

    public static void setDataSourceKey(String key){
         dataSourceKey.set(key);
    }

    public static void clearDataSourceKey(){
        dataSourceKey.remove();
    }

}
