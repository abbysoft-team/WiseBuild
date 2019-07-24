package ru.abbysoft.wisebuild;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import ru.abbysoft.wisebuild.model.CPU;
import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.model.MemoryModule;
import ru.abbysoft.wisebuild.model.Motherboard;
import ru.abbysoft.wisebuild.storage.DBFactory;
import ru.abbysoft.wisebuild.storage.DBInterface;

/**
 * Specify parameters for new component
 *
 * @author apopov
 */
public class PartCreationActivity extends AppCompatActivity implements Validator.ValidationListener {

    public static final String PART_TYPE_EXTRA = "PART_TYPE";
    private static final int NEW_IMAGE_PICKED = 1;
    private static final String LOG_TAG = "PART_CREATION_ACTIVITY";

    private volatile ComputerPart.ComputerPartType partType;
    private ImageView imageView;

    private TextView additionalParamLabel1;
    private TextView additionalParamLabel2;
    private TextView additionalParamSpinnerLabel;

    @NotEmpty
    private EditText additionalParamField1;

    @NotEmpty
    private EditText additionalParamField2;

    private Spinner additionalParamSpinner;

    @NotEmpty
    private EditText nameField;

    private EditText descriptionField;
    private EditText priceField;

    private Bitmap currentImage;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_creation);

        imageView = findViewById(R.id.new_component_image);
        nameField = findViewById(R.id.part_name_field);
        descriptionField = findViewById(R.id.part_description_field);
        priceField = findViewById(R.id.part_price_field);
        additionalParamLabel1 = findViewById(R.id.additional_parameter_1_label);
        additionalParamLabel2 = findViewById(R.id.additional_parameter_2_label);
        additionalParamField1 = findViewById(R.id.additional_parameter_1_field);
        additionalParamField2 = findViewById(R.id.additional_parameter_2_field);
        additionalParamSpinnerLabel = findViewById(R.id.additional_param_spinner_label);
        additionalParamSpinner = findViewById(R.id.additional_param_spinner);

        getPassedExtras();
        addAdditionalFields();
        addValidators();
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

    private void addValidators() {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    /**
     * Save component with current parameters
     *
     * @param view button
     */
    public void savePart(View view) {
        validator.validate();


        String name = nameField.getText().toString().trim();
        String description = descriptionField.getText().toString().trim();
        Integer price = null;
        if (!priceField.getText().toString().trim().isEmpty()) {
            price = Integer.parseInt(priceField.getText().toString());
        }

        ComputerPart part = null;
        switch (partType) {
            case CPU:
                part = getCPUPart(name);
                break;
            case MOTHERBOARD:
                part = getMotherboardPart(name);
                break;
            case MEMORY_MODULE:
                part = getMemoryPart(name);
                break;

                default:
                    break;
        }

        part.setDescription(description);
        part.setPhoto(currentImage);

        if (price != null) {
            part.setPriceUsd(price);
        }

        // actuall saving
        DBFactory.getDatabase().storePart(part);
        showSaveSuccessMessage();
    }

    private ComputerPart getCPUPart(String name) {
        String manufacturer = additionalParamField1.getText().toString();
        int cores = Integer.parseInt(additionalParamField2.getText().toString());

        return new CPU(name, manufacturer, cores);
    }

    private ComputerPart getMotherboardPart(String name) {
        Motherboard.SocketType socket =
                (Motherboard.SocketType) additionalParamSpinner.getSelectedItem();

        return new Motherboard(name, socket);
    }

    private ComputerPart getMemoryPart(String name) {
        int capacity = Integer.parseInt(additionalParamField1.getText().toString());
        MemoryModule.MemoryType type =
                (MemoryModule.MemoryType) additionalParamSpinner.getSelectedItem();

        return new MemoryModule(name, type, capacity);
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

            currentImage = picture;
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

    @Override
    public void onValidationSucceeded() {
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showSaveSuccessMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Part created successfully");
        builder.setTitle("Done");
        builder.setPositiveButton(R.string.ok, ((dialogInterface, i) -> finish()));

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
