package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.PersonaAnalysisDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PersonaAnalysisVO implements Serializable {

    private static final long serialVersionUID = 1003871760539594777L;
    private List<PersonaAnalysisDO> genderDimension;
    private List<PersonaAnalysisDO> ageDimension;
    private List<PersonaAnalysisDO> careerDimension;
    private List<PersonaAnalysisDO> incomeDimension;
    private List<PersonaAnalysisDO> hasCarDimension;
    private List<PersonaAnalysisDO> hasHomeDimension;
    private List<PersonaAnalysisDO> educationalLevelDimension;
    private List<PersonaAnalysisDO> countryOfOriginDimension;
}
