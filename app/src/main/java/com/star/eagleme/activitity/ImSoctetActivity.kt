package com.star.eagleme.activitity

import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import com.star.eagleme.R
import com.star.eagleme.socket.protocol.BasicProtocol
import com.star.eagleme.socket.protocol.DataProtocol
import com.star.eagleme.socket.request.ClientRequestTask
import com.star.eagleme.socket.request.RequestCallBack
import com.star.eagleme.widgets.animview.PointAnimView
import com.star.eagleme.widgets.refreshview.CommonRecyclerViewAdapter
import com.star.eagleme.widgets.refreshview.CommonRecyclerViewHolder
import com.star.eagleme.widgets.refreshview.CommonRefreshLayout
import com.star.eagleme.widgets.refreshview.HeaderReFresh
import java.util.*

class ImSoctetActivity : AppCompatActivity(), View.OnClickListener {


    private var clientRequestTask: ClientRequestTask? = null
    private val mHandler = Handler()

    private var etText: TextView? = null
    private var etSend: TextView? = null

    //下拉刷新控件
    private var crl: CommonRefreshLayout? = null

    //列表控件
    private var rv: RecyclerView? = null

    //显示的数据
    private val data = ArrayList<String>()

    private var HeaderReFresh: HeaderReFresh? = null
    private var pointAnimView: PointAnimView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build())

        clientRequestTask = ClientRequestTask(object : RequestCallBack {
            override fun onSuccess(msg: BasicProtocol) {
                Log.d("msg", msg.toString())

            }

            override fun onFailed(errorCode: Int, msg: String) {

            }
        })
        setRequest()

        //reFresh()
        pointAnimView!!.radius = 20f

    }

    fun setRequest() {
        mHandler.post { Thread(clientRequestTask).start() }

    }

    fun initViews() {
        etText = findViewById(R.id.et_text) as TextView
        etSend = findViewById(R.id.tv_send) as TextView

        pointAnimView = findViewById(R.id.pv_animview) as PointAnimView

        etSend!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_send -> {
                val sendText = etText!!.text.toString()
                if (sendText != null && sendText != "") {
                    val data = DataProtocol()
                    data.data = sendText
                    data.dtype = 1
                    data.msgId = 22
                    clientRequestTask!!.addRequest(data)
                }
            }


            else -> {
            }
        }
    }

    fun reFresh() {
        crl = findViewById(R.id.crl) as CommonRefreshLayout
        rv = findViewById(R.id.rv) as RecyclerView
        HeaderReFresh = HeaderReFresh(findViewById(R.id.rl_refresh), crl, this)

        //数据造假
        for (i in 0..99) {
            data.add("测试" + i)
        }


        //设置下拉刷新监听
        crl!!.setOnRefreshListener(HeaderReFresh)

        //初始化列表控件的布局管理器
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL

        //设置布局管理器
        rv!!.layoutManager = layout

        //设置适配器
        rv!!.adapter = object : CommonRecyclerViewAdapter<String>(this, data) {

            override fun convert(h: CommonRecyclerViewHolder, entity: String, position: Int) {
                h.setText(android.R.id.text1, entity)
            }

            override fun getLayoutViewId(viewType: Int): Int {
                return android.R.layout.simple_list_item_1
            }

        }


        //设置下拉刷新监听
        crl!!.setOnRefreshListener(HeaderReFresh)
        crl!!.toRefresh(50)

    }

}
