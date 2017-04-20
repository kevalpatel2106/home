package com.kevalpatel2106.home.things;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.kevalpatel2106.tensorflow.Classifier;
import com.kevalpatel2106.tensorflow.TensorFlowImageClassifier;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int IMAGE_WIDTH = 320;
    public static final int IMAGE_HEIGHT = 240;
    private TensorFlowImageClassifier mTensorFlowClassifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTensorFlowClassifier = new TensorFlowImageClassifier(this);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Face1.jpg", options);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                TensorFlowImageClassifier.INPUT_SIZE,
                TensorFlowImageClassifier.INPUT_SIZE,
                false);
        ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);
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
        } catch (Throwable t) {
            // close quietly
        }
    }
}
