package com.sneha.livestreaming.database;

import android.util.Log;

import com.sneha.livestreaming.database.realmmodel.MyFavoriteRealmModel;
import com.sneha.livestreaming.database.realmmodel.MyFavoritemodel;
import com.sneha.livestreaming.model.AllChannedlModel;
import com.sneha.livestreaming.model.AllDataModel;
import com.sneha.livestreaming.database.realmmodel.AllChannelRealmModel;
import com.sneha.livestreaming.database.realmmodel.CategoryRealmDataModel;
import com.sneha.livestreaming.database.realmmodel.InnerCategoryModel;
import com.sneha.livestreaming.database.realmmodel.InnerChannelModel;
import com.sneha.livestreaming.database.realmmodel.LatestChannelModel;
import com.sneha.livestreaming.database.realmmodel.LetestChannelRealmModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class RealamDatabase {

    public static RealamDatabase realamDatabase = null;


    /**
     * This method is used to get
     * the instance of RealmDatabase
     */

    public static RealamDatabase getInstance() {
        if (realamDatabase == null) {
            return new RealamDatabase();
        }
        return realamDatabase;
    }


    public void saveAllChannelModel(AllChannedlModel allChannedlModel) {
        Realm realm = Realm.getDefaultInstance();
        List<AllChannelRealmModel> arrayList = new ArrayList<>();
        try {
            if (realm.isInTransaction())
                realm.commitTransaction();
            realm.beginTransaction();
            for (int i = 0; i < allChannedlModel.getChannels().size(); i++) {
                AllChannelRealmModel allChannelRealmModel = new AllChannelRealmModel();
                allChannelRealmModel.setCat_id(allChannedlModel.getChannels().get(i).getCat_id());
                allChannelRealmModel.setChannels_name(allChannedlModel.getChannels().get(i).getChannels_name());
                allChannelRealmModel.setChannel_description(allChannedlModel.getChannels().get(i).getChannel_description());
                allChannelRealmModel.setChannel_type(allChannedlModel.getChannels().get(i).getChannel_type());
                allChannelRealmModel.setChannels_url(allChannedlModel.getChannels().get(i).getChannels_url());
                allChannelRealmModel.setChannels_image(allChannedlModel.getChannels().get(i).getChannels_image());
                allChannelRealmModel.setCreated_at(allChannedlModel.getChannels().get(i).getCreated_at());
                allChannelRealmModel.setChannels_id(allChannedlModel.getChannels().get(i).getChannels_id());
                allChannelRealmModel.setCategory_name(allChannedlModel.getChannels().get(i).getCategory_name());
                arrayList.add(allChannelRealmModel);
            }
            realm.copyToRealmOrUpdate(arrayList);
            realm.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            if (realm.isInTransaction())
                realm.commitTransaction();
        } finally {
            realm.close();
        }

    }


    public void saveAllcategoryModel(AllDataModel allDataModel) {
        Realm realm = Realm.getDefaultInstance();
        List<CategoryRealmDataModel> arrayList = new ArrayList<>();
        try {
            if (realm.isInTransaction())
                realm.commitTransaction();
            realm.beginTransaction();
            for (int i = 0; i < allDataModel.getCategories().size(); i++) {
                CategoryRealmDataModel categoryRealmDataModel = new CategoryRealmDataModel();
                categoryRealmDataModel.setCat_id(allDataModel.getCategories().get(i).getCat_id());
                categoryRealmDataModel.setImage(allDataModel.getCategories().get(i).getImage());
                categoryRealmDataModel.setName(allDataModel.getCategories().get(i).getName());
                arrayList.add(categoryRealmDataModel);
            }
            realm.copyToRealmOrUpdate(arrayList);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            if (realm.isInTransaction())
                realm.commitTransaction();
        } finally {
            realm.close();
        }
    }


    public void saveAllLatestChannelModel(AllDataModel allDataModel) {
        Realm realm = Realm.getDefaultInstance();
        List<LetestChannelRealmModel> arrayList = new ArrayList<>();
        try {
            if (realm.isInTransaction())
                realm.commitTransaction();
            realm.beginTransaction();
            for (int i = 0; i < allDataModel.getLatest_channel().size(); i++) {
                LetestChannelRealmModel letestChannelRealmModel = new LetestChannelRealmModel();
                letestChannelRealmModel.setId(allDataModel.getLatest_channel().get(i).getId());
                letestChannelRealmModel.setChannel_description(allDataModel.getLatest_channel().get(i).getChannel_description());
                letestChannelRealmModel.setChannel_type(allDataModel.getLatest_channel().get(i).getChannel_type());
                letestChannelRealmModel.setChannels_image(allDataModel.getLatest_channel().get(i).getChannels_image());
                letestChannelRealmModel.setChannels_name(allDataModel.getLatest_channel().get(i).getChannels_name());
                letestChannelRealmModel.setChannels_url(allDataModel.getLatest_channel().get(i).getChannels_url());
                letestChannelRealmModel.setCreated_at(allDataModel.getLatest_channel().get(i).getCreated_at());
                letestChannelRealmModel.setCat_id(allDataModel.getLatest_channel().get(i).getCat_id());
                letestChannelRealmModel.setChannels_id(allDataModel.getLatest_channel().get(i).getChannels_id());
                letestChannelRealmModel.setCategory_name(allDataModel.getLatest_channel().get(i).getCategory_name());

                arrayList.add(letestChannelRealmModel);
            }
            realm.copyToRealmOrUpdate(arrayList);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            if (realm.isInTransaction())
                realm.commitTransaction();
        } finally {
            realm.close();
        }
    }


    public void saveMyFavorite(MyFavoritemodel myFavoritemodel) {
        Realm realm = Realm.getDefaultInstance();
        try {
            if (realm.isInTransaction())
                realm.commitTransaction();
            realm.beginTransaction();
            MyFavoriteRealmModel myFavoriteRealmModel = new MyFavoriteRealmModel();
            myFavoriteRealmModel.setChannel_description(myFavoritemodel.getChannel_description());
            myFavoriteRealmModel.setChannel_type(myFavoritemodel.getChannel_type());
            myFavoriteRealmModel.setChannels_image(myFavoritemodel.getChannels_image());
            myFavoriteRealmModel.setChannels_name(myFavoritemodel.getChannels_name());
            myFavoriteRealmModel.setChannels_url(myFavoritemodel.getChannels_url());
            myFavoriteRealmModel.setCreated_at(myFavoritemodel.getCreated_at());
            myFavoriteRealmModel.setCat_id(myFavoritemodel.getCat_id());
            myFavoriteRealmModel.setChannels_id(myFavoritemodel.getChannels_id());
            myFavoriteRealmModel.setFavorite(myFavoritemodel.isFavorite());
            realm.copyToRealmOrUpdate(myFavoriteRealmModel);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            if (realm.isInTransaction())
                realm.commitTransaction();
        } finally {
            realm.close();
        }
    }


    public List<InnerChannelModel> getAllChannelList(String Channel_ID) {
        List<InnerChannelModel> arrayList = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<AllChannelRealmModel> results = realm.where(AllChannelRealmModel.class).equalTo("cat_id", Channel_ID).findAll();
            if (realm.isInTransaction())
                realm.commitTransaction();
            realm.beginTransaction();
            if (results != null) {
                for (int i = 0; i < results.size(); i++) {
                    InnerChannelModel innerChannelModel = new InnerChannelModel();
                    innerChannelModel.setChannel_description(results.get(i).getChannel_description());
                    innerChannelModel.setChannel_type(results.get(i).getChannel_type());
                    innerChannelModel.setChannels_image(results.get(i).getChannels_image());
                    innerChannelModel.setChannels_name(results.get(i).getChannels_name());
                    innerChannelModel.setChannels_url(results.get(i).getChannels_url());
                    innerChannelModel.setCreated_at(results.get(i).getCreated_at());
                    innerChannelModel.setCat_id(results.get(i).getCat_id());
                    innerChannelModel.setChannels_id(results.get(i).getChannels_id());
                    innerChannelModel.setCategory_name(results.get(i).getCategory_name());
                    arrayList.add(innerChannelModel);
                }
            }
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            if (realm.isInTransaction())
                realm.commitTransaction();
        } finally {
            realm.close();
        }
        return arrayList;
    }

    public List<InnerCategoryModel> getAllDataModel() {
        List<InnerCategoryModel> arrayList = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<CategoryRealmDataModel> results = realm.where(CategoryRealmDataModel.class).findAll();
            if (realm.isInTransaction())
                realm.commitTransaction();
            realm.beginTransaction();
            if (results != null) {
                for (int i = 0; i < results.size(); i++) {
                    InnerCategoryModel innerCategoryModel = new InnerCategoryModel();
                    innerCategoryModel.setCat_id(results.get(i).getCat_id());
                    innerCategoryModel.setImage(results.get(i).getImage());
                    innerCategoryModel.setName(results.get(i).getName());
                    arrayList.add(innerCategoryModel);
                }
            }
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            if (realm.isInTransaction())
                realm.commitTransaction();
        } finally {
            realm.close();
        }
        return arrayList;
    }

    public List<LatestChannelModel> getListOfAllLatestChannels() {
        List<LatestChannelModel> arrayList = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<LetestChannelRealmModel> results = realm.where(LetestChannelRealmModel.class).findAll();

            if (realm.isInTransaction())
                realm.commitTransaction();
            realm.beginTransaction();
            if (results != null) {
                for (int i = 0; i < results.size(); i++) {
                    LatestChannelModel latestChannelModel = new LatestChannelModel();
                    latestChannelModel.setId(results.get(i).getId());
                    latestChannelModel.setChannel_description(results.get(i).getChannel_description());
                    latestChannelModel.setChannel_type(results.get(i).getChannel_type());
                    latestChannelModel.setChannels_image(results.get(i).getChannels_image());
                    latestChannelModel.setChannels_name(results.get(i).getChannels_name());
                    latestChannelModel.setChannels_url(results.get(i).getChannels_url());
                    latestChannelModel.setCreated_at(results.get(i).getCreated_at());
                    latestChannelModel.setCat_id(results.get(i).getCat_id());
                    latestChannelModel.setChannels_id(results.get(i).getChannels_id());
                    latestChannelModel.setCategory_name(results.get(i).getCategory_name());
                    arrayList.add(latestChannelModel);
                }
            }
            realm.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            if (realm.isInTransaction())
                realm.commitTransaction();
        } finally {
            realm.close();
        }
        return arrayList;
    }

    public List<LatestChannelModel> getListOfAllLatestChannelsID(String Channel_ID) {
        List<LatestChannelModel> arrayList = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<LetestChannelRealmModel> results = realm.where(LetestChannelRealmModel.class).equalTo("channels_id", Channel_ID).findAll();

            if (realm.isInTransaction())
                realm.commitTransaction();
            realm.beginTransaction();
            if (results != null) {
                for (int i = 0; i < results.size(); i++) {
                    LatestChannelModel latestChannelModel = new LatestChannelModel();
                    latestChannelModel.setId(results.get(i).getId());
                    latestChannelModel.setChannel_description(results.get(i).getChannel_description());
                    latestChannelModel.setChannel_type(results.get(i).getChannel_type());
                    latestChannelModel.setChannels_image(results.get(i).getChannels_image());
                    latestChannelModel.setChannels_name(results.get(i).getChannels_name());
                    latestChannelModel.setChannels_url(results.get(i).getChannels_url());
                    latestChannelModel.setCreated_at(results.get(i).getCreated_at());
                    latestChannelModel.setCat_id(results.get(i).getCat_id());
                    latestChannelModel.setChannels_id(results.get(i).getChannels_id());
                    arrayList.add(latestChannelModel);
                }
            }
            realm.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            if (realm.isInTransaction())
                realm.commitTransaction();
        } finally {
            realm.close();
        }
        return arrayList;
    }



    public List<MyFavoritemodel> getAllMyFavorite() {
        List<MyFavoritemodel> arrayList = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<MyFavoriteRealmModel> results = realm.where(MyFavoriteRealmModel.class).findAll();
            if (realm.isInTransaction())
                realm.commitTransaction();
            realm.beginTransaction();
            if (results != null) {
                for (int i = 0; i < results.size(); i++) {
                    MyFavoritemodel myFavoritemodel = new MyFavoritemodel();
                    myFavoritemodel.setChannel_description(results.get(i).getChannel_description());
                    myFavoritemodel.setChannel_type(results.get(i).getChannel_type());
                    myFavoritemodel.setChannels_image(results.get(i).getChannels_image());
                    myFavoritemodel.setChannels_name(results.get(i).getChannels_name());
                    myFavoritemodel.setChannels_url(results.get(i).getChannels_url());
                    myFavoritemodel.setCreated_at(results.get(i).getCreated_at());
                    myFavoritemodel.setCat_id(results.get(i).getCat_id());
                    myFavoritemodel.setFavorite(results.get(i).isFavorite());
                    myFavoritemodel.setChannels_id(results.get(i).getChannels_id());
                    arrayList.add(myFavoritemodel);
                }
            }
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            if (realm.isInTransaction())
                realm.commitTransaction();
        } finally {
            realm.close();
        }
        return arrayList;
    }


    public void deleteRowInMyFavorate(String Channel_ID){

        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<MyFavoriteRealmModel> results = realm.where(MyFavoriteRealmModel.class).equalTo("channels_id", Channel_ID).findAll();
            if (realm.isInTransaction())
                realm.commitTransaction();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            if (realm.isInTransaction())
                realm.commitTransaction();
        } finally {
            realm.close();
        }
    }
    public List<MyFavoritemodel> getMyFavorite(String ChannelID) {
        List<MyFavoritemodel> arrayList = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<MyFavoriteRealmModel> results = realm.where(MyFavoriteRealmModel.class).equalTo("channels_id",ChannelID).findAll();
            if (realm.isInTransaction())
                realm.commitTransaction();
            realm.beginTransaction();
            if (results != null) {
                for (int i = 0; i < results.size(); i++) {
                    MyFavoritemodel myFavoritemodel = new MyFavoritemodel();
                    myFavoritemodel.setChannel_description(results.get(i).getChannel_description());
                    myFavoritemodel.setChannel_type(results.get(i).getChannel_type());
                    myFavoritemodel.setChannels_image(results.get(i).getChannels_image());
                    myFavoritemodel.setChannels_name(results.get(i).getChannels_name());
                    myFavoritemodel.setChannels_url(results.get(i).getChannels_url());
                    myFavoritemodel.setCreated_at(results.get(i).getCreated_at());
                    myFavoritemodel.setCat_id(results.get(i).getCat_id());
                    myFavoritemodel.setFavorite(results.get(i).isFavorite());
                    arrayList.add(myFavoritemodel);
                }
            }
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            if (realm.isInTransaction())
                realm.commitTransaction();
        } finally {
            realm.close();
        }
        return arrayList;
    }


    public void resetRealm() {


        Realm realm = Realm.getDefaultInstance();
        final RealmConfiguration realmConfiguration =realm.getConfiguration();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for(Class<? extends RealmModel> clazz : realmConfiguration.getRealmObjectClasses())
                        realm.delete(clazz);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (realm.isInTransaction())
                realm.commitTransaction();
        } finally {
            realm.close();
        }

    }



}
