package com.example.project;

public class SC {
	public String id ;
    public String studentId  ;
    public String courseId ;
    @Override
    public String toString()
    {
        String result = "�����˱��:"+this.studentId+","
        +"�鼮ISBN:"+this.courseId;
        return result;
    }
}

