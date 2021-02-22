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
				Intent myintent=new Intent(Student.this, CourseActivity.class);//��ʼ��ת
		    	finish();//�رմ�Activity����ʼ��ת
		    	startActivity(myintent);//��ʼ��ת
			}
			});
		
		lookBtn.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			String peopleId=noTxt.getText().toString();
			if (peopleId.length() == 0){peopleId="";}
			
			peoples=dbAdapter.consultStudent(peopleId);	

			if(peoples==null){	Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_LONG).show();return;}
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
				e.printStackTrace(); //�������д�ӡ������Ϣ
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
					Toast.makeText(getApplicationContext(), "���ʧ��", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "��ӳɹ�", Toast.LENGTH_LONG).show();
					peoples=dbAdapter.consultStudent("");	

					if(peoples==null){	Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_LONG).show();return;}
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
				new AlertDialog.Builder(Student.this).setTitle("�Ƿ�ȷ���޸�")
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                           //���ȷ���������¼�
        				if (!Right()) {
        					return;
        				}
        				Person person = new Person();
        				person.myName=nameTxt.getText().toString();
        				person.myClass=classTxt.getText().toString();
        				person.id=noTxt.getText().toString();
        				long colunm=dbAdapter.updateOneData("student", person.id, person);
        			if (colunm == -1) {
        				displayTxt.setText("���´���");
        			} else {
        				displayTxt.setText("�ɹ���������  " );
        			}
                    }
                })
                .setNegativeButton("����", new DialogInterface.OnClickListener() {
 
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            //���ȡ���������¼�
                    }
                }).show();
	
			}
		});
		deleteBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(Student.this).setTitle("�Ƿ�ȷ��ɾ��")
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                           //���ȷ���������¼�
                		
        				if (!Right()) {
        					return;
        				}
        				 String peopleId =noTxt.getText().toString();
        			long colunm=dbAdapter.deleteOneData(peopleId);
        				if (colunm == -1) {
        					displayTxt.setText("��Ӵ���");
        				} else {
        					displayTxt.setText("�ɹ�ɾ������  " );
        				}
        	
                    }
                })
                .setNegativeButton("����", new DialogInterface.OnClickListener() {
 
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            //���ȡ���������¼�
                    }
                }).show();
		
			}
		});*/
	}		

	private boolean Right() {
		if (noTxt.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "��������ȷ��ѧ����Ϣ��", Toast.LENGTH_LONG).show();
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
			Toast.makeText(getApplicationContext(), "��������ȷ��ѧ����Ϣ��", Toast.LENGTH_LONG).show();
			return false;
		} else {
			return true;
		}
	}

	 
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //��ȡ��Ӧ��item��positon
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    	removeorupdateId = info.position;
        menu.setHeaderTitle("����");
        menu.add(1,1,1,"ɾ��");
        menu.add(1,2,1,"�޸�");
    }
 
    @Override
    public boolean onContextItemSelected(MenuItem item) {
 
        switch (item.getItemId()){
        case 1:
           {
        	   
        	   new AlertDialog.Builder(Student.this).setTitle("�Ƿ�ȷ��ɾ��")
               .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                          //���ȷ���������¼�
               		
       			//	if (!Right()) {
       				//	return;
       				//}
       				int deleteId=removeorupdateId;
                    String stuId=peoples[deleteId].id;
       			long colunm=0;
       					
       					try{dbAdapter.deleteOneData(stuId);	}
       					catch(Exception  e){
       						Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_LONG).show();
       					}
       				if (colunm == -1) {
       					Toast.makeText(getApplicationContext(), "ɾ��ʧ�ܣ�id��"+stuId, Toast.LENGTH_LONG).show();
       				} else {
       					Toast.makeText(getApplicationContext(), "ɾ���ɹ�"+stuId, Toast.LENGTH_LONG).show();
       					peoples=dbAdapter.consultStudent("");	

       					if(peoples==null){	Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_LONG).show();return;}
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
               .setNegativeButton("����", new DialogInterface.OnClickListener() {

                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                           //���ȡ���������¼�
                   }
               }).show();
           }
		
               
			break;
        case 2: 
        	{
        		LayoutInflater usingdialoglayoutxml=LayoutInflater.from(Student.this);
    			final View myviewondialog=usingdialoglayoutxml.inflate(R.layout.editstudent, null);
    			AlertDialog mydialoginstance=new AlertDialog.Builder(Student.this)
    			.setTitle("ѧ����Ϣ�޸Ľ���").setView(myviewondialog).setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
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
        				Toast.makeText(getApplicationContext(), person.id+"����ʧ��", Toast.LENGTH_LONG).show();
        			} else {
        				Toast.makeText(getApplicationContext(), person.id+"���³ɹ�", Toast.LENGTH_LONG).show();
        				peoples=dbAdapter.consultStudent("");	

        				if(peoples==null){	Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_LONG).show();return;}
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
    			}).setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {
    				
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

	
			
	

