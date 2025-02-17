package io.github.z3r0c00l_2k.aquadroid

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.github.z3r0c00l_2k.aquadroid.helpers.SqliteHelper
import io.github.z3r0c00l_2k.aquadroid.utils.AppUtils
import io.github.z3r0c00l_2k.aquadroid.utils.ChartXValueFormatter
import kotlinx.android.synthetic.main.activity_stats.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max


class StatsActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var sqliteHelper: SqliteHelper
    private var totalPercentage: Float = 0f
    private var totalGlasses: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        sharedPref = getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        sqliteHelper = SqliteHelper(this)
        val wakeUpTime = sharedPref.getLong(AppUtils.WAKEUP_TIME, 0)
        val sleepingTime = sharedPref.getLong(AppUtils.SLEEPING_TIME_KEY, 0)

        btnBack.setOnClickListener {
            finish()
        }

        var entries = ArrayList<Entry>()
        val dateArray = ArrayList<String>()

//        val cursor: Cursor = sqliteHelper.getAllStats()

        var start = Calendar.getInstance()
        var end = Calendar.getInstance()
        start.roll(Calendar.DAY_OF_YEAR, -7)
        val values = sqliteHelper.getStatsInRange(start.time, end.time, sleepingTime)
        for (day in start.time.time..end.time.time step 86400000){
            val c = Calendar.getInstance()
            c.timeInMillis = day
            dateArray.add(SimpleDateFormat("dd-MM").format(c.time))
        }
        if (values.size > 0){
            entries = values
        } else {
            Toast.makeText(this, "Empty", Toast.LENGTH_LONG).show()
        }

        if (!entries.isEmpty()) {

            chart.description.isEnabled = false
            chart.animateY(1000, Easing.Linear)
            chart.viewPortHandler.setMaximumScaleX(1.5f)
            chart.xAxis.setDrawGridLines(false)
            chart.xAxis.position = XAxis.XAxisPosition.TOP
            chart.xAxis.isGranularityEnabled = true
            chart.legend.isEnabled = false
            chart.fitScreen()
            chart.isAutoScaleMinMaxEnabled = true
            chart.scaleX = 1f
            chart.setPinchZoom(true)
            chart.isScaleXEnabled = true
            chart.isScaleYEnabled = false
            chart.axisLeft.textColor = Color.BLACK
            chart.xAxis.textColor = Color.BLACK
            chart.axisLeft.setDrawAxisLine(false)
            chart.xAxis.setDrawAxisLine(false)
            chart.setDrawMarkers(false)
            chart.xAxis.labelCount = 5

            val leftAxis = chart.axisLeft
            leftAxis.axisMinimum = 0f // always start at zero
            val maxObject: Entry = entries.maxBy { it.y }!! // entries is not empty here
            leftAxis.axisMaximum = max(a = maxObject.y, b = 100f) + 15f // 15% margin on top
            val targetLine = LimitLine(100f, "")
            targetLine.enableDashedLine(5f, 5f, 0f)
            leftAxis.addLimitLine(targetLine)

            val rightAxis = chart.axisRight
            rightAxis.setDrawGridLines(false)
            rightAxis.setDrawZeroLine(false)
            rightAxis.setDrawAxisLine(false)
            rightAxis.setDrawLabels(false)

            val dataSet = LineDataSet(entries, "Label")
            dataSet.setDrawCircles(false)
            dataSet.lineWidth = 2.5f
            dataSet.color = ContextCompat.getColor(this, R.color.colorSecondaryDark)
            dataSet.setDrawFilled(true)
            dataSet.fillDrawable = getDrawable(R.drawable.graph_fill_gradiant)
            dataSet.setDrawValues(false)
            dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

            val lineData = LineData(dataSet)
            chart.xAxis.valueFormatter = (ChartXValueFormatter(dateArray))
            chart.data = lineData
            chart.invalidate()


            val remaining = sharedPref.getInt(
                AppUtils.TOTAL_INTAKE,
                0
            ) - sqliteHelper.getIntook(AppUtils.getCurrentDate()!!, sleepingTime)

            if (remaining > 0) {
                remainingIntake.text = "$remaining ml"
            } else {
                remainingIntake.text = "0 ml"
            }

            targetIntake.text = "${sharedPref.getInt(
                AppUtils.TOTAL_INTAKE,
                0
            )
            } ml"

            val percentage = sqliteHelper.getIntook(AppUtils.getCurrentDate()!!, sleepingTime) * 100 / sharedPref.getInt(
                AppUtils.TOTAL_INTAKE,
                0
            )
            waterLevelView.centerTitle = "$percentage%"
            waterLevelView.progressValue = percentage

        }
    }
}
