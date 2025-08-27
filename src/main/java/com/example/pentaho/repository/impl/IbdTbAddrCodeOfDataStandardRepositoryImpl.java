package com.example.pentaho.repository.impl;

import com.example.pentaho.component.Address;
import com.example.pentaho.component.DataStandardAndRespositoryDTO;
import com.example.pentaho.component.IbdTbAddrCodeOfDataStandardDTO;
import com.example.pentaho.component.IbdTbIhChangeDoorplateHis;
import com.example.pentaho.repository.IbdTbAddrCodeOfDataStandardRepository;
import com.example.pentaho.utils.ObjectMapperUtils;
import com.example.pentaho.utils.Resources;
import com.example.pentaho.utils.SqlExcetor;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class IbdTbAddrCodeOfDataStandardRepositoryImpl implements IbdTbAddrCodeOfDataStandardRepository {

    private final Logger log = LoggerFactory.getLogger(IbdTbAddrCodeOfDataStandardRepositoryImpl.class);

    private final SqlExcetor sqlExcetor;

    private final ObjectMapperUtils objectMapperUtils;

    private final Resources resources;

    public IbdTbAddrCodeOfDataStandardRepositoryImpl(
            @Qualifier("H2sqlExcetor") SqlExcetor sqlExcetor,
            Resources resources,
            ObjectMapperUtils objectMapperUtils) {
        this.sqlExcetor = sqlExcetor;
        this.resources = resources;
        this.objectMapperUtils = objectMapperUtils;
    }

    @Override
    public List<IbdTbAddrCodeOfDataStandardDTO> findByAddressIdGetNumFlrPOS(List<IbdTbIhChangeDoorplateHis> IbdTbIhChangeDoorplateHisList, Address address) {
//        List<IbdTbAddrCodeOfDataStandardDTO> resultList = new ArrayList<>();
//        String sql = resources.readAsString("classpath:sql/find-by-addressIds-get-numflrpos.sql");
//        Map<String,String> params = new HashMap<>() {{
//            put("addressId", "");
//        }};
//
//        IbdTbIhChangeDoorplateHisList.forEach(his -> {
//            IbdTbAddrCodeOfDataStandardDTO dto = new IbdTbAddrCodeOfDataStandardDTO();
//            if (StringUtils.isNullOrEmpty(his.getAddressId())){
//                dto.setSeq(his.getHistorySeq());
//                dto.setAdrVersion(his.getAdrVersion());
//                dto.setFullAddress(address.getOriginalAddress());
//                dto.setJoinStep("JE621");
//                resultList.add(dto);
//            }
//
//            if (StringUtils.isNotNullOrEmpty(his.getAddressId())) {
//                params.put("addressId", his.getAddressId());
//                List<IbdTbAddrCodeOfDataStandardDTO> list = namedParameterJdbcTemplate.queryForList(sql, params, IbdTbAddrCodeOfDataStandardDTO.class);
//                if ("F".equals(his.getStatus()) && "3".equals(his.getUpdateCode())
//                ) {
//                    list.forEach(standard->standard.setJoinStep("JD721"));
//                    resultList.addAll(list);
//                }else if("X".equals(his.getStatus())){
//                    list.forEach(standard->standard.setJoinStep("JE611"));
//                    resultList.addAll(list);
//                }else{
//                    resultList.addAll(list);
//                }
//
//            }
//        });
//        return resultList;
        return null;
    }



    @Override
    public List<IbdTbAddrCodeOfDataStandardDTO> findBySeqsFromStardardDataAndDataRepository(List<Integer> seq) {
        HashMap<String, Object> param = new HashMap<>() {{
            put("SEQ", seq);
        }};
        log.info("seq:{}",seq);
        try{
            String sql = resources.readAsString("classpath:sql/addressId-query/find-by-seqs-from-stardard-data-and-data-repository.sql");
            return sqlExcetor.queryForList(sql,param,IbdTbAddrCodeOfDataStandardDTO.class);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public List<DataStandardAndRespositoryDTO> findFromDataStandardAndRepository(List<Integer> seqs) {
//        String sql = new StringBuilder()
//                .append("SELECT DISTINCT A.SEQ \n" +
//                        ",A.ADDRESS_ID\n" +
//                        ",A.FULL_ADDRESS\n" +
//                        ",A.VALIDITY\n" +
//                        ",A.COUNTY\n" +
//                        ",A.COUNTY_CD\n" +
//                        ",A.TOWN\n" +
//                        ",A.TOWN_CD\n" +
//                        ",A.POST_CODE\n" +
//                        ",A.POST_CODE_DT\n" +
//                        ",A.TC_ROAD\n" +
//                        ",A.ROAD_ID\n" +
//                        ",A.ROAD_ID_DT\n" +
//                        ",A.X\n" +
//                        ",A.Y\n" +
//                        ",A.WGS_X\n" +
//                        ",A.WGS_Y\n" +
//                        ",A.GEOHASH\n" +
//                        ",A.XY_YEAR\n" +
//                        ",A.ADR_VERSION\n" +
//                        ",A.ETLDT\n" +
//                        ",B.* \n" +
//                        "FROM addr_ods.IBD_TB_ADDR_CODE_OF_DATA_STANDARD A\n" +
//                        "inner join\n" +
//                        "( \n" +
//                        "SELECT *\n" +
//                        "FROM addr_ods.IBD_TB_ADDR_DATA_REPOSITORY_NEW\n" +
//                        "WHERE ADR_VERSION in (select max(ADR_VERSION) from addr_ods.IBD_TB_ADDR_DATA_REPOSITORY_NEW) \n" +
//                        ") B\n" +
//                        "on A.seq = B.seq\n" +
//                        "and A.ADR_VERSION = B.ADR_VERSION  \n")
//                .append("WHERE  1 = 1 \n")
//                .append(seqs.isEmpty() ? "" : "and A.SEQ in (:seqs)")
//                .toString();
//        HashMap<String, Object> params = new HashMap<>() {{
//            put("seqs", seqs);
//        }};
//        return namedParameterJdbcTemplate.queryForList(sql,params,DataStandardAndRespositoryDTO.class);
        return null;
    }

    @Override
    public List<DataStandardAndRespositoryDTO> findFromDataStandardAndRepositoryByAddressIds(List<String> addressIds) {
        String sql = new StringBuilder()
                .append("" +
                        "SELECT DISTINCT A.SEQ \n" +
                        ",A.ADDRESS_ID\n" +
                        ",A.FULL_ADDRESS\n" +
                        ",A.VALIDITY\n" +
                        ",A.COUNTY\n" +
                        ",A.COUNTY_CD\n" +
                        ",A.TOWN\n" +
                        ",A.TOWN_CD\n" +
                        ",A.POST_CODE\n" +
                        ",A.POST_CODE_DT\n" +
                        ",A.TC_ROAD\n" +
                        ",A.ROAD_ID\n" +
                        ",A.ROAD_ID_DT\n" +
                        ",A.X\n" +
                        ",A.Y\n" +
                        ",A.WGS_X\n" +
                        ",A.WGS_Y\n" +
                        ",A.GEOHASH\n" +
                        ",A.XY_YEAR\n" +
                        ",A.ADR_VERSION\n" +
                        ",A.ETLDT\n" +
                        ",B.* \n" +
                        "FROM addr_ods.IBD_TB_ADDR_CODE_OF_DATA_STANDARD A\n" +
                        "inner join\n" +
                        "( \n" +
                        "SELECT *\n" +
                        "FROM addr_ods.IBD_TB_ADDR_DATA_REPOSITORY_NEW\n" +
                        "WHERE ADR_VERSION in (select max(ADR_VERSION) from addr_ods.IBD_TB_ADDR_DATA_REPOSITORY_NEW) \n" +
                        ") B\n" +
                        "on A.seq = B.seq\n" +
                        "and A.ADR_VERSION = B.ADR_VERSION  \n")
                .append("WHERE  1 = 1 \n")
                .append(addressIds.isEmpty()?"":"and A.ADDRESS_ID in (:addressIds)")
                .toString();
        Map<String, Object> params = new HashMap<>() {{
            put("addressIds", addressIds);
        }};
        try{
          return sqlExcetor.queryForList(sql, params, DataStandardAndRespositoryDTO.class);
        }catch (Exception e){
               e.printStackTrace();
        }
        return  null;
    }
}
