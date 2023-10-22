package com.datasoft.IgmMis.Controller;


import com.datasoft.IgmMis.Service.Pangaon.ConvertPanGaonContainerService;
import com.datasoft.IgmMis.Service.Pangaon.UploadExcelFileForPangaonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/Pangaon")
public class Pangaon {
    @Autowired
    private UploadExcelFileForPangaonService uploadExcelFileForPangaonService;
    @Autowired
    private ConvertPanGaonContainerService convertPanGaonContainerService;

    // Upload Excel File For Pangaon
    @RequestMapping(value = "/UploadExcelFileForPangaon", method= RequestMethod.POST)
    @ResponseBody
    public List uploadExcelFileForPangaon(@RequestParam("excelfile") MultipartFile multipartFile, @RequestParam("loginId") String loginId, @RequestParam("ip") String ip) throws IOException {
        List list =new ArrayList<>();
        uploadExcelFileForPangaonService.storeFile(multipartFile);
         list=uploadExcelFileForPangaonService.uploadExcelFile(multipartFile,loginId,ip);

        return list;
    }
    @RequestMapping(value = "/ConvertPangaonContainer/{visit}",method = RequestMethod.GET)
    @ResponseBody
    public  List convertPangaonContainer(@PathVariable String visit) throws IOException {
        List list =new ArrayList<>();
        list=convertPanGaonContainerService.convertPangaonContainer(visit);

        return list;

    }
}
