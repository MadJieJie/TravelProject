package com.fengjie.myapplication.modules.user.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;
import android.widget.EditText;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.base.BaseApplication;
import com.fengjie.myapplication.modules.user.bean.UserInfo;
import com.fengjie.myapplication.modules.user.utils.RetrofitUser;
import com.fengjie.myapplication.utils.often.AbstractSimpleObserver;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.fengjie.myapplication.utils.often.Utils;
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
	private Button mRegister_btn;
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
		
		mRegister_btn = ( Button ) findViewById(R.id.register_btn_register);
		mClose_fab = ( FloatingActionButton ) findViewById(R.id.finish_fab_register);
		
	}
	
	private void initView ()
	{
		mRegister_btn.setOnClickListener(v ->
		{
			if ( Utils.isMobileNumber(mAccount_et.getText().toString()) &&          /**判断账号是否手机号码*/
					     Utils.isNumberAndcharacter(mPassword_et.getText().toString()) )    /**判断密码是否字母和数字结合，并且8-16位*/
			{
				register();     //注册
			} else
			{
				ToastUtils.showShort(BaseApplication.sAppContext,"账号必须为手机号,密码必须字母与数字结合且8到16位！");
			}
		});
		
		mClose_fab.setOnClickListener(v -> finish());
		
	}
	
	/**
	 * 导入景点列表数据
	 */
	private void register ()
	{
		
		getServerDataByNet("register", mAccount_et.getText().toString(), mPassword_et.getText().toString(), mUserName_et.getText().toString())
				.subscribe(new AbstractSimpleObserver< UserInfo >()
				{
					@Override
					public void onNext ( UserInfo userInfo )
					{
						if ( userInfo.result.equals("success") )
						{
							ToastUtils.showShort(RegisterActivity.this, userInfo.result);
						} else
						{
							ToastUtils.showShort(RegisterActivity.this, userInfo.result);
						}
					}
				});
	}
	
	
	/**
	 * 从网络获取
	 */
	private Observable< UserInfo > getServerDataByNet ( final String biz, final String account, final String password, final String userName )
	{
		
		return RetrofitUser.getInstance()
				       .getRegisterResult(biz, account, password, userName)
				       .compose(this.bindToLifecycle());    //防止内存泄露
	}
	
}
