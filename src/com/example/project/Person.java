package com.example.project;
public class Person
{
    public String id ;
    public String myClass  ;
    public String myName ;
    @Override
    public String toString()
    {
        String result = "�����˱��: "+this.id+","
        +"����:"+this.myName+","
        +"��������:"+this.myClass;
        return result;
    }
    
    
}
