package com.example.moshemandel.gifyourself;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by moshemandel on 18/05/2017.
 */


class ServerComm extends AsyncTask<String, Void, String> {
    Context mContext;
    View mRootView;


    private ProgressBar spinner;

    private static final String TAG = "ServerComm";

    public ServerComm(Context context) {
        mContext = context;
        View mRootView = ((Activity) mContext).getWindow().getDecorView().findViewById(android.R.id.content);
        spinner = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

    }

    @Override
    protected String doInBackground(String... imgPath) {
        try {
            return execPost(imgPath[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    protected void onPostExecute(String result) {
        spinner.setVisibility(View.GONE);

        GifImageView gifImageView = (GifImageView) mRootView.findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.simpsons);
        gifImageView.setVisibility(View.VISIBLE);
    }

    private String execPost(String imagePath) throws IOException {
        final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");
        File origFile = new File(imagePath);
        String origFileSize = String.valueOf(origFile.length() / 1024);
//        String compressedImagePath = compressImage(imagePath);
        File inputFile = new File(imagePath);
        String newFileSize = String.valueOf(origFile.length() / 1024);
        Log.d("ServerComm", origFileSize + ", " + newFileSize);
        Request request = null;
        Response response = null;

//        final OkHttpClient client = new OkHttpClient();
        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1200, TimeUnit.SECONDS)
                .writeTimeout(1200, TimeUnit.SECONDS)
                .readTimeout(1200, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)

                .build();


        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "image.jpg",
                            RequestBody.create(MEDIA_TYPE_JPG, inputFile))
                    .build();

            request = new Request.Builder()
                    .url("http://132.65.120.155:8000")
                    .post(requestBody)
                    .build();

            response = client.newCall(request).execute();
            Log.d("ServerComm", response.toString());
            InputStream is = response.body().byteStream();
            BufferedInputStream input = new BufferedInputStream(is);
            OutputStream output = new FileOutputStream(createGifFile());
            byte[] data = new byte[1024];

            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

        } catch (Exception e) {
            response = client.newCall(request).execute();

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        }

//            InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());
//            Log.d("ServerComm", httpUrlConnection.getResponseMessage());
//            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//            nRead = 0;
//            byte[] data = new byte[16384];
//            while ((nRead = responseStream.read(data, 0, data.length)) != -1) {
//                buffer.write(data, 0, nRead);
//
//            }
//            buffer.flush();
////             Path file = Paths.get();
//
//            String newFilePath = getGIFpath();
//            Toast.makeText(mContext,newFilePath,
//                    Toast.LENGTH_SHORT).show();
//
//            FileOutputStream out = new FileOutputStream(newFilePath);
//            out.write(buffer.toByteArray());

        return response.toString();
    }

    private String compressImage(String path) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Bitmap b = BitmapFactory.decodeFile(path);
        Bitmap out = Bitmap.createScaledBitmap(b, 320, 480, false);
        File file = new File(dir, "resize.png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {
        }
        return file.getAbsolutePath();
    }


//        URL url;
//        HttpURLConnection urlConnection = null;
//        try {
//            url = new URL("http://45.58.49.173:8000");
//
//            urlConnection = (HttpURLConnection) url
//                    .openConnection();
//
////            InputStream in = urlConnection.getInputStream();
////
////            InputStreamReader isw = new InputStreamReader(in);
////
////            int data = isw.read();
////            while (data != -1) {
////                char current = (char) data;
////                data = isw.read();
////                System.out.print(current);
////            }
//            DataOutputStream dStream = new DataOutputStream(urlConnection.getOutputStream());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//        }
    // imagePath is path of new compressed image.
//        String attachmentName = "filedata";
//        String attachmentFileName = "my_img.jpg";
//        String serverIp = "45.58.49.173";
////        String serverIp = "127.0.0.1";
//        String serverPort = "8000";
//
//        String crlf = "\r\n";
//        String twoHyphens = "--";
//        String boundary =  "*****";
//        //Create connection
//        try{
//            HttpURLConnection httpUrlConnection = null;
//            URL url = new URL("http://" + serverIp +":" + serverPort);
//            httpUrlConnection = (HttpURLConnection) url.openConnection();
//            httpUrlConnection.setUseCaches(false);
//            httpUrlConnection.setDoOutput(true);
//            httpUrlConnection.setRequestMethod("POST");
//            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
//            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
//            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//            //Paths.get(attachmentFileName)
//            File inputFile = new File(imagePath);
//            InputStream fileStream = new BufferedInputStream(new FileInputStream(inputFile));
//            ByteArrayOutputStream fileBuffer = new ByteArrayOutputStream();
//            int nRead;
//            byte[] tempData = new byte[16384];
//            while ((nRead = fileStream.read(tempData, 0, tempData.length)) != -1) {
//                fileBuffer.write(tempData, 0, nRead);
//            }
//            byte[] byteArray = fileBuffer.toByteArray();
//            fileStream.close();
////             byte[] byteArray = Files.readAllBytes(path);
//
////             Bitmap bitmap = get your bit map;
////             byte[] byteArray = stream.toByteArray();
//
//            DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());
//            request.writeBytes(twoHyphens + boundary + crlf);
//            request.writeBytes("Content-Disposition: form-data; name=\"" +
//                    attachmentName + "\";filename=\"" +
//                    attachmentFileName + "\"" + crlf);
////            request.writeBytes("Content-Disposition: form-data; name=\"" + attachmentName + "\"" + crlf);
//            request.writeBytes("Content-Type: image/jpeg" + crlf);
//            request.writeBytes(crlf);
//            request.write(byteArray);
//            request.writeBytes(crlf);
//            /*request.writeBytes(crlf);
//            request.writeBytes(twoHyphens + boundary + crlf);
//            request.writeBytes("Content-Disposition: form-data; name=\"testingName\"" + crlf);
//            request.writeBytes(crlf);
//            request.writeBytes(imagePath);
//            request.writeBytes(crlf);*/
//            request.writeBytes(twoHyphens + boundary + twoHyphens);
//            request.flush();
//            request.close();
//            InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());
//            Log.d("ServerComm", httpUrlConnection.getResponseMessage());
//            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//            nRead = 0;
//            byte[] data = new byte[16384];
//            while ((nRead = responseStream.read(data, 0, data.length)) != -1) {
//                buffer.write(data, 0, nRead);
//
//            }
//            buffer.flush();
////             Path file = Paths.get();
//
//            String newFilePath = getGIFpath();
//            Toast.makeText(mContext,newFilePath,
//                    Toast.LENGTH_SHORT).show();
//
//            FileOutputStream out = new FileOutputStream(newFilePath);
//            out.write(buffer.toByteArray());
//            return newFilePath;
//
//        }
//        catch (Exception exception){
//            Log.e("Error", exception.getMessage());
//            return null;
//        }

//    }
/*    private String execPost(String imagePath){
        // imagePath is path of new compressed image.
        String attachmentName = "my_img";
        String attachmentFileName = "my_img.jpg";

        String serverIp = "45.58.49.173";
//        String serverIp = "127.0.0.1";
        String serverPort = "8000";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        //Create connection
        try{
            HttpURLConnection httpUrlConnection = null;
            URL url = new URL("http://" + serverIp +":" + serverPort);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            //Paths.get(attachmentFileName)
            File inputFile = new File(imagePath);
            InputStream fileStream = new BufferedInputStream(new FileInputStream(inputFile));
            ByteArrayOutputStream fileBuffer = new ByteArrayOutputStream();
            int nRead;
            byte[] tempData = new byte[16384];
            while ((nRead = fileStream.read(tempData, 0, tempData.length)) != -1) {
                fileBuffer.write(tempData, 0, nRead);
            }
            byte[] byteArray = fileBuffer.toByteArray();
            fileStream.close();
//             byte[] byteArray = Files.readAllBytes(path);

//             Bitmap bitmap = get your bit map;
//             byte[] byteArray = stream.toByteArray();

            *//*DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());
            request.writeBytes(twoHyphens + boundary + crlf);
//            request.writeBytes("Content-Disposition: form-data; name=\"" +
//                    attachmentName + "\";filename=\"" +
//                    attachmentFileName + "\"" + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" + attachmentName + "\";" + "filename=\"" + imagePath + "\"" + crlf);
            request.writeBytes("Content-Type: image/jpeg" + crlf);
            request.writeBytes(crlf);
            request.write(byteArray);
            *//**//*request.writeBytes(crlf);
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"testingName\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(imagePath);*//**//*
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens);
            request.flush();
            request.close();*//*

            InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            nRead = 0;
            byte[] data = new byte[16384];
            while ((nRead = responseStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
                String str = new String(data, "UTF-8");
                Log.d(TAG,str);
            }
            buffer.flush();
//             Path file = Paths.get();

            String newFilePath = getGIFpath();
            Toast.makeText(mContext,newFilePath,
                    Toast.LENGTH_SHORT).show();
            FileOutputStream out = new FileOutputStream(newFilePath);
            out.write(buffer.toByteArray());
            return newFilePath;
        }
        catch (Exception exception){
            Log.e("Error", exception.getMessage());
            return null;
        }

    }*/

    private String getGIFpath() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String gifFileName = "GIF_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                gifFileName,  /* prefix */
                ".gif",         /* suffix */
                storageDir      /* directory */
        );

        String gifPath = image.getAbsolutePath();
        return gifPath;
    }

    private File createGifFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "GIF_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".gif",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

}
