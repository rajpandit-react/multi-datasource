package multipledatasourcedemo.config;

import jakarta.servlet.*;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String boxId = httpServletRequest.getHeader("boxId");
        if(boxId == null || boxId.isEmpty()){
            servletResponse.getWriter().write("BoxId is require in Header");
            servletResponse.setContentType("text/plain");
            servletResponse.setCharacterEncoding("UTF-8");
            return;
        }else {
            try{
                System.out.println("boxId=> "+boxId+ " datasource set with box1");
                DataSourceContextHolder.setDataSourceKey(boxId);
                System.out.println("boxId=> "+boxId+ " dskey=> "+DataSourceContextHolder.getDataSourceKey());
                filterChain.doFilter(servletRequest,servletResponse);
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                System.out.println("data source clear");
                DataSourceContextHolder.clearDataSourceKey();
                System.out.println("data source remove => "+DataSourceContextHolder.getDataSourceKey());
            }

        }
    }
}
