package ru.abbysoft.wisebuild;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import ru.abbysoft.wisebuild.model.ComputerPart;

/**
 * Specify parameters for new component
 *
 * @author apopov
 */
public class PartCreationActivity extends AppCompatActivity {

    public static final String PART_TYPE_EXTRA = "PART_TYPE";
    private static final int NEW_IMAGE_PICKED = 1;
    private static final String LOG_TAG = "PART_CREATION_ACTIVITY";

    private volatile ComputerPart.ComputerPartType partType;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_creation);

        imageView = findViewById(R.id.new_component_image);

        getPassedExtras();
    }

    private void getPassedExtras() {
        Intent intent = getIntent();

        partType = (ComputerPart.ComputerPartType) intent.getSerializableExtra(PART_TYPE_EXTRA);

        TextView headerMessage = findViewById(R.id.part_creation_label);

        // TODO placeholders?
        headerMessage.setText(getString(R.string.creating_part_message) + partType.getReadableName());
    }

    /**
     * Save component with current parameters
     */
    public void savePart() {

    }

    /**
     * Choose image for component
     *
     * @param view button
     */
    public void addImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, NEW_IMAGE_PICKED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == NEW_IMAGE_PICKED) {
            Bitmap picture = getPicture(data.getData());
            if (picture == null) {
                return;
            }
            imageView.setImageBitmap(picture);
        }
    }

    private Bitmap getPicture(Uri selectedImage) {
        ParcelFileDescriptor parcelFileDescriptor = getFileDescriptor(selectedImage);
        if (parcelFileDescriptor == null) {
            return null;
        }

        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

        try {
            parcelFileDescriptor.close();
        } catch (IOException e) {
            return null;
        }

        return image;
    }

    private ParcelFileDescriptor getFileDescriptor(Uri selectedImage) {
        try {
            return getContentResolver().openFileDescriptor(selectedImage, "r");
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "Cannot load image");
            e.printStackTrace();
            return null;
        }
    }
}
