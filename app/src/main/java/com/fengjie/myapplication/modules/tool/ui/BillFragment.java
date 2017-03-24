package com.fengjie.myapplication.modules.tool.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import com.fengjie.myapplication.base.BaseApplication;
import com.fengjie.myapplication.modules.tool.base.weather.AbstractRetrofitFragment;
import com.fengjie.myapplication.modules.tool.bean.Bill;
import com.fengjie.myapplication.modules.tool.bean.BillRV;
import com.fengjie.myapplication.modules.tool.db.bill.BillDao;
import com.fengjie.myapplication.modules.tool.db.weather.Time;
import com.fengjie.myapplication.modules.tool.utils.weather.RxUtils;
import com.fengjie.myapplication.utils.often.DateUtils;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.jaouan.compoundlayout.CompoundLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static com.fengjie.myapplication.R.id.date_tv_bill;


/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief 记账本
 * @attention
 */

public class BillFragment extends AbstractRetrofitFragment
{
	
	private RecyclerView mRecyclerView = null;
	private static List< BillRV > mDatas = new ArrayList< BillRV >();
	private Context mContext = null;
	
	private CommonAdapter< BillRV > mAdapter = null;
	private EmptyWrapper mEmptyWrapper = null;
	private FloatingActionButton mAdd_fab;
	
	private AlertDialog mAlertDialog;
	private DatePicker mDatePicker = null;
	private TextView mMoney_et_bill;
	private TextView mDate_tv_bill;
	
	/** Paramters */
	private Map< Integer, String > mStrTypeMap = new HashMap< Integer, String >();
	private Map< String, Integer > mMipmapMap = new HashMap< String, Integer >();
	private int[] mMipmapIdArray = { R.mipmap.iv_eat_bill, R.mipmap.iv_traffic_bill, R.mipmap.iv_buy_bill, R.mipmap.iv_amusement_bill, R.mipmap.iv_stay_bill
			, R.mipmap.iv_ticket_bill, R.mipmap.iv_treat_bill, R.mipmap.iv_insurance_bill, R.mipmap.iv_other_bill };
	private Integer[] mViewId = { R.id.eat_rdl_bill, R.id.traffic_rdl_bill, R.id.buy_rdl_bill, R.id.amusement_rdl_bill, R.id.stay_rdl_bill
			, R.id.ticket_rdl_bill, R.id.treat_rdl_bill, R.id.insurance_rdl_bill, R.id.other_rdl_bill };
//	private String[] mTypeArray = { "餐饮", "交通", "购物", "娱乐", "住宿", "门票", "医疗", "保险", "其他" };
	private String[] mTypeArray = { "eat", "traffic", "buy", "amusement", "stay", "ticket", "treat", "insurance", "other" };
//	private String mTypeStr = "餐饮";
	private String mTypeStr = "eat";
	private boolean mIsTypeExist = false;
	private boolean mIsDateIsExistData = false;
	private volatile String mDate = null;
	
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
	public void onDestroy ()
	{
		super.onDestroy();
		keepDataToDB(mDate);    //将数据插入数据库
//		RxBus.getInstance().unRegister(this);           //订阅事件
	}
	
	
	private void findView ( View view )
	{
		mMoney_et_bill = ( TextView ) mView.findViewById(R.id.money_et_bill);
		mAdd_fab = ( FloatingActionButton ) mView.findViewById(R.id.add_fab_bill);
		mDate_tv_bill = ( TextView ) mView.findViewById(date_tv_bill);
	}
	
	private void initView ()
	{
		mView.findViewById(R.id.date_lv_bill).setOnClickListener(v -> mAlertDialog.show());
		
		mDate_tv_bill.setText(DateUtils.getNowDate());
		
		mAdd_fab.setOnClickListener(v ->         //添加账单条目
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
				}
				
				mEmptyWrapper.notifyDataSetChanged();     //更新Adapter
			}
			
		});
	}
	
	/**
	 * 初始化装载日期选择器的
	 */
	private void initDatePickerDialog ()
	{
		mDate = DateUtils.getNowDate();
		queryDateHaveData(mDate);           //检查该天是否存在数据,若存在则取出数据显示
		
		mDatePicker = new DatePicker(mContext);
		mDatePicker.setDate(Integer.parseInt(Time.getNowYear()), Integer.parseInt(Time.getNowMonth()));      //必选选择一个日期
		mDatePicker.setMode(DPMode.SINGLE);         //single-单选模式
		mDatePicker.setOnDatePickedListener(date ->
		{
			mDate = date;               //缓存当前选择的日期
//			ToastUtils.showShort(BaseApplication.sAppContext, date);      /**获得选择的日期*/
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
			new Handler().post(() -> keepDataToDB(mDate_tv_bill.getText().toString()));            /**先保存之前日期数据入数据库*/
			new Handler().postDelayed(() -> queryDateHaveData(mDate), 250);                     /**搜寻时间选择器*/
			mDate_tv_bill.setText(mDate);                                        /**更新TextView日期*/
			ToastUtils.showShort(BaseApplication.sAppContext, "已保存数据");
		});
//		mAlertDialog.dismiss();
		
	}
	
	/**
	 * 初始化选择界面Dialog.
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
		
		/**设置RecyclerView垂直放置内容*/
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

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
	
	/**
	 * 收集当前mDatas的数据
	 *
	 * @param bill
	 * @return
	 */
	private Bill getBillForMDatas ( Bill bill )
	{
		for ( BillRV billRV : mDatas )
		{
			if ( billRV.getTpye().equals("eat") )
			{
				bill.setEat(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("traffic") )
			{
				bill.setTraffic(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("buy") )
			{
				bill.setBuy(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("amusement") )
			{
				bill.setAmusement(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("stay") )
			{
				bill.setStay(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("ticket") )
			{
				bill.setTicket(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("treat") )
			{
				bill.setTreat(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("insurance") )
			{
				bill.setInsurance(Integer.parseInt(billRV.getMoney()));
			} else if ( billRV.getTpye().equals("other") )
			{
				bill.setOther(Integer.parseInt(billRV.getMoney()));
			}
			
		}
		return bill;
	}
	
	/**
	 * 检查该天是否存在数据方法,若存在则取出数据显示
	 */
	private void queryDateHaveData ( final String date )
	{
		Observable
				.create(new ObservableOnSubscribe< Bill >()
				{
					@Override
					public void subscribe ( ObservableEmitter< Bill > e ) throws Exception
					{
						Bill bill = BillDao.getQueryBillForDB(date);
						if ( bill != null )
							e.onNext(bill);
						else
							e.onNext(new Bill(Bill.NOT_EXIST_DATA));
						e.onComplete();
					}
				})
				.compose(RxUtils.rxSchedulerHelper())
				.subscribe(bill ->
				{
					
					mDatas.clear();              /**先清空现数据*/
					if ( bill.getId() > Bill.EXIST_DATA )         /**如果存在数据则更新*/
					{
						int[] intArray = bill.getAllArray();
						for ( int position = 0; position < bill.getAllArray().length; position++ )
						{
							if ( intArray[position] > 0 )
							{
								mDatas.add(new BillRV(mMipmapIdArray[position], intArray[position] + "", mTypeArray[position]));
							}
						}
						mEmptyWrapper.notifyDataSetChanged();    //更新RecyclerView
						mIsDateIsExistData = true;      //该日期有数据
					} else if ( bill.getId() == Bill.NOT_EXIST_DATA )        /**如果不存在数据则清空界面*/
					{
						mIsDateIsExistData = false;
						mEmptyWrapper.notifyDataSetChanged();    //更新RecyclerView
					}
				});
		
	}
	
	/**
	 * 将数据存入数据库,若数据库存在该date的数据则更新,反之插入
	 *
	 * @param date
	 */
	private void keepDataToDB ( final String date )
	{
		Observable
				.create(new ObservableOnSubscribe< Bill >()
				{
					@Override
					public void subscribe ( ObservableEmitter< Bill > e ) throws Exception
					{
						Bill bill = getBillForMDatas(new Bill(date));       //获取当前界面的所有数据
						if ( bill != null )
							e.onNext(bill);
						e.onComplete();
					}
				})
				.compose(RxUtils.rxSchedulerHelper())
				.subscribe(bill ->
				{
					if ( mIsDateIsExistData )                      /**数据库已存在数据,则更新*/
					{
						BillDao.updateDataToDB(bill);
					} else if ( mDatas.size() > 0 )             /**如果mData有数据则插入数据*/
					{
						BillDao.insertDataToDB(bill);
					}
				});
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
	
}


