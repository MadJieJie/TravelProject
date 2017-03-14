package com.fengjie.myapplication.modules.note.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.adapter.recyclerview.adapter.CommonAdapter;
import com.fengjie.myapplication.adapter.recyclerview.adapter.MultiItemTypeAdapter;
import com.fengjie.myapplication.adapter.recyclerview.base.ViewHolder;
import com.fengjie.myapplication.modules.note.bean.Note;
import com.fengjie.myapplication.modules.note.db.NoteDao;
import com.fengjie.myapplication.modules.note.utils.SpacesItemDecoration;
import com.fengjie.myapplication.utils.often.LogUtils;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Sendtion on 2016/10/21 0021 16:43
 * 邮箱：sendtion@163.com
 * 博客：http://sendtion.cn
 * 描述：主界面
 */

public class NoteListActivity extends RxAppCompatActivity
{
	private XRecyclerView mRecyclerView;
	private CommonAdapter< Note > mAdapter;
	private List< Note > mDatas = new ArrayList< Note >();
	private NoteDao noteDao = new NoteDao(this);
	private int groupId = 0;//分类ID
	private String groupName;
	
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_list);
		
		initView();
		initToolBar();
		initRecyclerView();
	}
	
	@Override
	protected void onResume ()
	{
		super.onResume();
		refreshNoteList();
	}
	
	private void initView()
	{
		
		
		final FloatingActionButton fab = ( FloatingActionButton ) findViewById(R.id.fab_main);
		fab.setOnClickListener(v ->
		{
//			Snackbar.make(v, "新建笔记", Snackbar.LENGTH_LONG).setAction("Action", null).show();
			Intent intent = new Intent(NoteListActivity.this, NewNoteActivity.class);
			intent.putExtra("groupName", groupName);
			intent.putExtra("flag", 0);
			startActivity(intent);
		});
	}
	
	private void initToolBar()
	{
		Toolbar toolbar = ( Toolbar ) findViewById(R.id.toolbar_noteList);
		setSupportActionBar(toolbar);
		setTitle("备忘录");
		toolbar.setNavigationOnClickListener(v->finish());
	}
	
	private void initRecyclerView()
	{
		mRecyclerView = ( XRecyclerView ) findViewById(R.id.content_rv_noteList);
		/****************** 设置XRecyclerView属性 **************************/
		mRecyclerView.addItemDecoration(new SpacesItemDecoration(0));//设置item间距
//		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//竖向列表
//		mRecyclerView.setLayoutManager(layoutManager);
		
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		mRecyclerView.setLoadingMoreEnabled(true);//开启上拉加载
		mRecyclerView.setPullRefreshEnabled(true);//开启下拉刷新
		mRecyclerView.setRefreshProgressStyle(ProgressStyle.SquareSpin);
		mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallScale);
		/****************** 设置XRecyclerView属性 **************************/
		
		mAdapter = new CommonAdapter< Note >(this, R.layout.item_note, mDatas)
		{
			@Override
			protected void convert ( ViewHolder holder, Note info, int position )
			{
				holder.setText(R.id.title_tv_list, mDatas.get(position).getTitle());
				holder.setText(R.id.summary_tv_list, mDatas.get(position).getContent());
				holder.setText(R.id.time_tv_list, mDatas.get(position).getCreateTime());
				holder.setText(R.id.group_tv_list, mDatas.get(position).getGroupName());
			}
		};
		
		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				ToastUtils.showShort(mDatas.get(position).getTitle());
				
				Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("note", mDatas.get(position));
				intent.putExtra("data", bundle);
				startActivity(intent);
			}
			
			@Override
			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(NoteListActivity.this);
				builder.setTitle("提示");
				builder.setMessage("确定删除笔记？");
				builder.setCancelable(false);
				builder.setPositiveButton("确定", ( dialog, which ) ->
				{
					int ret = noteDao.deleteNote(mDatas.get(position).getId());
					if ( ret > 0 )
					{
						ToastUtils.showShort("删除成功");
						refreshNoteList();
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
				return false;
			}
		});
		
		mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener()
		{
			@Override
			public void onRefresh ()
			{
				mRecyclerView.postDelayed(() -> mRecyclerView.refreshComplete(), 1000);
			}
			
			@Override
			public void onLoadMore ()
			{
				mRecyclerView.postDelayed(() -> mRecyclerView.refreshComplete(), 1000);
			}
		});
		
		mRecyclerView.setAdapter(mAdapter);
	}
	
	
	/**
	 * 刷新笔记列表
	 */
	private void refreshNoteList ()
	{
		mDatas = noteDao.queryNotesAll(groupId);
		LogUtils.d("Debug", "refreshNoteList: "+mDatas.size());
		LogUtils.d("Debug", "refreshNoteList: "+mDatas.get(1).getContent());
		mAdapter.notifyDataSetChanged();
	}

	
	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu ( Menu menu )
	{
		getMenuInflater().inflate(R.menu.menu_note_list, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected ( MenuItem item )
	{
		switch ( item.getItemId() )
		{
			case R.id.action_new_note:
				Intent intent = new Intent(NoteListActivity.this, NewNoteActivity.class);
				intent.putExtra("groupName", groupName);
				intent.putExtra("flag", 0);
				startActivity(intent);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
