/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.actionengine;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ActionEngineListener implements ServletContextListener {
        private ActionTimer at;
        public void contextInitialized(ServletContextEvent event) {
            try {
                //  Wait 20 seconds to start, process action queue with 5 seconds delay inbetween
                at = new ActionTimer(20000, 5000);
            } catch (Exception e)  {
                System.out.println("exception during ActionEngine initialization: " + e.toString());
            }
       }

        public void contextDestroyed(ServletContextEvent event) {
            at.cancel();
        }
    }


