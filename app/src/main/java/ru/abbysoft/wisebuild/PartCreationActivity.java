package ru.abbysoft.wisebuild;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.model.MemoryModule;
import ru.abbysoft.wisebuild.model.Motherboard;

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
    private TextView additionalParamLabel1;
    private TextView additionalParamLabel2;
    private TextView additionalParamSpinnerLabel;
    private EditText additionalParamField1;
    private EditText additionalParamField2;
    private Spinner additionalParamSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_creation);

        imageView = findViewById(R.id.new_component_image);
        additionalParamLabel1 = findViewById(R.id.additional_parameter_1_label);
        additionalParamLabel2 = findViewById(R.id.additional_parameter_2_label);
        additionalParamField1 = findViewById(R.id.additional_parameter_1_field);
        additionalParamField2 = findViewById(R.id.additional_parameter_2_field);
        additionalParamSpinnerLabel = findViewById(R.id.additional_param_spinner_label);
        additionalParamSpinner = findViewById(R.id.additional_param_spinner);

        getPassedExtras();
        addAdditionalFields();
    }

    private void getPassedExtras() {
        Intent intent = getIntent();

        partType = (ComputerPart.ComputerPartType) intent.getSerializableExtra(PART_TYPE_EXTRA);

        TextView headerMessage = findViewById(R.id.part_creation_label);

        // TODO placeholders?
        headerMessage.setText(getString(R.string.creating_part_message) + partType.getReadableName());
    }

    private void addAdditionalFields() {
        switch (partType) {
            case CPU:
                addFieldsForCPU();
                break;
            case MEMORY_MODULE:
                addFieldsForMemory();
                break;
            case MOTHERBOARD:
                addFieldsForMotherboard();
                break;

                default:
                    break;
        }
    }

    private void addFieldsForCPU() {
        ((ViewManager)additionalParamSpinnerLabel.getParent())
                .removeView(additionalParamSpinnerLabel);
        ((ViewManager)additionalParamSpinner.getParent())
                .removeView(additionalParamSpinner);

        additionalParamLabel1.setText(getString(R.string.manufacturer));
        additionalParamLabel2.setText(getString(R.string.num_of_cores));

        additionalParamField2.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void addFieldsForMemory() {
        ((ViewManager)additionalParamField2.getParent()).removeView(additionalParamField2);
        ((ViewManager)additionalParamLabel2.getParent()).removeView(additionalParamLabel2);

        additionalParamLabel1.setText(getString(R.string.num_of_cores));
        additionalParamField1.setInputType(InputType.TYPE_CLASS_NUMBER);

        additionalParamSpinnerLabel.setText(getString(R.string.type));

        configureSpinnerForMemory();
    }

    private void configureSpinnerForMemory() {
        MemoryModule.MemoryType[] array = MemoryModule.MemoryType.values();
        ArrayAdapter<MemoryModule.MemoryType> adapter =
                new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, array);
        additionalParamSpinner.setAdapter(adapter);
    }

    private void addFieldsForMotherboard() {
        ((ViewManager)additionalParamField1.getParent()).removeView(additionalParamField1);
        ((ViewManager)additionalParamField2.getParent()).removeView(additionalParamField2);
        ((ViewManager)additionalParamLabel1.getParent()).removeView(additionalParamLabel1);
        ((ViewManager)additionalParamLabel2.getParent()).removeView(additionalParamLabel2);

        additionalParamSpinnerLabel.setText(getString(R.string.socket));

        configureSpinnerForMotherboard();
    }

    private void configureSpinnerForMotherboard() {
        Motherboard.SocketType[] array = Motherboard.SocketType.values();
        ArrayAdapter<Motherboard.SocketType> adapter =
                new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, array);
        additionalParamSpinner.setAdapter(adapter);
    }


    /**
     * Save component with current parameters
     *
     * @param view button
     */
    public void savePart(View view) {

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
