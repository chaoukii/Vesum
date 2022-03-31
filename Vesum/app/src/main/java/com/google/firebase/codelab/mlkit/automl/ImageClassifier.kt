package com.google.firebase.codelab.mlkit.automl

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.ml.common.FirebaseMLException
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions
import java.io.IOException
import java.util.Locale
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel




class ImageClassifier
/** Initializes an `ImageClassifier`.  */
@Throws(FirebaseMLException::class)
internal constructor(context: Context) {

  /** MLKit AutoML Image Classifier  */
  private val labeler: FirebaseVisionImageLabeler?

  init {

    val localModel = FirebaseAutoMLLocalModel.Builder()
      .setAssetFilePath(LOCAL_MODEL_PATH)
      .build()


    val options = FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
      .setConfidenceThreshold(0.65f)  // Evaluate your model in the Firebase console
      // to determine an appropriate value.
      .build()

    labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options)
  }

  /** Classifies a frame from the preview stream.  */
  internal fun classifyFrame(bitmap: Bitmap): Task<String> {
    if (labeler == null) {
      Log.e(TAG, "Image classifier has not been initialized; Skipped.")
      val e = IllegalStateException("Uninitialized Classifier.")

      val completionSource = TaskCompletionSource<String>()
      completionSource.setException(e)
      return completionSource.task
    }

    val startTime = SystemClock.uptimeMillis()
    val image = FirebaseVisionImage.fromBitmap(bitmap)

    return labeler.processImage(image).continueWith {
        task ->
      val endTime = SystemClock.uptimeMillis()
      Log.d(TAG, "Time to run model inference: " + (endTime - startTime).toString())

      val labelProbList = task.result

      var textToShow = " " +
               "" +
              " model\n"
      textToShow += "Latency: " + (endTime - startTime).toString() + "ms\n"
      textToShow += if (labelProbList.isNullOrEmpty())
        "No Result"
      else
        printTopKLabels(labelProbList)

      // print the results
      textToShow
    }
  }

  /** Closes labeler to release resources.  */
  internal fun close() {
    try {
      labeler?.close()
    } catch (e: IOException) {
      Log.e(TAG, "Unable to close the labeler instance", e)
    }

  }

  /** Prints top-K labels, to be shown in UI as the results.  */
  private val printTopKLabels: (List<FirebaseVisionImageLabel>) -> String = {
    it.joinToString(
            separator = "\n",
            limit = RESULTS_TO_SHOW
    ) { label ->
      String.format(Locale.getDefault(), "Label: %s, Confidence: %4.2f", label.text, label.confidence)
    }
  }

  companion object {

    /** Tag for the [Log].  */
    private const val TAG = "MLKitAutoML"

    /** Path of local model file stored in Assets.  */
    private const val LOCAL_MODEL_PATH = "automl/manifest.json"

    /** Number of results to show in the UI.  */
    private const val RESULTS_TO_SHOW = 3


  }
}
