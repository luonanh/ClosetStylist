package com.example.closetstylist;

public class SDCardStorageFactory extends StorageFactory {
	
	@Override
	protected StorageInterface createStorage() {
		return new SDCardStorage();
	}

}
