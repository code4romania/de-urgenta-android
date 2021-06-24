package ro.code4.deurgenta.utils

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import ro.code4.deurgenta.helper.SchedulersProvider

class TestSchedulersProvider : SchedulersProvider {
    override fun computation(): Scheduler = Schedulers.trampoline()

    override fun io(): Scheduler = Schedulers.trampoline()

    override fun main(): Scheduler = Schedulers.trampoline()
}