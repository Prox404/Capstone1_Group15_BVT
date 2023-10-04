package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prox.babyvaccinationtracker.model.Customer;
import com.prox.babyvaccinationtracker.response.UploadImageResponse;
import com.prox.babyvaccinationtracker.service.api.ImageService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomerRegistration {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ImageService imageService;

    public CustomerRegistration() {
        // Khởi tạo Firebase Auth và Realtime Database
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("customers");

        // Khởi tạo Retrofit để tải hình ảnh lên imgbb
        Retrofit retrofit = RetrofitClient.getUploadImageClient();
        imageService = retrofit.create(ImageService.class);
    }

    public void registerCustomer(Customer customer, Bitmap avatar) {
        // Đăng ký người dùng vào Firebase Authentication
        Log.i("CustomerRegistration", customer.getCus_email() + " - " + customer.getCus_password());
//        uploadAvatar(avatar, "uaduiygaisdu");
        firebaseAuth.createUserWithEmailAndPassword(customer.getCus_email(), customer.getCus_password())
                .addOnCompleteListener(task -> {
                    Log.i("CustomerRegistration", "addOnCompleteListener");
                    if (task.isSuccessful()) {
                        Log.i("CustomerRegistration", "isSuccessful");
                        // Lấy thông tin người dùng đã đăng ký
                        String uid = task.getResult().getUser().getUid();
                        customer.setCus_password(null); // Xoá mật khẩu trước khi lưu vào Realtime Database

                        // Lưu thông tin khách hàng vào Realtime Database
                        databaseReference.child(uid).setValue(customer);

                        // Tải hình ảnh avatar lên imgbb và lưu đường dẫn vào Realtime Database
                        uploadAvatar(avatar, uid);
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // Đã tồn tại người dùng với cùng email, xử lý tùy ý
                            Log.i("CustomerRegistration", "Duplicated User");
                            // ...
                        } else {
                            // Đăng ký thất bại, xử lý tùy ý
                            // ...
                            Log.i("CustomerRegistration", "Failed Register User");

                        }
                    }
                });
    }

    private void uploadAvatar(Bitmap avatar, String uid) {
        // Convert Bitmap thành định dạng RequestBody
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        RequestBody requestBody = RequestBody.create( byteArray, MediaType.parse("image/*"));

//        String currentDate = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
//        File f = new File(context.getCacheDir(), "temp_" + currentDate);
//        try {
//            f.createNewFile();
//            FileOutputStream fos = new FileOutputStream(f);
//            fos.write(byteArray);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            // Xử lý ngoại lệ IOException ở đây, ví dụ: in ra thông báo lỗi
//            e.printStackTrace();
//        }

        // Tạo MultipartBody.Part cho hình ảnh avatar
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "avatar.png", requestBody);
//        RequestBody requestFile =  RequestBody.create( f, MediaType.parse("multipart/form-data"));
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", f.getName(), requestFile);
        // Gọi API để tải hình ảnh lên imgbb
        Call<UploadImageResponse> call = imageService.uploadImage(imagePart, "e88a4e866189b1d34716272538899d09");
        call.enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UploadImageResponse uploadImageResponse = response.body();
                    String avatarUrl = uploadImageResponse.getImageLink();

                    Log.i("Siuuuu", avatarUrl);
                    // Lưu đường dẫn hình ảnh avatar vào Realtime Database
                    databaseReference.child(uid).child("cus_avatar").setValue(avatarUrl);
                } else {
                    // Xử lý lỗi khi tải hình ảnh lên imgbb
                    // ...
                    Log.i("CustomerRegistration", "Failed Upload User Image 0");
                }
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                // Xử lý lỗi khi tải hình ảnh lên imgbb
                // ...
                Log.i("CustomerRegistration", "Failed Upload User Image");
            }
        });
    }
}