package com.example.project;



import android.widget.Toast;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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

public class Student extends Activity {
	private DBAdapter dbAdapter;
	private Button lookBtn;
//	private Button editBtn;
//	private Button deleteBtn;
	private Button addBtn;
	private Button jumpBtn;
	private Button photoBtn;
	private EditText noTxt;
	private EditText nameTxt; 
	private EditText classTxt;
	public String id;
	private ListView lv;
	public ArrayAdapter<String> adapter;
	//private DBOpenHelper dbOpenHelper;
	public int removeorupdateId;
	Person[] peoples;
	public EditText editstudentname;
	public EditText editstudentid;
	public EditText editstudentclass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_activity_main);
		startService(new Intent(Student.this, MusicService.class));
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		lookBtn=(Button)findViewById(R.id.button1);
		addBtn=(Button)findViewById(R.id.button2);
		//editBtn=(Button)findViewById(R.id.button3);
		//deleteBtn=(Button)findViewById(R.id.button4);
		noTxt=(EditText)findViewById(R.id.editText1);
		nameTxt=(EditText)findViewById(R.id.editText2);
		classTxt=(EditText)findViewById(R.id.editText3);
		photoBtn=(Button)findViewById(R.id.button4);
		lv=(ListView)findViewById(R.id.listView1);
		jumpBtn=(Button)findViewById(R.id.button3);
		this.registerForContextMenu(lv);
		jumpBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myintent=new Intent(Student.this, CourseActivity.class);//开始跳转
		    	finish();//关闭此Activity，开始跳转
		    	startActivity(myintent);//开始跳转
			}
			});
		
		lookBtn.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			String peopleId=noTxt.getText().toString();
			if (peopleId.length() == 0){peopleId="";}
			
			peoples=dbAdapter.consultStudent(peopleId);	

			if(peoples==null){	Toast.makeText(getApplicationContext(), "操作错误", Toast.LENGTH_LONG).show();return;}
			int len=peoples.length;
			String[] info=new String[len];
			for(int i=0;i<len;i++)
			{
				info[i]=peoples[i].toString();
			}
			 adapter=new ArrayAdapter<String>(Student.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
			lv.setAdapter(adapter);
			 
		}
	});
		photoBtn.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			Intent intent2=new Intent();
			try{intent2.setAction("android.media.action.IMAGE_CAPTURE");
			intent2.addCategory("android.intent.category.DEFAULT");
	
			startActivity(intent2);	}
			catch(Exception  e){
				e.printStackTrace(); //在命令行打印错误信息
				System.out.println(e.toString());
				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
			}
			
			
		}
	});
		addBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!isRight()) {
					return;
				}
				Person person = new Person();
				person.myName=nameTxt.getText().toString();
				person.myClass=classTxt.getText().toString();
				person.id=noTxt.getText().toString();
			  
			
					long colunm = dbAdapter.insert(person);	

				if (colunm == -1) {
					Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_LONG).show();
					peoples=dbAdapter.consultStudent("");	

					if(peoples==null){	Toast.makeText(getApplicationContext(), "操作错误", Toast.LENGTH_LONG).show();return;}
					int len=peoples.length;
					String[] info=new String[len];
					for(int i=0;i<len;i++)
					{
						info[i]=peoples[i].toString();
					}
					 adapter=new ArrayAdapter<String>(Student.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
					lv.setAdapter(adapter);
					
				}
				
			}
		});
	/*	editBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(Student.this).setTitle("是否确定修改")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                           //点击确定触发的事件
        				if (!Right()) {
        					return;
        				}
        				Person person = new Person();
        				person.myName=nameTxt.getText().toString();
        				person.myClass=classTxt.getText().toString();
        				person.id=noTxt.getText().toString();
        				long colunm=dbAdapter.updateOneData("student", person.id, person);
        			if (colunm == -1) {
        				displayTxt.setText("更新错误");
        			} else {
        				displayTxt.setText("成功更新数据  " );
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
		});
		deleteBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(Student.this).setTitle("是否确定删除")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                           //点击确定触发的事件
                		
        				if (!Right()) {
        					return;
        				}
        				 String peopleId =noTxt.getText().toString();
        			long colunm=dbAdapter.deleteOneData(peopleId);
        				if (colunm == -1) {
        					displayTxt.setText("添加错误");
        				} else {
        					displayTxt.setText("成功删除数据  " );
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
		});*/
	}		

	private boolean Right() {
		if (noTxt.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "请输入正确的学生信息！", Toast.LENGTH_LONG).show();
			return false;
		} else {
			return true;
		}
	}
	
	private boolean isRight() {
		if (noTxt.getText().length() == 0
				|| nameTxt.getText().length() == 0
				|| classTxt.getText().length() == 0
			) {
			Toast.makeText(getApplicationContext(), "请输入正确的学生信息！", Toast.LENGTH_LONG).show();
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
        menu.add(1,1,1,"删除");
        menu.add(1,2,1,"修改");
    }
 
    @Override
    public boolean onContextItemSelected(MenuItem item) {
 
        switch (item.getItemId()){
        case 1:
           {
        	   
        	   new AlertDialog.Builder(Student.this).setTitle("是否确定删除")
               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                          //点击确定触发的事件
               		
       			//	if (!Right()) {
       				//	return;
       				//}
       				int deleteId=removeorupdateId;
                    String stuId=peoples[deleteId].id;
       			long colunm=0;
       					
       					try{dbAdapter.deleteOneData(stuId);	}
       					catch(Exception  e){
       						Toast.makeText(getApplicationContext(), "操作错误", Toast.LENGTH_LONG).show();
       					}
       				if (colunm == -1) {
       					Toast.makeText(getApplicationContext(), "删除失败，id："+stuId, Toast.LENGTH_LONG).show();
       				} else {
       					Toast.makeText(getApplicationContext(), "删除成功"+stuId, Toast.LENGTH_LONG).show();
       					peoples=dbAdapter.consultStudent("");	

       					if(peoples==null){	Toast.makeText(getApplicationContext(), "操作错误", Toast.LENGTH_LONG).show();return;}
       					int len=peoples.length;
       					String[] info=new String[len];
       					for(int i=0;i<len;i++)
       					{
       						info[i]=peoples[i].toString();
       					}
       					 adapter=new ArrayAdapter<String>(Student.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
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
        case 2: 
        	{
        		LayoutInflater usingdialoglayoutxml=LayoutInflater.from(Student.this);
    			final View myviewondialog=usingdialoglayoutxml.inflate(R.layout.editstudent, null);
    			AlertDialog mydialoginstance=new AlertDialog.Builder(Student.this)
    			.setTitle("学生信息修改界面").setView(myviewondialog).setPositiveButton("确定",new DialogInterface.OnClickListener() {
    				//.setIcon(R.drawable.icon)
    				@Override
    				public void onClick(DialogInterface arg0, int arg1) {
    					// TODO Auto-generated method stub
    				
    					editstudentid=(EditText)myviewondialog.findViewById(R.id.editText1);
    					editstudentname=(EditText)myviewondialog.findViewById(R.id.editText2);
    					editstudentclass=(EditText)myviewondialog.findViewById(R.id.editText3);
    					Person person = new Person();
    					person.myName=editstudentname.getText().toString();
        				person.myClass=editstudentclass.getText().toString();
        				person.id=editstudentid.getText().toString();
        				String stuId=peoples[removeorupdateId].id;
        				long colunm=dbAdapter.updateOneData("student", stuId, person);
        		
        			if (colunm == -1) {
        				Toast.makeText(getApplicationContext(), person.id+"更新失败", Toast.LENGTH_LONG).show();
        			} else {
        				Toast.makeText(getApplicationContext(), person.id+"更新成功", Toast.LENGTH_LONG).show();
        				peoples=dbAdapter.consultStudent("");	

        				if(peoples==null){	Toast.makeText(getApplicationContext(), "操作错误", Toast.LENGTH_LONG).show();return;}
        				int len=peoples.length;
        				String[] info=new String[len];
        				for(int i=0;i<len;i++)
        				{
        					info[i]=peoples[i].toString();
        				}
        				 adapter=new ArrayAdapter<String>(Student.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	

}

	
			
	

