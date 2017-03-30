package database.repository;


import database.entities.Asset;
import database.entities.AssetType;
import database.entities.DaoSession;
import java.util.List;

public class AssetRepository {

    private DaoSession daoSession;

    public AssetRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public Long create(String type, String filename) {
        if(isValidAssetType(type)) {
            Asset asset = new Asset();
            asset.setType(type);
            asset.setFilename(filename);
            return daoSession.getAssetDao().insert(asset);
        } else {
            throw new IllegalArgumentException("Not valid asset type: " + type);
        }
    }

    public Asset get(Long id) {
        return daoSession.getAssetDao().load(id);
    }

    public List<Asset> getAll() {
        return daoSession.getAssetDao().loadAll();
    }

    public void delete(Long id) {
        daoSession.getAssetDao().deleteByKey(id);
    }

    private boolean isValidAssetType(String type) {
        for(String validAssetType : AssetType.VALID_TYPES) {
            if(type.equals(validAssetType)) {
                return true;
            }
        }
        return false;
    }

}
