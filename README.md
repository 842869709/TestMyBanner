# MyBanner
banner广告轮播控件

示例图片

![](https://github.com/842869709/TestMyBanner/blob/master/test.png)

## 1.用法
使用前，对于Android Studio的用户，可以选择添加:

```gradle
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
	dependencies {
	        implementation 'com.github.842869709:TestMyBanner:Tag'
	}
```
## 2.功能参数与含义
配置参数|参数含义
-|-
multiMode|	图片选着模式，单选/多选
selectLimit|	多选限制数量，默认为9
showCamera|	选择照片时是否显示拍照按钮
crop|	是否允许裁剪（单选有效）
style|	有裁剪时，裁剪框是矩形还是圆形
focusWidth|	矩形裁剪框宽度（圆形自动取宽高最小值）
focusHeight|	矩形裁剪框高度（圆形自动取宽高最小值）
outPutX|	裁剪后需要保存的图片宽度
outPutY|	裁剪后需要保存的图片高度
isSaveRectangle|	裁剪后的图片是按矩形区域保存还是裁剪框的形状，例如圆形裁剪的时候，该参数给true，那么保存的图片是矩形区域，如果该参数给fale，保存的图片是圆形区域
imageLoader|	需要使用的图片加载器，自需要实现ImageLoader接口即可

## 3.代码参考
配置及初始化
```
package com.yangxuydong.testmybanner

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.yangxuydong.mybanner.MyBanner
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : Activity(), MyBanner.OnBannerItemClickListening {

    var listImage: MutableList<String?> = ArrayList()
    var listTitle= listOf("11111111","222222222","333333333")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var url1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596001092317&di=b652671effa319c3d4ab2b49bd853756&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201105%2F17%2F113554rnu40q7nbgnn3lgq.jpg"
        var url2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596001243718&di=9ea8315e6c3aed92f6e4409c4bac662c&imgtype=0&src=http%3A%2F%2Fb.zol-img.com.cn%2Fsoft%2F5%2F493%2FceNcIqcSrEEgE.jpg"
        var url3 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1581611728,1637172277&fm=15&gp=0.jpg"

        listImage.add(url1)
        listImage.add(url2)
        listImage.add(url3)

        mb.setDelayTime(1500)
        mb.setErrorImage(R.mipmap.a)

        mb.setTitleVisible(true)
        mb.setPointerVisible(true)

        mb.setTitleSize(20f)
        mb.setTitleColor("#669900")
        mb.setTitleBackGroundColor("#00ffffff")
        mb.setTitleMarginBottom(10)

        mb.setPointerCheckedImage(R.mipmap.p_focused)
        mb.setPointerUnCheckedImage(R.mipmap.p_not_focus)
        mb.setPointerMargin(20,20)
        mb.setPointerSize(15,15)

        mb.initData(listImage,listTitle)

        mb.setOnBannerItemClickListening(this)
    }

    override fun onItemClickListening(p: Int) {
        Toast.makeText(this,"点击了="+p, Toast.LENGTH_SHORT).show()
    }
}
```
## v1.0.0初始化提交
