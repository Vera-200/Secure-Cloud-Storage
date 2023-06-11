package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Group {
    private int groupId;
    private int groupManagerId;
    private double groupUsage;
    private double groupStorage;
}
