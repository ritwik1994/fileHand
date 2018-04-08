package com.example.root.recyclertutorial;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private RecyclerView mRecyclerView;
    private SubActivityAdapter mAdapter;
    private WebView wv;
    String pdfFileName;
    Uri uri;
    EditText datePick;
    private int ViewSize = 0;
    private final static int FILE_SELECT_CODE = 1;
    final int duration = 50;
    private ActionBar actionBar;
    final int pixelsToMove = 200;
    String filename;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private RecyclerView.LayoutManager mLayoutManager;
    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            mRecyclerView.smoothScrollBy(pixelsToMove, 0);
            mHandler.postDelayed(this, duration);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        View mActionBarView = getLayoutInflater().inflate(R.layout.main_activity_action_bar, null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#339FD9")));
        actionBar.setElevation(0);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mRecyclerView = (RecyclerView) findViewById(R.id.hrlist_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
//                LinearLayoutManager.HORIZONTAL, false));
        datePick = (EditText)findViewById(R.id.defaultTime);
        datePick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate=Calendar.getInstance();
                int mYear, mMonth, mDay;
                mYear=mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();  }
        });
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(mRecyclerView);





        mAdapter = new SubActivityAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);

        TextView button = (TextView) findViewById(R.id.uploadFromMobile);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //intent.putExtra("browseCoa", itemToBrowse);
                //Intent chooser = Intent.createChooser(intent, "Select a File to Upload");
                try {
                    //startActivityForResult(chooser, FILE_SELECT_CODE);
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),FILE_SELECT_CODE);
                } catch (Exception ex) {
                    System.out.println("browseClick :"+ex);//android.content.ActivityNotFoundException ex
                }
//                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//                chooseFile.setType("*/*");
//                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
//                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });
//
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, 200, dy);
//                int lastItem = layoutManager.findLastCompletelyVisibleItemPosition();
//                if(lastItem == layoutManager
//                        .getItemCount()-1){
//                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
//                    Handler postHandler = new Handler();
//                    postHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mRecyclerView.setAdapter(null);
//                            mRecyclerView.setAdapter(mAdapter);
//                            mHandler.postDelayed(SCROLLING_RUNNABLE, 50000);
//                        }
//                    }, 50000);
//                }
//            }
//        });
//        mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);

        //Layout manager for the Recycler View
//        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                        String mimeType = getContentResolver().getType(uri);
                        if (mimeType == null) {
                            String path = getPath(this, uri);
                            if (path == null) {
                                filename = FilenameUtils.getName(uri.toString());
                            } else {
                                File file = new File(path);
                                filename = file.getName();
                            }
                        } else {
                            Uri returnUri = data.getData();
                            Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                            returnCursor.moveToFirst();
                            filename = returnCursor.getString(nameIndex);
                            String size = Long.toString(returnCursor.getLong(sizeIndex));
                        }
                        File fileSave = getExternalFilesDir(null);
                        String sourcePath = getExternalFilesDir(null).toString();
                        try {
                            copyFileStream(new File(sourcePath + "/" + filename), uri,this);
                            String parsedText="";
                            PdfReader reader = new PdfReader(sourcePath + "/" + filename);
                            int n = reader.getNumberOfPages();
                            for (int i = 0; i <n ; i++) {
                                parsedText   = parsedText+ PdfTextExtractor.getTextFromPage(reader, i+1).trim()+"\n"; //Extracting the content from the different pages
                            }
                            System.out.println(parsedText);
                            reader.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void copyFileStream(File dest, Uri uri, Context context)
            throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICKFILE_RESULT_CODE) {
////            uri = data.getData();
////            pdfFileName = getFileName(uri);
////            System.out.println(pdfFileName);
////            uri = data.getData();
////            pdfFileName = getFileName(uri);
////            System.out.println(pdfFileName);
//
//            try
//            {
//                Uri uri = data.getData();
//                pdfFileName = getFileName(uri);
//                String src = uri.getPath();
//                File source = new File(src);
////                File file = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/ritwikResume.pdf");
//                RandomAccessFile f = new RandomAccessFile(source, "r");
//                byte[] data1 = new byte[(int)f.length()];
//                f.readFully(data1);
////                pdfLoadImages(data1);
//            }
//            catch(Exception ignored)
//            {
//            }
////            String filename = uri.getLastPathSegment();
////            File destination = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/CustomFolder/" + filename);
////            copy(source, destination);
////            try
////            {
////                File file = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/ritwikResume.pdf");
////                RandomAccessFile f = new RandomAccessFile(file, "r");
////                byte[] data1 = new byte[(int)f.length()];
////                f.readFully(data1);
////                pdfLoadImages(data1);
////            }
////            catch(Exception ignored)
////            {
////            }
////            System.out.println(filePath);
//        }
//    }
//
//    public String getFileName(Uri uri) {
//        String result = null;
//        if (uri.getScheme().equals("content")) {
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            try {
//                if (cursor != null && cursor.moveToFirst()) {
//                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                }
//            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }
//            }
//        }
//        if (result == null) {
//            result = uri.getLastPathSegment();
//        }
//        return result;
//    }
//
//    private void copy(File source, File destination) {
//        try {
//            FileChannel in = new FileInputStream(source).getChannel();
//            FileChannel out = new FileOutputStream(destination).getChannel();
//
//            try {
//                in.transferTo(0, in.size(), out);
//            } catch (Exception f) {
//                // post to log
//            } finally {
//                if (in != null)
//                    in.close();
//                if (out != null)
//                    out.close();
//            }
//        }catch(Exception e){
//
//        }
//    }

    public static String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static List<SubActivityData> getData() {
        List<SubActivityData> subActivityData = new ArrayList<>();
        String[] cardTitle = {
                "Help the HR community by reporting defaultors",
                "Search for defaultors before making \n" +
                        "the hiring decision",
                "Join the conversation",
        };
        Integer[] cardImages = {
                R.drawable.ic_people_white_24px,
                R.drawable.ic_search_white_24px,
                R.drawable.ic_chat_white_24px
        };
        for (int i = 0; i < cardTitle.length; i++) {
            SubActivityData current = new SubActivityData();
            current.cardTitle = cardTitle[i];
            current.cardImage = cardImages[i];
            subActivityData.add(current);
        }

        return subActivityData;
    }
}
