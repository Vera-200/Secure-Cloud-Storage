package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Apply {
    private int applyId;
    private double applySize;
    private String applyStatus;
    private int applyRequestId;



    public int getApplyID() {
        return applyId;
    }
    public void setApplyID(int ApplyID) {
        applyId = ApplyID;
    }
    public double getApplySize() {
        return applySize;
    }
    public void setApplySize(double ApplySize) {
        applySize = ApplySize;
    }
    public String getApplyStatus() {
        return applyStatus;
    }
    public void setApplyStatus(String ApplyStatus) {
        applyStatus = ApplyStatus;
    }
    public int getApplyRequestId() {
        return applyRequestId;
    }
    public void setApplyRequestId(int ApplyUser) {
        applyRequestId = ApplyUser;
    }
}
