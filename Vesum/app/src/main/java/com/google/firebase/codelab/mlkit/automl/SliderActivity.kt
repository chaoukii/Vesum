package com.google.firebase.codelab.mlkit.automl

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntro2Fragment
import com.github.paolorotolo.appintro.model.SliderPagerBuilder


class SliderActivity : AppIntro() {

  @RequiresApi(Build.VERSION_CODES.M)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    showIntroSlides()
  }

  @RequiresApi(Build.VERSION_CODES.M)
  private fun showIntroSlides() {
    val pageOne = SliderPagerBuilder()
      .title(getString(R.string.slide_one_top_text))
      .description(getString(R.string.slide_one_down_text))
      .imageDrawable(R.drawable.work1)
      .bgColor(getColor(R.color.slide_one))
      .build()

    val pageTwo = SliderPagerBuilder()
      .title(getString(R.string.slide_two_top_text))
      .description(getString(R.string.slide_two_down_text))
      .imageDrawable(R.drawable.work2)
      .bgColor(getColor(R.color.slide_two))
      .build()

    val pageThree = SliderPagerBuilder()
      .title(getString(R.string.slide_three_top_text))
      .description(getString(R.string.slide_three_down_text))
      .imageDrawable(R.drawable.work3)
      .bgColor(getColor(R.color.slide_three))
      .build()

    val pageFour = SliderPagerBuilder()
      .title(getString(R.string.slide_four_top_text))
      .description(getString(R.string.slide_four_down_text))
      .imageDrawable(R.drawable.work4)
      .bgColor(getColor(R.color.slide_four))
      .build()

    addSlide(AppIntro2Fragment.newInstance(pageOne))
    addSlide(AppIntro2Fragment.newInstance(pageTwo))
    addSlide(AppIntro2Fragment.newInstance(pageThree))
    addSlide(AppIntro2Fragment.newInstance(pageFour))

    showStatusBar(false)
    setFadeAnimation()
  }

  private fun goToMain() {
    startActivity(Intent(this, StillImageActivity::class.java))
  }

  override fun onSkipPressed(currentFragment: Fragment?) {
    super.onSkipPressed(currentFragment)
    goToMain()
  }

  override fun onDonePressed(currentFragment: Fragment?) {
    super.onDonePressed(currentFragment)
    goToMain()
  }

  override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
    super.onSlideChanged(oldFragment, newFragment)
    Log.d("Hello", "Changed")
  }
}