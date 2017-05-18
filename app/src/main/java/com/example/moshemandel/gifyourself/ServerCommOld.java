package com.example.moshemandel.gifyourself;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by moshemandel on 18/05/2017.
 */

public class ServerCommOld extends AsyncTask<String, Void, String> {
    Context mContext;
    View mRootView;
    private ProgressBar spinner;

    private static final String TAG = "ServerComm";

    public ServerCommOld(Context context) {
        mContext = context;
        View mRootView = ((Activity)mContext).getWindow().getDecorView().findViewById(android.R.id.content);
        spinner = (ProgressBar)mRootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

    }

    @Override
    protected String doInBackground(String... imgPath) {
        return execPost(imgPath[0]);
    }


    protected void onPostExecute(String result) {
        spinner.setVisibility(View.GONE);

        GifImageView gifImageView = (GifImageView) mRootView.findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.simpsons);
        gifImageView.setVisibility(View.VISIBLE);
    }

    private String execPost(String imagePath){
        // imagePath is path of new compressed image.
        String attachmentName = "filedata";
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

            DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    attachmentName + "\";filename=\"" +
                    attachmentFileName + "\"" + crlf);
//            request.writeBytes("Content-Disposition: form-data; name=\"" + attachmentName + "\"" + crlf);
            request.writeBytes("Content-Type: image/jpeg" + crlf);
            request.writeBytes(crlf);
            request.write(byteArray);
            request.writeBytes(crlf);
            /*request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"testingName\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(imagePath);
            request.writeBytes(crlf);*/
            request.writeBytes(twoHyphens + boundary + twoHyphens);
            request.flush();
            request.close();
            InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());
            Log.d("ServerComm", httpUrlConnection.getResponseMessage());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            nRead = 0;
            byte[] data = new byte[16384];
            while ((nRead = responseStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);

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

    }
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


}
