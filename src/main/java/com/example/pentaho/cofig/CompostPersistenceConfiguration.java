package com.example.pentaho.cofig;


import com.example.pentaho.utils.SqlExcetor;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class CompostPersistenceConfiguration {

    @Bean(name = "H2")
    public DataSource dataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
//        dataSource.setURL("jdbc:h2:file:./h2/testdb");
        dataSource.setURL(" jdbc:h2:mem:testdb");
        dataSource.setUser("sa");
        dataSource.setPassword("1qaz@WSX");
        return dataSource;
    }

    @Bean(name = "H2Template")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("H2") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "H2sqlExcetor")
    public SqlExcetor sqlExcetor(@Qualifier("H2Template") NamedParameterJdbcTemplate namedParameterJdbcTemplate){
       return new SqlExcetor(namedParameterJdbcTemplate);
    }
}
