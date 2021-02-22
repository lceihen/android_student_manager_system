package com.example.project;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * @author
 * 
 */
public class DBAdapter {
	private static final String DB_NAME = "Test6student.db";// ���ݿ���
	public static final String STUDENT_TABLE = "student";// ѧ����student,mycourseone,relation
	public static final String COURSE_TABLE = "mycourseone";// �γ̱�
	public static final String RELATION_TABLE = "relation";// ѧ�����γ̹�ϵ��
	private static final int DB_VERSION = 1;// ���ݿ�汾��

	public static final String STUDENT_ID = "studentId";// ѧ����ѧ��
	public static final String STUDENT_NAME = "myName";// ѧ������
	public static final String STUDENT_CLASS = "myClass";// ѧ���༶

	public static final String COURSE_ID = "id";
	public static final String COURSE_NAME = "name";
	public static final String RELATION_ID = "relationId";

	private SQLiteDatabase db;
	private Context mcontext;
	private DBOpenHelper dbOpenHelper;

	public DBAdapter(Context context) {
		mcontext = context;
	}

	public void open() throws SQLiteException {
		// ����һ��DBOpenHelperʵ��
		dbOpenHelper = new DBOpenHelper(mcontext, DB_NAME, null, DB_VERSION);
		// ͨ��dbOpenHelper.getWritableDatabase()����dbOpenHelper.getReadableDatabase()
		//�������һ�����ݿ�SQLiteDatabaseʵ��������dbOpenHelper.getWritableDatabase()
		//�õ������ݿ���ж�д��Ȩ�ޣ���dbOpenHelper.getReadableDatabase()�õ������ݿ������ֻ����Ȩ�ޡ�
		try {
			db = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			db = dbOpenHelper.getReadableDatabase();
		}
	}

	// �ر����ݿ�
	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
	}

	/**
	 * ��������һ������
	 * 
	 * @param people
	 * @return
	 */
	public long insert(Person person) {
		// ContentValues��洢��һ���ֵ��
		ContentValues newValues = new ContentValues();

		newValues.put(STUDENT_ID, person.id);
		newValues.put(STUDENT_NAME, person.myName);
		newValues.put(STUDENT_CLASS, person.myClass);


		return db.insert(STUDENT_TABLE, null, newValues);
	}
	// ��ӿγ�

	
	// ͨ��idɾ��һ��ѧ����Ϣ
	public void deleteOneData(String id) {
		//return db.delete(STUDENT_TABLE, STUDENT_ID + "=" + id, null);
		String sql="delete from student where studentId='"+id+"';";
	 db.execSQL(sql);
	}
//ɾ����ϵ
	public void deleteOneration(String id)
	{
		String sql="delete from relation where relationId='"+id+"';";
		 db.execSQL(sql);
	}
	// ͨ��idɾ��һ���γ���Ϣ
	public void deleteOneCourse(String id) {
		String sql="delete from mycourseone where id='"+id+"';";
		 db.execSQL(sql);
	}

	// ��ӿγ�
	public long insert(Course course) {
		ContentValues newValues = new ContentValues();

		newValues.put("id", course.id);
		newValues.put("name", course.name);


		return db.insert("mycourseone", null, newValues);
	}
	//���ѧ���γ̹�ϵ
	public long insert(SC sc)
	{
		ContentValues newValues = new ContentValues();
		newValues.put(RELATION_ID, sc.studentId+sc.courseId);
		newValues.put(STUDENT_ID, sc.studentId);
		newValues.put(COURSE_ID,sc.courseId);


		return db.insert(RELATION_TABLE, null, newValues);
	}
	//�ҵĿγ̹�ϵ��ѯ
	public SC[] consultSC(String id){
		Cursor result = null;
		if(id!="")
		{
			String sql="select * from relation where studentId='"+id+"';";//ע��������ѧ����id
			result=	db.rawQuery(sql, null);

			
		}
		else
		{
			result = db.rawQuery("select * from relation", null);
			
		}return ConvertToSC(result);
	}
	// ��ѯstudentIDΪid��һ��ͬѧ����Ϣ
	public Person[] queryOneData(String id) {
		Cursor result = null;
		result = db.query(STUDENT_TABLE, null, STUDENT_ID + "=" + id, null,
				null, null, null);
		return ConvertToPeople(result);
	}

	// ��ѯ�γ�IDΪid������ѧ������Ϣ
	public Person[] queryAllData(String id) {
		Cursor result = db
				.rawQuery(
						"select a.studentId,a.myName,a.myClass from student a "
								+ "where a.studentId ="
								+ id + ");", null);
		return ConvertToPeople(result);
	}
	//�ҵ�ѧ����ѯ
	public Person[] consultStudent(String id){
		Cursor result = null;
		if(id!="")
		{
			String sql="select * from student where student.studentId='"+id+"';";
			result=	db.rawQuery(sql, null);
			
		}
		else
		{
			result = db.rawQuery("select * from student;", null);
			
		}return ConvertToPeople(result);
	}
//����
	public SCinfo[] consultSCinfo()
	{
		Cursor result = null;
		result = db.rawQuery("select * from student,mycourseone,relation where student.studentId=mycourseone.id and relation.studentId=student.studentId;", null);
		return  ConvertToSCinfo(result);
	}
	//����ת��
	public SCinfo[] ConvertToSCinfo(Cursor cursor)
	{
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		SCinfo[] scinfo = new SCinfo[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			scinfo[i] = new SCinfo();
			
			scinfo[i].id = cursor.getString(cursor.getColumnIndex(RELATION_ID));
			scinfo[i].studentId = cursor.getString(cursor
					.getColumnIndex(STUDENT_ID));
			scinfo[i].stuname = cursor.getString(cursor
					.getColumnIndex("myName"));
			scinfo[i].couname = cursor.getString(cursor
					.getColumnIndex(COURSE_NAME));
			
			cursor.moveToNext();
		}
		return scinfo;
	}
	//�ҵĿγ̲�ѯ
	public Course[] consultCourse(String id){
		Cursor result = null;
		if(id!="")
		{
		//	result = db.query(COURSE_TABLE, null, COURSE_ID + "=" + id, null,
				//	null, null, null);
			String sql="select * from mycourseone where mycourseone.id='"+id+"';";
		result=	db.rawQuery(sql, null);
			
		}
		else
		{
			result = db.rawQuery("select * from mycourseone;", null);
			
		}return ConvertToCourse(result);
	}
	
	
	// ��ѯcourseIDΪid��һ�ſγ���Ϣ
	public Course[] queryOneCourse(String id) {
		Cursor result = null;
		result = db.query(COURSE_TABLE, null, COURSE_ID + "=" + id, null, null,
				null, null);
		return ConvertToCourse(result);
	}
	// ��ѯ���еĿγ���Ϣ
	public Course[] queryAllCourse() {
		Cursor result = db.query(COURSE_TABLE, new String[] { COURSE_ID,
				COURSE_NAME}, null, null, null,
				null, null);
		return ConvertToCourse(result);
	}
	// �޸����ݿ�ѧ���������
	public long updateOneData(String table, String id, Person people) {
	//	ContentValues newValues = new ContentValues();

		//newValues.put(STUDENT_NAME, people.myName);
		//newValues.put(STUDENT_CLASS, people.myClass);
		//newValues.put(STUDENT_ID, people.id);
		//return db.update(table, newValues, STUDENT_ID + "=" + id, null);
		String sql="update student set studentId='"+people.id+"',myName='"+people.myName+"',myClass='"+people.myClass+"' where studentId='"+people.id+"';";
		db.execSQL(sql);
		return 0;
	}
	// �޸����ݿ�γ̱������
	public long updateOneCourse(String table, String id, Course course) {
		//ContentValues newValues = new ContentValues();

		//newValues.put(COURSE_NAME, course.name);
		//newValues.put(COURSE_ID, course.id);

		//return db.update(table, newValues, COURSE_ID+ "=" + id, null);
		String sql="update mycourseone set id='"+course.id+"',name='"+course.name+"' where id='"+id+"';";
		db.execSQL(sql);
		return 0;
	}
	//���¹�ϵ��Ϣ
	public void updaterelation(String table,String id,SC sc){
		String sql="update relation set studentId='"+sc.studentId+"',id='"+sc.courseId+"' where relationId='"+id+"';";
		db.execSQL(sql);
	}
public void deleterelation(String id)
{
	String sql="delete from relation where relationId='"+id+";";
	db.execSQL(sql);
	}
	private Course[] ConvertToCourse(Cursor cursor) {
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		Course[] courses = new Course[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			courses[i] = new Course();
			courses[i].id = cursor.getString(cursor.getColumnIndex(COURSE_ID));
			courses[i].name = cursor.getString(cursor
					.getColumnIndex(COURSE_NAME));
	
			cursor.moveToNext();
		}
		return courses;
	}
	//��ϵ���ת��
	///
	
	///
	//
	private SC[] ConvertToSC(Cursor cursor) {
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		SC[] sc = new SC[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			sc[i] = new SC();
			
			sc[i].id = cursor.getString(cursor.getColumnIndex(RELATION_ID));
			sc[i].studentId = cursor.getString(cursor
					.getColumnIndex(STUDENT_ID));
			sc[i].courseId = cursor.getString(cursor
					.getColumnIndex(COURSE_ID));
			cursor.moveToNext();
		}
		return sc;
	}
	// ��cursor��������ѯ�������ݷ�����Ӧ��������
	private Person[] ConvertToPeople(Cursor cursor) {
		// cursor.getCount()����û���ѯ�õ�����Ϣ����
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		Person[] peoples = new Person[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			peoples[i] = new Person();
			peoples[i].id = cursor.getString(cursor
					.getColumnIndex(STUDENT_ID));
			peoples[i].myName = cursor.getString(cursor
					.getColumnIndex(STUDENT_NAME));
			peoples[i].myClass = cursor.getString(cursor
					.getColumnIndex(STUDENT_CLASS));
			cursor.moveToNext();
		}
		return peoples;
	}


	/**
	 * ��̬Helper�࣬���ڽ��������ºʹ����ݿ�
	 */
	private static class DBOpenHelper extends SQLiteOpenHelper {
		// �������ݿ��sql���
		private static final String STUDENT_CREATE = "CREATE TABLE "
				+ STUDENT_TABLE + "(" + STUDENT_ID + " text primary key,"
				+ STUDENT_NAME + " text not null," + STUDENT_CLASS + " text);";
		private static final String COURSE_CREATE = "CREATE TABLE mycourseone (id text primary key ,name text not null);";
		private static final String RELATION_CREATE = "CREATE TABLE "
				+ RELATION_TABLE + "(" +RELATION_ID+" text primary key,"+ STUDENT_ID + " text not null,"
				+ COURSE_ID + " text not null);";
		// ���û�����DBOpenHelper�Ĺ��캯�������Զ����������onCreate(SQLiteDatabase db)����
		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// ִ��sql��䣬�������ݿ�
			db.execSQL(STUDENT_CREATE);
			db.execSQL(COURSE_CREATE);
			db.execSQL(RELATION_CREATE);
		}
		// ��cursor��������ѯ�������ݷ�����Ӧ��������
		
		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			// ���������ݿ���Ҫ����ʱ�����ã�
			// һ������ɾ���ɵ����ݿ��
			// ��������ת�Ƶ��°汾�����ݿ����
			_db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
			_db.execSQL("DROP TABLE IF EXISTS mycourseone" );
			_db.execSQL("DROP TABLE IF EXISTS " + RELATION_TABLE);
			onCreate(_db);
		}
	}
}
