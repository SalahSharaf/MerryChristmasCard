package com.example.android.merrychristmascard;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements ObservableScrollView.OnScrollChangedListener {
    private ObservableScrollView mScrollView;
    private View imgContainer;
    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Init your layout and set your listener
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScrollView = findViewById(R.id.scroll_view);
        mScrollView.setOnScrollChangedListener(this);
        // Store the reference of your image container
        imgContainer = findViewById(R.id.img_container);
        // Store the reference of your image
        mImageView = findViewById(R.id.img);
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {
        // Get scroll view screen bound
        Rect scrollBounds = new Rect();
        mScrollView.getHitRect(scrollBounds);

        // Check if image container is visible in the screen
        // so to apply the translation only when the container is visible to the user
        if (imgContainer.getLocalVisibleRect(scrollBounds)) {
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics ();
            display.getMetrics(outMetrics);
            // Get screen density
            float density  = getResources().getDisplayMetrics().density;

            // Get screen height in pixels
            float dpHeight = outMetrics.heightPixels / density;
            int screen_height_pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpHeight, getResources().getDisplayMetrics());
            int half_screen_height = screen_height_pixels/2;

            // Get image container height in pixels
            int container_height_pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());

            // Get the location that consider a vertical center for the image (where the translation should be zero)
            int center = half_screen_height - (container_height_pixels/2);

            // get the location (x,y) of the image container in pixels
            int[] loc_screen = {0,0};
            imgContainer.getLocationOnScreen(loc_screen);

            // trying to transform the current image container location into percentage
            // so when the image container is exaclty in the middle of the screen percentage should be zero
            // and as the image container getting closer to the edges of the screen should increase to 100%
            int final_loc = ((loc_screen[1]-center)*100)/half_screen_height;

            // translate the inner image taking consideration also the density of the screen
            mImageView.setTranslationY(-final_loc * 0.4f * density);
        }
    }
}