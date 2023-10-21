package com.prox.babyvaccinationtracker.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.VaccinationCertificate;
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public class AcceptRequestAdapter extends RecyclerView.Adapter<AcceptRequestAdapter.ViewHolder> {
    private Context context;
    private List<Vaccination_Registration> vaccinationRegistions;

    public AcceptRequestAdapter(Context context, List<Vaccination_Registration> vaccinationRegistions) {
        this.context = context;
        this.vaccinationRegistions = vaccinationRegistions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccination_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vaccination_Registration registration = vaccinationRegistions.get(position);
        Log.i("Aloo", "onBindViewHolder: " + registration.getBaby().getBaby_avatar());

        holder.babyNameTextView.setText(registration.getBaby().getBaby_name());
        holder.babyBirthdayTextView.setText(registration.getBaby().getBaby_birthday());
        holder.congenitalDiseaseTextView.setText(registration.getBaby().getBaby_congenital_disease());
        holder.vaccineNameTextView.setText(registration.getVaccine().getVaccine_name());
        holder.vaccineCenterNameTextView.setText(registration.getCenter().getCenter_name());
        String imageUrl = registration.getBaby().getBaby_avatar();
        //replace space with http to https
        imageUrl = imageUrl.replace("https", "http");
        Picasso.get().load(imageUrl)
                .error(R.drawable.ic_launcher_background)
                .into(holder.babyAvatarImageView);
    }

    @Override
    public int getItemCount() {
        return vaccinationRegistions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView babyNameTextView, babyBirthdayTextView, congenitalDiseaseTextView, vaccineNameTextView, vaccineCenterNameTextView;
        ImageView babyAvatarImageView;

        Button buttonAccept;
        Button buttonCancel;

        String QR_url = "";

        public ViewHolder(View itemView) {
            super(itemView);
            babyNameTextView = itemView.findViewById(R.id.babyNameTextView);
            babyAvatarImageView = itemView.findViewById(R.id.babyAvatarImageView);
            buttonAccept = itemView.findViewById(R.id.buttonAction);
            buttonCancel = itemView.findViewById(R.id.buttonAction2);
            babyBirthdayTextView = itemView.findViewById(R.id.babyBirthdayTextView);
            congenitalDiseaseTextView = itemView.findViewById(R.id.congenitalDiseaseTextView);
            vaccineNameTextView = itemView.findViewById(R.id.vaccineNameTextView);
            vaccineCenterNameTextView = itemView.findViewById(R.id.vaccineCenterNameTextView);

//            buttonAccept.setVisibility(View.GONE);

            buttonAccept.setText("Xác nhận đã tiêm chủng");

            buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Aloo", "onClick: " + getAdapterPosition());
                    // Update status & remove item from list
                    Vaccination_Registration vaccination_registration = vaccinationRegistions.get(getAdapterPosition());

                    DatabaseReference vaccinationCertificateReference = FirebaseDatabase.getInstance().getReference("Vaccination_Certificate");
                    String id = vaccination_registration.getRegister_id();
                    Bitmap b = generateQRCode(id);
                    uploadAvatar(b, id);
                    Log.i("Accept Request Image", "onClick: " + QR_url);
                    assert id != null;
                    VaccinationCertificate vaccinationCertificate = new VaccinationCertificate();
                    vaccinationCertificate.setBaby(vaccination_registration.getBaby());
                    vaccinationCertificate.setQr(QR_url);
                    vaccinationCertificate.setVaccine(vaccination_registration.getVaccine());
                    vaccinationCertificate.setCenter(vaccination_registration.getCenter());
                    vaccinationCertificateReference.child(id).setValue(vaccinationCertificate);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
                    databaseReference.child(vaccination_registration.getRegister_id()).child("status").setValue(3);
                    Log.i("Accept", "onClick: " + vaccinationRegistions.get(getAdapterPosition()).toString());
                    vaccinationRegistions.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
            // Initialize other views in your item layout here.
        }

        private void uploadAvatar(Bitmap bitmap, String id) {

            Uri uri = getImageUri(context, bitmap);
            MediaManager.get().upload(uri).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    Log.i("upload image", "onStart: ");
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    Log.i("upload image", "Uploading... ");
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    Log.i("upload image", "image URL: " + resultData.get("url").toString());
                    // save image url to firebase
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Certificate");
                    databaseReference.child(id).child("qr").setValue(resultData.get("url").toString());

                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    Log.i("upload image", "error " + error.getDescription());
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    Log.i("upload image", "Reshedule " + error.getDescription());
                }
            }).dispatch();
        }

        public Uri getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            Log.i("SomeImage", "getImageUri: " + path);
            return Uri.parse(path);
        }

        private Bitmap generateQRCode(String textToEncode) {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = qrCodeWriter.encode(textToEncode, BarcodeFormat.QR_CODE, 300, 300);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bitmap.setPixel(x, y, bitMatrix.get(x, y) ? ContextCompat.getColor(context, R.color.black) : ContextCompat.getColor(context, R.color.white));
                    }
                }

                return bitmap;

            } catch (WriterException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}