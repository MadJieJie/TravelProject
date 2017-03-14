package com.fengjie.myapplication.modules.user.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.base.fragment.AbstractFragment;
import com.fengjie.myapplication.event.Event;
import com.fengjie.myapplication.modules.user.bean.UserInfo;
import com.fengjie.myapplication.modules.user.utils.RetrofitUser;
import com.fengjie.myapplication.utils.often.AbstractSimpleObserver;
import com.fengjie.myapplication.utils.often.CircularAnimUtil;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.fengjie.myapplication.utils.rxbus.RxBus;
import com.fengjie.myapplication.view.DefinedMenu;

import io.reactivex.Observable;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class UserLoginFragment extends AbstractFragment
{
	
	private View mView = null;
	private Context mContext = null;
	
	/** widget */
	private EditText mAccount_et = null;
	private EditText mPassword_et = null;
	private Button mLogin_btn = null;
	private FloatingActionButton mRegister_fab = null;
	
	
	public static UserLoginFragment newInstance ()
	{
		Bundle args = new Bundle();
		
		UserLoginFragment fragment = new UserLoginFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate ( @Nullable Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		RxBus.getInstance()
				.register(this);
	}
	
	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_login, container, false);
			
			
			mAccount_et = ( EditText ) mView.findViewById(R.id.account_et_user);
			mPassword_et = ( EditText ) mView.findViewById(R.id.password_et_user);
			mRegister_fab = ( FloatingActionButton ) mView.findViewById(R.id.fab_register_user);
			mLogin_btn = ( Button ) mView.findViewById(R.id.login_btn_user);
			mContext = getContext();
		}

//		findView(view);
//		initMenu(getString(R.string.user),getMenuObjects(),this);
		return mView;
	}
	
	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		initView();
	}
	
	@Override
	public void onDestroy ()
	{
		super.onDestroy();
		RxBus.getInstance().unRegister(this);
	}
	
	@Override
	protected void findView ( View view )
	{
		mMenu = ( DefinedMenu ) mView.findViewById(R.id.menu_view_user);
	}
	
	@Override
	protected void initView ()
	{
		mLogin_btn.setOnClickListener(v ->
		{
			login();
		});
		mRegister_fab.setOnClickListener(v ->
		{
			Intent intent = new Intent(mContext, RegisterActivity.class);
			CircularAnimUtil.startActivity(getActivity(), intent, mRegister_fab, R.color.green);
		});
	}
	
	/**
	 * 登入事件
	 */
	private void login ()
	{
		
		getServerDataByNet("login", mAccount_et.getText().toString(), mPassword_et.getText().toString())
				.subscribe(new AbstractSimpleObserver< UserInfo >()
				{
					@Override
					public void onNext ( UserInfo userInfo )
					{
						if ( userInfo.result.equals("登入成功") )
						{
							ToastUtils.showShort(mContext, userInfo.result);
							postLoginSuccessEvent();
						}
						else
							ToastUtils.showShort(mContext, "error");

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
	private Observable< UserInfo > getServerDataByNet ( final String biz, final String account, final String password )
	{
		
		return RetrofitUser
				       .getInstance()
				       .getLoginResult(biz, account, password);
//				       .compose(this.bindToLifecycle());    //防止内存泄露
	}


//	@Override
//	protected void lazyLoad ()
//	{
//
//	}
	
	/**
	 * 发出登入成功事件
	 */
	private void postLoginSuccessEvent ()
	{
		RxBus.getInstance()
				.post(new Event(Event.EVENT_LOGIN_SUCCESS));
	}
	
}


