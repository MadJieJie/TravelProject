package com.fengjie.myapplication.utils.often;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Created by MadJieJie on 2017/3/11-16:37.
 * @brief
 * @attention
 */

public abstract class AbstractSimpleObserver<T> implements Observer<T>
{
	@Override
	public void onSubscribe ( Disposable d )
	{
		
	}
	
	@Override
	public void onError ( Throwable e )
	{
		LogUtils.e(e.toString());
	}
	
	@Override
	public void onComplete ()
	{
		ToastUtils.showShort("加载完毕，✺◟(∗❛ัᴗ❛ั∗)◞✺");
	}
	
	
}
