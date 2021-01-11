package py.com.personal.mimundo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloader {

    private static final int  MEGABYTE = 1024 * 1024;

    public static File downloadFile(String fileUrl, File directory){
        try {

            URL url = new URL(fileUrl);

            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestProperty ("Authorization", "LXu7vGmE2JbFgjjmr6xoDNTRiytVTRsPCRQUzZuNd3HfiLWy2IvcYEIs8usWxl5APHUh5yGuC");

            //urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            /*String NOMBRE_ARCHIVO = "test.pdf";
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/" + NOMBRE_ARCHIVO);*/

            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while((bufferLength = inputStream.read(buffer))>0 ){
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
            return directory;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("FileNotFoundException Abrir PDF");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.err.println("MalformedURLException Abrir PDF");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("IOException Abrir PDF");
        }

        return null;
    }

}
