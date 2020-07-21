package work.wtks.android.iidxresultmanager.ui.common

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


class SquareImageView : AppCompatImageView {
    constructor(context: Context) : super(context, null) {
        scaleType = ScaleType.CENTER_CROP
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        scaleType = ScaleType.CENTER_CROP
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        scaleType = ScaleType.CENTER_CROP
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }
}