package com.example.pentaho.resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.pentaho.PentahoApplication;
import com.example.pentaho.component.OpenAPISingleQueryTrackDTO;
import com.example.pentaho.component.OpenAPIStandardAddressDTO;
import com.example.pentaho.utils.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.*;

@SpringBootTest(classes = PentahoApplication.class)
@AutoConfigureMockMvc
class APIKeyResourceTest {


    private final static MultiValueMap<String, String>  VAILD_ADDRESS_ID = new LinkedMultiValueMap<>(){{
        put("addressId", Arrays.asList("BHB0915-2"));
    }};

    private final static MultiValueMap<String, String>  INVAILD_ADDRESS_ID  = new LinkedMultiValueMap<>(){{
        put("addressId", Arrays.asList("BHB0915-5"));
    }};

    private final static MultiValueMap<String, String>  EMPTY_ADDRESS_ID = new LinkedMultiValueMap<>(){{
        put("addressId", Arrays.asList(""));
    }};

    private final static String  QUERY_TRACK_URL  = "/api/api-key/query-track";

    private final static String  QUERY_TRACK_PARAM_NAMES  = "/api/api-key/query-track";



    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapperUtils objectMapperUtils;


    @Test
    void queryTrack() throws Exception {
        MvcResult response = mockMvc.perform(get(QUERY_TRACK_URL).params(VAILD_ADDRESS_ID))
                .andExpect(status().isOk()).andReturn();
        List list = objectMapperUtils.mapping(response.getResponse().getContentAsString(), List.class);
        list = list.stream().map(element -> {
            try {
                return objectMapperUtils.mapping(element, OpenAPISingleQueryTrackDTO.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        list.forEach(element -> {
            Assert.assertTrue("Expected OpenAPISingleQueryTrackDTO, but found: " + element.getClass().getName(),
                    element instanceof OpenAPISingleQueryTrackDTO);
        });
    }

    @Test
    void queryStandardAddress() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/api-key/query-standard-address").param("address", "臺北市松山區松基里慶城街４６巷１４號二樓"))
                .andExpect(status().isOk())
                .andReturn();
        OpenAPIStandardAddressDTO content = objectMapperUtils.mapping(mvcResult.getResponse().getContentAsString(), OpenAPIStandardAddressDTO.class);
        Assert.assertTrue("Expected OpenAPIStandardAddressDTO ,but fount: "+content.getClass().getName(),content instanceof  OpenAPIStandardAddressDTO);
    }

    @Test
    void queryAddressJson() throws Exception {
        mockMvc.perform(get("/api/api-key/query-single").param("address","臺北市松山區松基里慶城街４６巷１４號二樓"))
                .andExpect(status().isOk());
    }

    @Test
    void reviseAddress() throws Exception {
        mockMvc.perform(get("/api/api-key/revise-address").param("address","臺北市松山區松基里慶城街４６巷１４號二樓"))
                .andExpect(status().isOk());
    }
}