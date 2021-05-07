package com.devdd.recipe.base_android.utils.helper

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devdd.recipe.utils.extensions.cancelIfActive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CountDownTimerLifecycleScoped(
    private val coroutineScope: CoroutineScope,
    private val dispatchers: AppCoroutineDispatchers
) {

    constructor(
        coroutineScope: CoroutineScope,
        dispatchers: AppCoroutineDispatchers,
        countDownTime: Long,
        interval: Long
    )
            : this(coroutineScope, dispatchers) {
        this.mMillisInFuture = countDownTime
        this.mCountdownInterval = interval
    }

    /**
     * Millis since epoch when alarm should stop.
     */
    private var mMillisInFuture: Long = 0

    /**
     * The interval in millis that the user receives callbacks
     */
    private var mCountdownInterval: Long = 0

    private var mStopTimeInFuture: Long = 0

    /**
     * boolean representing if the timer was cancelled
     */
    private var countDownTickerJob: Job? = null

    @Synchronized
    fun start() {
        countDownTickerJob.cancelIfActive()
        if (mMillisInFuture <= 0) {
            _millisUntilFinished.value = 0L
        } else {
            mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture
            countDownTickerJob = coroutineScope.launch(dispatchers.computation) {
                startCountDownTicker()
            }
        }

    }

    @Synchronized
    fun reset() {
        countDownTickerJob.cancelIfActive()
        coroutineScope.launch(dispatchers.io) {
            _millisUntilFinished.postValue(0L)
        }
    }

    /**
     * Callback fired on regular interval.
     * @see millisUntilFinished The amount of time until finished.
     */
    private val _millisUntilFinished: MutableLiveData<Long> = MutableLiveData(0L)
    val millisUntilFinished: LiveData<Long> = _millisUntilFinished

    private suspend fun startCountDownTicker() {
        val millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime()
        if (millisLeft <= 0) {
            _millisUntilFinished.postValue(0L)
        } else {
            val lastTickStart = SystemClock.elapsedRealtime()
            _millisUntilFinished.postValue(millisLeft)

            // take into account user's onTick taking time to execute
            val lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart
            var delay: Long
            if (millisLeft < mCountdownInterval) {
                // just delay until done
                delay = millisLeft - lastTickDuration

                // special case: user's onTick took more than interval to
                // complete, trigger onFinish without delay
                if (delay < 0) delay = 0
            } else {
                delay = mCountdownInterval - lastTickDuration

                // special case: user's onTick took more than interval to
                // complete, skip to next interval
                while (delay < 0) delay += mCountdownInterval
            }
            kotlinx.coroutines.delay(delay)
            startCountDownTicker()
        }
    }
}