package ru.abbysoft.wisebuild

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MiscUtilUnitTest {

    // TODO Robolectric obviously doesn't support API 29
//    @Test
//    fun testShowErrorDialogAndFinish_activityFinished() {
//        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
//        activityScenario.use {
//            it.onActivity {
//                MiscUtils.showErrorDialogAndFinish("test", "test", it)
//                Truth.assertThat(it.isFinishing).isTrue()
//            }
//        }
//    }
//
//    @Test
//    fun testShowErrorDialog_activityNotFinished() {
//        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
//        activityScenario.use {
//            it.onActivity {
//                MiscUtils.showErrorDialog("error", it)
//                Truth.assertThat(it.isFinishing).isTrue()
//            }
//        }
//    }
}