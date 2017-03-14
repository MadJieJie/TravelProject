package com.fengjie.myapplication.modules.note.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.modules.note.bean.Group;
import com.fengjie.myapplication.modules.note.bean.Note;
import com.fengjie.myapplication.modules.note.db.GroupDao;
import com.fengjie.myapplication.modules.note.db.NoteDao;
import com.fengjie.myapplication.modules.note.utils.CommonUtil;
import com.fengjie.myapplication.modules.note.utils.SDCardUtil;
import com.fengjie.myapplication.modules.note.utils.StringUtils;
import com.fengjie.myapplication.modules.tool.utils.weather.RxUtils;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.sendtion.xrichtext.RichTextView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 笔记详情
 */
public class NoteActivity extends RxAppCompatActivity
{
	
	private TextView title_tv_note;//笔记标题
	private RichTextView content_rtd_note;//笔记内容
	private TextView time_tv_note;//笔记创建时间
	private TextView group_tv_note;//选择笔记分类
	//private ScrollView scroll_view;
	private Note note;//笔记对象
	private String myTitle;
	private String myContent;
	private String myGroupName;
	private NoteDao noteDao = new NoteDao(this);
	private GroupDao groupDao = new GroupDao(this);
	
	private ProgressDialog loadingDialog;
	
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		findView();
		initView();
		initToolBar();
		initLoadingDialog();
	}
	
	
	private void findView ()
	{
		title_tv_note = ( TextView ) findViewById(R.id.title_tv_note);//标题
		title_tv_note.setTextIsSelectable(true);
		content_rtd_note = ( RichTextView ) findViewById(R.id.content_rtd_note);//内容
		time_tv_note = ( TextView ) findViewById(R.id.time_tv_note);
		group_tv_note = ( TextView ) findViewById(R.id.group_tv_note);
	}
	
	private void initView ()
	{
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("data");
		note = ( Note ) bundle.getSerializable("note");
		
		myTitle = note.getTitle();
		myContent = note.getContent();
		Group group = groupDao.queryGroupById(note.getGroupId());
		myGroupName = group.getName();
		
		title_tv_note.setText(myTitle);
		content_rtd_note.post(() ->
		{
			content_rtd_note.clearAllLayout();
			showDataSync(myContent);
		});
		time_tv_note.setText(note.getCreateTime());
		group_tv_note.setText(myGroupName);
		setTitle("笔记详情");
		
	}
	
	private void initToolBar ()
	{
		Toolbar toolbar = ( Toolbar ) findViewById(R.id.toolbar_note);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//toolbar.setNavigationIcon(R.drawable.ic_dialog_info);
		toolbar.setNavigationOnClickListener(v -> finish());
	}
	
	private void initLoadingDialog ()
	{
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setMessage("数据加载中...");
		loadingDialog.setCanceledOnTouchOutside(false);
	}
	
	/**
	 * 异步方式显示数据
	 *
	 * @param html
	 */
	private void showDataSync ( final String html )
	{
		loadingDialog.show();
		
		
		Observable.create(new ObservableOnSubscribe< String >()
		{
			@Override
			public void subscribe ( ObservableEmitter< String > e ) throws Exception
			{
				showEditData(e, html);
			}
		})
//        .onBackpressureBuffer()
				.compose(RxUtils.rxSchedulerHelper())
				.subscribe(new Observer< String >()
				{
					@Override
					public void onSubscribe ( Disposable d )
					{
						
					}
					
					@Override
					public void onNext ( String s )
					{
						if ( s.contains(SDCardUtil.getPictureDir()) )
						{
							content_rtd_note.addImageViewAtIndex(content_rtd_note.getLastIndex(), s);
						} else
						{
							content_rtd_note.addTextViewAtIndex(content_rtd_note.getLastIndex(), s);
						}
					}
					
					@Override
					public void onError ( Throwable e )
					{
						e.printStackTrace();
						ToastUtils.showShort("解析错误：图片不存在或已损坏");
					}
					
					@Override
					public void onComplete ()
					{
						loadingDialog.dismiss();
					}
					
					
				});
		
	}
	
	/**
	 * 显示数据
	 *
	 * @param html
	 */
	private void showEditData ( ObservableEmitter< ? super String > subscriber, String html )
	{
		try
		{
			List< String > textList = StringUtils.cutStringByImgTag(html);
			for ( int i = 0; i < textList.size(); i++ )
			{
				String text = textList.get(i);
				if ( text.contains("<img") && text.contains("src=") )
				{
					String imagePath = StringUtils.getImgSrc(text);
					if ( new File(imagePath).exists() )
					{
						subscriber.onNext(imagePath);
					} else
					{
						ToastUtils.showShort("图片" + 1 + "已丢失，请重新插入！");
					}
				} else
				{
					subscriber.onNext(text);
				}
			}
			subscriber.onComplete();
		} catch( Exception e )
		{
			e.printStackTrace();
			subscriber.onError(e);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu ( Menu menu )
	{
		getMenuInflater().inflate(R.menu.menu_note, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected ( MenuItem item )
	{
		switch ( item.getItemId() )
		{
			case R.id.action_note_edit://编辑笔记
				Intent intent = new Intent(NoteActivity.this, NewNoteActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("note", note);
				intent.putExtra("data", bundle);
				intent.putExtra("flag", 1);//编辑笔记
				startActivity(intent);
				finish();
				break;
			case R.id.action_note_share://分享笔记
				CommonUtil.shareTextAndImage(this, note.getTitle(), note.getContent(), null);//分享图文
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
	}
	
	@Override
	public void onBackPressed ()
	{
		finish();
	}
}
