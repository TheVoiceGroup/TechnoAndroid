package tvg.com.technoandy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GalleryPicker extends AppCompatActivity {

    private GridView grdImages;
    private Button btnSelect, btnCancel;
    private ImageAdapter imageAdapter;
    private String[] arrPath;
    private boolean[] thumbnailsselection;
    private int ids[];
    private int count, colorSecondary, colorPrimary, colorText, max, min, size = 0;
    private TextView txt_images_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_picker);

        Intent intent = getIntent();
        colorSecondary = intent.getIntExtra("SECONDARY",android.R.color.black);
        colorPrimary = intent.getIntExtra("PRIMARY", android.R.color.black);
        colorText = intent.getIntExtra("TEXT", android.R.color.white);
        max = intent.getIntExtra("MAX", 1);
        min = intent.getIntExtra("MIN", 1);

        grdImages = findViewById(R.id.grdImages);
        btnSelect = findViewById(R.id.btnSelect);
        btnCancel = findViewById(R.id.btnCancel);
        txt_images_count = findViewById(R.id.txt_images_count);

        btnSelect.setBackgroundColor(colorPrimary);
        btnCancel.setBackgroundColor(colorPrimary);
        txt_images_count.setBackgroundColor(colorPrimary);
        txt_images_count.setTextColor(colorText);

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;

        Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
        this.count = imagecursor.getCount();
        this.arrPath = new String[this.count];
        ids = new int[count];
        this.thumbnailsselection = new boolean[this.count];
        for (int i = 0; i < this.count; i++) {
            imagecursor.moveToPosition(i);
            ids[i] = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            arrPath[i] = imagecursor.getString(dataColumnIndex);
        }

        imageAdapter = new ImageAdapter();
        grdImages.setAdapter(imageAdapter);
        imagecursor.close();

        btnSelect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final int len = thumbnailsselection.length;
                int cnt = 0;
                String selectImages = "";
                for (int i = 0; i < len; i++) {
                    if (thumbnailsselection[i]) {
                        cnt++;
                        selectImages = selectImages + arrPath[i] + "|";
                    }
                }
                if (cnt == 0) {
                    Toast.makeText(getApplicationContext(), "Please select at least one image", Toast.LENGTH_LONG).show();
                } else if (cnt >= 5) {
                    Toast.makeText(getApplicationContext(), "Please select images between" + min + " and " + max, Toast.LENGTH_LONG).show();
                }else {
                    Log.d("SelectedImages", selectImages);
                    Intent i = new Intent();
                    i.putExtra("data", selectImages);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                GalleryPicker.super.onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();

    }

    /**
     * Class method
     */

    /**
     * This method used to set bitmap.
     * @param iv represented ImageView
     * @param id represented id
     */

    private void setBitmap(final ImageView iv, final int id) {

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                return MediaStore.Images.Thumbnails.getThumbnail(getApplicationContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                iv.setImageBitmap(result);
            }
        }.execute();
    }


    /**
     * List adapter
     * @author tasol
     */

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.gallery_item, null);
                holder.imgThumb = convertView.findViewById(R.id.imgThumb);
                holder.chkImage = convertView.findViewById(R.id.chkImage);
                setCheckBoxColor(holder.chkImage, colorSecondary, colorPrimary);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.chkImage.setId(position);
            holder.imgThumb.setId(position);
            holder.chkImage.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsselection[id]) {
                        size--;
                        txt_images_count.setText(size + "Images Selected");
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        size++;
                        txt_images_count.setText(size + "Images Selected");
                        if (size>=max) {
                            Toast.makeText(GalleryPicker.this, "You've Reached maximum limit", Toast.LENGTH_SHORT).show();
                        }else {
                            cb.setChecked(true);
                            thumbnailsselection[id] = true;
                        }
                    }
                }
            });
            holder.imgThumb.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    int id = holder.chkImage.getId();
                    if (thumbnailsselection[id]) {
                        size--;
                        holder.chkImage.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        size++;
                        if (size>=max) {
                            Toast.makeText(GalleryPicker.this, "You've Reached maximum limit", Toast.LENGTH_SHORT).show();
                        }else {
                            holder.chkImage.setChecked(true);
                            thumbnailsselection[id] = true;
                        }
                    }
                }
            });
            try {
                setBitmap(holder.imgThumb, ids[position]);
            } catch (Throwable e) {
            }
            holder.chkImage.setChecked(thumbnailsselection[position]);
            holder.id = position;
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imgThumb;
        AppCompatCheckBox chkImage;
        int id;
    }

    public  void setCheckBoxColor(AppCompatCheckBox checkBox, int uncheckedColor, int checkedColor) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][] {
                        new int[] { -android.R.attr.state_checked }, // unchecked
                        new int[] {  android.R.attr.state_checked }  // checked
                },
                new int[] {
                        ContextCompat.getColor(this, uncheckedColor),
                        ContextCompat.getColor(this, checkedColor)
                }
        );
        checkBox.setSupportButtonTintList(colorStateList);
    }
}
