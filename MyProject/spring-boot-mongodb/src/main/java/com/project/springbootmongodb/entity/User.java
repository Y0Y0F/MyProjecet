package com.project.springbootmongodb.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "UserCollection")//Document指定数据插入到MongoDB数据库里的名字为UserCollection的集合中
public class User {

    private String id;

    private String name;

    private String familyId;

    private String familyName;

    private Integer age;

    private List<String> familyMember;


}
