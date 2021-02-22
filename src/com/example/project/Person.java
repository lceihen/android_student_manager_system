package com.example.project;
public class Person
{
    public String id ;
    public String myClass  ;
    public String myName ;
    @Override
    public String toString()
    {
        String result = "借书人编号: "+this.id+","
        +"姓名:"+this.myName+","
        +"所属部门:"+this.myClass;
        return result;
    }
    
    
}
