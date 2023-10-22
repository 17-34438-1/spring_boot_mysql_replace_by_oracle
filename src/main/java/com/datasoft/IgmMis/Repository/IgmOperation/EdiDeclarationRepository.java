package com.datasoft.IgmMis.Repository.IgmOperation;

import com.datasoft.IgmMis.Model.IgmOperation.EdiDeclarationModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface EdiDeclarationRepository extends CrudRepository<EdiDeclarationModel, Integer> {

    @Modifying()
    @Query(value = "UPDATE edi_stow_info\n" +
            "SET file_status='1',file_download_by=:log_id,file_download_date=NOW() \n" +
            "WHERE id=:id", nativeQuery = true)
    @Transactional
    Integer updateTodaysEdi(@Param("log_id") String log_id, @Param("id") Integer id);


}