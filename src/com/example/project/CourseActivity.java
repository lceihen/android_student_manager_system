package com.example.project;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CourseActivity extends Activity {
	public EditText idText;
	public EditText nameText;
	private Button lookBtn;
	private Button addBtn;
public EditText editcourseid;
public EditText editcoursename;
public int removeorupdateId;
public EditText editstudentname;
public EditText editstudentid;
private DBAdapter dbAdapter;
private ListView lv;
private Button jumpBtn;
public ArrayAdapter<String> adapter;
Course[] courses;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_activity_main);

		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		idText=(EditText)findViewById(R.id.editText11);
		nameText=(EditText)findViewById(R.id.editText12);
		lookBtn=(Button)findViewById(R.id.button1);
		addBtn=(Button)findViewById(R.id.button2);
		lv=(ListView)findViewById(R.id.listView1);
		this.registerForContextMenu(lv);
		jumpBtn=(Button)findViewById(R.id.button3);
		jumpBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myintent=new Intent(CourseActivity.this, SCActivity.class);//开始跳转
		    	finish();//关闭此Activity，开始跳转
		    	startActivity(myintent);//开始跳转
			}
			});
		lookBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String courseid=idText.getText().toString();
				if (courseid.length() == 0){courseid="";}
				courses=dbAdapter.consultCourse(courseid);
				if(courses==null){	Toast.makeText(getApplicationContext(), "操作错误", Toast.LENGTH_LONG).show();return;}
				//courses=dbAdapter.queryOneCourse(courseid);
				int len=courses.length;
				String[] info=new String[len];
				for(int i=0;i<len;i++)
				{
					info[i]=courses[i].toString();
				}
				 adapter=new ArrayAdapter<String>(CourseActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
				lv.setAdapter(adapter);
				 
			}
		});
		addBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!Right()) {
					return;
				}
				Course course=new Course();
				course.id=idText.getText().toString();
				course.name=nameText.getText().toString();
			  
				long colunm = dbAdapter.insert(course);
				if (colunm == -1) {
					Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_LONG).show();
					courses=dbAdapter.consultCourse("");
					if(courses==null){	Toast.makeText(getApplicationContext(), "操作错误", Toast.LENGTH_LONG).show();return;}
					//courses=dbAdapter.queryOneCourse(courseid);
					int len=courses.length;
					String[] info=new String[len];
					for(int i=0;i<len;i++)
					{
						info[i]=courses[i].toString();
					}
					 adapter=new ArrayAdapter<String>(CourseActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
					lv.setAdapter(adapter);
				}
				
			}
		});
		}
		catch(Exception e){Toast.makeText(getApplicationContext(), "操作错误", Toast.LENGTH_LONG).show();}	
}
	private boolean Right() {
		if (idText.getText().length() == 0
				|| nameText.getText().length() == 0
			) {
			Toast.makeText(getApplicationContext(), "请输入正确的课程信息！", Toast.LENGTH_LONG).show();
			return false;
		} else {
			return true;
		}
	}
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //获取对应的item的positon
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    	removeorupdateId = info.position;
        menu.setHeaderTitle("操作");
        menu.add(1,3,1,"删除");
        menu.add(1,4,1,"修改");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
 
        switch (item.getItemId()){
        case 3:
           {
        	   
        	   new AlertDialog.Builder(CourseActivity.this).setTitle("是否确定删除")
               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                          //点击确定触发的事件
            
       				
                    String couId=courses[removeorupdateId].id;
       			long colunm=0;
       			dbAdapter.deleteOneCourse(couId);
       				if (colunm == -1) {
       					Toast.makeText(getApplicationContext(), "删除失败，id："+couId, Toast.LENGTH_LONG).show();
       				} else {
       					Toast.makeText(getApplicationContext(), "删除成功"+couId, Toast.LENGTH_LONG).show();
       					courses=dbAdapter.consultCourse("");
    					if(courses==null){	Toast.makeText(getApplicationContext(), "操作错误", Toast.LENGTH_LONG).show();return;}
    					//courses=dbAdapter.queryOneCourse(courseid);
    					int len=courses.length;
    					String[] info=new String[len];
    					for(int i=0;i<len;i++)
    					{
    						info[i]=courses[i].toString();
    					}
    					 adapter=new ArrayAdapter<String>(CourseActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
    					lv.setAdapter(adapter);
       				}
       	
                   }
               })
               .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                           //点击取消触发的事件
                   }
               }).show();
           }
		
               
			break;
        case 4: 
        	{
        		LayoutInflater usingdialoglayoutxml=LayoutInflater.from(CourseActivity.this);
    			final View myviewondialog=usingdialoglayoutxml.inflate(R.layout.editcourse, null);
    			AlertDialog mydialoginstance=new AlertDialog.Builder(CourseActivity.this)
    			.setTitle("课程信息修改界面").setView(myviewondialog).setPositiveButton("确定",new DialogInterface.OnClickListener() {
    				//.setIcon(R.drawable.icon)
    				@Override
    				public void onClick(DialogInterface arg0, int arg1) {
    					// TODO Auto-generated method stub
    				
    					editstudentid=(EditText)myviewondialog.findViewById(R.id.editText1);
    					editstudentname=(EditText)myviewondialog.findViewById(R.id.editText2);
    				Course course=new Course();
    				course.name=editstudentname.getText().toString();
    				course.id=editstudentid.getText().toString();
        				String couId=courses[removeorupdateId].id;
        				long colunm=dbAdapter.updateOneCourse("mycourseone", couId, course);
        			if (colunm == -1) {
        				Toast.makeText(getApplicationContext(), course.id+"更新失败", Toast.LENGTH_LONG).show();
        			} else {
        				Toast.makeText(getApplicationContext(), course.id+"更新成功", Toast.LENGTH_LONG).show();
        				courses=dbAdapter.consultCourse("");
    					if(courses==null){	Toast.makeText(getApplicationContext(), "操作错误", Toast.LENGTH_LONG).show();return;}
    					//courses=dbAdapter.queryOneCourse(courseid);
    					int len=courses.length;
    					String[] info=new String[len];
    					for(int i=0;i<len;i++)
    					{
    						info[i]=courses[i].toString();
    					}
    					 adapter=new ArrayAdapter<String>(CourseActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
    					lv.setAdapter(adapter);
        			}
    				}
    			}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
    				
    				@Override
    				public void onClick(DialogInterface arg0, int arg1) {
    					// TODO Auto-generated method stub
    					
    				}
    			}).create();
    			mydialoginstance.show();
        	
        	}
            
            
        	
        	break;
        }
        return true;
    }
}