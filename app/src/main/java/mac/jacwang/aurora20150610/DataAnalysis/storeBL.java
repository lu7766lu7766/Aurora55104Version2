package mac.jacwang.aurora20150610.DataAnalysis;

import java.util.List;

public class storeBL {

    public List<t_store> analysisStoreList(String storeJson){

        return new storeDA().analysisStoreList(storeJson);
    }
}
