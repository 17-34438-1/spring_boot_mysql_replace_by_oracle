package com.datasoft.IgmMis.Controller;

import com.datasoft.IgmMis.Model.ResponseMessage;
import com.datasoft.IgmMis.Service.ExportReport.YardBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/yard")
public class YardBlockController {
    @Autowired
    private YardBlockService yardBlockService;

    ResponseMessage responseMessage;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    Object yardList(){
        List<Object> yardList = new ArrayList<Object>();
        String exception = null;
        try {
            yardList = yardBlockService.yardList();
            return yardList;
        }catch(Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        return yardList;
    }
}
