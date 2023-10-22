package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.IsoCode;
import com.datasoft.IgmMis.Repository.ExportReport.IsoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IsoCodeService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    private IsoCodeRepository isoCodeRepository;

    public List<IsoCode> IsoCodeList(){
        List<IsoCode> isoCodes = new ArrayList<IsoCode>();
        List isoCodeResult[] = isoCodeRepository.isoCodeList();
        String cont_iso_type="";
        String cont_size="";
        String cont_height="";
        String type="";
        for (int i = 0; i < isoCodeResult.length; i++) {

            IsoCode isoCode=new IsoCode();
            cont_iso_type = isoCodeResult[i].get(0).toString();
            isoCode.setCont_iso_type(cont_iso_type);
            System.out.println("isoCodeResult:"+cont_iso_type );
            if(isoCodeResult[i].get(1)!=null){
                cont_size = isoCodeResult[i].get(1).toString();

                System.out.println("cont_size:"+cont_size );
            }
            else{
                cont_iso_type="";
            }

            isoCode.setCont_size(cont_size);


            if(isoCodeResult[i].get(2)!=null){
                cont_height = isoCodeResult[i].get(2).toString();

                System.out.println("cont_height:"+cont_height );
            }
            else{
                cont_height="";
            }
            isoCode.setCont_height(cont_height);



            if(isoCodeResult[i].get(3)!=null){
                type = isoCodeResult[i].get(3).toString();

                System.out.println("cont_type:"+type );
            }
            else{
                type="";
            }

            isoCode.setCont_type(type);



            isoCodes.add(isoCode);
        }
        return isoCodes ;
    }

    public List<IsoCode> IsoCodeListFor_cont_iso_type(String types){
        List<IsoCode> isoCodes = new ArrayList<IsoCode>();
        List isoCodeResult[] = isoCodeRepository.isoCode(types);
        String cont_iso_type="";
        String cont_size="";
        String cont_height="";
        String type="";
        for (int i = 0; i < isoCodeResult.length; i++) {

            IsoCode isoCode=new IsoCode();
            cont_iso_type = isoCodeResult[i].get(0).toString();
            isoCode.setCont_iso_type(cont_iso_type);
            System.out.println("isoCodeResult:"+cont_iso_type );
            if(isoCodeResult[i].get(1)!=null){
                cont_size = isoCodeResult[i].get(1).toString();
                System.out.println("cont_size:"+cont_size );
            }
            else{
                cont_iso_type="";
            }
            isoCode.setCont_size(cont_size);

            if(isoCodeResult[i].get(2)!=null){
                cont_height = isoCodeResult[i].get(2).toString();

                System.out.println("cont_height:"+cont_height );
            }
            else{
                cont_height="";
            }
            isoCode.setCont_height(cont_height);


            if(isoCodeResult[i].get(3)!=null){
                type = isoCodeResult[i].get(3).toString();

                System.out.println("cont_type:"+type );
            }
            else{
                type="";
            }

            isoCode.setCont_type(type);



            isoCodes.add(isoCode);
        }
        return isoCodes ;
    }
}
