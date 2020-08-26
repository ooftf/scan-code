package com.ooftf.component.scancode

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanBase

class DefinedActivity : AppCompatActivity() {

    private lateinit var remoteView: RemoteView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_defined)
        //1.get screen density to caculate viewfinder's rect
        //initialize RemoteView instance, and set calling back for scanning result
        remoteView = RemoteView.Builder().setContext(this).setIsCustomView(true).setBoundingBox(
            Rect(0,0,
                Int.MAX_VALUE,Int.MAX_VALUE)
        ).setFormat(HmsScan.ALL_SCAN_TYPE).build()
        remoteView.setOnResultCallback { result ->
            if (result != null && result.size > 0 && result[0] != null && !TextUtils.isEmpty(result[0].getOriginalValue())) {
                val intent = Intent()
                intent.apply {
                    putExtra(ScanUtil.RESULT, result[0]) }
                setResult(Activity.RESULT_OK, intent)
                this.finish()
            }
        }
        remoteView.onCreate(savedInstanceState)
        // Add the defined RemoteView to the page layout.
        val params = FrameLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        val frameLayout = findViewById<FrameLayout>(R.id.rim1)
        frameLayout.addView(remoteView, params)
    }

    //manage remoteView lifecycle
    override fun onStart() {
        super.onStart()
        remoteView.onStart()
    }

    override fun onResume() {
        super.onResume()
        remoteView.onResume()
    }

    override fun onPause() {
        super.onPause()
        remoteView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        remoteView.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        remoteView.onStop()
    }

    override fun onRequestPermissionsResult(var1: Int, var2: Array<String?>, var3: IntArray) {
        remoteView.onRequestPermissionsResult(var1, var2, var3, this)
        super.onRequestPermissionsResult(var1, var2, var3)
    }

}