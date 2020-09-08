package com.yangxuydong.mybanner

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.*
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.squareup.picasso.Picasso
import com.yangxuydong.mybanner.MyViewPager.OnViewPagerTouchEvent
import kotlinx.android.synthetic.main.banner.view.*
import java.lang.reflect.Field


/**
 *创建时间：2020/7/8
 *编写人：czy_yangxudong
 *功能描述：自定义view banner
 */
 public class MyBanner(context: Context?, attributeSet: AttributeSet?) : RelativeLayout(
    context,
    attributeSet
),
    ViewPager.OnPageChangeListener,OnViewPagerTouchEvent{

    //改变前的位置
    var prePosition=1

    //循环间隔时间
    private var DELAY_TIME:Long=1500
    //滚动时间
    private var SCROLL_TIME:Int=300
    //标记是否显示标题
    private var IS_TITLE_VISIBLE:Boolean=true
    //标记是否显示指示器
    private var IS_POINTER_VISIBLE:Boolean=true
    //标题字体字号
    private var TITLE_SIZE:Float=20f
    //标题字体颜色
    private var TITLE_COLOR: String ="#333333"
    //标题背景颜色
    private var TITLE_BACKGROUND_COLOR: String ="#33666666"
    //标题底部边距
    private var TITLE_MARGIN_BOTTOM: Int =0
    //指示器底部边距
    private var POINTER_MARGIN_BOTTOM: Int =0
    //指示器自身间距
    private var POINTER_MARGIN_LEFT: Int =15
    //指示器宽度
    private var POINTER_WIDTH: Int =0
    //指示器高度
    private var POINTER_HEIGHT: Int =0
    //指示器 选中状态的图片
    private var POINTER_CHECKED_IMAGE:Int =android.R.color.holo_orange_dark
    //指示器 未选中状态的图片
    private var POINTER_UNCHECKED_IMAGE:Int =android.R.color.black
    //图片加载错误的时候默认显示的图片
    private var ERROR_IMAGE:Int =android.R.color.transparent

    companion object{
        private var listImage= arrayListOf<ImageView>()
        private var listTitle= arrayListOf<String>()

        //改变后的位置
        var pos =1

        lateinit var myOnBannerItemClickListening:OnBannerItemClickListening
        private val mHandler = Handler()

        //是否暂停
        private var isPause = false
    }


    //设置滚动间隔时间
    fun setDelayTime(time: Long){
        DELAY_TIME=time
    }

    //设置滚动时间
    fun setScrollTime(time: Int){
        SCROLL_TIME=time
    }

    //设置标题显示与否
    fun setTitleVisible(boolean: Boolean){
        IS_TITLE_VISIBLE=boolean
    }

    //设置指示器显示与否
    fun setPointerVisible(boolean: Boolean){
        IS_POINTER_VISIBLE=boolean
    }

    //设置标题字号
    fun setTitleSize(f: Float){
        TITLE_SIZE=f
    }

    //设置标题颜色
    fun setTitleColor(s: String){
        TITLE_COLOR=s
    }

    //设置标题背景颜色
    fun setTitleBackGroundColor(s: String){
        TITLE_BACKGROUND_COLOR=s
    }

    //设置标题底边距
    fun setTitleMarginBottom(i: Int){
        TITLE_MARGIN_BOTTOM=i
    }

    //设置指示器选中显示的图片
    fun setPointerCheckedImage(i: Int){
        POINTER_CHECKED_IMAGE=i
    }

    //设置指示器未选中显示的图片
    fun setPointerUnCheckedImage(i: Int){
        POINTER_UNCHECKED_IMAGE=i
    }

    //设置指示器底边距 与 指示器之间的间距
    fun setPointerMargin(marginBottom: Int, marginLeft: Int){
        POINTER_MARGIN_BOTTOM=marginBottom
        POINTER_MARGIN_LEFT=marginLeft
    }

    //设置指示器宽和高
    fun setPointerSize(width: Int, height: Int){
        POINTER_WIDTH=width
        POINTER_HEIGHT=height
    }

    //设置加载错误默认展示的图片
    fun setErrorImage(i: Int){
        ERROR_IMAGE=i
    }

    init {
        View.inflate(context, R.layout.banner, this)

        initView()
    }

    private fun initView() {
        //设置单次滚动持续时间
        try {
            val field: Field = ViewPager::class.java.getDeclaredField("mScroller")
            field.setAccessible(true)
            val scroller = FixedSpeedScroller(
                vp.getContext(),
                AccelerateInterpolator()
            )
            field.set(vp, scroller)
            scroller.setmDuration(SCROLL_TIME)
        } catch (e: Exception) {

        }

        vp.setOnViewPagerTouchEventListener(this)

        vp.adapter=MyAdapter()
    }

    fun initData(imags: MutableList<String?>, title: List<String>){
        listImage.clear()
        listTitle.clear()
        listTitle.addAll(title)

        if (imags.size==1){
            var imageView=ImageView(context)
            Picasso.get().load(imags.get(0)).error(ERROR_IMAGE).into(imageView)
            imageView.scaleType=ImageView.ScaleType.FIT_XY
            listImage.add(imageView)

            var point=ImageView(context)
            var layoutParams=LinearLayout.LayoutParams(POINTER_WIDTH, POINTER_HEIGHT)
            layoutParams.leftMargin=POINTER_MARGIN_LEFT
            point.layoutParams=layoutParams

            point.setBackgroundResource(POINTER_UNCHECKED_IMAGE)
            ll_point.addView(point)
        }else{
            for (i in 0..imags.size+1){
                //Log.i("test","test="+i)
                //实现原理：如果传过来的集合里有a b c ,则把集合变成 c a b c a
                var imageView=ImageView(context)
                if (i==0){
                    Picasso.get().load(imags.get(imags.size - 1)).error(ERROR_IMAGE).into(imageView)
                }else if (i==imags.size+1){
                    Picasso.get().load(imags.get(0)).error(ERROR_IMAGE).into(imageView)
                }else{
                    Picasso.get().load(imags.get(i - 1)).error(ERROR_IMAGE).into(imageView)

                    //var point=View(context)
                    var point=ImageView(context)
                    var layoutParams=LinearLayout.LayoutParams(POINTER_WIDTH, POINTER_HEIGHT)
                    layoutParams.leftMargin=POINTER_MARGIN_LEFT
                    point.layoutParams=layoutParams

                    point.setBackgroundResource(POINTER_UNCHECKED_IMAGE)
                    ll_point.addView(point)
                }
                imageView.scaleType=ImageView.ScaleType.FIT_XY
                listImage.add(imageView)
            }
        }

        vp.adapter=MyAdapter()
        vp.setCurrentItem(1, false)

        if (IS_TITLE_VISIBLE){
            tv_title.visibility= View.VISIBLE

            tv_title.setText(title.get(0))
            tv_title.textSize=TITLE_SIZE
            tv_title.setTextColor(Color.parseColor(TITLE_COLOR))
            tv_title.setBackgroundColor(Color.parseColor(TITLE_BACKGROUND_COLOR))

            val p =tv_title.getLayoutParams() as LinearLayout.LayoutParams
            p.bottomMargin =TITLE_MARGIN_BOTTOM
            tv_title.setLayoutParams(p)
        }else{
            tv_title.visibility= View.GONE
        }
        if (IS_POINTER_VISIBLE){
            ll_point.visibility= View.VISIBLE

            val params =ll_point.getLayoutParams() as LinearLayout.LayoutParams
            params.bottomMargin =POINTER_MARGIN_BOTTOM
            ll_point.setLayoutParams(params)

        }else{
            ll_point.visibility= View.GONE
        }

        ll_point.getChildAt(0).setBackgroundResource(POINTER_CHECKED_IMAGE)

        vp.setOnPageChangeListener(this)

        if (imags.size>1){
            mHandler.postDelayed(MyRunnable, DELAY_TIME)
        }
    }


    private val MyRunnable = object : Runnable {
        override fun run() {
            vp.setCurrentItem(vp.currentItem + 1, true)
            if (!isPause){
                mHandler.postDelayed(this, DELAY_TIME)
            }
        }
    }


    class MyAdapter: PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view==`object`
        }

        override fun getCount(): Int {
            return listImage.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(listImage.get(position))

            listImage.get(position).setOnClickListener { e->
                Log.i("test", "点击了" + (pos - 1))
                myOnBannerItemClickListening.onItemClickListening(pos - 1)
            }

            return listImage.get(position)
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(listImage.get(position))
        }

    }


    //page滚动状态改变的时候调用
    override fun onPageScrollStateChanged(state: Int) {
        if (state== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            if (pos== listImage.size-2||pos==1){
                vp.setCurrentItem(pos, false)
            }
        }
    }

    //page滚动监听
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}


    //page选中监听
    override fun onPageSelected(position: Int) {
        pos=position
        if (position==0){
            //如果滚到到第0个item，就瞬间把角标变成1倒数第二个item，并setCurrentItem
            pos= listImage.size-2
        }else if(position== listImage.size-1){
            //如果滚动到集合的最后一个item，就瞬间把角标变成1，并setCurrentItem
            pos=1
        }

        Log.i("test", "改变后的位置pos:" + pos);
        //Log.i("test", "prePos:" + prePosition + ",pos:" + pos);

        if (prePosition!=pos){
            tv_title.text=listTitle.get(pos - 1)

            ll_point.getChildAt(pos - 1).setBackgroundResource(POINTER_CHECKED_IMAGE)
            ll_point.getChildAt(prePosition - 1).setBackgroundResource(POINTER_UNCHECKED_IMAGE)

            prePosition=pos
        }
    }

    interface OnBannerItemClickListening{
        fun onItemClickListening(p: Int)
    }

    fun setOnBannerItemClickListening(onItemClickListening: OnBannerItemClickListening){
        myOnBannerItemClickListening=onItemClickListening
    }

    override fun onTouchDown() {
        isPause=true
        mHandler.removeCallbacks(MyRunnable)
    }

    override fun onTouchUp() {
        isPause=false
        mHandler.postDelayed(MyRunnable, DELAY_TIME)
    }

}