package com.datasoft.IgmMis.Model.ImportReport;


import java.util.List;

public class ListOfNotStrippedAssignmentDeliveryContainersMainModel {
    private List<ListOfNotStrippedAssignmentDeliveryContainersModel> strippedAssignmentDeliveryContainersModelList;
    private Integer total20;
    private Integer total40;
    private String totalContainers;

    public List<ListOfNotStrippedAssignmentDeliveryContainersModel> getStrippedAssignmentDeliveryContainersModelList() {
        return strippedAssignmentDeliveryContainersModelList;
    }

    public void setStrippedAssignmentDeliveryContainersModelList(List<ListOfNotStrippedAssignmentDeliveryContainersModel> strippedAssignmentDeliveryContainersModelList) {
        this.strippedAssignmentDeliveryContainersModelList = strippedAssignmentDeliveryContainersModelList;
    }

    public Integer getTotal20() {
        return total20;
    }

    public void setTotal20(Integer total20) {
        this.total20 = total20;
    }

    public Integer getTotal40() {
        return total40;
    }

    public void setTotal40(Integer total40) {
        this.total40 = total40;
    }

    public String getTotalContainers() {
        return totalContainers;
    }

    public void setTotalContainers(String totalContainers) {
        this.totalContainers = totalContainers;
    }
}
