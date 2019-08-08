package ru.abbysoft.wisebuild;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ru.abbysoft.wisebuild.databinding.ParamDescription;
import ru.abbysoft.wisebuild.databinding.ParamView;
import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.storage.DBFactory;
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

    private ArrayList<ParamView> paramViews;

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
        headerMessage = findViewById(R.id.part_creation_label);

        //configurePriceField();

        getPassedExtras();
        if (isExtrasNotValid()) {
            return;
        }

        paramViews = addFields();

        if (partBeingCreated()) {
            configureViewForCreation();
        } else {
            configureViewForExistingPart();
        }

        setData(part);
    }

    private void getPassedExtras() {
        Intent intent = getIntent();

        part = getPartFromDB(intent.getLongExtra(PART_ID_EXTRA, -1));
        partType = (ComputerPart.ComputerPartType) intent.getSerializableExtra(PART_TYPE_EXTRA);

        if (part == null && partType == null) {
            partNotFound();
        }
        if (part == null) {
            setPartFromType();
        }
    }

    private void setPartFromType() {
        try {
            part = MiscUtils.instantiatePartOfType(partType);
        } catch (InstantiationException e) {
            e.printStackTrace();
            partNotFound();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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

    private ComputerPart createEmptyPartOfRequiredType() {
        try {
            return (ComputerPart) partType.getObjectClass().newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        Log.e(LOG_TAG, "Cannot create part for PartView activity");
        finish();
        throw new IllegalStateException();
    }

//
//    private void configurePriceField() {
//        priceField.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String text = priceField.getText().toString();
//
//                if (text.isEmpty()) {
//                    return;
//                }
//                if (text.startsWith("$")) {
//                    return;
//                }
//                if (text.contains("$")) {
//                    text = text.replace("$", "");
//                }
//
//                text = "$" + text;
//                priceField.setText(text.trim());
//                priceField.setSelection(text.length());
//            }
//        });
//    }

    private ArrayList<ParamView> addFields() {
        View referenceContainer = findViewById(R.id.part_creation_reference_container);
        ViewGroup container = findViewById(R.id.part_creation_parameters_container);

        ArrayList<ParamView> params = new ArrayList<>(10);

        ParamView view;
        for (ParamDescription description : part.getAllParameters()) {
            view = ParamView.Companion.createFor(this, description);
            if (view == null) continue;

            // add field to layout
            LinearLayout layout = new LinearLayout(this);
            layout.setLayoutParams(referenceContainer.getLayoutParams());
            layout.setOrientation(LinearLayout.HORIZONTAL);

            TextView textView = new TextView(this);
            textView.setText(description.getName());
            textView.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3f));

            view.getView().setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));


            layout.addView(textView);
            layout.addView(view.getView());

            container.addView(layout);
            params.add(view);
        }

        referenceContainer.setVisibility(View.GONE);

        return params;
    }

    private boolean partBeingCreated() {
        return part == null;
    }

    private void configureViewForCreation() {
        //addValidators();

        headerMessage.setText(
                getString(R.string.creating_part_message, partType.getReadableName()));
    }

    private void configureViewForExistingPart() {
        headerMessage.setText(part.getTrimmedName());

        disableAllFields();
        hideSaveButton();
    }

    private void disableAllFields() {
        for (ParamView view : paramViews) {
            view.getView().setEnabled(false);
        }
    }

    private void hideSaveButton() {
        findViewById(R.id.save_component_button).setVisibility(View.INVISIBLE);
    }

    private void setData(ComputerPart part) {
        for (ParamView paramView : paramViews) {
            paramView.setInput(part);
        }
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

        // actuall saving
        DBFactory.getDatabase().storePart(part);
        showSaveSuccessMessage();
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
