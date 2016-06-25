package com.ace.easyteacher.DataBase;


import org.xutils.db.annotation.Column;

import java.io.Serializable;

public class BaseBean implements Serializable{
    @Column(name = "id",isId = true)
    private int id;
}
