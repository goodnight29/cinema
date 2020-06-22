package com.qhit.fsaas.bo;


import com.qhit.fsaas.util.excelUtils.excel.annotation.ExcelField;

import java.io.Serializable;

public class PersonTemplate implements Serializable {

    /**
     * 客户姓名
     */
    private String name;

    /**
     * 客户喜好
     */
    private String preferred;

    public PersonTemplate() {
    }

    public PersonTemplate(String name, String preferred) {
        this.name = name;
        this.preferred = preferred;
    }

    @ExcelField(type = 0, title = "客户姓名", align = 2, sort = 1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ExcelField(type = 0, title = "客户喜好(默认/随机:null,靠过道:aisle,靠窗:windows,婴儿挂篮:basket)", align = 2, sort = 2)
    public String getPreferred() {
        return preferred;
    }

    public void setPreferred(String preferred) {
        this.preferred = preferred;
    }
}
