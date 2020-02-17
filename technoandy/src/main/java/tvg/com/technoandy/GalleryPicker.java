package tvg.com.technoandy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryPicker extends AppCompatActivity {

    private RecyclerView grdImages;
    private Button btnSelect, btnCancel;
    private ImageAdapter imageAdapter;
    private String[] arrPath;
    private boolean[] thumbnailsselection;
    private int ids[];
    private int count, max, min, size = 0;
    private TextView txt_images_count;
    private String colorPrimary, colorSecondary, colorText;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_picker);

        Intent intent = getIntent();
        colorSecondary = intent.getStringExtra("SECONDARY");
        colorPrimary = intent.getStringExtra("PRIMARY");
        colorText = intent.getStringExtra("TEXT");
        max = intent.getIntExtra("MAX", 1);
        min = intent.getIntExtra("MIN", 1);

        grdImages = findViewById(R.id.grdImages);
        grdImages.setLayoutManager(new GridLayoutManager(this, 3));
        grdImages.setHasFixedSize(false);
        grdImages.getRecycledViewPool().setMaxRecycledViews(0, 0);
        btnSelect = findViewById(R.id.btnSelect);
        btnCancel = findViewById(R.id.btnCancel);
        txt_images_count = findViewById(R.id.txt_images_count);

        btnSelect.setBackgroundColor(Color.parseColor(colorPrimary));
        btnCancel.setBackgroundColor(Color.parseColor(colorPrimary));
        btnSelect.setTextColor(Color.parseColor(colorText));
        btnCancel.setTextColor(Color.parseColor(colorText));
        txt_images_count.setBackgroundColor(Color.parseColor(colorPrimary));
        txt_images_count.setTextColor(Color.parseColor(colorText));

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;

        Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");
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
            Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(getApplicationContext().getContentResolver(), ids[i], MediaStore.Images.Thumbnails.MICRO_KIND, null);
            bitmaps.add(bitmap);
        }

        imageAdapter = new ImageAdapter(bitmaps);
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


    @Override
    protected void onPause() {
        super.onPause();
        super.onBackPressed();
    }

    /**
     * List adapter
     * @author tasol
     */

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        private LayoutInflater mInflater;
        private ArrayList<Bitmap> bitmaps;

        public ImageAdapter(ArrayList<Bitmap> bitmaps) {
            this.bitmaps = bitmaps;
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            setCheckBoxColor(holder.chkImage, Color.parseColor(colorSecondary), Color.parseColor(colorPrimary));
            holder.chkImage.setId(position);
            holder.imgThumb.setId(position);
            holder.chkImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsselection[id]) {
                        size--;
                        txt_images_count.setText(size + " Images Selected");
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        if (size>=max) {
                            Toast.makeText(GalleryPicker.this, "You've Reached maximum limit", Toast.LENGTH_SHORT).show();
                        }else {
                            size++;
                            txt_images_count.setText(size + " Images Selected");
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
                        txt_images_count.setText(size + " Images Selected");
                        holder.chkImage.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        if (size>=max) {
                            Toast.makeText(GalleryPicker.this, "You've Reached maximum limit", Toast.LENGTH_SHORT).show();
                        }else {
                            size++;
                            txt_images_count.setText(size + " Images Selected");
                            holder.chkImage.setChecked(true);
                            thumbnailsselection[id] = true;
                        }
                    }
                }
            });
            try {
                holder.imgThumb.setImageBitmap(bitmaps.get(position));
            } catch (Throwable e) {
            }
            holder.chkImage.setChecked(thumbnailsselection[position]);
        }

        @Override
        public int getItemCount() {
            return count;
        }

//        public View getView(int position, View convertView, ViewGroup parent) {
//            final ViewHolder holder;
////            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = mInflater.inflate(R.layout.gallery_item, parent, false);
//                holder.imgThumb = convertView.findViewById(R.id.imgThumb);
//                holder.chkImage = convertView.findViewById(R.id.chkImage);
//                setCheckBoxColor(holder.chkImage, Color.parseColor(colorSecondary), Color.parseColor(colorPrimary));
//                convertView.setTag(holder);
//            //}
//
//            return convertView;
//        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgThumb;
            AppCompatCheckBox chkImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imgThumb = itemView.findViewById(R.id.imgThumb);
                chkImage = itemView.findViewById(R.id.chkImage);
            }
        }
    }

    public  void setCheckBoxColor(AppCompatCheckBox checkBox, int uncheckedColor, int checkedColor) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][] {
                        new int[] { -android.R.attr.state_checked }, // unchecked
                        new int[] {  android.R.attr.state_checked }  // checked
                },
                new int[] {
                        uncheckedColor,
                        checkedColor
                }
        );
        checkBox.setSupportButtonTintList(colorStateList);
    }
}
