package com.datasoft.IgmMis.Controller;


import com.datasoft.IgmMis.Model.IgmOperation.EdiDeclarationModel;
//import com.datasoft.IgmMis.Service.IgmOperation.ConvertIgmService;
import com.datasoft.IgmMis.Service.IgmOperation.TodaysEdiDeclarationService;
import com.datasoft.IgmMis.Service.IgmOperation.ViewIgmGeneralInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/IgmOperation")
public class IgmController {

    //@Autowired
    //private ConvertIgmService convertIgmService;

    @Autowired
    private TodaysEdiDeclarationService todaysEdiDeclarationService;
    @Autowired
    ViewIgmGeneralInfoService viewIgmGeneralInfoService;

    @RequestMapping(value = "/TodaysEdiDeclaration",method = RequestMethod.GET)
    @ResponseBody
    public List getExportLoadedVoyNo(){

        return todaysEdiDeclarationService.TodayEdiDeclaration();
    }

    @RequestMapping(value = "/TodaysEdiDeclarationGetVesselListId",method = RequestMethod.GET)
    @ResponseBody
    public List getVesselListId(){
        return todaysEdiDeclarationService.getVesselListId();
    }

    @RequestMapping(value = "/TodaysEdiDeclarationByRotation/{rotation_no}",method = RequestMethod.GET)
    @ResponseBody
    public List getTodayEdiDeclarationSearchByRotation(@PathVariable("rotation_no") String rotation){
        String Rotation=rotation.replace('/','_');
        return todaysEdiDeclarationService.TodayEdiDeclarationSearchByRotation(Rotation);
    }


    @RequestMapping(value = "/TodaysEdiDeclarationById/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public List getTodayEdiDeclarationSearchById(@PathVariable Integer ids){
        return todaysEdiDeclarationService.TodayEdiDeclarationById(ids);
    }


    @RequestMapping(value = "/UpdateEdi",method = RequestMethod.PUT, consumes="application/json")

    public Boolean UpdateEdiDeclarationByIdAndLogin(@RequestBody EdiDeclarationModel todaysEdiDeclaration) throws IOException {
        String file_download_by = todaysEdiDeclaration.getFile_download_by();
        Integer id = todaysEdiDeclaration.getId();
        Boolean isLaborTypeExists =todaysEdiDeclarationService.UpdateEdiDeclarationByIdAndLogin(file_download_by,id);
        return  isLaborTypeExists;
    }

    // @RequestMapping(value = "/ConvertIgm/{rotation}",method = RequestMethod.GET)
    // @ResponseBody
    // public List convertIgmContainer(@PathVariable String rotation) throws IOException {
    //     List list =new ArrayList<>();

    //     String Rotation=rotation.replace('_','/');

    //     list=convertIgmService.convertIgmContainer(Rotation);

    //     return list;

    // }

//View Igm detail
    @RequestMapping(value = "/ViewIgmList/{type}/{limit}/{start}/{status}", method= RequestMethod.GET)
    @ResponseBody
    public List getIgmContainerList(@PathVariable String type, @PathVariable Integer limit,@PathVariable Integer start, @PathVariable Integer status)  {
        List list =new ArrayList<>();

        list=viewIgmGeneralInfoService.getIgmViewList(type,limit,start,status);

        return list;
    }
    @RequestMapping(value = "/ViewIgmSearchList/{type}/{searchType}/{searchValue}", method= RequestMethod.GET)
    @ResponseBody
    public List getViewIgmSearchList(@PathVariable String type, @PathVariable String searchType,@PathVariable String searchValue)  {
        List list =new ArrayList<>();
        if(searchType.equals("Import") || searchType.equals("Export")){
            searchValue=searchValue.replace("-","/");
        }
        list=viewIgmGeneralInfoService.searchList(type,searchType,searchValue);

        return list;
    }

    @RequestMapping(value = "/UploadEdiInfo/{id}", method= RequestMethod.GET)
    @ResponseBody
    public List getUploadEdiInfo(@PathVariable String id)  {
        List list =new ArrayList<>();

        list=viewIgmGeneralInfoService.getEdiUploadFileInfo(id);

        return list;
    }

    // Upload Edi File
    @RequestMapping(value = "/uploadEdi", method= RequestMethod.POST)
    @ResponseBody
    public List uploadEdi(@RequestParam("excelfile") MultipartFile multipartFile, @RequestParam("edi") MultipartFile ediFile,
                          @RequestParam("rotation") String rotation, @RequestParam("imp_voyage") String imp_voyage,
                          @RequestParam("exp_voyage") String exp_voyage, @RequestParam("vslName") String vslName,
                          @RequestParam("imo_no") String imo_no, @RequestParam("loa") String loa,
                          @RequestParam("nrt") String nrt, @RequestParam("grt") String grt,
                          @RequestParam("flag") String flag, @RequestParam("call_sign") String call_sign, @RequestParam("beam") String beam,
                          @RequestParam("loginId") String loginId, @RequestParam("ip") String ip) throws IOException {
        List list =new ArrayList<>();
        String fileNameEdi=viewIgmGeneralInfoService.storeFile(ediFile,rotation);
        String fileNameStow=viewIgmGeneralInfoService.storeFile(multipartFile,rotation);
        list=viewIgmGeneralInfoService.insertionOrUpdateAfterEdiUpload(rotation,imp_voyage,exp_voyage,vslName,imo_no,loa,
                nrt,grt,flag,call_sign,beam,loginId,ip,fileNameEdi,fileNameStow);


        return list;
    }
    @RequestMapping(value = "/downloadEdi/{fileName}", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws FileNotFoundException {
        // Load file as Resource
        Resource resource = viewIgmGeneralInfoService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {

        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }




}