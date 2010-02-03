/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.actionengine;

import java.util.Timer;
import java.util.TimerTask;



/**
 * Date: Oct 9, 2007
*/
public class ActionTimer {
    private final Timer timer = new Timer();

    public ActionTimer(long delay, long period) {
        timer.schedule(new TimerTask() {
            @Override
			public void run() {
                ActionEngine.processActions();
             }
        }, delay, period);
    }

    public void cancel() {
        timer.cancel();
    }


}

