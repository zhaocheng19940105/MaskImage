package com.zc.mask;

import android.app.Activity;
import android.os.Bundle;

import com.mask.maskimage.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.main);
        super.onCreate(savedInstanceState);
        MaskImage maskImage = (MaskImage) findViewById(R.id.mask_image);
        maskImage.setImageResource(R.drawable.test);
        MaskImage maskImage2 = (MaskImage) findViewById(R.id.mask_image2);
        maskImage2.setMaskImg(R.drawable.mask);
        maskImage2.setImageResource(R.drawable.test);
    }

}
