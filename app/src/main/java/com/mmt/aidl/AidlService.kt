package com.mmt.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * Copyright © 2020 妈妈团. All rights reserved.
 * @author yangchong
 * @version 1.0
 * @date 12/1/20 2:45 PM
 * @description
 */
class AidlService : Service() {

    private val tag = this.javaClass.simpleName

    private var mBooks = mutableListOf<Book>()

    override fun onCreate() {
        super.onCreate()
        val book = Book("Android开发艺术探索", "25元")
        mBooks.add(book)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBookManager;
    }

    //由AIDL文件生成的BookManager
    private val mBookManager: IMyAidlInterface.Stub = object : IMyAidlInterface.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
            Log.e(
                tag, "basicTypes: anInt = " + anInt +
                        "aLong" + aLong +
                        "aBoolean" + aBoolean +
                        "aFloat" + aFloat +
                        "aDouble" + aDouble +
                        "aString" + aString
            )
        }

        override fun addBook(book: Book?) {
            synchronized(this) {
                //尝试修改book的参数，主要是为了观察其到客户端的反馈
                //打印mBooks列表，观察客户端传过来的值
                Log.e(tag, "addBook1: " + mBooks.size)
                book?.let {
                    book.price = "12元"
//                    if (!mBooks.contains(book)) {
                    mBooks.add(book)
//                    }
                }
                Log.e(tag, "addBook2: " + mBooks.size)
                for (mBook in mBooks) {
                    Log.e(tag, "addBook: mBook$mBook")
                }
            }
        }

        override fun getBooks(): MutableList<Book> {
            synchronized(this) {
                return mBooks
            }
        }

    }

}