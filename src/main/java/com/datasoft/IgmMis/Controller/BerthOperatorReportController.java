package com.datasoft.IgmMis.Controller;

import com.datasoft.IgmMis.Model.ImportReport.BerthOperatorReport;

import com.datasoft.IgmMis.Service.ImportReport.BerthOperatorReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
public class BerthOperatorReportController {

    @Autowired
    BerthOperatorReportService berthOperatorReportService;

    @RequestMapping(value = "/mloCodeByRotation/{rotation}", method = RequestMethod.GET)
    public ResponseEntity mloCodeByRotation(@PathVariable("rotation") String rotation){
        rotation = rotation.replace("_","/");
        return  berthOperatorReportService.mloCodeByRotation(rotation);
    }

    @RequestMapping(value = "/vslName/{rotation}", method = RequestMethod.GET)
    public ResponseEntity vslName(@PathVariable("rotation") String rotation){
        rotation = rotation.replace("_","/");
//        System.out.println("## vslName by rotation : "+berthOperatorReportService.vslName(rotation));
        return  berthOperatorReportService.vslName(rotation);
    }

    @RequestMapping(value = "/getBerthOptReport", method = RequestMethod.POST, consumes="application/json")
    public List<BerthOperatorReport> getBerthOptReport(@RequestBody BerthOperatorReport brOptRpt) throws IOException, SQLException {
// System.out.println("Controller : "+brOptRpt.toString());
        System.out.println("Controller rot : "+brOptRpt.getImport_Rotation_No());
        System.out.println("Controller mlo : "+brOptRpt.getMlocode());
        System.out.println("Controller cstatus : "+brOptRpt.getCStatus());

// System.out.println("Controller format : "+brOptRpt.getFormat());

        return berthOperatorReportService.getBerthOptReport(brOptRpt);
    }
}
