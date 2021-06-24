package ro.code4.deurgenta.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit.SECONDS

fun <T> LiveData<T>.obtainValue(): T? {
    val latch = CountDownLatch(1)
    var event: T? = null
    val observer = Observer<T> {
        event = it
        latch.countDown()
    }
    this.observeForever(observer)
    latch.await(2, SECONDS)
    this.removeObserver(observer)
    return event
}