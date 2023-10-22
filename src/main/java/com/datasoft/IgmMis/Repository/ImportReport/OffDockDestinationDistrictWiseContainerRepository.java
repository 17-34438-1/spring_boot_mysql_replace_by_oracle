package com.datasoft.IgmMis.Repository.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.OffDockDestinationDistrictWiseContainerModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffDockDestinationDistrictWiseContainerRepository extends CrudRepository<OffDockDestinationDistrictWiseContainerModel,String> {
    @Query(value ="SELECT id,cont_size,cont_status,cont_height,test,mlocode,(CASE \n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Barguna%') THEN UPPER('Barguna')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Barisal%') THEN UPPER('Barisal')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Bhola%') THEN UPPER('Bhola')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Jhalokati%') THEN UPPER('Jhalokati')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Patuakhali%') THEN UPPER('Patuakhali')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Pirojpur%') THEN UPPER('Pirojpur')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Bandarban%') THEN UPPER('Bandarban')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Brahmanbaria%') THEN UPPER('Brahmanbaria')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Chandpur%') THEN UPPER('Chandpur')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Chittagong%') THEN UPPER('Chittagong')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Comilla%') THEN UPPER('Comilla')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Coxs Bazar%') THEN UPPER('Coxs Bazar')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Feni%') THEN UPPER('Feni')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Khagrachhari%') THEN UPPER('Khagrachhari')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Lakshmipur%') THEN UPPER('Lakshmipur')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Noakhali%') THEN UPPER('Noakhali')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Rangamati%') THEN UPPER('Rangamati')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Dhaka%') THEN UPPER('Dhaka')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Faridpur%') THEN UPPER('Faridpur')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Gazipur%') THEN UPPER('Gazipur')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Gopalganj%') THEN UPPER('Gopalganj')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Jamalpur%') THEN UPPER('Jamalpur')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Kishoreganj%') THEN UPPER('Kishoreganj')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Madaripur%') THEN UPPER('Madaripur')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Manikganj%') THEN UPPER('Manikganj')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Manikgonj%') THEN UPPER('Manikganj')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Munshiganj%') THEN UPPER('Munshiganj')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Mymensingh%') THEN UPPER('Mymensingh')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Narayanganj%') THEN UPPER('Narayanganj')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Narayangonj%') THEN UPPER('Narayanganj')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Narsingdi%') THEN UPPER('Narsingdi')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Netrakona%') THEN UPPER('Netrakona')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Rajbari%') THEN UPPER('Rajbari')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Shariatpur%') THEN UPPER('Shariatpur')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Sherpur%') THEN UPPER('Sherpur')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Tangail%') THEN UPPER('Tangail')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Bagerhat%') THEN UPPER('Bagerhat')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Chuadanga%') THEN UPPER('Chuadanga')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Jessore%') THEN UPPER('Jessore')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Jhenaidah%') THEN UPPER('Jhenaidah')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Khulna%') THEN UPPER('Khulna')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Kushtia%') THEN UPPER('Kushtia')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Magura%') THEN UPPER('Magura')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Meherpur%') THEN UPPER('Meherpur')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Narail%') THEN UPPER('Narail')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Satkhira%') THEN UPPER('Satkhira')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Bogra%') THEN UPPER('Bogra')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Joypurhat%') THEN UPPER('Joypurhat')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Naogaon%') THEN UPPER('Naogaon')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Natore%') THEN UPPER('Natore')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Nawabganj%') THEN UPPER('Nawabganj')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Pabna%') THEN UPPER('Pabna')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Rajshahi%') THEN UPPER('Rajshahi')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Sirajganj%') THEN UPPER('Sirajganj')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Dinajpur%') THEN UPPER('Dinajpur')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Gaibandha%') THEN UPPER('Gaibandha')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Kurigram%') THEN UPPER('Kurigram')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Lalmonirhat%') THEN UPPER('Lalmonirhat')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Nilphamari%') THEN UPPER('Nilphamari')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Panchagarh%') THEN UPPER('Panchagarh')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Rangpu%') THEN UPPER('Rangpu')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Thakurgaon%') THEN UPPER('Thakurgaon')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Habiganj%') THEN UPPER('Habiganj')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Moulvibazar%') THEN UPPER('Moulvibazar')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Sunamganj%') THEN UPPER('Sunamganj')\n" +
            "WHEN UPPER(NotifyDesc) LIKE UPPER('%Sylhet%') THEN UPPER('Sylhet')\n" +
            "ELSE 'Other'\n" +
            "END\n" +
            " ) AS dist FROM (\n" +
            "SELECT DISTINCT cont_number AS id,cont_size,cont_status,cont_height,NotifyDesc AS test,mlocode,\n" +
            "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(UPPER(CONVERT(NotifyDesc USING latin1)),'.',''),'-',''),',',''),'  ',' '),' ',''),'\\r\\n',''),'\\n',''),'^',''),'\\t','') AS NotifyDesc\n" +
            "FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "INNER JOIN organization_profiles ON igm_detail_container.off_dock_id= organization_profiles.id \n" +
            "WHERE Import_Rotation_No=:importRotation AND igm_detail_container.off_dock_id='2591'\n" +
            ") AS tmp ORDER BY dist",nativeQuery = true )
    public List<OffDockDestinationDistrictWiseContainerModel> getOffDockDestinationDistrictWiseContainerList(@Param("importRotation") String importRotation);//@Param("offDockId") Integer offDockId
}
