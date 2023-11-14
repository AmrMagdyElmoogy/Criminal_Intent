package com.example.criminalintent.View

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.criminalintent.Model.Crime
import com.example.criminalintent.R
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class CriminalDetailsTest {

    private var fragmentScenario = launchFragmentInContainer<CriminalDetails>()

    @After
    fun tearDown() {
        fragmentScenario.close()
    }

    @Test
    fun textHiAndCheckedToCompareAgainstCrimeObject() {
        lateinit var crime: Crime
        onView(withId(R.id.crime_title))
            .perform(typeText("Hi"))

        onView(withId(R.id.crime_solved))
            .perform(click())

        fragmentScenario.onFragment {
            crime = it.crime
        }
        assertEquals(
            crime.title, "Hi"
        )

        assertEquals(
            crime.isSolved, true
        )
    }
}