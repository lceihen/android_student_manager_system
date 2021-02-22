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

public class SCActivity extends Activity {
	public EditText courseidTxt;
	public EditText studentidTxt;
	private Button lookBtn;
	private Button addBtn;
	private Button infoBtn;
	private DBAdapter dbAdapter;
	private Button jumpBtn;
	public String id;
	private ListView lv;
	public ArrayAdapter<String> adapter;
	public int removeorupdateId;
	SC[] scs;
	SCinfo[] scinfo;
	public EditText editstudentname;
	public EditText editstudentid;
	private EditText editstuid;
	private EditText editcouid;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sc_activity_main);
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		courseidTxt=(EditText)findViewById(R.id.editText1);
		studentidTxt=(EditText)findViewById(R.id.editText2);
		lookBtn=(Button)findViewById(R.id.button1);
		addBtn=(Button)findViewById(R.id.button2);
		jumpBtn=(Button)findViewById(R.id.button3);
		infoBtn=(Button)findViewById(R.id.button4);
		jumpBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myintent=new Intent(SCActivity.this, Student.class);//��ʼ��ת
		    	finish();//�رմ�Activity����ʼ��ת
		    	startActivity(myintent);//��ʼ��ת
			}
			});
		lv=(ListView)findViewById(R.id.listView1);
			this.registerForContextMenu(lv);
		lookBtn.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
		
			String peopleId=studentidTxt.getText().toString();
			if (peopleId.length() == 0){peopleId="";}
			scs=dbAdapter.consultSC(peopleId);
			if(scs==null){	Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_LONG).show();return;}
			int len=scs.length;
			String[] info=new String[len];
			for(int i=0;i<len;i++)
			{
				info[i]=scs[i].toString();
			}
			 adapter=new ArrayAdapter<String>(SCActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
			lv.setAdapter(adapter);
			 
		}
	});
		infoBtn.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
		
			String peopleId=studentidTxt.getText().toString();
			if (peopleId.length() == 0){peopleId="";}
			try{
				scinfo=dbAdapter.consultSCinfo();
				}
			catch(Exception  e){
				
			}
			
			
			int len=scinfo.length;
			String[] info=new String[len];
			for(int i=0;i<len;i++)
			{
				info[i]=scinfo[i].toString();
			}
			 adapter=new ArrayAdapter<String>(SCActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
			lv.setAdapter(adapter);
			 
		}
	});
		addBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!isRight()) {
					return;
				}
				SC sc = new SC();
				sc.studentId=studentidTxt.getText().toString();
				sc.courseId=courseidTxt.getText().toString();
				sc.id=sc.studentId+sc.courseId;
			  
				long colunm = dbAdapter.insert(sc);
				if (colunm == -1) {
					Toast.makeText(getApplicationContext(), "���ʧ��", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "��ӳɹ�", Toast.LENGTH_LONG).show();
					scs=dbAdapter.consultSC("");
					if(scs==null){	Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_LONG).show();return;}
					int len=scs.length;
					String[] info=new String[len];
					for(int i=0;i<len;i++)
					{
						info[i]=scs[i].toString();
					}
					 adapter=new ArrayAdapter<String>(SCActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
					lv.setAdapter(adapter);
				}
				
			}
		});
		}
		catch(Exception e){Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_LONG).show();}
	}	
	private boolean Right() {
		if (studentidTxt.getText().length() == 0 ) {
			Toast.makeText(getApplicationContext(), "��������ȷ��ѧ�ţ�", Toast.LENGTH_LONG).show();
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
	        	   
	        	   new AlertDialog.Builder(SCActivity.this).setTitle("�Ƿ�ȷ��ɾ��")
	               .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
	                   @Override
	                   public void onClick(DialogInterface dialog, int which) {
	                          //���ȷ���������¼�
	               		
	       			//	if (!Right()) {
	       				//	return;
	       				//}
	       				int deleteId=removeorupdateId;
	                    String stuId=scs[deleteId].id;
	       			long colunm=0;
	       					dbAdapter.deleteOneration(stuId);
	       				if (colunm == -1) {
	       					Toast.makeText(getApplicationContext(), "ɾ��ʧ�ܣ�id��"+stuId, Toast.LENGTH_LONG).show();
	       				} else {
	       					Toast.makeText(getApplicationContext(), "ɾ���ɹ�"+stuId, Toast.LENGTH_LONG).show();
	       					scs=dbAdapter.consultSC("");
	       					
	       					int len=scs.length;
	       					String[] info=new String[len];
	       					for(int i=0;i<len;i++)
	       					{
	       						info[i]=scs[i].toString();
	       					}
	       					 adapter=new ArrayAdapter<String>(SCActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
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
	        		LayoutInflater usingdialoglayoutxml=LayoutInflater.from(SCActivity.this);
	    			final View myviewondialog=usingdialoglayoutxml.inflate(R.layout.editsc, null);
	    			AlertDialog mydialoginstance=new AlertDialog.Builder(SCActivity.this)
	    			.setTitle("ѧ����Ϣ�޸Ľ���").setView(myviewondialog).setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
	    				//.setIcon(R.drawable.icon)
	    				@Override
	    				public void onClick(DialogInterface arg0, int arg1) {
	    					// TODO Auto-generated method stub
	    				
	    					editstuid=(EditText)myviewondialog.findViewById(R.id.editText2);//ѧ��id
	    					editcouid=(EditText)myviewondialog.findViewById(R.id.editText1);//�γ�id
	    					SC sc = new SC();
	    					sc.studentId=editstuid.getText().toString();
	        				sc.courseId=editcouid.getText().toString();
	        				
	        				String stuId=scs[removeorupdateId].id;
	        				sc.id=stuId;
	        				long colunm=0;
	        				dbAdapter.updaterelation("relation", stuId, sc);
	        			if (colunm == -1) {
	        				Toast.makeText(getApplicationContext(), sc.id+"����ʧ��", Toast.LENGTH_LONG).show();
	        			} else {
	        				Toast.makeText(getApplicationContext(), sc.id+"���³ɹ�", Toast.LENGTH_LONG).show();
	        					scs=dbAdapter.consultSC("");
	       					
	       					int len=scs.length;
	       					String[] info=new String[len];
	       					for(int i=0;i<len;i++)
	       					{
	       						info[i]=scs[i].toString();
	       					}
	       					 adapter=new ArrayAdapter<String>(SCActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
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
	private boolean isRight() {
		if (studentidTxt.getText().length() == 0 || courseidTxt.getText().length() == 0 ) {
			Toast.makeText(getApplicationContext(), "��������ȷ��ѧ�źͿγ̺ţ�", Toast.LENGTH_LONG).show();
			return false;
		} else {
			return true;
		}
	}
}