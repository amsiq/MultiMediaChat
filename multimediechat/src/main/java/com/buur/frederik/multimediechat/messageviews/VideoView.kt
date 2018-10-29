package com.buur.frederik.multimediechat.messageviews

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import com.buur.frederik.multimediechat.R
import com.buur.frederik.multimediechat.models.MMData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_video.view.*

class VideoView: SuperView, View.OnClickListener {

    private var disp: Disposable? = null

    constructor(context: Context) : super(context) {
        View.inflate(context, R.layout.view_video, this)
    }

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun setup(isSender: Boolean, mmData: MMData, time: Int?) {
        this.isSender = isSender
        this.mmData = mmData

        val uri = Uri.parse((mmData.source))
        disp = Observable.just(vidMsgContent.setVideoURI(uri))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {})


        this.setParams(vidMsgContainer)
        vidMsgContainer.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == vidMsgContainer) {
            val intent = Intent(context, EnlargedImageView::class.java)
            this.mmData?.let { data ->
                intent.putExtra("source", data.source)
                intent.putExtra("type", data.type)
                context.startActivity(intent)
            }
        }
    }

    override fun onDetachedFromWindow() {
        if (disp?.isDisposed == false) {
            disp?.dispose()
        }
        super.onDetachedFromWindow()
    }

}