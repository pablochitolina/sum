package br.com.droidgo.sum;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.droidgo.sum.bean.UserDataSingleton;
 
public class SplashActivity extends Activity {
 
    // Splash screen timer
    private static int LOGO_TIME_OUT = 800;

    
    private UserDataSingleton userData = UserDataSingleton.getInstance();
    

    ImageView imgLogo;
 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userData.setMostraMsg(Boolean.TRUE);
        userData.setInstrucao(getResources().getString(R.string.labelInstrucao));
        
        Typeface fonttype = Typeface.createFromAsset(getAssets(),"fonts/secret_love.ttf");
        userData.setFonttype(fonttype);
        
        
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        
       
        
        new Handler().postDelayed(new Runnable() {
        	 
            @Override
            public void run() {
            	Intent i;
            	i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            
        }, LOGO_TIME_OUT);
        
       
        
    }
}
