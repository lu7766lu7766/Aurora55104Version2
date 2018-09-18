package mac.jacwang.aurora20150610.DataAnalysis;

import java.util.List;

public class ADBL {

    public List<data_model> analysisListAD(String storeJson){

        return new ADDA().analysisListAD(storeJson);
    }

    public List<data_model> analysisHalfAD(String storeJson){

        return new ADDA().analysisHalfAD(storeJson);
    }
}
