package fitness.buddy.comp231;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 6000;
    //variables
    Animation topAnim, botAnim;
    ImageView image;
    TextView logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_activity);

        //animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(this,R.anim.bot_animation);

        image = findViewById(R.id.imgs);
        logo = findViewById(R.id.textView);
        slogan = findViewById(R.id.textView2);

        image.setAnimation(topAnim);
        logo.setAnimation(botAnim);
        slogan.setAnimation(botAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}
