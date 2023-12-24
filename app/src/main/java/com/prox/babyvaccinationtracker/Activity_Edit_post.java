package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.model.Post;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;

public class Activity_Edit_post extends AppCompatActivity {
    ImageButton imageButtonClose_edit;
    EditText editTextPopupContent_edit,editTextHashtag_edit;
    RecyclerView recycleViewImage_edit;
    LinearLayout linearLayoutAddImage_edit;
    Button buttonEditPost_edt;

    ArrayList<String> image_old = new ArrayList<>();
    ArrayList<Uri> image_new = new ArrayList<>();
    ArrayList<Uri> image_all = new ArrayList<>();
    RecyclerAdapter adapter_image;

    int bounary_image_old_new;

    View loadingLayoutPost;
    boolean check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        Post post = (Post) getIntent().getSerializableExtra("Post");
        if(post == null){
            finish();
        }
        Log.i("POST_edit", post+"");

        imageButtonClose_edit = findViewById(R.id.imageButtonClose_edit);
        editTextPopupContent_edit = findViewById(R.id.editTextPopupContent_edit);
        editTextHashtag_edit = findViewById(R.id.editTextHashtag_edit);
        recycleViewImage_edit = findViewById(R.id.recycleViewImage_edit);
        linearLayoutAddImage_edit = findViewById(R.id.linearLayoutAddImage_edit);
        buttonEditPost_edt = findViewById(R.id.buttonEditPost_edt);
        loadingLayoutPost = findViewById(R.id.loadingLayoutPost);
        // load data
        editTextPopupContent_edit.setText(post.getContent());
        if(post.getHashtags()!= null){
            editTextHashtag_edit.setText(String.join(" ", post.getHashtags()));
        }
        if(post.getImage_url()!= null){
            image_old = new ArrayList<>(post.getImage_url());
            bounary_image_old_new = image_old.size();
            for(String a : post.getImage_url()){
                image_all.add(Uri.parse(a));
            }

            adapter_image = new RecyclerAdapter(image_all, Activity_Edit_post.this);
            recycleViewImage_edit.setLayoutManager
                    (new GridLayoutManager(Activity_Edit_post.this, 3));
            recycleViewImage_edit.setAdapter(adapter_image);
        }else {
            adapter_image = new RecyclerAdapter(image_all, Activity_Edit_post.this);
            recycleViewImage_edit.setLayoutManager
                    (new GridLayoutManager(Activity_Edit_post.this, 3));
            recycleViewImage_edit.setAdapter(adapter_image);
        }
        adapter_image.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Uri imageUri) {
                int position = image_all.indexOf(imageUri);
                if(position != -1){
                    if(position < bounary_image_old_new){
                        image_all.remove(position);
                        image_old.remove(position);
                        bounary_image_old_new--;
                        adapter_image.notifyDataSetChanged();
                    }
                    else {
                        image_all.remove(position);
                        image_new.remove(position);
                        adapter_image.notifyDataSetChanged();
                    }

                }
            }
        });

        // close
        imageButtonClose_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Thêm ảnh
        linearLayoutAddImage_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        // chỉnh sửa post
        buttonEditPost_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showConfirmDialog("Xác nhận cập nhật",
                        "Bạn có chắc muốn cập nhật thông tin vắc-xin không?",
                        new ConfirmationCallback() {
                    @Override
                    public void onConfirm(boolean result) {
                        if(result){
                            loadingLayoutPost.setVisibility(View.VISIBLE);
                            String content = editTextPopupContent_edit.getText().toString();
                            String hashtag = editTextHashtag_edit.getText().toString();

                            if(!content.isEmpty()){
                                if (hashtag.length() > 0) {
                                    if (!checkHashtag(hashtag)) {
                                        editTextHashtag_edit.setError("Hashtag không hợp lệ");
                                        loadingLayoutPost.setVisibility(View.GONE);
                                        return;
                                    }
                                }
                                int size = image_new.size();
                                String id = post.getPost_id();

                                DatabaseReference reference = FirebaseDatabase
                                        .getInstance().getReference("posts").child(id);

                                if(size > 0){
                                    ArrayList<String> Url_image = new ArrayList<>();

                                    for(int i = 0 ; i < size ; i++){
                                        MediaManager.get().upload(""+image_new.get(i))
                                                .callback(new UploadCallback() {
                                            @Override
                                            public void onStart(String requestId) {
                                                Log.i("upload image", "onStart: ");
                                            }

                                            @Override
                                            public void onProgress(String requestId,
                                                                   long bytes,
                                                                   long totalBytes) {
                                                Log.i("uploading image", "Uploading... ");
                                            }

                                            @Override
                                            public void onSuccess(String requestId,
                                                                  Map resultData) {
                                                String new_image = resultData.get("url").toString();
                                                Url_image.add(new_image);
                                                if(Url_image.size() == size){
                                                    ArrayList<String> new_image_firebase;
                                                    if(image_old.size() > 0){
                                                        new_image_firebase = merge_two_array
                                                                (image_old,Url_image);
                                                    }
                                                    else {
                                                        new_image_firebase = new ArrayList<>
                                                                (Url_image);
                                                    }
                                                    reference.addListenerForSingleValueEvent
                                                            (new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            snapshot.child("content").getRef()
                                                                    .setValue(content);

                                                            if(!hashtag.isEmpty()){
                                                                ArrayList<String> hashtagList
                                                                        = getHashtag(hashtag);
                                                                snapshot.child("hashtags").getRef()
                                                                        .setValue(hashtagList);
                                                            }

                                                            snapshot.child("image_url").getRef()
                                                                    .setValue(new_image_firebase)
                                                                    .addOnCompleteListener
                                                                            (new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete
                                                                                (@NonNull Task<Void> task)
                                                                        {
                                                                            Toast.makeText
                                                                                    (Activity_Edit_post.this,
                                                                                    "Cập nhập bài đăng thành công",
                                                                                    Toast.LENGTH_LONG);
                                                                            loadingLayoutPost.setVisibility(View.GONE);
                                                                            finish();
                                                                        }
                                                                    });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onError(String requestId, ErrorInfo error) {
                                                Log.i("upload image onError", "error " +
                                                        error.getDescription());
                                            }

                                            @Override
                                            public void onReschedule(String requestId, ErrorInfo error) {
                                                Log.i("upload image onReschedule", "Reshedule " +
                                                        error.getDescription());
                                            }
                                        }).dispatch();
                                    }
                                }else {
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            snapshot.child("content").getRef().setValue(content);

                                            if(!hashtag.isEmpty()){
                                                ArrayList<String> hashtagList = getHashtag(hashtag);
                                                snapshot.child("hashtags").getRef()
                                                        .setValue(hashtagList);
                                            }
                                            snapshot.child("image_url").getRef().setValue(image_old)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(Activity_Edit_post.this,
                                                            "Cập nhập bài đăng thành công",
                                                            Toast.LENGTH_LONG).show();
                                                    loadingLayoutPost.setVisibility(View.GONE);
                                                    finish();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }else {
                                loadingLayoutPost.setVisibility(View.GONE);
                                Toast.makeText(Activity_Edit_post.this,
                                        "Không được để trống nội dung", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });


            }
        });

    }
    public boolean isValidHashtag(String hashtag) {
        String hashtagRegex = "^#[\\p{L}\\p{N}_]+$";

        Pattern pattern = Pattern.compile(hashtagRegex);
        Matcher matcher = pattern.matcher(hashtag);

        return matcher.matches();
    }
    public boolean checkHashtag(String hashtag){
        String[] hashtags = hashtag.split(" ");
        for (String s : hashtags) {
            if (!isValidHashtag(s)) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<String> merge_two_array(ArrayList<String> image_old, ArrayList<String> image_new) {
        int size_old = image_old.size();
        int size_new = image_new.size();
        ArrayList<String> merge_image = new ArrayList<>();
        for (int i = 0; i < size_old; i++) {
            merge_image.add(image_old.get(i));
        }
        for (int i = 0; i < size_new; i++) {
            merge_image.add(image_new.get(i));
        }
        return merge_image;
    }

    private void showConfirmDialog(String title, String message, final ConfirmationCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Người dùng đồng ý
                        callback.onConfirm(true);
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Người dùng từ chối
                        callback.onConfirm(false);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface ConfirmationCallback {
        void onConfirm(boolean result);
    }

    public ArrayList<String> getHashtag(String content) {
        ArrayList<String> hashtag = new ArrayList<>();
        String[] words = content.split(" ");
        for (String word : words) {
            if (word.startsWith("#")) {
                hashtag.add(word);
            }
        }
        return hashtag;
    }

    // Kiểm tra quyền truy cập kho ảnh
    private static final int PERMISSION_CODE = 1;
    private static final int PICK_IMAGE = 1;

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission
                (this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            accessTheGallery();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessTheGallery();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Kho ảnh người dùng
    public void accessTheGallery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE);
    }

    // xử lý sự kiện chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri selectedImageUri = data.getData();
                String filePath = getRealPathFromUri(selectedImageUri, this);
                if (!image_all.contains(Uri.parse(filePath))) {
                    image_all.add(Uri.parse(filePath));
                    image_new.add(Uri.parse(filePath));
                } else {
                    Toast.makeText
                            (this,
                                    "Ảnh đã được chọn trước đó",
                                    Toast.LENGTH_SHORT).show();
                }
                adapter_image.notifyDataSetChanged();
            }
        }
    }

    // chuyển đường dẫn thành Uri
    private String getRealPathFromUri(Uri imageUri, Activity activity) {
        Cursor cursor = activity.getContentResolver()
                .query(imageUri, null, null, null, null);
        if (cursor == null) {
            return imageUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}