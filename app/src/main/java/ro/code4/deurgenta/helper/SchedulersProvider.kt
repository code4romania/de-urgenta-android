package ro.code4.deurgenta.helper

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulersProvider {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun main(): Scheduler
}

class SchedulersProviderImpl : SchedulersProvider {

    override fun computation(): Scheduler = Schedulers.computation()

    override fun io(): Scheduler = Schedulers.io()

    override fun main(): Scheduler = AndroidSchedulers.mainThread()
}