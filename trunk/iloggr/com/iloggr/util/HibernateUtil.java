/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.util;



import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.MySQLInnoDBDialect;


public final class HibernateUtil {


//    static final ConsoleAppender appender = new ConsoleAppender(new PatternLayout());
     private static final AnnotationConfiguration annotationConfiguration =  new AnnotationConfiguration();
    private static final Logger log = Logger.getLogger(HibernateUtil.class);
    static {
        log.info("com.iloggr.util.HibernateUtil.java initialization");
//       log.addAppender(appender);
    }

    

    public static SessionFactory sessionFactory = null;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null)
            sessionFactory = createDefaultSessionFactory();
        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory newSessionFactory) {
        sessionFactory = newSessionFactory;
    }

    private static SessionFactory createDefaultSessionFactory() {
        try {
             configure(annotationConfiguration, null);
             log.info("Configured sessionFactory with annotation configuration");
             return
                    annotationConfiguration
                            .setNamingStrategy(new ImprovedNamingStrategy())
                            .buildSessionFactory();
         } catch (Throwable e) {
            log.error("Initial SessionFactory creation failed.", e);
            throw new ExceptionInInitializerError(e);
        }
    }

   
    public static void rebuildDb(boolean buildSchema) {
     try {
        getSessionFactory();
        if (!buildSchema) return;
        Connection connection = sessionFactory.openStatelessSession().connection();
        try {
            connection.setAutoCommit(false);

            tearDownDb();

            for (String schemaDdl : annotationConfiguration.generateSchemaCreationScript(new MySQLInnoDBDialect())) {
                log.info("Executing setup DDL: " + schemaDdl);
                connection.createStatement().execute(schemaDdl);
            }
            connection.commit();

        } catch (SQLException e) {
            log.error("HibernateUtil - rebuildDb: " + e.toString());
            connection.rollback();
            throw e;

        } finally {
            connection.close();
        }
     } catch (Exception e) {
         log.error("HibernateUtil - rebuildDb: Cannot get a session: " + e.toString());
     }
    }

    private static void tearDownDb(
            /*Configuration configuration,
            SessionFactory sf,
            Dialect dialect*/) throws SQLException {

        Connection connection = sessionFactory.openStatelessSession().connection();
        try {
            connection.setAutoCommit(false);
            for (String schemaDdl : annotationConfiguration.generateDropSchemaScript(new  MySQLInnoDBDialect())) {
                log.info("Executing teardown DDL: " +  schemaDdl);
                try {
                    connection.createStatement().execute(schemaDdl);
                } catch (Exception e) {
                    // since it is a teardown, continue but log problem
                    log.error("Exception during teardown operation: " + schemaDdl + ", exception: " + e.toString());
                }
            }

            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;

        } finally {
            connection.close();
        }
    }

     private static void configure(Configuration configuration, URL hibernateCfgXml) {
        if (hibernateCfgXml != null) {
            log.info("configuring from custom: " + hibernateCfgXml);
            configuration.configure(hibernateCfgXml);
        } else {
            try {
                URL defaultConfiguration =   
                     Thread.currentThread().getContextClassLoader().getResource("Hibernate.cfg.xml");

            if(defaultConfiguration != null) {
                log.info("configuring from default configuration: " + defaultConfiguration);
                configuration.configure(defaultConfiguration);
            }
            } catch (Exception e)       {
                  log.info("cannot load default configuration URL: " + e.toString());
            }

        }
    }

      private HibernateUtil() {
        BasicConfigurator.configure();
       
        /* utility */ }
 
 

    


}


