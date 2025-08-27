package com.example.pentaho.repository;


import com.example.pentaho.component.IbdTbIhChangeDoorplateHis;
import com.example.pentaho.component.OpenAPIIbdTbIhChangeDoorplateHis;

import java.util.List;

public interface IbdTbIhChangeDoorplateHisRepository {

    List<OpenAPIIbdTbIhChangeDoorplateHis> OpenAPIFindByAddressId(String addressId);

    List<IbdTbIhChangeDoorplateHis> findByHistorySeq(List<String> seq);

}
