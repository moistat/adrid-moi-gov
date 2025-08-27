package com.example.pentaho.service;

import com.example.pentaho.component.*;
import com.example.pentaho.repository.IbdTbAddrCodeOfDataStandardRepository;
import com.example.pentaho.repository.IbdTbIhChangeDoorplateHisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SingleTrackQueryService {

    @Autowired
    private IbdTbAddrCodeOfDataStandardRepository ibdTbAddrCodeOfDataStandardRepository;

    @Autowired
    private IbdTbIhChangeDoorplateHisRepository ibdTbIhChangeDoorplateHisRepository;



    public List<OpenAPISingleQueryTrackDTO> OpenAPIQuerySingleTrack(String addressId)  {
        OpenAPISingleQueryTrackDTO dto = new OpenAPISingleQueryTrackDTO();
        List<OpenAPISingleQueryTrackDTO> result = new ArrayList();
        List<OpenAPIIbdTbIhChangeDoorplateHis> byAddressId = ibdTbIhChangeDoorplateHisRepository.OpenAPIFindByAddressId(addressId);
        if (byAddressId.isEmpty() || byAddressId == null){
            ArrayList<OpenAPIIbdTbIhChangeDoorplateHis> empty = new ArrayList<>();
            dto.setData(empty);
        }else{
            dto.setText("");
            dto.setData(byAddressId);
        }
        result.add(dto);
        return result;
    }
}
