package com.ooftf.component.scancode

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 .setAction("Action", null).show()*/
            newViewBtnClick(view)
        }
        my.setOnClickListener {
            startActivityForResult(Intent(this, DefinedActivity::class.java), REQUEST_CODE_SCAN)
        }
    }

    val DEFAULT_VIEW = 456
    val REQUEST_CODE_SCAN = 413
    fun newViewBtnClick(view: View?) {
        //DEFAULT_VIEW为用户自定义用于接收权限校验结果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                DEFAULT_VIEW
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.size < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return
        }
        if (requestCode == DEFAULT_VIEW) {
            //调用DefaultView扫码界面

            ScanUtil.startScan(
                this@MainActivity,
                REQUEST_CODE_SCAN,
                HmsScanAnalyzerOptions.Creator().apply {
                    setHmsScanTypes(HmsScan.ALL_SCAN_TYPE)
                }.create()
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode !== Activity.RESULT_OK || android.R.attr.data == null) {
            return
        }
        //从onActivityResult返回data中，用 ScanUtil.RESULT作为key值取到HmsScan返回值
        //从onActivityResult返回data中，用 ScanUtil.RESULT作为key值取到HmsScan返回值
        if (requestCode == REQUEST_CODE_SCAN) {
            data?.let {
                val obj = data.getParcelableExtra<Parcelable>(ScanUtil.RESULT)
                if (obj is HmsScan) {
                    if (!TextUtils.isEmpty(obj.getOriginalValue())) {
                        Toast.makeText(this, obj.getOriginalValue(), Toast.LENGTH_SHORT).show()
                    }
                    return
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}