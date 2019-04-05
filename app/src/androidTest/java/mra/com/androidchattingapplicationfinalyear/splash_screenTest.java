package mra.com.androidchattingapplicationfinalyear;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class splash_screenTest {

    @Rule
    public ActivityTestRule<splash_screen> mActivityTestRule = new ActivityTestRule<>(splash_screen.class);

    @Test
    public void splash_screenTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3598785);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialEditText = onView(
                allOf(withId(R.id.uname),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.Layout_OTP1),
                                                4)),
                                0),
                        isDisplayed()));
        materialEditText.perform(click());

        ViewInteraction materialEditText2 = onView(
                allOf(withId(R.id.uname),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.Layout_OTP1),
                                                4)),
                                0),
                        isDisplayed()));
        materialEditText2.perform(click());

        ViewInteraction materialEditText3 = onView(
                allOf(withId(R.id.uname),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.Layout_OTP1),
                                                4)),
                                0),
                        isDisplayed()));
        materialEditText3.perform(replaceText("a@yahoo.com"), closeSoftKeyboard());

        ViewInteraction materialEditText4 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.Layout_OTP1),
                                                4)),
                                1),
                        isDisplayed()));
        materialEditText4.perform(replaceText("123456789"), closeSoftKeyboard());

        ViewInteraction materialEditText5 = onView(
                allOf(withId(R.id.password), withText("123456789"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.Layout_OTP1),
                                                4)),
                                1),
                        isDisplayed()));
        materialEditText5.perform(pressImeActionButton());

        ViewInteraction button = onView(
                allOf(withId(R.id.LoginUser),
                        childAtPosition(
                                allOf(withId(R.id.Layout_OTP1),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                0)),
                                5),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.LoginUser), withText("Log in"),
                        childAtPosition(
                                allOf(withId(R.id.Layout_OTP1),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatButton.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
