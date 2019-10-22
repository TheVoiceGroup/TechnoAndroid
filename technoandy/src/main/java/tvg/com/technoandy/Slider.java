package tvg.com.technoandy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Slider extends PagerAdapter {

    private Context context;
    private ArrayList<String> data;
    private  int layout, image_res;
    private boolean StringList, StringLayoutList;

    public Slider(Context context, ArrayList<String> data){
        this.context = context;
        this.data = data;
        StringList = true;
        StringLayoutList = false;
    }

    public Slider(Context context, ArrayList<String> data, int layout, int imageres){
        this.context = context;
        this.data = data;
        this.layout = layout;
        this.image_res = imageres;
        StringList = false;
        StringLayoutList = true;
    }

    @Override
    public int getCount() {
        if (StringList){
            return data.size();
        }else if (StringLayoutList){
            return data.size();
        }else {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (StringList){
            View view = LayoutInflater.from(context).inflate(R.layout.layout_slider, null);
            ImageView imageView = view.findViewById(R.id.img_slider);
            Picasso.get().load(data.get(position)).into(imageView);
            container.addView(view);
            return view;
        }else if (StringLayoutList){
            View view = LayoutInflater.from(context).inflate(layout, null);
            ImageView imageView = view.findViewById(image_res);
            Picasso.get().load(data.get(position)).into(imageView);
            container.addView(view);
            return view;
        }else {
            return false;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
    }
}
