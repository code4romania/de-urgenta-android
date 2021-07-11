package ro.code4.deurgenta.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ro.code4.deurgenta.helper.Status
import ro.code4.deurgenta.repositories.AccountRepository
import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.EmailNotValid
import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.PasswordTooShort
import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.Valid
import ro.code4.deurgenta.ui.auth.reset.ResetPasswordViewModel
import ro.code4.deurgenta.utils.TestSchedulersProvider
import ro.code4.deurgenta.utils.obtainValue

class ResetPasswordViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: ResetPasswordViewModel
    private lateinit var fakeAccountRepository: FakeAccountRepository
    private val testSchedulersProvider = TestSchedulersProvider()

    @Before
    fun setUp() {
        fakeAccountRepository = FakeAccountRepository()
        viewModel = ResetPasswordViewModel(fakeAccountRepository, testSchedulersProvider)
    }

    @Test
    fun validEmail_showsProperValidationStatus() {
        viewModel.validateEmail(validEmail)
        assertEquals(Valid, viewModel.emailValidationStatus.obtainValue())
    }

    @Test
    fun validEmail_showsProperValidationStatusOnTyping() {
        var email = ""
        validEmail.forEachIndexed { index, ch ->
            email += ch
            viewModel.validateEmail(email)
            if (index in listOf(validEmail.length - 1, validEmail.length - 2, validEmail.length - 3)) {
                // the test email is valid only when the user gets to the last 3 chars
                assertEquals(Valid, viewModel.emailValidationStatus.obtainValue())
            } else {
                assertEquals(EmailNotValid, viewModel.emailValidationStatus.obtainValue())
            }
        }
    }

    @Test
    fun emptyEmailIdentifier_showsProperValidationStatus() {
        viewModel.validateEmail("@email.com")
        assertEquals(EmailNotValid, viewModel.emailValidationStatus.obtainValue())
    }

    @Test
    fun emptyEmailProvider_showsProperValidationStatus() {
        viewModel.validateEmail("valid@")
        assertEquals(EmailNotValid, viewModel.emailValidationStatus.obtainValue())
    }

    @Test
    fun emailProviderInvalid_showsProperValidationStatus() {
        viewModel.resetPassword("valid@com", "")
        assertEquals(EmailNotValid, viewModel.emailValidationStatus.obtainValue())
    }

    @Test
    fun validPassword_showsProperValidationStatus() {
        viewModel.validatePassword(validPassword)
        assertEquals(Valid, viewModel.passwordValidationStatus.obtainValue())
    }

    @Test
    fun passwordTooShort_showsProperValidationStatus() {
        var password = ""
        password.forEachIndexed { index, ch ->
            password += ch
            viewModel.validatePassword(password)
            if (index == validPassword.length - 1) {
                // we have a valid password of 8, the minimum length required
                assertEquals(Valid, viewModel.passwordValidationStatus.obtainValue())
            } else {
                assertEquals(PasswordTooShort, viewModel.passwordValidationStatus.obtainValue())
            }
        }
    }

    @Test
    fun validEmailAndPassword_triggersRequest() {
        viewModel.resetPassword(validEmail, validPassword)
        assertEquals(true, viewModel.resetInitiated.obtainValue())
    }

    @Test
    fun validEmailAndPassword_showsLoadingOnResetRequest() {
        viewModel.resetPassword(validEmail, validPassword)
        assertEquals(Status.Loading, viewModel.resetStatus.obtainValue()?.status)
    }

    @Test
    fun validEmailAndPassword_showsExpectedEventsOnRequestSuccess() {
        viewModel.resetPassword(validEmail, validPassword)
        assertEquals(Status.Loading, viewModel.resetStatus.obtainValue()?.status)
        fakeAccountRepository.completable.onComplete()
        assertEquals(Status.Success, viewModel.resetStatus.obtainValue()?.status)
    }

    @Test
    fun validEmailAndPassword_showsExpectedEventsOnRequestFailure() {
        viewModel.resetPassword(validEmail, validPassword)
        assertEquals(Status.Loading, viewModel.resetStatus.obtainValue()?.status)
        fakeAccountRepository.completable.onError(IOException())
        assertEquals(Status.Error, viewModel.resetStatus.obtainValue()?.status)
    }

    companion object {
        const val validEmail = "valid@email.com"
        const val validPassword = "abcdeA1!"
    }
}

/**
 * A fake [AccountRepository] implementation which can be finished with complete/error events in tests.
 */
internal class FakeAccountRepository : AccountRepository {

    val completable = CompletableSubject.create()

    override fun resetPassword(accountEmail: String, newPassword: String): Completable {
        return completable
    }
}
