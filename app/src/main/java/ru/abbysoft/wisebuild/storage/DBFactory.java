package ru.abbysoft.wisebuild.storage;

/**
 * This class is responsible for creation and access to
 * db.
 *
 * @author apopov
 */
public class DBFactory {

    /**
     * Only way to access application-wide object of DBInterface
     *
     * @return current db
     */
    public static DBInterface getDatabase() {
        return InMemoryDB.INSTANCE;
    }
}
