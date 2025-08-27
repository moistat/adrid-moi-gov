package com.example.pentaho.repository;

import com.example.pentaho.component.Address;
import com.example.pentaho.component.DataStandardAndRespositoryDTO;
import com.example.pentaho.component.IbdTbAddrCodeOfDataStandardDTO;
import com.example.pentaho.component.IbdTbIhChangeDoorplateHis;

import java.util.List;


public interface IbdTbAddrCodeOfDataStandardRepository {


    List<IbdTbAddrCodeOfDataStandardDTO> findByAddressIdGetNumFlrPOS(List<IbdTbIhChangeDoorplateHis> addressId, Address address);

    List<IbdTbAddrCodeOfDataStandardDTO> findBySeqsFromStardardDataAndDataRepository(List<Integer> seq);

    List<DataStandardAndRespositoryDTO> findFromDataStandardAndRepository(List<Integer> seq);

    List<DataStandardAndRespositoryDTO> findFromDataStandardAndRepositoryByAddressIds(List<String> addressIds);

}
