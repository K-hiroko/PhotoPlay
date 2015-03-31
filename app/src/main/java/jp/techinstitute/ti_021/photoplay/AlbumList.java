package jp.techinstitute.ti_021.photoplay;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by hiroko on 2015/03/31.
 */
public class AlbumList extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // XMLをViewに設定する。
        setContentView(R.layout.album_list);

        String[] strDspAlbDate = {"2014/10/01", "2015/01/04", "2015/02/05", "2015/03/30", "2015/04/04", "2015/04/20"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strDspAlbDate);

        ListView lsvAlb = (ListView) findViewById(R.id.lsv_album_lst);
        lsvAlb.setAdapter(adapter);

    }
}
