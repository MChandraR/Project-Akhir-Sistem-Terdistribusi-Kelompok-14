package com.mcr.mnews.util

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException





class ProgressRequestBody (file: File, content_type: String?, listener: UploadCallbacks): RequestBody() {
    private var mFile: File? = null
    val mPath: String? = null
    var mListener: UploadCallbacks? = null
    private var content_type: String? = null
    private val DEFAULT_BUFFER_SIZE = 2048

    init {
        this.content_type = content_type
        mFile = file
        mListener = listener
    }

    override fun contentType(): MediaType? {
        return MediaType.parse(content_type)
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile!!.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val fileLength = mFile!!.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val `in` = FileInputStream(mFile)
        var uploaded: Long = 0

        try {
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (`in`.read(buffer).also { read = it } != -1) {
                // update progress on UI thread
                handler.post(ProgressUpdater(uploaded, fileLength, mListener))
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }
        } finally {
            `in`.close()
        }
    }


    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int)
        fun onError()
        fun onFinish()
    }


     class ProgressUpdater(uploaded: Long, total: Long,var mListener: UploadCallbacks?) : Runnable {
        private var mUploaded: Long = 0
        private var mTotal: Long = 0
        init {
            mUploaded = uploaded
            mTotal = total
        }

        override fun run() {
            mListener!!.onProgressUpdate((100 * mUploaded / mTotal).toInt());
        }

    }
}

