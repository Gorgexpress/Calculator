package com.example.michael.calculator;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.TextView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Michael on 3/4/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class GUITest {

    @Rule
    public ActivityTestRule<GUI> mActivityRule = new ActivityTestRule<>(
            GUI.class);

    @After
    public void clearText(){
        onView(withId(R.id.bclear)).perform(click());
    }
    @Test
    public void testCommas1(){
        onView(withId(R.id.b1)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("1")));
        onView(withId(R.id.b2)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("12")));
        onView(withId(R.id.b3)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("123")));
        onView(withId(R.id.b4)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("1,234")));
        onView(withId(R.id.b5)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("12,345")));
        onView(withId(R.id.b6)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("123,456")));
        onView(withId(R.id.b7)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("1,234,567")));
        onView(withId(R.id.b8)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("12,345,678")));
    }

    @Test
     public void testCommas2(){
        for(int i = 0; i < 8; i++)
            onView(withId(R.id.b1)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("11,111,111")));
        onView(withId(R.id.bdel)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("1,111,111")));
        onView(withId(R.id.bdel)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("111,111")));
        onView(withId(R.id.bdel)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("11,111")));
        onView(withId(R.id.bdel)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("1,111")));
        onView(withId(R.id.bdel)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("111")));
    }

    @Test
    public void testCommas3(){
        for(int i = 0; i < 8; i++)
            onView(withId(R.id.b1)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("11,111,111")));
        onView(withId(R.id.badd)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("11,111,111+")));
        for(int i = 0; i < 8; i++)
            onView(withId(R.id.b1)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("11,111,111+11,111,111")));
    }

    @Test
    public void testZero(){
        onView(withId(R.id.b0)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("0")));
        onView(withId(R.id.b0)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("0")));
        onView(withId(R.id.bdec)).perform(click());
        onView(withId(R.id.b0)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("0.0")));
        onView(withId(R.id.b0)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("0.00")));
        onView(withId(R.id.bclear)).perform(click());
        onView(withId(R.id.b1)).perform(click());
        onView(withId(R.id.b0)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("10")));
    }

    @Test
    public void testOps(){
        onView(withId(R.id.badd)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("")));
        onView(withId(R.id.bmul)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("")));
        onView(withId(R.id.bdiv)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("")));
        onView(withId(R.id.brp)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("")));
        onView(withId(R.id.b1)).perform(click());
        onView(withId(R.id.bsub)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("1-")));
        onView(withId(R.id.badd)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("1+")));
        onView(withId(R.id.bmul)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("1*")));
        onView(withId(R.id.bdiv)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("1/")));
        onView(withId(R.id.brp)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("1/")));
    }

    @Test
    public void testParenthesis(){
        onView(withId(R.id.brp)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("")));
        onView(withId(R.id.blp)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("(")));
        onView(withId(R.id.b1)).perform(click());
        onView(withId(R.id.brp)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("(1)")));
        onView(withId(R.id.brp)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("(1)")));
        onView(withId(R.id.blp)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("(1)")));
        onView(withId(R.id.badd)).perform(click());
        for(int i = 0; i < 3; i++)
            onView(withId(R.id.blp)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("(1)+(((")));
        onView(withId(R.id.b1)).perform(click());
        onView(withId(R.id.brp)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("(1)+(((1)")));
        for (int i = 0; i < 5; i++){
            onView(withId(R.id.bmul)).perform(click());
            onView(withId(R.id.b1)).perform(click());
            onView(withId(R.id.brp)).perform(click());
        }
        onView(withId(R.id.textView)).check(matches(withText("(1)+(((1)*1)*1)*1*1*1")));
    }
}
