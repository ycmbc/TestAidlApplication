package com.mmt.app2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.mmt.aidl.Book
import com.mmt.aidl.IMyAidlInterface

class MainActivity : AppCompatActivity() {

    private var mBookManager: IMyAidlInterface? = null
    private var mBound = false
    private var mBooks = mutableListOf<Book>()

    private var index = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_add).setOnClickListener {
            if (!mBound) {
                attemptToBindService()
                Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            if (mBookManager == null)
                return@setOnClickListener

            val book = Book("APP研发录$index", "30元")
            mBookManager!!.addBook(book)
            index++
        }

        findViewById<Button>(R.id.btn_get).setOnClickListener {
            if (!mBound) {
                attemptToBindService()
                Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            if (mBookManager == null)
                return@setOnClickListener
            mBooks = mBookManager!!.books
            mBooks.forEach {
                Log.e("tag", "getBooks: $it")
            }
        }
    }

    private fun attemptToBindService() {
        val intent = Intent()
        //添加服务端service action
        intent.action = "com.mmt.aidl.service"
        intent.setPackage("com.mmt.aidl")
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.e("tag", "service connected")
            mBookManager = IMyAidlInterface.Stub.asInterface(service)
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.e("tag", "service disconnected")
            mBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        if (!mBound) {
            attemptToBindService()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            unbindService(mServiceConnection)
            mBound = false
        }
    }
}