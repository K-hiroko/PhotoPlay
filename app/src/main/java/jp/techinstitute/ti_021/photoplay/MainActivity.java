package jp.techinstitute.ti_021.photoplay;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    public final static int ROW_HEIGHT = 200;

    public final static int COLUMN_WIDTH = 200;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // グリッドビューを取得する。
        mGridView = (GridView) findViewById(R.id.grid_current_photo);

        // WindowManagerを取得する。
        // TODO:getWidthに取り消し線が…。
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int intScreenWidth = display.getWidth();

        // カラム数をGridViewにセットする。
        int intColumns = (int) Math.floor(intScreenWidth / COLUMN_WIDTH);
        mGridView.setNumColumns(intColumns);


        PhotoGalleyAdapter adapter = new PhotoGalleyAdapter(MainActivity.this);

        if (adapter.getCount() <= 0) {
            Toast.makeText(this, "写真が一枚もありません", Toast.LENGTH_SHORT);
            finish();
        }

        // グリッドビューにデータをセットする。
        mGridView.setAdapter(adapter);

        // ウインドウタイトルに画像枚数を表示
        setTitle(getTitle() + String.valueOf(adapter.getCount()));

        // グリッドをクリックしたら、対象画像の詳細ページに遷移する。
        // TODO;未実装

    }

    private class PhotoGalleyAdapter extends BaseAdapter {

        // ContentResolverから取得し画像情報
        private ArrayList<Long> mPhotoId = new ArrayList<Long>();
        private ArrayList<String> mPhotoTitle = new ArrayList<String>();

        private Context mContext = null;
        private ContentResolver mResolver = null;

        public PhotoGalleyAdapter(Context context) {
            mContext = context;
            mResolver = getContentResolver();
            Cursor cursor = mResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));

                mPhotoId.add(id);
                mPhotoTitle.add(name);
            }
            cursor.close();
        }

        @Override
        public int getCount() {
            return mPhotoId.size();
        }

        @Override
        public Object getItem(int position) {
            return mPhotoTitle.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mPhotoId.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
            } else {
                imageView = (ImageView) convertView;
            }

            // サムネイルのビットマップを取得する。
            Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(mResolver, mPhotoId.get(position), MediaStore.Images.Thumbnails.MICRO_KIND, new BitmapFactory.Options());

            // デバックコード エラーが発生するのはID:909
            //Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(mResolver, 909, MediaStore.Images.Thumbnails.MICRO_KIND, new BitmapFactory.Options());

            // ビットマップの画像の大きさを揃える。
//           Log.e("情報出力","Id内容："+mPhotoId.get(position));
//           Log.e("情報出力","Title内容："+mPhotoTitle.get(position));
            //Log.e("情報出力","Bitmap内容："+bitmap.toString());
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            }
            Bitmap reSizedBitmap = Bitmap.createScaledBitmap(bitmap, COLUMN_WIDTH, ROW_HEIGHT, true);
            imageView.setImageBitmap(reSizedBitmap);

            return imageView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
