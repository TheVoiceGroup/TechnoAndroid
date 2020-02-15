package tvg.com.technoandy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
    public ImageResult imageResult;
    private ArrayList<String> imagesPathList;
    private Uri outPutfileUri;

    public ImagePicker(Activity activity){
        this.activity = activity;
    }

    public void SelectImageFromGallery(String secondaryColor, String primaryColor, String textColor, int maxvalue, int minvalue){
        Intent intent = new Intent(activity,GalleryPicker.class);
        intent.putExtra("SECONDARY", secondaryColor);
        intent.putExtra("PRIMARY", primaryColor);
        intent.putExtra("TEXT", textColor);
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
        if(RequestCode == GALLERY_REQUEST && intent!=null){
            ArrayList<Bitmap> bitmaps = new ArrayList<>();
            String[] imagesPath = intent.getStringExtra("data").split("\\|");
            ArrayList<String> images = new ArrayList<>();
            Bitmap bitmap = null;
            for (int i=0;i<imagesPath.length;i++){
                images.add(imagesPath[i]);
                bitmap = BitmapFactory.decodeFile(imagesPath[i]);
                try {
                    bitmaps.add(modifyOrientation(bitmap, imagesPath[i]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            imageResult.onImageResult(images, null, null, bitmaps);

        } else if (RequestCode == CAMERA_REQUEST){
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

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
