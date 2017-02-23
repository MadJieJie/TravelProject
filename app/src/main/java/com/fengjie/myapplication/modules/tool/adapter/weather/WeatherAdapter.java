package com.fengjie.myapplication.modules.tool.adapter.weather;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.modules.tool.bean.Weather;
import com.fengjie.myapplication.utils.weather.ImageLoader;
import com.fengjie.myapplication.modules.tool.db.weather.SharedPreferenceUtil;
import com.fengjie.myapplication.utils.weather.Util;

import butterknife.BindView;

import static com.fengjie.myapplication.R.id.flu_brief;
import static com.fengjie.myapplication.R.id.flu_txt;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief  RecyclerView的Adapter
 * @attention 必须转换成GBK,因为URL端接收的是中文
 */

public class WeatherAdapter extends AnimRecyclerViewAdapter< RecyclerView.ViewHolder >
{
	private static String TAG = WeatherAdapter.class.getSimpleName();

	private Context mContext;
	/**
	 * 参数为第几个卡片
	 */
	private static final int TYPE_ONE = 0;
	private static final int TYPE_TWO = 1;
	private static final int TYPE_THREE = 2;
	private static final int TYPE_FORE = 3;

	private Weather mWeatherData;

	public WeatherAdapter ( Weather weatherData )
	{
		this.mWeatherData = weatherData;
	}

	/**
	 * 返回卡片位置
	 * @param position
	 * @return
	 */
	@Override
	public int getItemViewType ( int position )
	{
		if ( position == WeatherAdapter.TYPE_ONE )
		{
			return WeatherAdapter.TYPE_ONE;
		}
		if ( position == WeatherAdapter.TYPE_TWO )
		{
			return WeatherAdapter.TYPE_TWO;
		}
		if ( position == WeatherAdapter.TYPE_THREE )
		{
			return WeatherAdapter.TYPE_THREE;
		}
		if ( position == WeatherAdapter.TYPE_FORE )
		{
			return WeatherAdapter.TYPE_FORE;
		}
		return super.getItemViewType(position);
	}

	/**
	 * 对不同的卡片进行不同的 持有
 * @param parent
	 * @param viewType
	 * @return
	 */
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder ( ViewGroup parent, int viewType )
	{
		mContext = parent.getContext();
		switch ( viewType )
		{
			case TYPE_ONE:
				return new NowWeatherViewHolder(
						                               LayoutInflater.from(mContext).inflate(R.layout.item_temperature, parent, false));
			case TYPE_TWO:
				return new HoursWeatherViewHolder(
						                                 LayoutInflater.from(mContext).inflate(R.layout.item_hour_info, parent, false));
			case TYPE_THREE:
				return new SuggestionViewHolder(
						                               LayoutInflater.from(mContext).inflate(R.layout.item_suggestion, parent, false));
			case TYPE_FORE:
				return new ForecastViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_forecast, parent, false));
		}
		return null;
	}

	@Override
	public void onBindViewHolder ( RecyclerView.ViewHolder holder, int position )
	{
		int itemType = getItemViewType(position);
		switch ( itemType )
		{
			case TYPE_ONE:
				( ( NowWeatherViewHolder ) holder ).bind(mWeatherData);
				break;
			case TYPE_TWO:
				( ( HoursWeatherViewHolder ) holder ).bind(mWeatherData);
				break;
			case TYPE_THREE:
				( ( SuggestionViewHolder ) holder ).bind(mWeatherData);
				break;
			case TYPE_FORE:
				( ( ForecastViewHolder ) holder ).bind(mWeatherData);
				break;
			default:
				break;
		}
		if ( SharedPreferenceUtil.getInstance().getMainAnim() )
		{
			showItemAnim(holder.itemView, position);
		}
	}

	@Override
	public int getItemCount ()
	{
		return mWeatherData.status != null ? 4 : 0;
	}

	/**
	 * 当前天气情况
	 */
	private class NowWeatherViewHolder extends BaseViewHolder< Weather >
	{

//		@BindView ( R.id.weather_icon )
		ImageView weatherIcon;
//		@BindView ( R.id.temp_flu )
		TextView tempFlu;
//		@BindView ( R.id.temp_max )
		TextView tempMax;
//		@BindView ( R.id.temp_min )
		TextView tempMin;
//		@BindView ( R.id.temp_pm )
		TextView tempPm;
//		@BindView ( R.id.temp_quality )
		TextView tempQuality;
//		@BindView ( R.id.cardView )
		CardView cardView;

		NowWeatherViewHolder ( View itemView )
		{
			super(itemView);
			weatherIcon = ( ImageView ) itemView.findViewById(R.id.weather_icon);
			tempFlu = ( TextView ) itemView.findViewById(R.id.temp_flu);
			tempMax = ( TextView ) itemView.findViewById(R.id.temp_max);
			tempMin = ( TextView ) itemView.findViewById(R.id.temp_min);
			tempPm = ( TextView ) itemView.findViewById(R.id.temp_pm);
			tempQuality = ( TextView ) itemView.findViewById(R.id.temp_quality);
			cardView = ( CardView ) itemView.findViewById(R.id.cardView);
		}

		protected void bind ( Weather weather )
		{
			try
			{
				tempFlu.setText(String.format("%s℃", weather.now.tmp));
				tempMax.setText(
						String.format("↑ %s ℃", weather.dailyForecast.get(0).tmp.max));
				tempMin.setText(
						String.format("↓ %s ℃", weather.dailyForecast.get(0).tmp.min));

				tempPm.setText(String.format("PM2.5: %s μg/m?", Util.safeText(weather.aqi.city.pm25)));
				tempQuality.setText(Util.safeText("空气质量： ", weather.aqi.city.qlty));
				ImageLoader.load(itemView.getContext(),
						SharedPreferenceUtil.getInstance().getInt(weather.now.cond.txt, R.mipmap.none),
						weatherIcon);
			} catch( Exception e )
			{
			}
		}
	}

	/**
	 * 当日小时预告
	 */
	private class HoursWeatherViewHolder extends BaseViewHolder< Weather >
	{
		private LinearLayout itemHourInfoLayout;
		private TextView[] mClock = new TextView[mWeatherData.hourlyForecast.size()];
		private TextView[] mTemp = new TextView[mWeatherData.hourlyForecast.size()];
		private TextView[] mHumidity = new TextView[mWeatherData.hourlyForecast.size()];
		private TextView[] mWind = new TextView[mWeatherData.hourlyForecast.size()];

		HoursWeatherViewHolder ( View itemView )
		{
			super(itemView);
			itemHourInfoLayout = ( LinearLayout ) itemView.findViewById(R.id.item_hour_info_linearlayout);

			for ( int i = 0; i < mWeatherData.hourlyForecast.size(); i++ )
			{
				View view = View.inflate(mContext, R.layout.item_hour_info_line, null);
				mClock[i] = ( TextView ) view.findViewById(R.id.one_clock);
				mTemp[i] = ( TextView ) view.findViewById(R.id.one_temp);
				mHumidity[i] = ( TextView ) view.findViewById(R.id.one_humidity);
				mWind[i] = ( TextView ) view.findViewById(R.id.one_wind);
				itemHourInfoLayout.addView(view);
			}
		}

		protected void bind ( Weather weather )
		{

			try
			{
				for ( int i = 0; i < weather.hourlyForecast.size(); i++ )
				{
					//s.subString(s.length-3,s.length);
					//第一个参数是开始截取的位置，第二个是结束位置。
					String mDate = weather.hourlyForecast.get(i).date;
					mClock[i].setText(
							mDate.substring(mDate.length() - 5, mDate.length()));
					mTemp[i].setText(
							String.format("%s℃", weather.hourlyForecast.get(i).tmp));
					mHumidity[i].setText(
							String.format("%s%%", weather.hourlyForecast.get(i).hum)
					);
					mWind[i].setText(
							String.format("%sKm/h", weather.hourlyForecast.get(i).wind.spd)
					);
				}
			} catch( Exception e )
			{
			}
		}
	}

	/**
	 * 当日建议
	 */
	private class SuggestionViewHolder extends BaseViewHolder< Weather >
	{
		@BindView ( R.id.cloth_brief )
		TextView clothBrief;
		@BindView ( R.id.cloth_txt )
		TextView clothTxt;
		@BindView ( R.id.sport_brief )
		TextView sportBrief;
		@BindView ( R.id.sport_txt )
		TextView sportTxt;
		@BindView ( R.id.travel_brief )
		TextView travelBrief;
		@BindView ( R.id.travel_txt )
		TextView travelTxt;
		@BindView ( flu_brief )
		TextView fluBrief;
		@BindView ( flu_txt )
		TextView fluTxt;

		SuggestionViewHolder ( View itemView )
		{
			super(itemView);
			clothBrief = ( TextView ) itemView.findViewById(R.id.cloth_brief);
			clothTxt = ( TextView ) itemView.findViewById(R.id.cloth_txt);
			sportBrief = ( TextView ) itemView.findViewById(R.id.sport_brief);
			sportTxt = ( TextView ) itemView.findViewById(R.id.sport_txt);
			travelBrief = ( TextView ) itemView.findViewById(R.id.travel_brief);
			travelTxt = ( TextView ) itemView.findViewById(R.id.travel_txt);
			fluBrief = ( TextView ) itemView.findViewById(flu_brief);
			fluTxt = ( TextView ) itemView.findViewById(flu_txt);
		}

		protected void bind ( Weather weather )
		{
			try
			{
				clothBrief.setText(String.format("穿衣指数---%s", weather.suggestion.drsg.brf));
				clothTxt.setText(weather.suggestion.drsg.txt);

				sportBrief.setText(String.format("运动指数---%s", weather.suggestion.sport.brf));
				sportTxt.setText(weather.suggestion.sport.txt);

				travelBrief.setText(String.format("旅游指数---%s", weather.suggestion.trav.brf));
				travelTxt.setText(weather.suggestion.trav.txt);

				fluBrief.setText(String.format("感冒指数---%s", weather.suggestion.flu.brf));
				fluTxt.setText(weather.suggestion.flu.txt);
			} catch( Exception e )
			{
			}
		}
	}

	/**
	 * 未来天气
	 */
	private class ForecastViewHolder extends BaseViewHolder< Weather >
	{
		private LinearLayout forecastLinear;
		private TextView[] forecastDate = new TextView[mWeatherData.dailyForecast.size()];
		private TextView[] forecastTemp = new TextView[mWeatherData.dailyForecast.size()];
		private TextView[] forecastTxt = new TextView[mWeatherData.dailyForecast.size()];
		private ImageView[] forecastIcon = new ImageView[mWeatherData.dailyForecast.size()];

		ForecastViewHolder ( View itemView )
		{
			super(itemView);
			forecastLinear = ( LinearLayout ) itemView.findViewById(R.id.forecast_linear);
			for ( int i = 0; i < mWeatherData.dailyForecast.size(); i++ )
			{
				View view = View.inflate(mContext, R.layout.item_forecast_line, null);
				forecastDate[i] = ( TextView ) view.findViewById(R.id.forecast_date);
				forecastTemp[i] = ( TextView ) view.findViewById(R.id.forecast_temp);
				forecastTxt[i] = ( TextView ) view.findViewById(R.id.forecast_txt);
				forecastIcon[i] = ( ImageView ) view.findViewById(R.id.forecast_icon);
				forecastLinear.addView(view);
			}
		}

		protected void bind ( Weather weather )
		{
			try
			{
				//今日 明日
				forecastDate[0].setText(mContext.getString(R.string.today));
				forecastDate[1].setText(mContext.getString(R.string.tomorrow));
				for ( int i = 0; i < weather.dailyForecast.size(); i++ )
				{
					if ( i > 1 )
					{
						try
						{
							forecastDate[i].setText(
									Util.dayForWeek(weather.dailyForecast.get(i).date));
						} catch( Exception e )
						{
						}
					}
					ImageLoader.load(mContext,
							SharedPreferenceUtil.getInstance().getInt(weather.dailyForecast.get(i).cond.txtD, R.mipmap.none),
							forecastIcon[i]);
					forecastTemp[i].setText(
							String.format("%s℃ - %s℃",
									weather.dailyForecast.get(i).tmp.min,
									weather.dailyForecast.get(i).tmp.max));
					forecastTxt[i].setText(
							String.format("%s。 %s %s %s km/h。 降水几率 %s%%。",
									weather.dailyForecast.get(i).cond.txtD,
									weather.dailyForecast.get(i).wind.sc,
									weather.dailyForecast.get(i).wind.dir,
									weather.dailyForecast.get(i).wind.spd,
									weather.dailyForecast.get(i).pop));
				}
			} catch( Exception e )
			{
			}
		}
	}
}
