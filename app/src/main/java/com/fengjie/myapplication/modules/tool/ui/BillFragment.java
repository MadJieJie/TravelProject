package com.fengjie.myapplication.modules.tool.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.adapter.recyclerview.adapter.CommonAdapter;
import com.fengjie.myapplication.adapter.recyclerview.base.ViewHolder;
import com.fengjie.myapplication.adapter.recyclerview.wrapper.EmptyWrapper;
import com.fengjie.myapplication.modules.note.utils.DateUtils;
import com.fengjie.myapplication.modules.tool.base.weather.AbstractRetrofitFragment;
import com.fengjie.myapplication.modules.tool.bean.Bill;
import com.fengjie.myapplication.modules.tool.bean.BillRV;
import com.fengjie.myapplication.modules.tool.db.bill.BillDao;
import com.fengjie.myapplication.modules.tool.db.weather.Time;
import com.fengjie.myapplication.utils.often.LogUtils;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.fengjie.myapplication.utils.rxbus.RxBus;
import com.jaouan.compoundlayout.CompoundLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.aigestudio.datepicker.views.DatePicker;


/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class BillFragment extends AbstractRetrofitFragment
{
	
	private RecyclerView mRecyclerView = null;
	private static List< BillRV > mDatas = new ArrayList< BillRV >();
	private Context mContext = null;
//	private TwinklingRefreshLayout mTwinklingRefreshLayout;
	
	private CommonAdapter< BillRV > mAdapter = null;
	private EmptyWrapper mEmptyWrapper = null;
	//	private EmptyWrapper mEmptyWrapper = null;
	private FloatingActionButton add_fab;
	
	private AlertDialog mAlertDialog;
	private DatePicker mDatePicker = null;
	private TextView mMoney_et_bill;
	/** Paramters */
	private Map< Integer, String > mStrTypeMap = new HashMap< Integer, String >();
	private Map< String, Integer > mMipmapMap = new HashMap< String, Integer >();
	private int[] mMipmapIdArray = { R.mipmap.iv_eat_bill, R.mipmap.iv_traffic_bill, R.mipmap.iv_buy_bill, R.mipmap.iv_amusement_bill, R.mipmap.iv_stay_bill
			, R.mipmap.iv_ticket_bill, R.mipmap.iv_treat_bill, R.mipmap.iv_insurance_bill, R.mipmap.iv_other_bill };
	private Integer[] mViewId = { R.id.eat_rdl_bill, R.id.traffic_rdl_bill, R.id.buy_rdl_bill, R.id.amusement_rdl_bill, R.id.stay_rdl_bill
			, R.id.ticket_rdl_bill, R.id.treat_rdl_bill, R.id.insurance_rdl_bill, R.id.other_rdl_bill };
	private String[] mTypeArray = { "餐饮", "交通", "购物", "娱乐", "住宿", "门票", "医疗", "保险", "其他" };
	private String mTypeStr = "餐饮";
	private boolean mIsTypeExist = false;
	private boolean mIsDateIsExist = false;
	
	/**
	 * 工厂模式
	 *
	 * @return 返回新的实例
	 */
	public static BillFragment newInstance ()
	{
		Bundle args = new Bundle();
		
		BillFragment fragment = new BillFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate ( @Nullable Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		
	}
	
	
	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_bill, container, false);
			mContext = getContext();
		}
		mIsCreateView = true;
		
		return mView;
	}
	
	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		findView(mView);
		initView();
		initRecycleView();
		initRadioButton();
		initDatePickerDialog();
//		initRefreshLayout();
//		initRxBus();
	}
	
	@Override
	public void onStart ()
	{
		super.onStart();
		Bill bill = BillDao.getQueryBillForDB(DateUtils.getNowDate()); /**检查该天是否存在,若存在则取出数据显示*/
		if ( bill != null )
		{
			mDatas.clear();     //清空现数据
			int[] intArray = bill.getAllArray();
			for ( int position = 0; position < bill.getAllArray().length; position++ )
			{
				if ( intArray[position] > 0 )
				{
					mDatas.add(new BillRV(mMipmapIdArray[position], intArray[position] + "", mTypeArray[position]));
				}
			}
			mAdapter.notifyDataSetChanged();    //更新
			mIsDateIsExist = true;
		} else
		{
			mIsDateIsExist = false;
		}
	}
	
	@Override
	public void onPause ()
	{
		super.onPause();
		
		
	}
	
	@Override
	public void onDestroy ()
	{
		super.onDestroy();
		LogUtils.d("Debug", "onDestroy: " + "bill's data insert db");        //将数据插入数据库
		Bill bill = new Bill(DateUtils.getNowDate());
		
		bill = getBillForMDatas(bill);
		
		if ( mIsDateIsExist )                      /**数据库已存在数据,则更新*/
		{
			BillDao.updateDataToDB(bill);
		} else if ( mDatas.size() > 0 )             /**如果mData有数据则插入数据*/
		{
			BillDao.insertDataToDB(bill);
		}
		RxBus.getInstance().unRegister(this);
	}
	
	
	private void findView ( View view )
	{
		mMoney_et_bill = ( TextView ) mView.findViewById(R.id.money_et_bill);
		add_fab = ( FloatingActionButton ) mView.findViewById(R.id.add_fab_bill);
//		mTwinklingRefreshLayout = ( TwinklingRefreshLayout ) view.findViewById(R.id.refresh_trl_scenery);
	}
	
	private void initView ()
	{
		mView.findViewById(R.id.date_lv_bill).setOnClickListener(v ->
		{
			mAlertDialog.show();
		});
		
		add_fab.setOnClickListener(v ->         //添加账单条目
		{
			if ( mMoney_et_bill.getText().toString().equals("") )
			{
				ToastUtils.showShort("输入有误,请重新输入!");
			} else
			{
				for ( Iterator< BillRV > iterator = mDatas.iterator(); iterator.hasNext(); )        /**使用迭代器持有对象真实的引用进行操作*/
				{
					BillRV billBean = iterator.next();
					if ( billBean.getTpye().equals(mTypeStr) )
					{
						billBean.setMoney(( Integer.parseInt(billBean.getMoney()) + Integer.parseInt(mMoney_et_bill.getText().toString()) ) + "");
						mIsTypeExist = true;
						break;
					}
				}
				
				if ( mIsTypeExist )     //如果该类型的消费存在
				{
					mIsTypeExist = false;
				} else              //如果该类型的消费不存在
				{
					mDatas.add(new BillRV(0, mMoney_et_bill.getText().toString(), mTypeStr));
//					mDatas.add(new BillRV(mID, mMoney_et_bill.getText().toString(), mTypeStr));
				}
				
				mEmptyWrapper.notifyDataSetChanged();     //更新Adapter
			}
			
		});
	}
	
	private void initDatePickerDialog ()
	{
		mDatePicker = new DatePicker(mContext);
//		mDatePicker.setMode(DPMode.SINGLE);     //single-单选模式
		mDatePicker.setDate(Integer.parseInt(Time.getNowYear()), Integer.parseInt(Time.getNowMonth()));      //必选选择一个日期
		mDatePicker.setOnDateSelectedListener(date ->
		{
			
		});

//		mDatePicker.setDPDecor(new DPDecor(){
//
//		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setView(mDatePicker);
		mAlertDialog = builder.create();
		mAlertDialog.setCancelable(true);       //触摸外部不能关闭
		mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", ( dialog, which ) ->
		{
//			ToastUtils.showShort(mContext, mDatePicker.getYear() + "-" + mDatePicker.getMonth() + mDatePicker.getDayOfMonth());
		});
//		mAlertDialog.dismiss();
		
	}
	
	/**
	 * 初始化选择界面
	 */
	private void initRadioButton ()
	{
		
		for ( int i = 0; i < mViewId.length; i++ )
		{
			mStrTypeMap.put(mViewId[i], mTypeArray[i]);            //
			mMipmapMap.put(mTypeArray[i], mMipmapIdArray[i]);
			bindCompoundListener(( CompoundLayout ) mView.findViewById(mViewId[i]));
		}
		( ( CompoundLayout ) mView.findViewById(R.id.eat_rdl_bill) ).setChecked(true);
	}
	
	private void initRecycleView ()
	{
		mRecyclerView = ( RecyclerView ) mView.findViewById(R.id.content_rv_bill);
//
//		mDatas.add(new String[]{ "金额", "¥0.00" });
//		mDatas.add(new String[]{ "日期", DateUtils.getNowDate() });
//		mDatas.add(new String[]{ "备注", "" });
//
//		/**设置RecyclerView垂直放置内容*/
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
//
//
		mAdapter = new CommonAdapter< BillRV >(mContext, R.layout.item_bill, mDatas)
		{
			@Override
			protected void convert ( ViewHolder holder, BillRV info, int position )
			{
				holder
						.setBackgroundRes(R.id.type_iv_bill, mMipmapMap.get(info.getTpye()))
						.setText(R.id.type_tv_bill, info.getTpye())
						.setText(R.id.money_tv_bill, info.getMoney());
			}
		};
		mEmptyWrapper = new EmptyWrapper(mAdapter);
		mEmptyWrapper.setEmptyView(R.layout.view_empty);
		
		mRecyclerView.setAdapter(mEmptyWrapper);
		
	}
	
	/**
	 * Bind compound listener.
	 *
	 * @param compoundLayout Compound layout.
	 */
	private CompoundLayout bindCompoundListener ( final CompoundLayout compoundLayout )
	{
		compoundLayout.setOnCheckedChangeListener(( compound, checked ) ->
		{
			{
				if ( checked )
				{
					compound.setBackgroundColor(getResources().getColor(R.color.color_str_tool));
					compound.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out));
					for ( Integer integer : mViewId )
					{
						if ( integer == compound.getId() )
						{
							mTypeStr = mStrTypeMap.get(compound.getId());
							break;
						}
					}
				} else
				{
					compound.setBackgroundColor(getResources().getColor(R.color.white));
				}
			}
		});
		return compoundLayout;
	}
	
	private Bill getBillForMDatas ( Bill bill )
	{
		for ( BillRV billRV : mDatas )
		{
			if ( billRV.getTpye().equals("餐饮") )
			{
				bill.setEat(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("交通") )
			{
				bill.setTraffic(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("购物") )
			{
				bill.setBuy(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("娱乐") )
			{
				bill.setAmusement(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("住宿") )
			{
				bill.setStay(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("门票") )
			{
				bill.setTicket(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("医疗") )
			{
				bill.setTreat(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("保险") )
			{
				bill.setInsurance(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("其他") )
			{
				bill.setOther(Integer.parseInt(billRV.getMoney()));
			}
		}
		return bill;
	}
	
	private void initRxBus ()
	{
//		RxBus.getInstance()
//				.register(this)
//				.tObservable(Event.class)
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(
//						event ->
//						{
//							if ( event.getEvent() == Event.EVENT_CHANGE_CITY )
//							{
//								SceneryConstant.PAGE = 1;       //将页数置为1
//								mDatas.clear(); //清除所有对象
//								lazyLoad();     //重新导入数据
//							}
//
//						}
//				);
	}
	
	/**
	 * 懒加载
	 */
	@Override
	protected void lazyLoad ()
	{
//		if ( mTwinklingRefreshLayout != null )
//			mTwinklingRefreshLayout.startRefresh();
	}

//	@Override
//	protected void onFragmentVisibleChange ( boolean isVisible )
//	{
//		if ( isVisible )    //   do things when fragment is visible
//		{
////			mTwinklingRefreshLayout.startRefresh();
////			initView();
////			initRecycleView();
////			initRefreshLayout();
////			loadData();
//		} else
//		{
////			mTwinklingRefreshLayout.finishRefreshing();
//		}
//	}
	
	/**
	 * 导入景点列表数据
	 */
//	private void loadData ()
//	{
//		/**观察者**/
//		getSceneryDataByNet(
//				SharedPreferenceUtil.getInstance().getCityProId(), SharedPreferenceUtil.getInstance().getCityId(), SceneryConstant.PAGE, false)
//				.subscribe(new Observer< Scenery >()
//				{
//					@Override
//					public void onSubscribe ( Disposable d )
//					{
//
//					}
//
//					@Override
//					public void onNext ( Scenery scenery )  //scenery must not null,because emitter can not send null object.
//					{
//
//						if ( scenery.result != null && ! mDatas.contains(scenery.result) )        //成立条件：获取数据不为空&原本容器不包含现获取数据
//						{
//							SceneryConstant.PAGE++;
//							LogUtils.d(scenery.result.get(0).address);
//							mDatas.addAll(scenery.result);
//							mEmptyWrapper.notifyDataSetChanged();
//						} else
//						{
//							ToastUtils.showShort(mContext, scenery.reason);
//							LogUtils.d(scenery.reason);
//						}
//					}
//
//					@Override
//					public void onError ( Throwable e )
//					{
//						LogUtils.d(e.toString());
//					}
//
//					@Override
//					public void onComplete ()
//					{
//						ToastUtils.showShort(mContext, getString(R.string.addComplete));
//					}
//				});
//	}
//
//	/**
//	 * 从网络获取景点列表数据
//	 *
//	 * @param proId    API need.
//	 * @param cityId   API need.
//	 * @param page     第几页
//	 * @param paybyvas 是否密文
//	 * @return
//	 */
//	private Observable< Scenery > getSceneryDataByNet ( final int proId, final int cityId, final int page, final boolean paybyvas )
//	{
//
//		return RetrofitScenery.getInstance()
//				       .getScenery(proId, cityId, page, paybyvas)
//				       .compose(this.bindToLifecycle());   /**更新天气处*/
//	}
//
//
//		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
//		{
//			@Override
//			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
//			{
//				String url = mDatas.get(position).url;
//				String title = mDatas.get(position).title;
//				LogUtils.d(url);
//
//				/**build-WebView**/
//
//				Utils.showWeb(mContext,title, url);
//
////				Intent intent = new Intent(mContext, WebActivity.class);
////				intent.putExtra("URL",url);
////				startActivity(intent);
//			}
//
//			@Override
//			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
//			{
//				return false;
//			}
//		});
//
//		mEmptyWrapper = new EmptyWrapper< Scenery.ResultBean >(mAdapter);
//		mEmptyWrapper.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.view_empty, mRecyclerView, false));
//
//		mRecyclerView.setAdapter(mEmptyWrapper);
//
//
//	}
//
//	/**
//	 * 初始化上下拉刷新布局
//	 */
//	private void initRefreshLayout ()
//	{
//		BezierLayout headerView = new BezierLayout(mContext);
//		mTwinklingRefreshLayout.setHeaderView(headerView);
//		mTwinklingRefreshLayout.setOverScrollBottomShow(false);
//
//		mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter()
//		{
//			@Override
//			public void onRefresh ( TwinklingRefreshLayout refreshLayout )      //上拉监听
//			{
//				super.onRefresh(refreshLayout);
//				new Handler().postDelayed(() ->
//				{
//					loadData();
//					refreshLayout.finishRefreshing();       //关闭刷新图标
//				}, 500);
//			}
//
//			@Override
//			public void onLoadMore ( TwinklingRefreshLayout refreshLayout )     //下拉监听
//			{
//				super.onLoadMore(refreshLayout);
//				new Handler().postDelayed(() ->
//				{
//					loadData();
//					refreshLayout.finishLoadmore();
//				}, 500);
//			}
//		});
//	}
	
}


