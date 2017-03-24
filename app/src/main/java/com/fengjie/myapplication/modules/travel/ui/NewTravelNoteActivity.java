package com.fengjie.myapplication.modules.travel.ui;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.base.BaseApplication;
import com.fengjie.myapplication.modules.tool.db.weather.Time;
import com.fengjie.myapplication.modules.travel.bean.TravelNote;
import com.fengjie.myapplication.modules.travel.db.TravelNoteDao;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import static com.fengjie.myapplication.modules.travel.base.TravelNoteConstant.EDIT_MODE;
import static com.fengjie.myapplication.modules.travel.base.TravelNoteConstant.EXCEPTION_ID;
import static com.fengjie.myapplication.modules.travel.base.TravelNoteConstant.ID;
import static com.fengjie.myapplication.modules.travel.base.TravelNoteConstant.MODE;
import static com.fengjie.myapplication.modules.travel.base.TravelNoteConstant.NEW_NOTE_MODE;


public class NewTravelNoteActivity extends RxAppCompatActivity
{
	/** Widght */
	private EditText title_et_newNote = null;
	private EditText content_et_newNote = null;
	private TextView saveTime_tv_newNote = null;
	
	
	/** Parameters */
	private static final int CUT_TITLE_LENGTH = 15;     //截取的标题长度
	
	private String mMode;      //获取传输过来的字符串标志位
	private int mID = EXCEPTION_ID;
	
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_note);
		mMode = getIntent().getStringExtra(MODE) != null ? getIntent().getStringExtra(MODE) : NEW_NOTE_MODE;      //获取传输过来的字符串标志位
		mID = getIntent().getIntExtra(ID, EXCEPTION_ID);     //默认植入异常ID
		findView();
		initView();
		initToolBar();
	}
	
	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
	}
	
	private void findView ()
	{
		title_et_newNote = ( EditText ) findViewById(R.id.title_et_newNote);        //题目
		content_et_newNote = ( EditText ) findViewById(R.id.content_et_newNote);    //内容
		saveTime_tv_newNote = ( TextView ) findViewById(R.id.saveTime_tv_newNote);  //保存时间
		
	}
	
	private void initView ()
	{
		if ( mMode.equals(NEW_NOTE_MODE) )
		{
			initNewNoteMode();
		} else if ( mMode.equals(EDIT_MODE) )
		{
			initEditMode();
		}
		
	}
	
	private void initNewNoteMode ()
	{
		
	}
	
	private void initEditMode ()
	{
		TravelNote note = TravelNoteDao.getQueryInfoForDB(mID);
		title_et_newNote.setText(note.getTitle());
		content_et_newNote.setText(note.getContent());
		saveTime_tv_newNote.setText(note.getCreateTime());
	}
	
	private void initToolBar ()
	{
		Toolbar toolbar = ( Toolbar ) findViewById(R.id.toolbar_newNote);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//toolbar.setNavigationIcon(R.drawable.ic_dialog_info);
		toolbar.setNavigationOnClickListener(v -> finish());
	}
	
	
	/**
	 * 从XML中读出menu配置
	 *
	 * @param menu
	 * @return
	 */
	@Override
	public boolean onCreateOptionsMenu ( Menu menu )
	{
		if ( mMode.equals(NEW_NOTE_MODE) )
		{
			setTitle("新建游记");
			getMenuInflater().inflate(R.menu.menu_new_note, menu);
		} else if ( mMode.equals(EDIT_MODE) )
		{
			setTitle("查看游记");
			getMenuInflater().inflate(R.menu.menu_edit, menu);
			title_et_newNote.setEnabled(false);
			content_et_newNote.setEnabled(false);       //设置不可编辑状态
		}
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected ( MenuItem item )
	{
		switch ( item.getItemId() )
		{
			case R.id.action_new_save:          //保存
				if ( mMode.equals(EDIT_MODE) )          //传入对象进行更新数据
				{
					TravelNote note = new TravelNote();
					note.setId(mID);
					note.setTitle(title_et_newNote.getText().toString());
					note.setAuthor("anonymity");
					note.setContent(content_et_newNote.getText().toString());
					note.setUpdateTime(Time.getNowYMDHMSTime());
					TravelNoteDao.updateDataToDB(note);
					ToastUtils.showShort(BaseApplication.sAppContext, "已经更新笔记");
					finish();           //退出界面
				} else if ( mMode.equals(NEW_NOTE_MODE) )                              //传入对象新建数据
				{
					if ( TravelNoteDao.insertDataToDB(
							new TravelNote(title_et_newNote.getText().toString(),       //标题
									              BaseApplication.sUserName,                          //作者
									              content_et_newNote.getText().toString(),//内容
									              Time.getNowYMDHMSTime())) )           //时间
					{
						ToastUtils.showShort(BaseApplication.sAppContext, "已经保存笔记");
						finish();           //退出界面
					} else
					{
						ToastUtils.showShort(BaseApplication.sAppContext, "保存失败,请检查内容是否为空");
					}
				}
				break;
			case R.id.action_edit_image:
				title_et_newNote.setEnabled(title_et_newNote.isEnabled() ? false : true);
				content_et_newNote.setEnabled(content_et_newNote.isEnabled() ? false : true);       //设置不可编辑状态
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume ()
	{
		super.onResume();
	}
	
	@Override
	protected void onStop ()
	{
		super.onStop();
		//如果APP处于后台，或者手机锁屏，则启用密码锁
//		if (
////				CommonUtil.isAppOnBackground(getApplicationContext()) ||
//				CommonUtil.isLockScreeen(getApplicationContext()) )
//		{
//			saveNoteData(true);//处于后台时保存数据
//		}
		
	}

//	@Override
//	public void onBackPressed ()
//	{
////		dealwithExit();
//	}
}
