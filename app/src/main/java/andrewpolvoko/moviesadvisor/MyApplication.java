package andrewpolvoko.moviesadvisor;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import info.movito.themoviedbapi.TmdbApi;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    static TmdbApi mTmdbApi;
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        try {
            new Thread(new Runnable() {
                public void run() {
                    mTmdbApi = new TmdbApi(Constants.TMDB_API_KEY);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Realm.init(this);
        //Realm.deleteRealm(new RealmConfiguration.Builder().build());
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().build());

        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                            .build());
        }
    }


}
