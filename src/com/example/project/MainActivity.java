package com.example.project;



import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;

public class MainActivity extends Activity {
	private Button btn1;
	private String name;
	private String password;
	private static final String INTENAL_ACTION_1="�����ǵ�һ��ACTION";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		//Button mybutton=(Button)findViewById(R.id.button1);
		//Intent myintent=new Intent(MainActivity.this, CourseActivity.class);//��ʼ��ת
		//Intent myintent=new Intent(MainActivity.this, SCActivity.class);//��ʼ��ת
			//Intent myintent=new Intent(MainActivity.this, Student.class);
    //	finish();
    //	startActivity(myintent);
	
	try{
			
		}
		catch(Exception  e){
			Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_LONG).show();
		}
		 btn1=(Button)findViewById(R.id.button1);
		 btn1.setOnClickListener(new OnClickListener(){
			public void onClick(View v)
			{
						EditText usernametext=(EditText)findViewById(R.id.editText1);
						name=usernametext.getText().toString(); 
						EditText passwordtext=(EditText)findViewById(R.id.editText2);
						password=passwordtext.getText().toString(); 
				
						if(name.equals("admin") && password.equals("123456"))
						{
							Intent intent= new Intent(INTENAL_ACTION_1);
			                sendBroadcast(intent);//�㲥
							Intent myintent=new Intent(MainActivity.this, Student.class);
				        	finish();//�رմ�Activity
				        	startActivity(myintent);
							//����
						}
						else
						{
							Toast.makeText(getApplicationContext(), "�û����������벻��ȷ", Toast.LENGTH_LONG).show();
						}
			}
			});
	}
    protected void onStart() {//��onStart�ж�̬ע��㲥,��ȻҲ������onCreate����ע��
        super.onStart();
        IntentFilter intentFilter= new IntentFilter(INTENAL_ACTION_1);
        registerReceiver(mybcrIntenal2,intentFilter);
    }
    private BroadcastReceiver mybcrIntenal2 = new BroadcastReceiver() {// ����
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Toast.makeText(getApplicationContext(), "��ʼ��¼...", Toast.LENGTH_SHORT).show();
        }
    };
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
