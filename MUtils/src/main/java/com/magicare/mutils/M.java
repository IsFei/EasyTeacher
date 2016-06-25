package com.magicare.mutils;

import com.magicare.mutils.http.HttpManagerImpl;
import com.magicare.mutils.task.TaskController;
import com.magicare.mutils.task.TaskControllerImpl;

/**
 * @author justin on 2015/12/10 17:37
 *         justin@magicare.me
 * @version V1.0
 */
public class M {


    public static TaskController task() {
        if (Ext.taskController == null) {
            TaskControllerImpl.registerInstance();
        }
        return Ext.taskController;
    }

    public static HttpManager http() {
        if (Ext.httpManager == null) {
            HttpManagerImpl.registerInstance();
        }
        return Ext.httpManager;
    }


    public static class Ext {
        private static TaskController taskController;
        private static HttpManager httpManager;

        private Ext() {

        }

        public static void setTaskController(TaskController controller) {
            taskController = controller;
        }

        public static void setHttpManager(HttpManager manager) {
            httpManager = manager;
        }
    }
}
