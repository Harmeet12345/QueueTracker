//package qt.com.queuetracker.backgroundTask;
//
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.AsyncTask;
//import android.view.View;
//
///**
// * Created by vinove on 6/4/16.
// */
//public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//    String url;
//
//    @Override
//    protected Bitmap doInBackground(String... params) {
//        url = params[0];
//        return download_Image(url);
//    }
//
//    @Override
//    protected void onPostExecute(Bitmap result) {
//        try {
//            if (result != null) {
//                for (int i = 0; i < imageList.size(); i++) {
//                    if (url.equalsIgnoreCase(imageList.get(i))) {
//                        bitmap[i] = result;
//                    }
//                }
//            }
//
//            if (bitmap[0] != null && bitmap[1] != null && bitmap[2] != null) {
//                if (progressDia != null && progressDia.isShowing())
//                    progressDia.dismiss();
//                queue_rel.setVisibility(View.VISIBLE);
//                name.setText(nameResult);
//                age.setText(" | " + ageResult);
//                startCountDown();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    int counter = 0;
//
//
//            public void onFinish() {
//                try {
//                    imageDp.setVisibility(View.VISIBLE);
//                    threewordtext.setVisibility(View.VISIBLE);
//                    queue_rel.setVisibility(View.GONE);
//                    blur_rel.setVisibility(View.VISIBLE);
//                    Bitmap originalBitmap = ((BitmapDrawable) imageDp.getDrawable()).getBitmap();
//                    Bitmap blurredBitmap = BlurBuilder.blur(context, originalBitmap);
//                    imageDpBlur.setImageBitmap(blurredBitmap);
//                    if (haikuList.size() > 0) {
//                        onewordtext_blur.setText(haikuList.get(0));
//                        twowordtext_blur.setText(haikuList.get(1));
//                        threewordtext_blur.setText(haikuList.get(2));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();