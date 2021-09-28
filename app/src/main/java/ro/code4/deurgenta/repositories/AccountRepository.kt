package ro.code4.deurgenta.repositories

import io.reactivex.rxjava3.core.Completable
import ro.code4.deurgenta.data.model.requests.ResetPasswordRequest
import ro.code4.deurgenta.services.AccountService

interface AccountRepository {
    fun resetPassword(accountEmail: String, newPassword: String): Completable
}

class AccountRepositoryImpl(
    private val accountService: AccountService
) : AccountRepository {

    override fun resetPassword(accountEmail: String, newPassword: String): Completable =
        accountService.resetPassword(ResetPasswordRequest(accountEmail, newPassword)).doOnComplete {
            println("Complete!")
        }.doOnError {
            println("Error: $it")
        }
}