package ro.code4.deurgenta.ui.auth.login

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.textfield.TextInputLayout
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.CompletableSubject
import java.io.IOException
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.junit.After
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.requests.ResetPasswordRequest
import ro.code4.deurgenta.services.AccountService
import ro.code4.deurgenta.ui.auth.reset.ResetPasswordFragment

@RunWith(AndroidJUnit4::class)
class ResetPasswordFragmentTest : KoinTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var overriddenModule: Module
    private lateinit var scenario: FragmentScenario<ResetPasswordFragment>
    private lateinit var invalidEmailText: String
    private lateinit var passwordMismatchText: String
    private lateinit var passwordTooShortText: String
    private lateinit var dialogLoadingText: String
    private lateinit var dialogSuccessText: String
    private lateinit var dialogFailureText: String
    private lateinit var mockAccountService: AccountService

    @Before
    fun setUp() {
        mockAccountService = mockk()
        overriddenModule = module(override = true) {
            single { mockAccountService }
        }
        loadKoinModules(overriddenModule)
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
        scenario.onFragment { fragment ->
            invalidEmailText = fragment.getString(R.string.reset_password_invalid_email)
            passwordMismatchText = fragment.getString(R.string.reset_password_different_passwords)
            passwordTooShortText = fragment.getString(R.string.reset_password_password_too_short)
            dialogLoadingText = fragment.getString(R.string.reset_password_loading)
            dialogSuccessText = fragment.getString(R.string.reset_password_success)
            dialogFailureText = fragment.getString(R.string.reset_password_failure)
        }
    }

    @After
    fun tearDown() {
        unloadKoinModules(overriddenModule)
    }

    @Test
    fun typingEmailShowsExpectedStates() {
        onView(withId(R.id.email_input_layout)).check(matches(hasError(null)))
        validEmail.forEachIndexed { index, ch ->
            onView(withId(R.id.email_input)).perform(typeText(ch.toString()))
            if (index in listOf(validEmail.length - 1, validEmail.length - 2, validEmail.length - 3)) {
                onView(withId(R.id.email_input_layout)).check(matches(hasError(null)))
            } else {
                onView(withId(R.id.email_input_layout)).check(matches(hasError(invalidEmailText)))
            }
        }
        onView(withId(R.id.btn_reset_password)).perform(click())
        onView(withId(R.id.email_input_layout)).check(matches(hasError(null)))
    }

    @Test
    fun typingPasswordShowsExpectedStates() {
        onView(withId(R.id.new_password_input_layout)).check(matches(hasError(null)))
        validPassword.forEachIndexed { index, ch ->
            onView(withId(R.id.new_password_input)).perform(typeText(ch.toString()))
            if (index == validPassword.length - 1) {
                onView(withId(R.id.new_password_input_layout)).check(matches(hasError(null)))
            } else {
                onView(withId(R.id.new_password_input_layout)).check(matches(hasError(passwordTooShortText)))
            }
        }
        onView(withId(R.id.confirm_password_input)).perform(typeText(validPassword))
        onView(withId(R.id.btn_reset_password)).perform(click())
        onView(withId(R.id.new_password_input_layout)).check(matches(hasError(null)))
    }

    @Test
    fun passwordsDoNotMatchShowsErrorAndPreventsReset() {
        onView(withId(R.id.email_input)).perform(typeText(validEmail))
        onView(withId(R.id.new_password_input)).perform(typeText(validPassword))
        onView(withId(R.id.confirm_password_input)).perform(typeText("${validPassword}A"))
        closeSoftKeyboard()
        onView(withId(R.id.btn_reset_password)).perform(click())
        onView(withId(R.id.confirm_password_input_layout)).check(matches(hasError(passwordMismatchText)))
        onView(withId(R.id.container)).isNotPresentInLayout()
    }

    @Test
    @Ignore("Flaky test! Some issues related to multithreading.")
    fun showsLoadingWhileMakingBackendCall() {
        println("PPPPPP $mockAccountService")
        every { mockAccountService.resetPassword(ResetPasswordRequest(validEmail, validPassword)) } returns
                Completable.never()
        onView(withId(R.id.email_input)).perform(typeText(validEmail))
        onView(withId(R.id.new_password_input)).perform(typeText(validPassword))
        onView(withId(R.id.confirm_password_input)).perform(typeText(validPassword))
        closeSoftKeyboard()
        onView(withId(R.id.btn_reset_password)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.infoView)).check(matches(withText(dialogLoadingText)))
        verify {
            mockAccountService.resetPassword(ResetPasswordRequest(validEmail, validPassword))
        }
    }

    @Test
    @Ignore("Flaky test! Some issues related to multithreading.")
    fun showsSuccessWhenBackendCallSucceeds() {
        println("DDDD $mockAccountService")
        val completableSubject = CompletableSubject.create()
        every { mockAccountService.resetPassword(ResetPasswordRequest(validEmail, validPassword)) } returns
                completableSubject
        onView(withId(R.id.email_input)).perform(typeText(validEmail))
        onView(withId(R.id.new_password_input)).perform(typeText(validPassword))
        onView(withId(R.id.confirm_password_input)).perform(typeText(validPassword))
        closeSoftKeyboard()
        onView(withId(R.id.btn_reset_password)).perform(click())
        onView(withId(R.id.infoView)).check(matches(withText(dialogLoadingText)))
        completableSubject.onComplete()
        completableSubject.subscribe({
            onView(withId(R.id.infoView)).check(matches(withText(dialogSuccessText)))
            verify {
                mockAccountService.resetPassword(ResetPasswordRequest(validEmail, validPassword))
            }
        }, {
            fail("This request should not fail!")
        })
    }

    @Test
    @Ignore("Flaky test! Some issues related to multithreading.")
    fun showsErrorWhenBackendCallFails() {
        val completableSubject = CompletableSubject.create()
        every { mockAccountService.resetPassword(ResetPasswordRequest(validEmail, validPassword)) } returns
                completableSubject
        onView(withId(R.id.email_input)).perform(typeText(validEmail))
        onView(withId(R.id.new_password_input)).perform(typeText(validPassword))
        onView(withId(R.id.confirm_password_input)).perform(typeText(validPassword))
        closeSoftKeyboard()
        onView(withId(R.id.btn_reset_password)).perform(click())
        onView(withId(R.id.infoView)).check(matches(withText(dialogLoadingText)))
        completableSubject.onError(IOException())
        Thread.sleep(3000)
        onView(withId(R.id.infoView)).check(matches(withText(dialogFailureText)))
        verify {
            mockAccountService.resetPassword(ResetPasswordRequest(validEmail, validPassword))
        }
    }

    @Test
    @Ignore("Flaky test! Some issues related to multithreading.")
    fun showsInputAgainOnUserChoiceWhenBackendFails() {
        val completableSubject = CompletableSubject.create()
        every { mockAccountService.resetPassword(ResetPasswordRequest(validEmail, validPassword)) } returns
                completableSubject
        onView(withId(R.id.email_input)).perform(typeText(validEmail))
        onView(withId(R.id.new_password_input)).perform(typeText(validPassword))
        onView(withId(R.id.confirm_password_input)).perform(typeText(validPassword))
        closeSoftKeyboard()
        onView(withId(R.id.btn_reset_password)).perform(click())
        onView(withId(R.id.infoView)).check(matches(withText(dialogLoadingText)))
        completableSubject.onError(IOException())
        Thread.sleep(3000)
        onView(withId(R.id.infoView)).check(matches(withText(dialogFailureText)))
        onView(withId(R.id.btn_reset_action)).perform(click())
        onView(withId(R.id.container)).isNotPresentInLayout()
        verify {
            mockAccountService.resetPassword(ResetPasswordRequest(validEmail, validPassword))
        }
    }

    /**
     * Matcher to match the error text on a [TextInputLayout]. The text can be null, in which case the matcher tests
     * for no error being shown.
     *
     * @param errorMessage the error text to match, test if error is not shown when errorMessage is null
     */
    private fun hasError(errorMessage: String?): Matcher<View> {
        return object : TypeSafeDiagnosingMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendValue(errorMessage)
            }

            override fun matchesSafely(item: View, mismatchDescription: Description): Boolean {
                mismatchDescription.appendValue((item as TextInputLayout).error)
                return item.error == errorMessage
            }
        }
    }

    /**
     * A [ViewInteraction] which checks for a [NoMatchingViewException] being thrown as an indication that the target
     * [View] isn't present in the view hierarchy.
     */
    private fun ViewInteraction.isNotPresentInLayout() = try {
        check(matches(not(isDisplayed())))
        true
    } catch (e: NoMatchingViewException) {
        false
    }

    companion object {
        private const val validEmail = "valid@email.com"
        private const val validPassword = "abcdeA1!"
    }
}
