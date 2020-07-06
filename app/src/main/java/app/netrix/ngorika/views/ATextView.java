package app.netrix.ngorika.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import app.netrix.ngorika.R;
import app.netrix.ngorika.utils.Convert;


public class ATextView extends android.support.v7.widget.AppCompatTextView {
	private onSizeChangedListener lsn;
	private int width=0;
	private int height=0;
	private int rightPD;
	public ATextView(Context context) {
		super(context);
		this.rightPD= Convert.PixelFromDp(getContext(), 47);
		init(null);
		// TODO Auto-generated constructor stub
	}
	public ATextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.rightPD=Convert.PixelFromDp(getContext(), 47);
		init(attrs);
		// TODO Auto-generated constructor stub
	}
	private void init(AttributeSet attrs){
		if(attrs != null){
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MagicTextView);

			if(a.hasValue(R.styleable.MagicTextView_font)){
		        String stringFont = a.getString(R.styleable.MagicTextView_font);
			    Typeface font = Typeface.createFromAsset(getContext().getAssets(), stringFont);
			    this.setTypeface(font);
			}
			a.recycle();
		}
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.width=w;
		this.height=h;
		if(lsn!=null) lsn.onChanged(w, h, oldw, oldh);
	}
	public void setOnSizeChangedListener(onSizeChangedListener lsn){
		this.lsn=lsn;
	}
	
	public interface onSizeChangedListener{
		public void onChanged(int w, int h, int oldw, int oldh);
	}
	public void setFRText(String value){
		if(this.width!=0){
			Rect bounds = new Rect();
			Paint textPaint = getPaint();
			textPaint.getTextBounds(value,0,value.length(),bounds);
			int height = bounds.height();
			int width = bounds.width();
			if(width>this.width-rightPD){
				setPadding(0, 0, 0, 0);
			}else{
				setPadding(0, 0, rightPD, 0);
			}
		}
		
		setText(value);
	}

}
