package com.example.closetstylist;

public class GoogleAppEngineStorageFactory extends StorageFactory {
	ItemDatabaseHelper dbHelper;

	public GoogleAppEngineStorageFactory(ItemDatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
	}
	@Override
	protected StorageInterface createStorage() {
		return new GoogleAppEngineStorage(dbHelper);
	}
}
