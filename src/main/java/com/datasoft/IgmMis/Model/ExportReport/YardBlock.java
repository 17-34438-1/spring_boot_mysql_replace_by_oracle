package com.datasoft.IgmMis.Model.ExportReport;

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
public class YardBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String terminal;
    private String block_unit;
    private String block;
    private String block_cpa;
    private String amount;
    private String capacity_teus;
    private String capacity_feus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getBlock_unit() {
        return block_unit;
    }

    public void setBlock_unit(String block_unit) {
        this.block_unit = block_unit;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getBlock_cpa() {
        return block_cpa;
    }

    public void setBlock_cpa(String block_cpa) {
        this.block_cpa = block_cpa;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCapacity_teus() {
        return capacity_teus;
    }

    public void setCapacity_teus(String capacity_teus) {
        this.capacity_teus = capacity_teus;
    }

    public String getCapacity_feus() {
        return capacity_feus;
    }

    public void setCapacity_feus(String capacity_feus) {
        this.capacity_feus = capacity_feus;
    }
}
