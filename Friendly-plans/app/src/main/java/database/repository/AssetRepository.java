package database.repository;

import database.entities.Asset;
import database.entities.DaoSession;
import java.util.List;
import pg.autyzm.friendly_plans.asset.AssetType;

public class AssetRepository {

    private DaoSession daoSession;

    public AssetRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public Long create(AssetType type, String filename) {
        Asset asset = new Asset();
        asset.setType(type.toString());
        asset.setFilename(filename);
        return daoSession.getAssetDao().insert(asset);
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

}
