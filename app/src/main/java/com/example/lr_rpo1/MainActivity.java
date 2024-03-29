package com.example.lr_rpo1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lr_rpo1.databinding.ActivityMainBinding;


import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'lr_rpo1' library on application startup.
    static {
        System.loadLibrary("lr_rpo1");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // обработка результата
                        String pin = data.getStringExtra("pin");
                        Toast.makeText(MainActivity.this, pin, Toast.LENGTH_SHORT).show();
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int res = initRng();
        byte[] v = randomBytes(10);
        byte[] keys = "qwertyuiopasdfgh".getBytes();
        byte[] shifrArr = encrypt(keys, v);

        byte[] deshArr = decrypt(keys, shifrArr);
        //TextView tv = binding.sampleText;
        //tv.setText(stringFromJNI());

    }

    public static byte[] stringToHex(String s) {
        byte[] hex;
        try {
            hex = Hex.decodeHex(s.toCharArray());
        } catch (DecoderException ex) {
            hex = null;
        }
        return hex;
    }


    public void onButtonClick(View v) {
        Intent it = new Intent(this, PinpadActivity.class);
        //startActivity(it);
        mStartForResult.launch(it);
    }


    /**
     * A native method that is implemented by the 'lr_rpo1' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public static native int initRng();

    public static native byte[] randomBytes(int no);

    public static native byte[] encrypt(byte[] key, byte[] data);

    public static native byte[] decrypt(byte[] key, byte[] data);
}