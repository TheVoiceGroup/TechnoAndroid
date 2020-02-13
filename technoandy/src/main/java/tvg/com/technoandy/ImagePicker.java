package tvg.com.technoandy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ImagePicker implements ActivityResult {

    private Activity activity;
    public int GALLERY_REQUEST = 1011;
    public int CAMERA_REQUEST = 1055;
    private ImageResult imageResult;
    private ArrayList<String> imagesPathList;
    private Uri outPutfileUri;

    public ImagePicker(Activity activity){
        this.activity = activity;
    }

    public void SelectImageFromGallery(int uncheckedcolor, int checkedcolor, int maxvalue, int minvalue){
        Intent intent = new Intent(activity,GalleryPicker.class);
        intent.putExtra("SECONDARY", uncheckedcolor);
        intent.putExtra("PRIMARY", checkedcolor);
        intent.putExtra("MAX", maxvalue);
        intent.putExtra("MIN", minvalue);
        activity.startActivityForResult(intent,GALLERY_REQUEST);
    }

    public void CaptureImageFromCamera(){
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "tempphoto.jpg");
        outPutfileUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", createImageFile());
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        activity.startActivityForResult(intent, CAMERA_REQUEST);
    }

    private File createImageFile(){
        File imageFile = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }


    @Override
    public void onImageActivityResult(int RequestCode, int ResultCode, Intent intent) {
        if(RequestCode == 1){
            ArrayList<Bitmap> bitmaps = new ArrayList<>();
            String[] imagesPath = intent.getStringExtra("data").split("\\|");
            ArrayList<String> images = new ArrayList<>();
            Bitmap bitmap = null;
            for (int i=0;i<imagesPath.length;i++){
                images.add(imagesPath[i]);
                bitmap = BitmapFactory.decodeFile(imagesPath[i]);
                bitmaps.add(bitmap);
            }

            imageResult.onImageResult(images, null, null, bitmaps);

        } else if (RequestCode == 2){
            String uri = outPutfileUri.toString();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), outPutfileUri);
                imageResult.onImageResult(null, uri, bitmap, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface ImageResult{
        void onImageResult(ArrayList<String> imagesPathList, String ImagePath, Bitmap bitmap, ArrayList<Bitmap> bitmaps);
    }

}
