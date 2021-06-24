package ro.code4.deurgenta.repositories

import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import java.io.IOException
import org.junit.Before
import org.junit.Test
import ro.code4.deurgenta.data.model.requests.ResetPasswordRequest
import ro.code4.deurgenta.services.AccountService
import ro.code4.deurgenta.ui.auth.login.ResetPasswordViewModelTest.Companion.validEmail
import ro.code4.deurgenta.ui.auth.login.ResetPasswordViewModelTest.Companion.validPassword

class AccountRepositoryTest {
    private lateinit var mockAccountService: AccountService
    private lateinit var accountRepository: AccountRepository

    @Before
    fun setUp() {
        mockAccountService = mockk()
        accountRepository = AccountRepositoryImpl(mockAccountService)
    }

    @Test
    fun backendSucceeds_showsSuccess() {
        val testValidCredentials = ResetPasswordRequest(validEmail, validPassword)
        every { mockAccountService.resetPassword(testValidCredentials) } returns Completable.complete()
        val testObserver = TestObserver<Unit>()
        accountRepository.resetPassword(validEmail, validPassword).subscribe(testObserver)
        testObserver.assertNoErrors().assertComplete()
    }

    @Test
    fun backendFails_showsFailure() {
        val testValidCredentials = ResetPasswordRequest(validEmail, validPassword)
        every { mockAccountService.resetPassword(testValidCredentials) } returns Completable.error(IOException())
        val testObserver = TestObserver<Unit>()
        accountRepository.resetPassword(validEmail, validPassword).subscribe(testObserver)
        testObserver.assertError(IOException::class.java)
    }
}