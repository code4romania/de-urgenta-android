package ro.code4.deurgenta.helper

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

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