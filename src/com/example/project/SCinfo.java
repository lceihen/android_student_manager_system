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
        String result = "姓名 : "+this.stuname+","
        	
        +"学号:"+this.studentId+","
        + "课程名称 : "+this.couname+",";
        return result;
    }
}
