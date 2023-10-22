package com.datasoft.IgmMis.Controller;


import com.datasoft.IgmMis.Model.IgmOperation.EdiDeclarationModel;
import com.datasoft.IgmMis.Service.IgmOperation.TodaysEdiDeclarationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/IgmOperation")
public class IgmController {

    @Autowired
    private TodaysEdiDeclarationService todaysEdiDeclarationService;

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


}