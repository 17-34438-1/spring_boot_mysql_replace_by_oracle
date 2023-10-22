package com.datasoft.IgmMis.Model.IgmOperation;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EdiDeclarationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String file_download_by;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFile_download_by() {
        return file_download_by;
    }

    public void setFile_download_by(String file_download_by) {
        this.file_download_by = file_download_by;
    }
}
