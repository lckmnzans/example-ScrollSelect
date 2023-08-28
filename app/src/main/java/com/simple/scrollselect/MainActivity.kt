package com.simple.scrollselect

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val TAG: String = MainActivity::class.java.simpleName

    private var firstItemHeightDate: Float = 0f
    private var paddingDate: Float = 0f
    private var itemHeightDate: Float = 0f
    private var allPixelsDate: Int = 0
    private var finalHeightDate: Int = 0
    private lateinit var dateAdapter: DateAdapter
    private var labelerDates: ArrayList<LabelerDate> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getRecyclerViewDate()
    }

    private fun getRecyclerViewDate() {
        val recyclerViewDate = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerViewDate?.postDelayed({
            setDateValue()
        }, 300)

        val vtoDate = recyclerViewDate?.viewTreeObserver
        vtoDate?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                recyclerViewDate.viewTreeObserver.removeOnPreDrawListener(this)
                finalHeightDate = recyclerViewDate.measuredHeight
                itemHeightDate = 100f
                paddingDate = (finalHeightDate - itemHeightDate) / 2f
                firstItemHeightDate = paddingDate
                allPixelsDate = 0

                val dateLayoutManager = LinearLayoutManager(applicationContext)
                dateLayoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerViewDate.layoutManager = dateLayoutManager
                recyclerViewDate.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        synchronized(this) {
                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                calculatePositionAndScrollDate(recyclerView)
                            }
                        }
                    }

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        allPixelsDate += dy
                    }
                })

                if (labelerDates == null) {
                    labelerDates = ArrayList()
                }
                genLabelerDate()
                dateAdapter = DateAdapter(labelerDates, firstItemHeightDate.toInt())
                recyclerViewDate.adapter = dateAdapter
                dateAdapter.setSelectedItem(dateAdapter.itemCount - 1)

                return true
            }
        })
    }

    private fun genLabelerDate() {
        for (i in 0 until 32) {
            val type = if (i == 0 || i == 31) {
                DateAdapter.VIEW_TYPE_PADDING
            } else {
                DateAdapter.VIEW_TYPE_ITEM
            }
            val labelerDate = LabelerDate(type, i.toString())
            labelerDates.add(labelerDate)
        }
    }

    private fun calculatePositionAndScrollDate(recyclerView: RecyclerView) {
        var expectedPositionDate =
            ((allPixelsDate + paddingDate - firstItemHeightDate) / itemHeightDate).roundToInt()

        if (expectedPositionDate == -1) {
            expectedPositionDate = 0
        } else if (expectedPositionDate >= (recyclerView.adapter?.itemCount?.minus(2) ?: 0)) {
            expectedPositionDate--
        }
        scrollListToPositionDate(recyclerView, expectedPositionDate)
    }

    private fun scrollListToPositionDate(recyclerView: RecyclerView, expectedPositionDate: Int) {
        val targetScrollPosDate =
            expectedPositionDate * itemHeightDate + firstItemHeightDate - paddingDate
        val missingPxDate = targetScrollPosDate - allPixelsDate
        if (missingPxDate != 0f) {
            recyclerView.smoothScrollBy(0, missingPxDate.toInt())
        }
        setDateValue()
    }

    private fun setDateValue() {
        val expectedPositionDateColor =
            ((allPixelsDate + paddingDate - firstItemHeightDate) / itemHeightDate).roundToInt()
        val setColorDate = expectedPositionDateColor + 1
        //set color here
        dateAdapter.setSelectedItem(setColorDate)
    }
}
