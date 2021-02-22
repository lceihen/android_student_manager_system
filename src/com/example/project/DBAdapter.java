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
	private static final String DB_NAME = "Test6student.db";// 数据库名
	public static final String STUDENT_TABLE = "student";// 学生表student,mycourseone,relation
	public static final String COURSE_TABLE = "mycourseone";// 课程表
	public static final String RELATION_TABLE = "relation";// 学生、课程关系表
	private static final int DB_VERSION = 1;// 数据库版本号

	public static final String STUDENT_ID = "studentId";// 学生的学号
	public static final String STUDENT_NAME = "myName";// 学生姓名
	public static final String STUDENT_CLASS = "myClass";// 学生班级

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
		// 创建一个DBOpenHelper实例
		dbOpenHelper = new DBOpenHelper(mcontext, DB_NAME, null, DB_VERSION);
		// 通过dbOpenHelper.getWritableDatabase()或者dbOpenHelper.getReadableDatabase()
		//创建或打开一个数据库SQLiteDatabase实例，其中dbOpenHelper.getWritableDatabase()
		//得到的数据库具有读写的权限，而dbOpenHelper.getReadableDatabase()得到的数据库则具有只读的权限。
		try {
			db = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			db = dbOpenHelper.getReadableDatabase();
		}
	}

	// 关闭数据库
	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
	}

	/**
	 * 向表中添加一条数据
	 * 
	 * @param people
	 * @return
	 */
	public long insert(Person person) {
		// ContentValues类存储了一组键值对
		ContentValues newValues = new ContentValues();

		newValues.put(STUDENT_ID, person.id);
		newValues.put(STUDENT_NAME, person.myName);
		newValues.put(STUDENT_CLASS, person.myClass);


		return db.insert(STUDENT_TABLE, null, newValues);
	}
	// 添加课程

	
	// 通过id删除一条学生信息
	public void deleteOneData(String id) {
		//return db.delete(STUDENT_TABLE, STUDENT_ID + "=" + id, null);
		String sql="delete from student where studentId='"+id+"';";
	 db.execSQL(sql);
	}
//删除关系
	public void deleteOneration(String id)
	{
		String sql="delete from relation where relationId='"+id+"';";
		 db.execSQL(sql);
	}
	// 通过id删除一条课程信息
	public void deleteOneCourse(String id) {
		String sql="delete from mycourseone where id='"+id+"';";
		 db.execSQL(sql);
	}

	// 添加课程
	public long insert(Course course) {
		ContentValues newValues = new ContentValues();

		newValues.put("id", course.id);
		newValues.put("name", course.name);


		return db.insert("mycourseone", null, newValues);
	}
	//添加学生课程关系
	public long insert(SC sc)
	{
		ContentValues newValues = new ContentValues();
		newValues.put(RELATION_ID, sc.studentId+sc.courseId);
		newValues.put(STUDENT_ID, sc.studentId);
		newValues.put(COURSE_ID,sc.courseId);


		return db.insert(RELATION_TABLE, null, newValues);
	}
	//我的课程关系查询
	public SC[] consultSC(String id){
		Cursor result = null;
		if(id!="")
		{
			String sql="select * from relation where studentId='"+id+"';";//注意这里是学生的id
			result=	db.rawQuery(sql, null);

			
		}
		else
		{
			result = db.rawQuery("select * from relation", null);
			
		}return ConvertToSC(result);
	}
	// 查询studentID为id的一个同学的信息
	public Person[] queryOneData(String id) {
		Cursor result = null;
		result = db.query(STUDENT_TABLE, null, STUDENT_ID + "=" + id, null,
				null, null, null);
		return ConvertToPeople(result);
	}

	// 查询课程ID为id的所有学生的信息
	public Person[] queryAllData(String id) {
		Cursor result = db
				.rawQuery(
						"select a.studentId,a.myName,a.myClass from student a "
								+ "where a.studentId ="
								+ id + ");", null);
		return ConvertToPeople(result);
	}
	//我的学生查询
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
//详情
	public SCinfo[] consultSCinfo()
	{
		Cursor result = null;
		result = db.rawQuery("select * from student,mycourseone,relation where student.studentId=mycourseone.id and relation.studentId=student.studentId;", null);
		return  ConvertToSCinfo(result);
	}
	//详情转换
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
	//我的课程查询
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
	
	
	// 查询courseID为id的一门课程信息
	public Course[] queryOneCourse(String id) {
		Cursor result = null;
		result = db.query(COURSE_TABLE, null, COURSE_ID + "=" + id, null, null,
				null, null);
		return ConvertToCourse(result);
	}
	// 查询所有的课程信息
	public Course[] queryAllCourse() {
		Cursor result = db.query(COURSE_TABLE, new String[] { COURSE_ID,
				COURSE_NAME}, null, null, null,
				null, null);
		return ConvertToCourse(result);
	}
	// 修改数据库学生表的数据
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
	// 修改数据库课程表的数据
	public long updateOneCourse(String table, String id, Course course) {
		//ContentValues newValues = new ContentValues();

		//newValues.put(COURSE_NAME, course.name);
		//newValues.put(COURSE_ID, course.id);

		//return db.update(table, newValues, COURSE_ID+ "=" + id, null);
		String sql="update mycourseone set id='"+course.id+"',name='"+course.name+"' where id='"+id+"';";
		db.execSQL(sql);
		return 0;
	}
	//更新关系信息
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
	//关系表的转换
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
	// 用cursor操作将查询到的数据放入相应的数组中
	private Person[] ConvertToPeople(Cursor cursor) {
		// cursor.getCount()获得用户查询得到的信息条数
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
	 * 静态Helper类，用于建立、更新和打开数据库
	 */
	private static class DBOpenHelper extends SQLiteOpenHelper {
		// 创建数据库的sql语句
		private static final String STUDENT_CREATE = "CREATE TABLE "
				+ STUDENT_TABLE + "(" + STUDENT_ID + " text primary key,"
				+ STUDENT_NAME + " text not null," + STUDENT_CLASS + " text);";
		private static final String COURSE_CREATE = "CREATE TABLE mycourseone (id text primary key ,name text not null);";
		private static final String RELATION_CREATE = "CREATE TABLE "
				+ RELATION_TABLE + "(" +RELATION_ID+" text primary key,"+ STUDENT_ID + " text not null,"
				+ COURSE_ID + " text not null);";
		// 在用户创建DBOpenHelper的构造函数，其自动调用自身的onCreate(SQLiteDatabase db)函数
		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// 执行sql语句，创建数据库
			db.execSQL(STUDENT_CREATE);
			db.execSQL(COURSE_CREATE);
			db.execSQL(RELATION_CREATE);
		}
		// 用cursor操作将查询到的数据放入相应的数组中
		
		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			// 函数在数据库需要升级时被调用，
			// 一般用来删除旧的数据库表，
			// 并将数据转移到新版本的数据库表中
			_db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
			_db.execSQL("DROP TABLE IF EXISTS mycourseone" );
			_db.execSQL("DROP TABLE IF EXISTS " + RELATION_TABLE);
			onCreate(_db);
		}
	}
}
