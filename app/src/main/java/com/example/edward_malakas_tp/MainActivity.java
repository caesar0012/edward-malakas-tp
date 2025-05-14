package com.example.edward_malakas_tp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton btnReset;
    private ImageView[] slotImageViews;
    List<Integer> shuffledCardList = new ArrayList<>();
    List<Integer> hiddenCardList = new ArrayList<>();
    private String firstClickedCardName = null;
    private ImageView firstClickedImageView = null;
    private final int[] cardDrawables = {
            R.drawable.ic_one,
            R.drawable.ic_one,
            R.drawable.ic_two,
            R.drawable.ic_two,
            R.drawable.ic_three,
            R.drawable.ic_three,
            R.drawable.ic_four,
            R.drawable.ic_four,
            R.drawable.ic_five,
            R.drawable.ic_five,
            R.drawable.ic_six,
            R.drawable.ic_ssix
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnReset = findViewById(R.id.btnReset);

        //--this is the fbid
        slotImageViews = new ImageView[]{
                findViewById(R.id.imgCv1),
                findViewById(R.id.imgCv2),
                findViewById(R.id.imgCv3),
                findViewById(R.id.imgCv4),
                findViewById(R.id.imgCv5),
                findViewById(R.id.imgCv6),
                findViewById(R.id.imgCv7),
                findViewById(R.id.imgCv8),
                findViewById(R.id.imgCv9),
                findViewById(R.id.imgCv10),
                findViewById(R.id.imgCv11),
                findViewById(R.id.imgCv12)
        };
        shuffleCards();
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffleCards();
            }
        });
        // Set click listeners for card ImageViews
        for (ImageView imageView : slotImageViews) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    revealCard((ImageView) v);
                }
            });
        }

    }

    private void shuffleCards() {
        hiddenCardList.clear();
        shuffledCardList.clear();



        // Store the hidden card drawables in a list
        for (int card : cardDrawables) {
            hiddenCardList.add(card);
        }

        // Shuffle the hidden card drawables
        Collections.shuffle(hiddenCardList);

        // Populate the shuffled card list with the shuffled drawables and the default hidden card drawable
        for (int i = 0; i < slotImageViews.length; i++) {
            int drawable;
            if (i < hiddenCardList.size()) {
                drawable = hiddenCardList.get(i); // Use the shuffled hidden card drawable
            } else {
                drawable = R.drawable.ic_question; // Use the default hidden card drawable
            }
            shuffledCardList.add(drawable);
        }

        // Set the ImageViews with the shuffled card drawables
        for (int i = 0; i < slotImageViews.length; i++) {
            slotImageViews[i].setImageResource(shuffledCardList.get(i));
        }

        new Handler().postDelayed(() -> {
            // Hide cards with default drawable after 1 second
            for (ImageView imageView : slotImageViews) {
                imageView.setImageResource(R.drawable.ic_question);
            }
        }, 1000);
    }

    private void revealCard(ImageView imageView) {
        // Get the index of the clicked ImageView in slotImageViews array
        int index = -1;
        for (int i = 0; i < slotImageViews.length; i++) {
            if (slotImageViews[i] == imageView) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            // Get the corresponding card drawable and set it to the ImageView
            int cardDrawableId = shuffledCardList.get(index);
            imageView.setImageResource(cardDrawableId);

            // Get the resource name of the drawable
            String cardName = getResources().getResourceName(cardDrawableId);

            // Create local variables to hold the ImageView references
            ImageView firstClickedImageViewCopy = firstClickedImageView;
            ImageView imageViewCopy = imageView;

            // If it's the first card clicked, store its name and ImageView
            if (firstClickedCardName == null) {
                firstClickedCardName = cardName;
                firstClickedImageView = imageView;
            } else {
                // Compare the names of the first and second clicked cards
                if (cardName.equals(firstClickedCardName)) {
                    // If the names match, keep the cards revealed
                    Toast.makeText(this, "Match!", Toast.LENGTH_SHORT).show();
                } else {
                    // If the names don't match, hide them again after a brief delay
                    new Handler().postDelayed(() -> {
                        imageViewCopy.setImageResource(R.drawable.ic_question);
                        firstClickedImageViewCopy.setImageResource(R.drawable.ic_question);
                        Toast.makeText(this, "Not Match", Toast.LENGTH_SHORT).show();
                    }, 1000);
                }
                // Reset the first clicked card information
                firstClickedCardName = null;
                firstClickedImageView = null;
            }
        }
    }

}
