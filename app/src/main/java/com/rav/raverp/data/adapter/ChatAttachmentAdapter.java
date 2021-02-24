package com.rav.raverp.data.adapter;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.rav.raverp.R;
import com.rav.raverp.utils.ByteVolleyRequest;
import com.rav.raverp.utils.ViewUtils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatAttachmentAdapter extends RecyclerView.Adapter<ChatAttachmentAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> hashMapArrayList;
    String type;
    ProgressDialog mProgressDialog;

    URL url;


    public ChatAttachmentAdapter(Context context, ArrayList<HashMap<String, String>> hashMapArrayList, String type) {
        this.context = context;
        this.hashMapArrayList = hashMapArrayList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attachment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAttachment.setText("File" + (position + 1));
        //holder.tvAttachment.setText(hashMapArrayList.get(position).get("attachment"));

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Please wait, we are downloading your image file...");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase("chat")) {
                    //new GetImages("https://ravgroup.org/images/SupportAttachments/" + hashMapArrayList.get(position).get("attachment"), hashMapArrayList.get(position).get("attachment"), ".jpg");
                    new DownloadTask().execute(stringToURL("https://ravgroup.org/images/SupportAttachments/" + hashMapArrayList.get(position).get("attachment")));
                    // downloadByVolley("https://ravgroup.org/images/SupportAttachments/" + hashMapArrayList.get(position).get("attachment"), hashMapArrayList.get(position).get("attachment"));
                } else {
                    //  new GetImages("https://ravgroup.org/images/SupportDocs/" + hashMapArrayList.get(position).get("attachment"), hashMapArrayList.get(position).get("attachment"), ".jpg");
                    new DownloadTask().execute(stringToURL("https://ravgroup.org/images/SupportDocs/" + hashMapArrayList.get(position).get("attachment")));
                    // downloadByVolley("https://ravgroup.org/images/SupportDocs/" + hashMapArrayList.get(position).get("attachment"), hashMapArrayList.get(position).get("attachment"));
                }

            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public int getItemCount() {
        return hashMapArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llMain;
        TextView tvAttachment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llMain = itemView.findViewById(R.id.llMain);
            tvAttachment = itemView.findViewById(R.id.tvAttachment);
        }
    }

    private class DownloadTask extends AsyncTask<URL, Void, Bitmap> {
        protected void onPreExecute() {
            mProgressDialog.show();
        }

        protected Bitmap doInBackground(URL... urls) {

            URL url = urls[0];
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                return BitmapFactory.decodeStream(bufferedInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result) {
            // Hide the progress dialog
            mProgressDialog.dismiss();

            if (result != null) {
                Toast.makeText(context, "File Download Successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Notify user that an error occurred while downloading image
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        }

    }

    protected URL stringToURL(String fileurl) {
        try {
            url = new URL(fileurl);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl, imagename_;
        ProgressDialog progressDialog;
        private Bitmap bitmap;
        private FileOutputStream fos;
        String ext;

        private GetImages(String requestUrl, String imagename, String ext) {
            this.requestUrl = requestUrl;
            this.imagename_ = new Date().toString();
            this.ext = ext;
            progressDialog = new ProgressDialog(context,
                    ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Downloading...");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                saveToSdCard(requestUrl, imagename_, ext);
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            Toast.makeText(context, "File Downloaded", Toast.LENGTH_SHORT).show();

            // Log.d(TAG, "onPostExecute: " + imagename_);
            // Log.d(TAG, "onPostExecute: " + ext);
        }
    }

    public void ShowNoticification(String uri) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(uri); // set your audio path
        intent.setDataAndType(Uri.fromFile(file), "/");

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Download completed")
                .setContentText("See File")
                .setContentIntent(pIntent);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    public String saveToSdCard(String url, String filename, String ext) {

        String stored = null;

        File sdcard = Environment.getExternalStorageDirectory();

        File folder = new File(sdcard.getAbsoluteFile(), "RavBusiness");//the dot makes this directory hidden to the user
        folder.mkdir();
        File file = new File(folder.getAbsoluteFile(), filename + "." + ext);
        if (file.exists())
            return stored;

        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(file));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            return null; // swallow a 404
        } catch (IOException e) {
            return null; // swallow a 404
        }

        ShowNoticification(file.getAbsolutePath());
        return stored;
    }


    public boolean downloadFile(final String path) {
        try {
            URL url = new URL(path);

            URLConnection ucon = url.openConnection();
            ucon.setReadTimeout(5000);
            ucon.setConnectTimeout(10000);

            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);

            File file = new File(context.getDir("filesdir", Context.MODE_PRIVATE) + "png");

            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileOutputStream outStream = new FileOutputStream(file);
            byte[] buff = new byte[5 * 1024];

            int len;
            while ((len = inStream.read(buff)) != -1) {
                outStream.write(buff, 0, len);
            }

            outStream.flush();
            outStream.close();
            inStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void downloadByVolley(String url, String file_name) {
        ViewUtils.startProgressDialog(context);
        //our custom volley request
        ByteVolleyRequest volleyMultipartRequest = new ByteVolleyRequest(Request.Method.GET, url,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        ViewUtils.endProgressDialog();
                        try {
                            if (response != null) {
                                String state = "";
                                state = Environment.getExternalStorageState();
                                if (Environment.MEDIA_MOUNTED.equals(state)) {

                                    File direct = new File(Environment.getExternalStorageDirectory()
                                            + "/.private");
                                    if (!direct.exists()) {
                                        direct.mkdirs();
                                    }
                                    File myFile = new File(direct, file_name + ".jpg");
                                    FileOutputStream fstream = new FileOutputStream(myFile);
                                    fstream.write(response);
                                    fstream.close();

                                } else {
//                                    Toast.makeText(MainActivity.this, "External Storage Not Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ViewUtils.endProgressDialog();

//                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        //adding the request to volley
        Volley.newRequestQueue(context).add(volleyMultipartRequest);
    }


}
