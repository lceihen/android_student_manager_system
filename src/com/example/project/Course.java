package com.example.project;

public class Course {
	   public String id ;
	    public String name  ;

	    @Override
	    public String toString()
	    {
	        String result = "�鼮ISBN: "+this.id+","
	        +"�鼮����"+this.name;
	        return result;
	    }
	    
}
