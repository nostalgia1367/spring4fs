package config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import spring.AuthService;
import spring.ChangePasswordService;
import spring.MemberDao;
import spring.MemberRegisterService;

@Configuration
@EnableTransactionManagement
public class NestedProfileConfig {

	@Autowired
	private DataSource dataSource;
	
	@Bean
	public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager txMgr = new DataSourceTransactionManager();
		txMgr.setDataSource(dataSource);
		return txMgr;
	}

	@Bean
	public MemberDao memberDao() {
		return new MemberDao(dataSource);
	}

	@Bean
	public MemberRegisterService memberRegSvc() {
		return new MemberRegisterService(memberDao());
	}
	
	@Bean
	public ChangePasswordService changePwdSvc() {
		return new ChangePasswordService(memberDao());
	}
	
	@Bean
	public AuthService authService() {
		AuthService authSvc = new AuthService();
		authSvc.setMemberDao(memberDao());
		return authSvc;
	}
	
    @Configuration
    @Profile("dev")
    public static class DataSourceDev {
    	@Bean
    	public DataSource dataSource() {
    		ComboPooledDataSource ds = new ComboPooledDataSource();
    		try {
    			ds.setDriverClass("com.mysql.jdbc.Driver");
    		} catch (PropertyVetoException e) {
    			throw new RuntimeException(e);
    		}
    		ds.setJdbcUrl("jdbc:mysql://yolombc-database-develop-origin.cojqinpvmiky.ap-northeast-2.rds.amazonaws.com:3306/spring4fs?characterEncoding=utf8");
    		ds.setUser("spring4");
    		ds.setPassword("spring4");
    		return ds;
    	}
    }
    
    @Configuration
    @Profile("real")
    public static class DataSourceReal {
    	@Bean
    	public DataSource dataSource() {
    		ComboPooledDataSource ds = new ComboPooledDataSource();
    		try {
    			ds.setDriverClass("com.mysql.jdbc.Driver");
    		} catch (PropertyVetoException e) {
    			throw new RuntimeException(e);
    		}
    		ds.setJdbcUrl("jdbc:mysql://realhost/realdb?characterEncoding=utf8");
    		ds.setUser("realuser");
    		ds.setPassword("realpw");
    		return ds;
    	}
    }
}
