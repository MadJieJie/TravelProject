package com.fengjie.myapplication.modules.user.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;
import android.widget.EditText;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.modules.user.bean.UserInfo;
import com.fengjie.myapplication.modules.user.utils.RetrofitUser;
import com.fengjie.myapplication.utils.often.AbstractSimpleObserver;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;

/**
 * @author Created by MadJieJie on 2017/3/11-10:07.
 * @brief
 * @attention
 */

public class RegisterActivity extends RxAppCompatActivity
{
	/** Widget */
	private EditText mAccount_et;
	private EditText mPassword_et;
	private EditText mUserName_et;
	
	
	private Button mRgister_btn;
	private FloatingActionButton mClose_fab;
	
	@Override
	protected void onCreate ( @Nullable Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		findView();
		initView();
	}
	
	private void findView ()
	{
		mAccount_et = ( EditText ) findViewById(R.id.account_et_register);
		mPassword_et = ( EditText ) findViewById(R.id.password_et_register);
		mUserName_et = ( EditText ) findViewById(R.id.userName_et_register);
		
		mRgister_btn = ( Button ) findViewById(R.id.register_btn_register);
		mClose_fab = ( FloatingActionButton ) findViewById(R.id.finish_fab_register);
		
	}
	
	private void initView ()
	{
		mRgister_btn.setOnClickListener(v->{
			register();
		});
		
		mClose_fab.setOnClickListener(v ->
		{
			finish();
		});
	}
	
	/**
	 * 导入景点列表数据
	 */
	private void register ()
	{
		
		getServerDataByNet("register", mAccount_et.getText().toString(), mPassword_et.getText().toString(),mUserName_et.getText().toString())
				.subscribe(new AbstractSimpleObserver< UserInfo >()
				{
					@Override
					public void onNext ( UserInfo userInfo )
					{
						if ( userInfo.result.equals("success") )
						{
							ToastUtils.showShort(RegisterActivity.this, userInfo.result);
						}
						else
						{
							ToastUtils.showShort(RegisterActivity.this, userInfo.result);
						}

//						if ( hotel.result != null && ! mDatas.contains(hotel.result) )        //成立条件：获取数据不为空&原本容器不包含现获取数据
//						{
//							SceneryConstant.PAGE++;
//							LogUtils.d(hotel.result.get(0).address);
//							mDatas.addAll(hotel.result);
//							mEmptyWrapper.notifyDataSetChanged();
//						} else
//						{
//							ToastUtils.showShort(hotel.reason);
//							LogUtils.d(hotel.reason);
//						}
					}
				});
		
	}
	
	/**
	 * 从网络获取
	 */
	private Observable< UserInfo > getServerDataByNet ( final String biz, final String account, final String password ,final String userName)
	{
		
		return RetrofitUser.getInstance()
				       .getRegisterResult(biz, account, password,userName)
				       .compose(this.bindToLifecycle());    //防止内存泄露
	}
	
}
