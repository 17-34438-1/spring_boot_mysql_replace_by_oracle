package com.datasoft.IgmMis.Repository.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.IsoCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IsoCodeRepository extends CrudRepository<IsoCode,Long>{
    @Query(value = "SELECT DISTINCT cont_iso_type,cont_size,cont_height,cont_type FROM igm_detail_container", nativeQuery = true)
    public List[] isoCodeList();

    @Query(value = "SELECT DISTINCT cont_iso_type,cont_size,cont_height,cont_type FROM igm_detail_container where cont_iso_type=:type", nativeQuery = true)
    public List[] isoCode(@Param("type")String type);
}
