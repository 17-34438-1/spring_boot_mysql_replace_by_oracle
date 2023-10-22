package com.datasoft.IgmMis.Controller;

import com.datasoft.IgmMis.Service.HandlingReport.HandlingReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping(value = "/HandlingReport")
    public class HandlingReportController {


    @Autowired
    private HandlingReportService handlingReportService;



    //StartHandlingReport

    @RequestMapping(value = "/HandlingReport/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getContainerHandlingReportByRotation(@PathVariable String importRotaition){

        String rotation=importRotaition.replace('_','/');
        return handlingReportService.getContainerHandlingReportByRotation(rotation);

    }



    @RequestMapping(value = "/ContainerHandlingImportReport/{importRotaition}/{work_date}",method = RequestMethod.GET)
    public @ResponseBody List ContainerHandlingImportReport(@PathVariable String importRotaition, @PathVariable String work_date){
        String rotation=importRotaition.replace('_','/');
        Integer vvdgkey=handlingReportService.getHandlingReport_vvdgkey(rotation);
        return handlingReportService.ContainerHandlingImportReport(vvdgkey,work_date);

    }



    @RequestMapping(value = "/ContainerHandlingExportReport/{importRotaition}/{work_date}",method = RequestMethod.GET)
    public @ResponseBody List ContainerHandlingExportReport(@PathVariable String importRotaition, @PathVariable String work_date){
        String rotation=importRotaition.replace('_','/');
        Integer vvdgkey=handlingReportService.getHandlingReport_vvdgkey(rotation);
        return handlingReportService.ContainerHandlingExportReport(vvdgkey,work_date);

    }


    //EndHandlingReport
}
