package com.example.pentaho.repository.impl;


import com.example.pentaho.component.IbdTbIhChangeDoorplateHis;
import com.example.pentaho.component.OpenAPIIbdTbIhChangeDoorplateHis;
import com.example.pentaho.repository.IbdTbIhChangeDoorplateHisRepository;
import com.example.pentaho.utils.Resources;
import com.example.pentaho.utils.SqlExcetor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class IbdTbIhChangeDoorplateHisRepositoryImpl implements IbdTbIhChangeDoorplateHisRepository {

    private final Logger log = LoggerFactory.getLogger(IbdTbAddrCodeOfDataStandardRepositoryImpl.class);

    private final SqlExcetor sqlExcetor;


    private final Resources resources;

    public IbdTbIhChangeDoorplateHisRepositoryImpl(
            @Qualifier("H2sqlExcetor") SqlExcetor sqlExcetor,
            Resources resources) {
        this.sqlExcetor = sqlExcetor;
        this.resources = resources;
    }



    @Override
    public List<OpenAPIIbdTbIhChangeDoorplateHis> OpenAPIFindByAddressId(String addressId) {
        HashMap<String, Object> param = new HashMap<>() {{
            put("addressId", addressId);
        }};
        String query = resources.readAsString("classpath:sql/addressId-track/openapi-single-query-track.sql");
        return sqlExcetor.queryForList(query, param,OpenAPIIbdTbIhChangeDoorplateHis.class);
    }


    @Override
    public List<IbdTbIhChangeDoorplateHis> findByHistorySeq(List<String> seq) {
        List<Integer> seqWhenEmpty = new ArrayList<>();
        seqWhenEmpty.add(-1);
        Map<String,Object> params = new HashMap<>(){{
            put("SEQ", seq.isEmpty() ? seqWhenEmpty: seq);
        }};
        String query ="SELECT \n" +
                "ADDRESS_ID , \n" +
                "STATUS , \n" +
                "HISTORY_SEQ , \n" +
                "ADR_VERSION , \n" +
                "UPDATE_CODE \n"+
                "FROM ADDR_ODS.IBD_TB_IH_CHANGE_DOORPLATE_HIS \n" +
                "WHERE HISTORY_SEQ IN (:SEQ) \n" +
                "AND ADR_VERSION IN (SELECT MAX( ADR_VERSION ) FROM ADDR_ODS.IBD_TB_IH_CHANGE_DOORPLATE_HIS)";
        return sqlExcetor.queryForList(query,params, IbdTbIhChangeDoorplateHis.class);
    }

}
