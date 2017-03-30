package pg.autyzm.friendly_plans.database;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import database.entities.Asset;
import database.entities.AssetDao;
import database.entities.AssetType;
import database.entities.DaoSession;
import database.repository.AssetRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssetRepositoryTest {

    public static final String ASSET_FILENAME = "Asset/file.jpg";
    public static final String ASSET_TYPE = AssetType.PICTURE;
    public static final String WRONG_ASSET_TYPE = "Very bad type for asset";

    @InjectMocks
    AssetRepository assetRepository;

    @Mock
    private DaoSession daoSession;

    @Mock
    private AssetDao assetDao;

    private Long randomId;

    @Before
    public void setUp() {
        randomId = new Random().nextLong();
        Asset asset = new Asset();
        asset.setId(randomId);
        asset.setType(ASSET_TYPE);
        asset.setFilename(ASSET_FILENAME);

        List<Asset> assets = new ArrayList<>();
        assets.add(asset);

        when(daoSession.getAssetDao()).thenReturn(assetDao);
        when(assetDao.insert(any(Asset.class))).thenReturn(randomId);
        when(assetDao.load(randomId)).thenReturn(asset);
        when(assetDao.loadAll()).thenReturn(assets);
    }

    @Test
    public void When_CreatingAsset_Expect_InsertMethodBeCalled() {
        assetRepository.create(ASSET_TYPE, ASSET_FILENAME);
        verify(assetDao).insert(any(Asset.class));
    }

    @Test
    public void When_CreatingAsset_Expect_NewIdBeReturned() {
        Long id = assetRepository.create(ASSET_TYPE, ASSET_FILENAME);
        assertThat(id, is(equalTo(randomId)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void When_CreatingAssetWithWrongType_Expect_ExceptionBeThrown() {
        assetRepository.create(WRONG_ASSET_TYPE, ASSET_FILENAME);
    }

    @Test
    public void When_GettingAsset_Expect_LoadMethodBeCalled() {
        assetRepository.get(randomId);
        verify(assetDao).load(randomId);
    }

    @Test
    public void When_GettingAsset_Expect_AssetToBeReturned() {
        Asset asset = assetRepository.get(randomId);
        assertThat(asset.getType(), is(equalTo(ASSET_TYPE)));
        assertThat(asset.getFilename(), is(equalTo(ASSET_FILENAME)));
    }

    @Test
    public void When_DeletingAsset_Expect_DeleteByKeyMethodBeCalled() {
        assetRepository.delete(randomId);
        verify(assetDao).deleteByKey(randomId);
    }

    @Test
    public void When_GettingAllAssets_Expect_LoadAllMethodBeCalled() {
        assetRepository.getAll();
        verify(assetDao).loadAll();
    }

    @Test
    public void When_GettingAllAssets_Expect_AssetsListBeReturned() {
        List<Asset> assets = assetRepository.getAll();
        assertThat(assets.size(), is(equalTo(1)));
    }

}
