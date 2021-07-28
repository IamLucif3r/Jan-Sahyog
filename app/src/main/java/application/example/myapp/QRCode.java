package application.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Objects;

import androidmads.library.qrgenearator.QRGEncoder;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class QRCode extends AppCompatActivity {


    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;
    public final static int WIDTH = 500;

    FirebaseAuth fAuth;
    String userid;

    String TAG = "GenerateQrCode";
    EditText editText;
    ImageView qrimg;
    Button submit, scan;
    String input;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code);
        qrimg = (ImageView) findViewById(R.id.qrcode);
        scan = (Button) findViewById(R.id.button24);
        fAuth = FirebaseAuth.getInstance();
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), QRCodeScanner.class);
                startActivity(i);
            }
        });

    /*    submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input=editText.getText().toString().trim();
                input = Objects.requireNonNull(fAuth.getCurrentUser()).getUid().toString();

                if (input.length() > 0) {
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    assert manager != null;
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    ImageView imageView = (ImageView) findViewById(R.id.qrcode);
                    try {
                        Bitmap bitmap = encodeAsBitmap(input);
                        qrimg.setImageBitmap(bitmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

/*
                    qrgEncoder=new QRGEncoder(input,null, QRGContents.Type.TEXT,smallerDimension);
                    try{
                        bitmap=qrgEncoder.encodeAsBitmap(input);
                        qrimg.setImageBitmap(bitmap);

                    }catch (WriterException e){
                        Log.v(TAG,e.toString());
                    }
                } else {
                    editText.setError("Required");
                }
            }
        });*/

        input = Objects.requireNonNull(fAuth.getCurrentUser()).getUid().toString();

        if (input.length() > 0) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            assert manager != null;
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            ImageView imageView = (ImageView) findViewById(R.id.qrcode);
            try {
                Bitmap bitmap = encodeAsBitmap(input);
                qrimg.setImageBitmap(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }

/*
                    qrgEncoder=new QRGEncoder(input,null, QRGContents.Type.TEXT,smallerDimension);
                    try{
                        bitmap=qrgEncoder.encodeAsBitmap(input);
                        qrimg.setImageBitmap(bitmap);

                    }catch (WriterException e){
                        Log.v(TAG,e.toString());
                    }
                } else {
                    editText.setError("Required");
                }
    }

}
/*
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }


}
*/
        }}
            private Bitmap encodeAsBitmap(String str)  throws WriterException {   BitMatrix result;
                try {
                    result = new MultiFormatWriter().encode(str,
                            BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
                } catch (IllegalArgumentException iae) {
                    // Unsupported format
                    return null;
                }
                int w = result.getWidth();
                int h = result.getHeight();
                int[] pixels = new int[w * h];
                for (int y = 0; y < h; y++) {
                    int offset = y * w;
                    for (int x = 0; x < w; x++) {
                        pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                    }
                }
                Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
                return bitmap;
            }

      }