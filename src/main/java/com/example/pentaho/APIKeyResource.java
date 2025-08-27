package com.example.pentaho;

import com.example.pentaho.component.*;
import com.example.pentaho.exception.MoiException;
import com.example.pentaho.service.SingleQueryService;
import com.example.pentaho.service.SingleTrackQueryService;
import com.example.pentaho.utils.*;
import com.google.common.base.Strings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/api-key")
public class APIKeyResource {

    private static final Logger log = LoggerFactory.getLogger(APIKeyResource.class);
    @Autowired
    private SingleTrackQueryService singleTrackQueryService;

    @Autowired
    private SingleQueryService singleQueryService;

    @Autowired
    private AddressParser  addressParser;

    @Autowired
    private JoinStepDesUtil joinStepDesUtil;

    @Autowired
    private ObjectMapperUtils objectMapperUtils;


    @Operation(description = "取得指定【地址識別碼】之異動軌跡"
            , responses = {
                    @ApiResponse(responseCode = "200", description = "",
                            content = @Content(schema = @Schema(implementation = OpenAPISingleQueryTrackDTO.class))),
                  })
    @GetMapping("/query-track")
    public ResponseEntity<List<OpenAPISingleQueryTrackDTO>> queryTrack(
            @Parameter(
                    description = "編碼",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "BHB0915-2")
                    )
            ) @RequestParam("addressId") String addressId) {
        try {
            if (addressId.indexOf("\"") >= 0) {
                addressId = addressId.replaceAll("\"", "").trim();
            }
            return new ResponseEntity<>(singleTrackQueryService.OpenAPIQuerySingleTrack(addressId), HttpStatus.OK);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(description = "指定【地址】之標準格式地址"
            , responses = {
                    @ApiResponse(responseCode = "200", description = "",
                            content = @Content(schema = @Schema(implementation = Address.class))),
                    @ApiResponse(responseCode = "500", description = "",
                            content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(value = ""))
                    )})
    @GetMapping("/query-standard-address")
    public ResponseEntity<OpenAPIStandardAddressDTO> queryStandardAddress(
            @Parameter(description ="地址",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "")
            )) @RequestParam("address") String address) {
        Address addressObj = addressParser.parseAddress(address, null);
        if (StringUtils.isNotNullOrEmpty(addressObj.getContinuousNum())) {
            singleQueryService.formatCoutinuousFlrNum(addressObj.getContinuousNum(), addressObj);
        }
        addressParser.extractRoom(addressObj);
        return new ResponseEntity<>(mappingOpenAPIStandardAddress(addressObj),HttpStatus.OK);
    }


    private OpenAPIStandardAddressDTO mappingOpenAPIStandardAddress(Address address){
        NullFormatter.formatNullValue(address,Address::getZipcode,Address::setZipcode,()->"");
        NullFormatter.formatNullValue(address,Address::getCounty,Address::setCounty,()->"");
        NullFormatter.formatNullValue(address,Address::getTown,Address::setTown,()->"");
        NullFormatter.formatNullValue(address,Address::getVillage,Address::setVillage,()->"");
        NullFormatter.formatNullValue(address,Address::getNeighbor,Address::setNeighbor,()->"");
        NullFormatter.formatNullValue(address,Address::getRoad,Address::setRoad,()->"");
        NullFormatter.formatNullValue(address,Address::getSpecialArea,Address::setSpecialArea,()->"");
        NullFormatter.formatNullValue(address,Address::getArea,Address::setArea,()->"");
        NullFormatter.formatNullValue(address,Address::getLane,Address::setLane,()->"");
        NullFormatter.formatNullValue(address,Address::getAlley,Address::setAlley,()->"");
        NullFormatter.formatNullValue(address,Address::getSubAlley,Address::setSubAlley,()->"");
        NullFormatter.formatNullValue(address,Address::getRoom,Address::setRoom,()->"");
        String numFlr1 = formatNumFlr(Strings.isNullOrEmpty(address.getNumFlr1()) ? "" : address.getNumFlr1());
        String numFlr2 = formatNumFlr(Strings.isNullOrEmpty(address.getNumFlr2()) ? "" : address.getNumFlr2());
        String numFlr3 = formatNumFlr(Strings.isNullOrEmpty(address.getNumFlr3()) ? "" : address.getNumFlr3());
        String numFlr4 = formatNumFlr(Strings.isNullOrEmpty(address.getNumFlr4()) ? "" : address.getNumFlr4());
        String numFlr5 = formatNumFlr(Strings.isNullOrEmpty(address.getNumFlr5()) ? "" : address.getNumFlr5());
        String parseSuccessed = address.isParseSuccessed() ? "是" : "否";
        OpenAPIStandardAddressDTO dto;
        try {
            dto = objectMapperUtils.mapping(address, OpenAPIStandardAddressDTO.class);
            String numFrl = numFlr1 + numFlr2 + numFlr3 + numFlr4 + numFlr5;
            dto.setZipcode(address.getZipcode());
            dto.setNumFlr(numFrl);
            dto.setParseSuccessed(parseSuccessed);
        }catch (Exception e){
            dto = new OpenAPIStandardAddressDTO();
        }
        return dto;
    }

    private String formatNumFlr(String numFlrId){
       if(numFlrId.indexOf("樓")>0){
           String cleanStr = numFlrId.replace("樓", "");
           numFlrId = NumberParser.replaceWithChineseNumber(cleanStr) + "樓";
       }
       return numFlrId;
    }


    @Operation(description = "取得指定【地址】之地址識別碼相關資訊"
    )
    @GetMapping("/query-single")
    public ResponseEntity<List<OpenAPISingleQueryDTO>> queryAddressJson(
            @Parameter(
                    description = "地址、縣市、鄉鎮市區以,區隔組合成字串(順序不可改)。",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "")
                    )
            ) @RequestParam("address") String singleQueryStr) {
        try {
            List<OpenAPISingleQueryDTO> response = new ArrayList<OpenAPISingleQueryDTO>();
            if (singleQueryStr.indexOf(",") >= 0) {
                String[] params = singleQueryStr.split(",");
                SingleQueryDTO singleQueryDTO = new SingleQueryDTO();
                switch (params.length) {
                    case 1:
                        singleQueryDTO.setOriginalAddress(params[0]);
                        break;
                    case 2:
                        singleQueryDTO.setOriginalAddress(params[0]);
                        singleQueryDTO.setCounty(params[1]);
                        break;
                    case 3:
                        singleQueryDTO.setOriginalAddress(params[0]);
                        singleQueryDTO.setCounty(params[1]);
                        singleQueryDTO.setTown(params[2]);
                        break;
                    default:
                        throw new MoiException("格式輸入錯誤，請重新確認");
                }
                SingleQueryResultDTO result = singleQueryService.findJson(singleQueryDTO);
                result.getData().forEach(data->{
                    try {
                        data.setJoinStep(joinStepDesUtil.getJoinStepDes(data.getJoinStep()));
                    } catch (IOException e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                });

                result.getData().forEach(data->{
                    mappingOpenAPISingleQuery(data,response);
                });
                return ResponseEntity.ok(response);
            } else {
                SingleQueryDTO singleQueryDTO = new SingleQueryDTO();
                singleQueryDTO.setOriginalAddress(singleQueryStr);
                SingleQueryResultDTO result = singleQueryService.findJson(singleQueryDTO);
                    result.getData().forEach(data->{
                        try {
                            data.setJoinStep(joinStepDesUtil.getJoinStepDes(data.getJoinStep()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                result.getData().forEach(data->{
                    mappingOpenAPISingleQuery(data,response);
                });
                return ResponseEntity.ok(response);
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void mappingOpenAPISingleQuery(IbdTbAddrCodeOfDataStandardDTO original,List<OpenAPISingleQueryDTO> results){
        OpenAPISingleQueryDTO openAPISingleQueryDTO;
        try{
            openAPISingleQueryDTO = objectMapperUtils.mapping(original, OpenAPISingleQueryDTO.class);
            log.info("openAPISingleQueryDTO:{}",openAPISingleQueryDTO);
            results.add(openAPISingleQueryDTO);
        }catch (Exception e){
            openAPISingleQueryDTO = new OpenAPISingleQueryDTO();
            results.add(openAPISingleQueryDTO);
        }
    }


    @Operation(description = "取得指定【地址】之地址識別碼相關資訊"
    )
    @GetMapping("/revise-address")
    public ResponseEntity<List<Map<String,String>>> reviseAddress(
            @Parameter(
                    description = "來源地址(限路地名寫錯)",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "")
                    )
            ) @RequestParam("address") String originalAddressStr) {
            SingleQueryDTO singleQueryDTO = new SingleQueryDTO();
            singleQueryDTO.setOriginalAddress(originalAddressStr);
            List<Map<String, String>> resultList = singleQueryService.reviseAddress(singleQueryDTO);
            return ResponseEntity.ok(resultList);
    }

}
