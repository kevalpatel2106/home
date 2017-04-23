package com.kevalpatel2106.homedemo.things.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kevalpatel2106.tensorflow.Classifier;
import com.kevalpatel2106.tensorflow.TensorFlowImageClassifier;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TensorFlowImageClassifier mTensorFlowClassifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTensorFlowClassifier = new TensorFlowImageClassifier(this);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Face1.jpg", options);

        final List<Classifier.Recognition> results = mTensorFlowClassifier.recognizeImage(bitmap);
        for (Classifier.Recognition reco : results) {
            Log.d("Recognized", "onCreate: " + reco.getTitle() + " " + reco.getConfidence());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mTensorFlowClassifier != null) mTensorFlowClassifier.close();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
