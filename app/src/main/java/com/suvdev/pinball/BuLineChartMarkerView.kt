package com.suvdev.pinball

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.single_marker_chart.view.*

class BuLineChartMarkerView(context: Context, layoutResource: Int, val myarrayList: List<String>) : MarkerView(context, layoutResource) {


    override fun refreshContent(e: Entry?, highlight: Highlight?) {

        e?.let {
            tvDate.text = myarrayList.get(it.x.toInt())
            tvMarkerName.text = "" + it.y
        }
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-(height / 2)).toFloat())
    }

}