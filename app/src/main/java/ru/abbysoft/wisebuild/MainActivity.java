package ru.abbysoft.wisebuild;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.abbysoft.wisebuild.assembly.CreateAssemblyActivity;
import ru.abbysoft.wisebuild.browser.PartBrowserActivity;
import ru.abbysoft.wisebuild.model.CPU;
import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.model.MemoryModule;
import ru.abbysoft.wisebuild.model.Motherboard;
import ru.abbysoft.wisebuild.storage.DBFactory;
import ru.abbysoft.wisebuild.storage.DBInterface;
import ru.abbysoft.wisebuild.utils.ModelUtils;

/**
 * MainActivity class
 *
 * Here you can choose action to do
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createTestData();
    }

    /**
     * Temporary method for populating default DB
     */
    private void createTestData() {
        DBInterface db = DBFactory.getDatabase();

        db.storePart(new CPU("Intel core i3", "Intel", 2));
        db.storePart(new CPU("Intel core i5", "Intel", 4));
        db.storePart(new CPU("Intel core i7", "Intel", 8));
        db.storePart(new Motherboard("Asus MX243", Motherboard.SocketType.LGA1155));
        db.storePart(new MemoryModule("Samsung S400488", MemoryModule.MemoryType.DDR4, 8000));
    }

    /**
     * This method is called when addPartButton clicked
     * @param view addPartButton
     */
    public void addPartButtonClicked(View view) {
        AddPartActivity.createIntentFrom(this, getClass());
    }

    public void browsePartsButtonClicked(View view) {
        Intent intent = new Intent(this, PartBrowserActivity.class);
        startActivity(intent);
    }

    public void createAssemblyButtonClicked(View view) {
        CreateAssemblyActivity.launchActivityFrom(this);
    }

    public void openRandomPartButtonClicked(View view) {
        ComputerPart randomOne = ModelUtils.generateRandomPart();
        DBFactory.getDatabase().storePart(randomOne);

        PartParametersActivity.launchForViewParametersOf(randomOne.getId(), this);
    }
}
