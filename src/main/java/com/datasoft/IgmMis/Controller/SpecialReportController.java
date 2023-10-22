package com.datasoft.IgmMis.Controller;

import com.datasoft.IgmMis.Model.SpecialReport.FeederDischargeSummary;
import com.datasoft.IgmMis.Service.SpecialReport.FeederDischargeSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/specialReport")
public class SpecialReportController {
    @Autowired
    private FeederDischargeSummaryService feederDischargeSummaryService;

    @RequestMapping(value = "/feederDischargeSummary/{rotation}", method = RequestMethod.GET)
    public ResponseEntity<FeederDischargeSummary> feederDischargeSummary(@PathVariable("rotation") String rotation) {
        rotation = rotation.replace('_', '/');
        FeederDischargeSummary feederDischargeSummary = feederDischargeSummaryService.feederDischargeSummary(rotation);
        return new ResponseEntity<>(feederDischargeSummary, HttpStatus.OK);
    }

}
