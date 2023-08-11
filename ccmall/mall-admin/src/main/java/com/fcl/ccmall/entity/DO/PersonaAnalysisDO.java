package com.fcl.ccmall.entity.DO;

import lombok.Data;

import java.io.Serializable;

@Data
public class PersonaAnalysisDO implements Serializable {
    private static final long serialVersionUID = -7649280481618716677L;
    private Integer count;
    private String tag;
}
