package com.example.project;

public class SCinfo {
	public String id ;
    public String studentId  ;
    public String courseId ;
    public String stuname ;
    public String couname ;
    @Override
    public String toString()
    {
        String result = "���� : "+this.stuname+","
        	
        +"ѧ��:"+this.studentId+","
        + "�γ����� : "+this.couname+",";
        return result;
    }
}
