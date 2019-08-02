package ru.abbysoft.wisebuild;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import ru.abbysoft.wisebuild.model.PartParameter;
import ru.abbysoft.wisebuild.storage.DBFactory;
import ru.abbysoft.wisebuild.utils.LayoutUtils;
import ru.abbysoft.wisebuild.utils.MiscUtils;

/**
 * Specify parameters for new component
 *
 * @author apopov
 */
public class PartParametersActivity extends AppCompatActivity implements Validator.ValidationListener {

    public static final String PART_TYPE_EXTRA = "PART_TYPE";
    public static final String PART_ID_EXTRA = "PART ID";

    private static final int NEW_IMAGE_PICKED = 1;
    private static final String LOG_TAG = "PART_CREATION_ACTIVITY";
    private static final int MAX_IMAGE_SIZE = 1000;
    private static final String IMAGE_URL_BUNDLED = "Bundled image";

    private volatile ComputerPart.ComputerPartType partType;
    private ComputerPart part;
    private ImageView imageView;

    private TextView headerMessage;

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
    private Uri currentImageUri;
    private Validator validator;
    private boolean validationResult;

    public static void launch(Context context, ComputerPart.ComputerPartType partType) {
        Intent intent = new Intent(context, PartParametersActivity.class);
        intent.putExtra(PartParametersActivity.PART_TYPE_EXTRA, partType);
        context.startActivity(intent);
    }

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
        headerMessage = findViewById(R.id.part_creation_label);

        configurePriceField();

        getPassedExtras();

        if (isExtrasNotValid()) {
            return;
        }

        if (partBeingCreated()) {
            addAdditionalFields();

            configureViewForCreation();
        } else {
            configureViewForExistingPart();
        }
    }

    private void configurePriceField() {
        priceField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String text = priceField.getText().toString();

                if (text.isEmpty()) {
                    return;
                }
                if (text.startsWith("$")) {
                    return;
                }
                if (text.contains("$")) {
                    text = text.replace("$", "");
                }

                text = "$" + text;
                priceField.setText(text.trim());
                priceField.setSelection(text.length());
            }
        });
    }

    private void getPassedExtras() {
        Intent intent = getIntent();

        part = getPartFromDB(intent.getLongExtra(PART_ID_EXTRA, -1));
        partType = (ComputerPart.ComputerPartType) intent.getSerializableExtra(PART_TYPE_EXTRA);

        if (part == null && partType == null) {
            partNotFound();
        }
    }

    private ComputerPart getPartFromDB(long id) {
        ComputerPart part = DBFactory.getDatabase().getPart(id);

        return part;
    }

    private void partNotFound() {
        MiscUtils.showErrorDialogAndFinish("Error", "Part not found", this);
    }

    private boolean isExtrasNotValid() {
        return part == null && partType == null;
    }

    private void addAdditionalFields() {
        ComputerPart.ComputerPartType type = partType == null ? part.getType() : partType;

        switch (type) {
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

        additionalParamLabel1.setText(getString(R.string.capacity_mb));
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
        LayoutUtils.removeViewFromLayout(additionalParamField1);
        LayoutUtils.removeViewFromLayout(additionalParamField2);
        LayoutUtils.removeViewFromLayout(additionalParamLabel1);
        LayoutUtils.removeViewFromLayout(additionalParamLabel2);

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

    private boolean partBeingCreated() {
        return part == null;
    }

    private void configureViewForCreation() {
        addValidators();

        headerMessage.setText(
                getString(R.string.creating_part_message, partType.getReadableName()));
    }

    private void configureViewForExistingPart() {
        headerMessage.setText(part.getType().getReadableName());
        nameField.setText(part.getFullName());
        descriptionField.setText(descriptionField.getText());
        String price = "$" + part.getPriceUsd();
        priceField.setText(price);

        hideUnusedViews();
        addPartParameterFields();

        // disable fields for editing
        nameField.setKeyListener(null);
        descriptionField.setKeyListener(null);
        priceField.setKeyListener(null);
    }

    private void hideUnusedViews() {
        Button saveButton = findViewById(R.id.save_component_button);
        saveButton.setVisibility(View.INVISIBLE);
        Button addPhotoButton = findViewById(R.id.add_photo_button);
        addPhotoButton.setVisibility(View.INVISIBLE);
        LayoutUtils.removeViewFromLayout(additionalParamField1);
        LayoutUtils.removeViewFromLayout(additionalParamField2);
        LayoutUtils.removeViewFromLayout(additionalParamLabel1);
        LayoutUtils.removeViewFromLayout(additionalParamLabel2);
        LayoutUtils.removeViewFromLayout(additionalParamSpinner);
        LayoutUtils.removeViewFromLayout(additionalParamSpinnerLabel);
    }

    private void addPartParameterFields() {
        List<PartParameter> parameters = part.getParameters();
        ViewGroup container = findViewById(R.id.part_creation_parameters_container);
        for (PartParameter parameter : parameters) {
            addParameterField(parameter, container);
        }
    }

    private void addParameterField(PartParameter parameter, ViewGroup container) {
        TextView labelView = new TextView(this);
        labelView.setText(parameter.getName());
        labelView.setLayoutParams(additionalParamLabel1.getLayoutParams());

        EditText editText = new EditText(this);
        editText.setText(parameter.getValue().toString());
        editText.setKeyListener(null);
        editText.setLayoutParams(additionalParamField1.getLayoutParams());

        container.addView(labelView);
        container.addView(editText);
    }

    /**
     * Save component with current parameters
     *
     * @param view button
     */
    public void savePart(View view) {
        validator.validate();
        if (!validationResult) {
            return;
        }

        String name = nameField.getText().toString().trim();
        String description = descriptionField.getText().toString().trim();
        Integer price = null;
        if (!priceField.getText().toString().trim().isEmpty()) {
            price = MiscUtils.getPriceFromCurrencyField(priceField);
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

            loadImage(data.getData());
        }
    }

    private void loadImage(Uri imageUri) {
        Bitmap picture = getPicture(imageUri);
        if (picture == null) {
            return;
        }
        picture = resizeBitmap(picture);
        imageView.setImageBitmap(picture);

        currentImage = picture;
        currentImageUri = imageUri;
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

    private Bitmap resizeBitmap(Bitmap bigImage) {
        if (bigImage.getHeight() <= MAX_IMAGE_SIZE) {
            return bigImage;
        }
        if (bigImage.getWidth() <= MAX_IMAGE_SIZE) {
            return bigImage;
        }
        double rate = bigImage.getWidth() * 1.0 / bigImage.getHeight();
        return Bitmap.createScaledBitmap(
                bigImage, MAX_IMAGE_SIZE, ((int) (rate * MAX_IMAGE_SIZE)), false);
    }

    @Override
    public void onValidationSucceeded() {
        validationResult = true;
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

        validationResult = false;
    }

    private void showSaveSuccessMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Part created successfully");
        builder.setTitle("Done");
        builder.setPositiveButton(R.string.ok, ((dialogInterface, i) -> finish()));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (currentImageUri != null) {
            outState.putParcelable(IMAGE_URL_BUNDLED, currentImageUri);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        setBundledImage(savedInstanceState);
    }

    private void setBundledImage(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        Uri imageUri = savedInstanceState.getParcelable(IMAGE_URL_BUNDLED);
        if (imageUri == null) {
            return;
        }

        loadImage(imageUri);
    }

    /**
     * Launch detailed information for specific part
     *
     * @param partId part id in db which parameters should be displayed
     * @param context context
     */
    public static void launchForViewParametersOf(long partId, Context context) {
        Intent intent = new Intent(context, PartParametersActivity.class);
        intent.putExtra(PART_ID_EXTRA, partId);

        context.startActivity(intent);
    }
}
