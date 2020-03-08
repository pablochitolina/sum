package br.com.droidgo.sum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore.Images;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import br.com.droidgo.sum.basegameutils.BaseGameUtils;
import br.com.droidgo.sum.bean.Coord;
import br.com.droidgo.sum.bean.UserDataSingleton;
import br.com.droidgo.sum.util.Constants;
import br.com.droidgo.sum.util.RateThisApp;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements;
import com.google.android.gms.games.achievement.Achievements.LoadAchievementsResult;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards.LoadPlayerScoreResult;
import com.google.android.gms.plus.Plus;

public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
View.OnClickListener {

	private static final int TEMPO_DELAY1 = 10;
	private static final int TEMPO_DELAY2 = 20;
	private static final int TEMPO_DELAY3 = 30;
	private static final int TEMPO_DELAY4 = 40;
	private static final int TEMPO_DELAY5 = 50;
	private static final int TEMPO_DELAY6 = 60;
	private static final long TEMPO_VIBRAR = 50;
	private int idFirstClick = 0;
	private Intent shareIntent;
	private LinearLayout linearLayoutDica;
	private RelativeLayout rl1, rl2, rl3, rl4, rl5;
	private TextView txt1, txt2, txt3, txt4, txt5;
	private TextView txtDicaInit;
	private ArrayList<TextView> listTxt;
	private Boolean firstTime = false;
	private int multTime = 1;
	private Button btnL1C1, btnL1C2, btnL1C3, btnL1C4; 
	private Button btnL2C1, btnL2C2, btnL2C3, btnL2C4; 
	private Button btnL3C1, btnL3C2, btnL3C3, btnL3C4; 
	private Button btnL4C1, btnL4C2, btnL4C3, btnL4C4; 
	private TextView txtTitPontos, txtPontos, txtTitMaxPontos, txtMaxPontos;
	private Button btnFirstClick, btnSecondClick;
	private Boolean fistClick = true;
	private HashMap<Integer, Coord> mapaCoord;
	private HashSet<Integer> listClicked;
	private ArrayList<Button> listButtons;
	private Typeface fonttype;
	private int tamButton = 0;
	private SharedPreferences sharedpreferences;
	private int dicas = 0;
	private int numVoltar = 5;
	private Stack<String> pilha;
	private int movimentos = 0;
	private InterstitialAd interstitial;
	private Boolean adLoaded = false;
	private Menu menuText;
	public static int REQUEST_ACHIEVEMENTS = 1;
	public static int REQUEST_LEADERBOARD = 2;
	private int sumAchievments = 0;
	private AdView adViewBanner;
	private static final String TAG = "MainActivity";
	private static final int RC_SIGN_IN = 9001;
	private GoogleApiClient mGoogleApiClient;
	private boolean mResolvingConnectionFailure = false;
	private boolean mSignInClicked = false;
	private boolean logarPlayGames = false;
	private boolean mAutoStartSignInFlow = true;
	private boolean perguntarNovamente = true;
	private boolean contemBanner = false;
	private boolean saveAchievments = false;
	private boolean saveLeaderboards = false;
    private UserDataSingleton userData = UserDataSingleton.getInstance();
    private boolean mostraMsgVideo = false;
    
    int build = 0 ;
	String version = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
        .addApi(Games.API).addScope(Games.SCOPE_GAMES)
        .build();
        
        setContentView(R.layout.activity_main);
        
        RateThisApp.onStart(this);
        // If the criteria is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);
        
    	ActionBar ab = getActionBar();
        ab.setTitle("");
        
        try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			build = pInfo.versionCode;
			version = pInfo.versionName;
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
		/*try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"br.com.pablodev.sum", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String sign = Base64
						.encodeToString(md.digest(), Base64.DEFAULT);
				Log.i("MY KEY HASH:", sign);
			}
		} catch (NameNotFoundException e) {
		} catch (NoSuchAlgorithmException e) {
		}*/
        
        adViewBanner = new AdView(this);
    	adViewBanner.setAdSize(AdSize.BANNER);
    	adViewBanner.setAdUnitId(Constants.MY_AD_UNIT_ID_BANNER);
        
        if(isNetworkAvailable() && Constants.ADS_MAIN_INTERSTITIAL){
        	
	        interstitial = new InterstitialAd(this);
	        interstitial.setAdUnitId(Constants.MY_AD_UNIT_ID);
	
	        AdRequest adRequest = new AdRequest.Builder().build();
	        /*String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
	        final String deviceId = md5(android_id).toUpperCase();
	        AdRequest adRequest = new AdRequest.Builder()
	        .addTestDevice(deviceId)
	        .build();*/
	        
	        interstitial.loadAd(adRequest);
	
	        interstitial.setAdListener(new AdListener() {
	        	
	            public void onAdClosed(){
	            	super.onAdClosed();
	            	
	            	/*if ((Constants.NUM_CLICKS - getNumClicks()) <= 0){
	            		Toast.makeText(MainActivity.this, getResources().getString(R.string.labelMensagemNaoExibida) + " " + Constants.NUM_HOURS + " " + getResources().getString(R.string.labelHoras), Toast.LENGTH_LONG).show();
	            	}
	            	saveClick();*/
	            	adLoaded = false;
	            	
	            	//refreshChamado();
	            	
	            	AdRequest adRequest = new AdRequest.Builder().build();
	            	
	            	/*String android_id = Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
			        final String deviceId = md5(android_id).toUpperCase();
			        AdRequest adRequest = new AdRequest.Builder()
			        .addTestDevice(deviceId)
			        .build();*/
			        
	                interstitial.loadAd(adRequest);
	            }
	            
	            public void onAdLoaded(){
	            	super.onAdLoaded();
	            	adLoaded = true;
	            }
	            public void onAdOpened(){
	            	super.onAdOpened();
	            	/*int click = getNumClicks();
	            	if(click < Constants.NUM_CLICKS){
	        			mostraMsg(click);
	        		}*/
	            	
	            }
	            
	            public void onAdFailedToLoad(int errorCode) {
	            	super.onAdFailedToLoad(errorCode);
	                String errorReason = "";
	                switch(errorCode) {
	                    case AdRequest.ERROR_CODE_INTERNAL_ERROR:
	                        errorReason = "Internal error";
	                        break;
	                    case AdRequest.ERROR_CODE_INVALID_REQUEST:
	                        errorReason = "Invalid request";
	                        break;
	                    case AdRequest.ERROR_CODE_NETWORK_ERROR:
	                        errorReason = "Network Error";
	                        break;
	                    case AdRequest.ERROR_CODE_NO_FILL:
	                        errorReason = "No fill";
	                        break;
	                }
	                Log.i("onAdFailedToLoad()", errorReason);
	                adLoaded = false;
	                AdRequest adRequest = new AdRequest.Builder().build();
	            	
	            	/*String android_id = Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
			        final String deviceId = md5(android_id).toUpperCase();
			        AdRequest adRequest = new AdRequest.Builder()
			        .addTestDevice(deviceId)
			        .build();*/
			        
	                interstitial.loadAd(adRequest);
	                
	            }
	        });
        }else{
        	adLoaded = false;
        }

        fonttype = Typeface.createFromAsset(getAssets(),"fonts/nexa_bold.otf");
        userData.setFonttype(fonttype);
        pilha = new Stack<String>();
        listButtons = new ArrayList<Button>();
        listClicked = new HashSet<Integer>();
        mapaCoord = new HashMap<Integer, Coord>();
        listTxt = new ArrayList<TextView>();
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        rl2 = (RelativeLayout) findViewById(R.id.rl2);
        rl3 = (RelativeLayout) findViewById(R.id.rl3);
        rl4 = (RelativeLayout) findViewById(R.id.rl4);
        rl5 = (RelativeLayout) findViewById(R.id.rl5);
        linearLayoutDica = (LinearLayout) findViewById(R.id.linearLayoutDica);
        linearLayoutDica.setVisibility(View.GONE);
        
        sharedpreferences = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        
        defineTamanho();
        
        initTextViews();
        
        initButtons();
        
        if (sharedpreferences.contains(Constants.FIRST_TIME)) {
        	carregaPrefs();
        }else{
        	firstTime();
        }
        
    }
    
    private void watchYoutubeVideo(String id){//6yvTIlSYeeE
        try{
             Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
             startActivity(intent);                 
             }catch (ActivityNotFoundException ex){
                 Intent intent=new Intent(Intent.ACTION_VIEW, 
                 Uri.parse("http://www.youtube.com/watch?v="+id));
                 startActivity(intent);
             }
    }
    
    private void firstTime(){
    	
    	mostraMsgVideo = true;
    	firstTime = true;
    	multTime = 30;
    	
    	Editor editor = sharedpreferences.edit();
		editor.putBoolean(Constants.FIRST_TIME, Boolean.FALSE);
		editor.commit();
		
    	txtDicaInit = (TextView) findViewById(R.id.txtDicaInit);
    	txtDicaInit.setTypeface(fonttype);
    	txtDicaInit.setTextSize(30);
    	txtDicaInit.setTextColor(Color.YELLOW);
    	txtDicaInit.setText(getResources().getString(R.string.labelTop1));
    	
    	linearLayoutDica.setVisibility(View.VISIBLE);
    	pilha = new Stack<String>();
    	numVoltar = 5;
    	    	
    	for(Button b : listButtons){
			mudaButton(b);
		}
    	
    	for(TextView tv : listTxt){
			mudaTextView(tv);
		}
    	
    	/*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	menuText.findItem(R.id.menuBackMenu).setTitle(getResources().getString(R.string.labelVoltar) +  "(" + pilha.size() + ")");
            	
            }
        }, 500);*/
    	
        
		mudaEsquemaCores();
		
    }

    private  void vibrar(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(TEMPO_VIBRAR);
    }

	private void definirCorTv(TextView tv){
		
		int key = Integer.parseInt(tv.getText().toString());
		
		int id = R.drawable.default_btn1048576;
		
		switch (key) {
		case 2:
			tv.setTextSize(sizeFont(tv.getText().toString())*3/4);
			tv.setTextColor(Color.GRAY);
			break;
		case 4:
			tv.setTextSize(sizeFont(tv.getText().toString())*3/4);
			tv.setTextColor(Color.GRAY);
			break;
		case 8:
			tv.setTextSize(sizeFont(tv.getText().toString())*3/4);
			tv.setTextColor(Color.GRAY);
			break;
		case 16:
			tv.setTextSize(sizeFont(tv.getText().toString())*3/4);
			tv.setTextColor(Color.GRAY);
			break;
		case 32:
			tv.setTextSize(sizeFont(tv.getText().toString())*3/4);
			tv.setTextColor(Color.GRAY);
			break;
		case 64:
			tv.setTextSize(sizeFont(tv.getText().toString())*3/4);
			tv.setTextColor(Constants.COR_64);
			vibrar();
			break;
		case 128:
			tv.setTextSize(sizeFont(tv.getText().toString())*3/4);
			tv.setTextColor(Constants.COR_128);
			vibrar();
			break;
		case 256:
			tv.setTextSize(sizeFont(tv.getText().toString())*3/4);
			tv.setTextColor(Constants.COR_256);
			vibrar();
			break;
		default:
			tv.setTextSize(7);
			break;
		}
		
	}
    
    protected void onStart() {
        super.onStart();
    	if(logarPlayGames){
    		saveLeaderboards = true;
    		mGoogleApiClient.connect();
    		
    	}
      }
    
    protected void onResume() {
        super.onResume();
    	if(logarPlayGames){
    		saveLeaderboards = true;
    		mGoogleApiClient.connect();
    	}
      }
    
    @Override
    public void onConnected(Bundle bundle) {
      Log.d(TAG, "onConnected() called. Sign in successful!");
      if(userData.getMostraMsg()){
    	  Toast.makeText(MainActivity.this, getResources().getString(R.string.labelConectadoPlayGames), Toast.LENGTH_SHORT).show();
    	  userData.setMostraMsg(Boolean.FALSE);
      }
      //resetAchievements();
      loginPlayGamesSuccess();
    }
    
    private void loginPlayGamesSuccess(){
    	
    	if(saveAchievments){
    		salvaAchievements();
    		saveAchievments = false;
    	}
    	if(saveLeaderboards){
    		salvaLeaderboards();
    		saveLeaderboards = false;
    	}
    	
    	logarPlayGames = true;
    	
    	new Handler().postDelayed(new Runnable() {
       	 
            @Override
            public void run() {
                
            	menuText.findItem(R.id.openGooglePlayGames).setIcon(getResources().getDrawable(R.drawable.logo_play_games_on));
            	//menuText.findItem(R.id.menuPlayGamesLogon).setVisible(Boolean.FALSE);
        		menuText.findItem(R.id.menuPlayGamesConquistas).setVisible(Boolean.TRUE);
        		menuText.findItem(R.id.menuPlayGamesRank).setVisible(Boolean.TRUE);
        		menuText.findItem(R.id.menuPlayGamesLogoff).setVisible(Boolean.TRUE);
 
            }
            
        }, 200);
    	
    	Games.Achievements.load(mGoogleApiClient, false).setResultCallback(new ResultCallback<Achievements.LoadAchievementsResult>() {
			
			@Override
			public void onResult(LoadAchievementsResult result) {
				 Achievement ach;
		            AchievementBuffer aBuffer = result.getAchievements();
		            Iterator<Achievement> aIterator = aBuffer.iterator();
		            
		    		int steps = 0;

		            while (aIterator.hasNext()) {
		                ach = aIterator.next();
		                if (ach.getState() == Achievement.STATE_UNLOCKED) {
		                	steps++;
		                }
		            }
		            aBuffer.close();
		            userData.setNumConquistas(steps);
		            
		           // if(steps == 0){
		            //	Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id1));
		        	//	userData.setNumConquistas(1);
		            //	mostraMsg();
		            //}
			}
		});
		
		Games.Leaderboards.loadCurrentPlayerLeaderboardScore(mGoogleApiClient,
	            getString(R.string.pairsum_leaderboard_id),
	            LeaderboardVariant.TIME_SPAN_ALL_TIME,
	            LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(
	            new ResultCallback<LoadPlayerScoreResult>() {

	                @Override
	                public void onResult(LoadPlayerScoreResult arg0) {
	                	if(arg0 != null){
		                    LeaderboardScore c = arg0.getScore();
		                    if(c != null){
			                    Long score = c.getRawScore();
			                    if(score > userData.getScore()){
			                    	userData.setScore(score);
			                    	txtMaxPontos.setText(userData.getScore()+"");
			                    }
		                    }
	                	}
	                }
	             });
		
		Editor editor = sharedpreferences.edit();
		editor.putBoolean(Constants.LOGAR_PLAYGAMES, logarPlayGames);
		editor.commit();
		
    }
    
    private void mostraMsg(){
    	
    	new AlertDialog.Builder(this)
		.setCancelable(Boolean.FALSE)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(getResources().getString(R.string.app_name))
        .setMessage(getResources().getString(R.string.labelDesbloqueou512) )
        .setPositiveButton(getResources().getString(R.string.labelSim), new DialogInterface.OnClickListener()
	        {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	refreshChamado();
	            }
	
	        })
	        .setNegativeButton(getResources().getString(R.string.labelNao), null)
	        .show();
    }
    
    private void loginPlayGamesError(){
    	
    	new Handler().postDelayed(new Runnable() {
          	 
            @Override
            public void run() {
                
            	menuText.findItem(R.id.openGooglePlayGames).setIcon(getResources().getDrawable(R.drawable.logo_play_games_off));
        		//menuText.findItem(R.id.menuPlayGamesLogon).setVisible(Boolean.TRUE);
        		menuText.findItem(R.id.menuPlayGamesConquistas).setVisible(Boolean.FALSE);
        		menuText.findItem(R.id.menuPlayGamesRank).setVisible(Boolean.FALSE);
        		menuText.findItem(R.id.menuPlayGamesLogoff).setVisible(Boolean.FALSE);
 
            }
            
        }, 200);
	
    }

    @Override
    public void onConnectionSuspended(int i) {
      Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
      loginPlayGamesError();
      mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
      Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);
      loginPlayGamesError();
      if (mResolvingConnectionFailure) {
        Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
        return;
      }

      if (mSignInClicked || mAutoStartSignInFlow) {
        mAutoStartSignInFlow = false;
        mSignInClicked = false;
        mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient,
            connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
      }
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
      if (requestCode == RC_SIGN_IN) {
        Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
            + responseCode + ", intent=" + intent);
        mSignInClicked = false;
        mResolvingConnectionFailure = false;
        if (responseCode == RESULT_OK) {
          mGoogleApiClient.connect();
        } else {
        	BaseGameUtils.showActivityResultError(this,
                    requestCode, responseCode, R.string.signin_failure);
        }
      }
    }
    
    
    private void createShareIntent() {
    	
    	String playStoreLink = "https://play.google.com/store/apps/details?id=" + getPackageName();
    	String yourShareText = String.format(getResources().getString(R.string.labelMensagem), userData.getScore()) + "\nDownload: " + playStoreLink;
    	
    	if(isExternalStorageWritable() && isExternalStorageReadable()){
	    	shareIntent = new Intent(android.content.Intent.ACTION_SEND);
	    	shareIntent.setType("image/jpeg");
	    	shareIntent.putExtra(Intent.EXTRA_SUBJECT,  getResources().getString(R.string.app_name));
	    	shareIntent.putExtra(Intent.EXTRA_TEXT, yourShareText);
	        Bitmap img = screenShot(findViewById(R.id.linearLayoutMain));
	        String pathofBmp = Images.Media.insertImage(getContentResolver(), img,getResources().getString(R.string.app_name), null);
	        if(pathofBmp != null){
	        	Uri bmpUri = Uri.parse(pathofBmp);
	        	shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri); // Optional, just if you wanna share an image.
	        }
	
	        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.labelShareWith)));
        }else{
        	shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain").setText(yourShareText).getIntent();
        	startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.labelShareWith)));
        }
    }
    
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        menuText = menu;
        
        MenuItem item = menu.findItem(R.id.menu_item_share);
        
        menu.findItem(R.id.menuPlayGamesLogon).setVisible(Boolean.FALSE);
        if(mGoogleApiClient.isConnected()){
        	menu.findItem(R.id.openGooglePlayGames).setIcon(getResources().getDrawable(R.drawable.logo_play_games_on));
        	//menu.findItem(R.id.menuPlayGamesLogon).setVisible(Boolean.FALSE);
        	menu.findItem(R.id.menuPlayGamesConquistas).setVisible(Boolean.TRUE);
        	menu.findItem(R.id.menuPlayGamesRank).setVisible(Boolean.TRUE);
        	menu.findItem(R.id.menuPlayGamesLogoff).setVisible(Boolean.TRUE);
        }else{
        	menu.findItem(R.id.openGooglePlayGames).setIcon(getResources().getDrawable(R.drawable.logo_play_games_off));
        	//menu.findItem(R.id.menuPlayGamesLogon).setVisible(Boolean.TRUE);
        	menu.findItem(R.id.menuPlayGamesConquistas).setVisible(Boolean.FALSE);
        	menu.findItem(R.id.menuPlayGamesRank).setVisible(Boolean.FALSE);
        	menu.findItem(R.id.menuPlayGamesLogoff).setVisible(Boolean.FALSE);
        }
        
        menu.findItem(R.id.menuBackMenu).setTitle(getResources().getString(R.string.labelVoltar) +  "(" + pilha.size() + ")");
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
      switch (item.getItemId()) {
		case R.id.menuBackMenu:
			back(null);
			break;
		case R.id.menuSobreMenu:
			sobre();
			break;
		case R.id.menu_item_share:
			createShareIntent();
			break;
		case R.id.menuMailBug:
			sendEmail("[Bug Report]");
		break;
		case R.id.menuMailSugestion:
			sendEmail("[Suggestions]");
		break;
		case R.id.menuBack:
			back(null);
			break;
		case R.id.menuNovo:
			refresh(null);
			break;
		case R.id.menuNovoMenu:
			refresh(null);
			break;
		case R.id.openGooglePlayGames:
			if(!mGoogleApiClient.isConnected())
				mGoogleApiClient.connect();
			break;
		case R.id.menuPlayGamesLogoff:
			logarPlayGames = false;
			if(mGoogleApiClient.isConnected()){
				mGoogleApiClient.disconnect();
			}
	    	
			menuText.findItem(R.id.openGooglePlayGames).setIcon(getResources().getDrawable(R.drawable.logo_play_games_off));
	    	//menuText.findItem(R.id.menuPlayGamesLogon).setVisible(Boolean.TRUE);
			menuText.findItem(R.id.menuPlayGamesConquistas).setVisible(Boolean.FALSE);
			menuText.findItem(R.id.menuPlayGamesRank).setVisible(Boolean.FALSE);
			menuText.findItem(R.id.menuPlayGamesLogoff).setVisible(Boolean.FALSE);
			
			Editor editor = sharedpreferences.edit();
			editor.putBoolean(Constants.LOGAR_PLAYGAMES, logarPlayGames);
			editor.commit();
			break;
			
		case R.id.menuPlayGamesConquistas:
			if(mGoogleApiClient.isConnected()){
				//salvaAchievements();
				startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
			}
			break;
		case R.id.menuPlayGamesRank:
			if(mGoogleApiClient.isConnected()){
				//salvaLeaderboards();
				startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient), REQUEST_LEADERBOARD);
			}
			break;
		case R.id.menuSairMenu:
			saveStatus();
			finish();
			break;
		case R.id.menuVideoMenu: 
			//startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=6yvTIlSYeeE")));
			String id = getResources().getString(R.string.youtube_id);
			watchYoutubeVideo(id);
			break;
		case R.id.menuAvaliar:
			String appPackage = getPackageName();
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackage));
			startActivity(intent);
		break;
		default:
			break;
		}

      return true;
    } 
    
    
    private void sobre(){
    	new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(getResources().getString(R.string.labelSobre))
        .setMessage(String.format(getResources().getString(R.string.labelSobreInfo),version,build) )
        .setPositiveButton(getResources().getString(R.string.labelOkay), null)
	    .show();
    }
    
    private void displayInterstitial(){
    	if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
    
    private void carregaPrefs(){
    	
		if (sharedpreferences.contains(Constants.MAX_PONTOS)) {
			userData.setScore(Long.parseLong(sharedpreferences.getString(Constants.MAX_PONTOS,"")));
			txtMaxPontos.setText(userData.getScore() + "");
        }else{
        	txtMaxPontos.setText("0");
        	userData.setScore(0L);
        }
		
		if (sharedpreferences.contains(Constants.STATUS)) {
			decodeStatus(sharedpreferences.getString(Constants.STATUS,""));
			//Log.i("status",sharedpreferences.getString(Constants.STATUS,""));
        }else{
        	embaralha();
        }
		
		if (sharedpreferences.contains(Constants.NUM_VOLTAR)) {
			numVoltar = sharedpreferences.getInt(Constants.NUM_VOLTAR,0);
			//Log.i("numVoltar",numVoltar + "");
		}
		
		if (sharedpreferences.contains(Constants.PILHA1)) {
			pilha.add(sharedpreferences.getString(Constants.PILHA1,""));
			//Log.i("PILHA1",sharedpreferences.getString(Constants.PILHA1,""));
		}
		if (sharedpreferences.contains(Constants.PILHA2)) {
			pilha.add(sharedpreferences.getString(Constants.PILHA2,""));
			//Log.i("PILHA2",sharedpreferences.getString(Constants.PILHA2,""));
		}
		if (sharedpreferences.contains(Constants.PILHA3)) {
			pilha.add(sharedpreferences.getString(Constants.PILHA3,""));
			//Log.i("PILHA3",sharedpreferences.getString(Constants.PILHA3,""));
		}
		if (sharedpreferences.contains(Constants.PILHA4)) {
			pilha.add(sharedpreferences.getString(Constants.PILHA4,""));
			//Log.i("PILHA4",sharedpreferences.getString(Constants.PILHA4,""));
		}
		if (sharedpreferences.contains(Constants.PILHA5)) {
			pilha.add(sharedpreferences.getString(Constants.PILHA5,""));
			//Log.i("PILHA5",sharedpreferences.getString(Constants.PILHA5,""));
		}
       /* if (sharedpreferences.contains(Constants.TICKTS)) {
            numTickts = sharedpreferences.getInt(Constants.TICKTS,0);
            txtTickts.setText(numTickts + "");
            //Log.i("PILHA4",sharedpreferences.getString(Constants.PILHA4,""));
        }*/
		
		if (sharedpreferences.contains(Constants.LOGAR_PLAYGAMES)) {
    		logarPlayGames = sharedpreferences.getBoolean(Constants.LOGAR_PLAYGAMES,Boolean.FALSE);
    		if(logarPlayGames){
    			mGoogleApiClient.connect();
    		}
        }else{
        	logarPlayGames = false;
        }
		
		if (sharedpreferences.contains(Constants.PERGUNTAR_NOVAMENTE)) {
    		perguntarNovamente = sharedpreferences.getBoolean(Constants.PERGUNTAR_NOVAMENTE,true);
        }else{
        	perguntarNovamente = true;
        	Editor editor = sharedpreferences.edit();
    		editor.putBoolean(Constants.PERGUNTAR_NOVAMENTE, perguntarNovamente);
    		editor.commit();
        }
		
		/*if(perguntarNovamente && !logarPlayGames){
			new AlertDialog.Builder(this)
			.setCancelable(Boolean.FALSE)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(getResources().getString(R.string.app_name))
	        .setMessage(getResources().getString(R.string.labelDesejaConectarAoGP) )
	        .setPositiveButton(getResources().getString(R.string.labelSim), new DialogInterface.OnClickListener()
		        {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	mGoogleApiClient.connect();
		            }
		
		        })
		        .setNeutralButton(getResources().getString(R.string.labelNunca), new DialogInterface.OnClickListener()
		        {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	perguntarNovamente = false;
		            	Editor editor = sharedpreferences.edit();
		        		editor.putBoolean(Constants.PERGUNTAR_NOVAMENTE, perguntarNovamente);
		        		editor.commit();
		            }
		
		        })
		        .setNegativeButton(getResources().getString(R.string.labelAgoraNao), null)
		        .show();
		}*/
		
		mudaCores();
		
    }
    
    private void mudaCores(){
    	
    	LinearLayout layoutMaxPontos = (LinearLayout) findViewById(R.id.layoutMaxPontos); //default_background_txt
    	LinearLayout layoutPontos = (LinearLayout) findViewById(R.id.layoutPontos); //default_background_txt
    	RelativeLayout linearLayoutMain = (RelativeLayout) findViewById(R.id.linearLayoutMain);
    	
    	layoutMaxPontos.setBackground(getResources().getDrawable(R.drawable.default_background_txt_1));
    	layoutPontos.setBackground(getResources().getDrawable(R.drawable.default_background_txt_1));
    	linearLayoutMain.setBackground(getResources().getDrawable(R.drawable.default_background));
    	
    	for(Button b : listButtons){
    		definirCor(b);
    	}
    }
    
    private int getCor(int maior){
    	
    	int cor;
    	
    	switch (maior) {
    	case 512:
			cor = Constants.COR_512;
			break;
		case 1024:
			cor = Constants.COR_1024;
			break;
		case 2048:
			cor = Constants.COR_2048;
			break;
		case 4096:
			cor = Constants.COR_4096;
			break;
		case 8192:
			cor = Constants.COR_8192;
			break;
		case 16384:
			cor = Constants.COR_16384;
			break;
		case 32768:
			cor = Constants.COR_32768;
			break;
		case 65536:
			cor = Constants.COR_65536;
			break;
		case 131072:
			cor = Constants.COR_131072;
			break;
		case 262144:
			cor = Constants.COR_262144;
			break;
		case 524288:
			cor = Constants.COR_524288;
			break;
		case 1048576:
			cor = Constants.COR_1048576;
			break;
		default:
			cor = Constants.COR_1048576;
			break;
		}
    	
    	return cor;
    }
    
    public void mudaEsquemaCores(){
    	
    	int maior = 1;
    	
    	for(Button b : listButtons){
    		if(Integer.parseInt(b.getText().toString()) > maior && 
    				Integer.parseInt(b.getText().toString()) >= 512){
    			maior = Integer.parseInt(b.getText().toString());
    		}
    	}
    	
    	LinearLayout layoutPontos = (LinearLayout) findViewById(R.id.layoutPontos);
    	LinearLayout layoutMaxPontos = (LinearLayout) findViewById(R.id.layoutMaxPontos);

    	int id = getResources().getIdentifier("default_background_txt_"+maior, "drawable", getApplicationContext().getPackageName());
    	layoutPontos.setBackground(getResources().getDrawable(id));
    	layoutMaxPontos.setBackground(getResources().getDrawable(id));

    }
    
    public void verifica(View v){
    	
    	if(fistClick){

    		if(firstTime){
    			txtDicaInit.setText(getResources().getString(R.string.labelTop2));
    			txtDicaInit.setTextSize(25);
    		}else{
    			linearLayoutDica.setVisibility(View.GONE);
    		}
    		
    		btnFirstClick = (Button)v;
    		fistClick = false;
    		
    		mostraDicas();
    		

    		
    	}else{
    		
    		btnSecondClick = (Button)v;
    		
    		if(clickValido()){
    			
	    		if(btnFirstClick.getText().toString().equals(btnSecondClick.getText().toString())){
	    			if(firstTime){
	        			txtDicaInit.setText(getResources().getString(R.string.labelDica3));
	        			txtDicaInit.setTextSize(15);
	        			firstTime = false;
                    }else{
                        if(mostraMsgVideo){
                            mostraMsgVideo = false;
                            new AlertDialog.Builder(this)
                                    .setCancelable(Boolean.TRUE)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle(getResources().getString(R.string.labelVideo))
                                    .setMessage(getResources().getString(R.string.labelVideoAssitir))
                                    .setPositiveButton(getResources().getString(R.string.labelSim), new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String id = getResources().getString(R.string.youtube_id);
                                            watchYoutubeVideo(id);
                                        }

                                    })
                                    .setNegativeButton(getResources().getString(R.string.labelAgoraNao), null)
                                    .show();

                        }

                    }
	    			if(numVoltar > 0){
	    				addPilha();
	    			}
	    			fistClick = true;
	    			//listClicked.add(btnFirstClick.getId());
	        		listClicked.add(btnSecondClick.getId());
	        		
	    			int sum = Integer.parseInt(btnFirstClick.getText().toString()) + Integer.parseInt(btnSecondClick.getText().toString());
	    			
	    			Boolean temMaior = false;
	    			
	    			for(Button b : listButtons){
	    				if(Integer.parseInt(b.getText().toString()) >= sum){
	    					temMaior = true;
	    				}
	    			}
	    			
	    			if(!temMaior){
	    				sumAchievments = sum;
	    				verificaConquista();
	    			}
	    			
	    			btnSecondClick.setText(sum+"");
	    			definirCor(btnSecondClick);
	    			btnFirstClick.setText(listTxt.get(0).getText().toString());
	    			btnFirstClick.setTextColor(Color.TRANSPARENT);
	    			btnFirstClick.setBackground(getResources().getDrawable(R.drawable.default_btnchange));
	    			
	    			idFirstClick = btnFirstClick.getId();
	    			for(Button b : listButtons){
	    				if(b.getId() != idFirstClick){
		    				definirCor(b);
		    			}
	    			}
	    			
	    			andaFila();
	    			
	    			embaralha();
	    			
	    			saveStatus();
	    			
	    		}else{
	    			for(Button b : listButtons){
	    				definirCor(b);
	    			}
	        		fistClick = true;
	    		}
	    		
    		}else{
    			for(Button b : listButtons){
    				definirCor(b);
    			}
        		fistClick = true;
    		}
    	}
    	 
    }
    
    private void andaFila(){
    	
    	final String str0 = listTxt.get(1).getText().toString();
    	final String str1 = listTxt.get(2).getText().toString();
    	final String str2 = listTxt.get(3).getText().toString();
    	final String str3 = listTxt.get(4).getText().toString();
    	
    	listTxt.get(4).setTextColor(Color.TRANSPARENT);
    	
    	rl5.setBackground(getResources().getDrawable(R.drawable.default_btnchange));
    	
    	new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	mudaTextView(listTxt.get(4));
            	rl5.setBackground(getResources().getDrawable(R.drawable.default_btninit));
            	rl4.setBackground(getResources().getDrawable(R.drawable.default_btnchange));
            	listTxt.get(3).setTextColor(Color.TRANSPARENT);
            	listTxt.get(3).setText(str3);
            }
        }, TEMPO_DELAY1 * multTime);
    	new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	definirCorTv(listTxt.get(3));
            	rl4.setBackground(getResources().getDrawable(R.drawable.default_btninit));
            	rl3.setBackground(getResources().getDrawable(R.drawable.default_btnchange));
            	listTxt.get(2).setText(str2);
            	listTxt.get(2).setTextColor(Color.TRANSPARENT);
            }
        }, TEMPO_DELAY2 * multTime);
    	new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	definirCorTv(listTxt.get(2));
            	rl3.setBackground(getResources().getDrawable(R.drawable.default_btninit));
            	rl2.setBackground(getResources().getDrawable(R.drawable.default_btnchange));
            	listTxt.get(1).setText(str1);
            	listTxt.get(1).setTextColor(Color.TRANSPARENT);
            }
        }, TEMPO_DELAY3 * multTime);
    	new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	definirCorTv(listTxt.get(1));
            	rl2.setBackground(getResources().getDrawable(R.drawable.default_btninit));
            	rl1.setBackground(getResources().getDrawable(R.drawable.default_btnchange));
            	listTxt.get(0).setText(str0);
            	listTxt.get(0).setTextColor(Color.TRANSPARENT);
            }
        }, TEMPO_DELAY4 * multTime);
    	
    	new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	rl1.setBackground(getResources().getDrawable(R.drawable.default_btninit));
            	definirCorTv(listTxt.get(0));
            }
        }, TEMPO_DELAY5 * multTime);
    	new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	definirCor(btnFirstClick);
            }
        }, TEMPO_DELAY6 * multTime);
    	
    	
    	multTime = 1;
    	
    }
    
    private void salvaLeaderboards(){
    	Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.pairsum_leaderboard_id), userData.getScore());
    }
    
    private void salvaAchievements(){
    	switch (sumAchievments) {
    	case 512:
    		Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id1));
    		userData.setNumConquistas(1);
			break;
		case 1024:
			Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id2));
			userData.setNumConquistas(2);
			break;
		case 2048:
			Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id3));
			userData.setNumConquistas(3);
			break;
		case 4096:
			Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id4));
			userData.setNumConquistas(4);
			break;
		case 8192:
			Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id5));
			userData.setNumConquistas(5);
			break;
		case 16384:
			Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id6));
			userData.setNumConquistas(6);
			break;
		case 32768:
			Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id7));
			userData.setNumConquistas(7);
			break;
		case 65536:
			Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id8));
			userData.setNumConquistas(8);
			break;
		case 131072:
			Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id9));
			userData.setNumConquistas(9);
			break;
		case 262144:
			Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id10));
			userData.setNumConquistas(10);
			break;
		case 524288:
			Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id11));
			userData.setNumConquistas(11);
			break;
		case 1048576:
			Games.Achievements.unlock(mGoogleApiClient, getString(R.string.pairsum_achievement_id12));
			userData.setNumConquistas(12);
			break;
		default:
			userData.setNumConquistas(0);
			break;
		}
    	
    }
    
    private void verificaConquista(){
    	
    	if(sumAchievments == 512
    			|| sumAchievments == 1024
    			|| sumAchievments == 2048
    			|| sumAchievments == 4096
    			|| sumAchievments == 8192
    			|| sumAchievments == 16384
    			|| sumAchievments == 32768
    			|| sumAchievments == 65536
    			|| sumAchievments == 131072
    			|| sumAchievments == 262144
    			|| sumAchievments == 524288
    			|| sumAchievments == 1048576){
	    	if(!mGoogleApiClient.isConnected() && perguntarNovamente){
	    		new AlertDialog.Builder(this)
	    		.setCancelable(Boolean.FALSE)
	            .setIcon(android.R.drawable.ic_dialog_alert)
	            .setTitle(getResources().getString(R.string.labelParabens))
	            .setMessage(String.format(getResources().getString(R.string.labelConectarConquista), sumAchievments) )
	            .setPositiveButton(getResources().getString(R.string.labelSim), new DialogInterface.OnClickListener()
		        {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	saveAchievments = true;
		            	mGoogleApiClient.connect();
		            }
		
		        })
		        .setNeutralButton(getResources().getString(R.string.labelNunca), new DialogInterface.OnClickListener()
		        {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	perguntarNovamente = false;
		            	Editor editor = sharedpreferences.edit();
		        		editor.putBoolean(Constants.PERGUNTAR_NOVAMENTE, perguntarNovamente);
		        		editor.commit();
		            }
		
		        })
		        .setNegativeButton(getResources().getString(R.string.labelAgoraNao), null)
			        .show();
	    	}else{
	    		if(mGoogleApiClient.isConnected()){
	    			salvaAchievements();
	    		}
	    	}
    	}
    		
    }
    
    protected void sendEmail(String subject) {

        String TO = getResources().getString(R.string.mail);
        
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",TO, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,  subject 
    		+ " V(" + version + ") B("+
    		build + ")");

        try {
           startActivity(Intent.createChooser(emailIntent, "Send mail..."));
           Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
           Toast.makeText(MainActivity.this, 
           "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
     }
    
    private void atualiza(Integer pontuacao){
    	
    	int mov = numMovimentos();
    	//txtMovimentos.setText(mov + "");
    	txtPontos.setText(pontuacao + "");
		//txtTitMovimentos.setTextColor(Color.WHITE);
		
    	/*if(mov == 2){
    		txtMovimentos.setTextColor(Color.RED);
    	}else{
    		txtMovimentos.setTextColor(Color.WHITE);
    	}*/
    	
    	if(Integer.parseInt(txtMaxPontos.getText().toString()) < pontuacao){
    		txtMaxPontos.setText(pontuacao+"");
    		userData.setScore(pontuacao.longValue());
    		
    		if(mGoogleApiClient.isConnected()){
    			salvaLeaderboards();
    		}
    		
    	}else{
    		userData.setScore(Long.parseLong(txtMaxPontos.getText().toString()));
    	}
    	
    	if(mov == 0){
    		//numVoltar = 0;
    		//pilha = new Stack<String>();
    		//listClicked = new HashSet<Integer>();
    		String negative = "Ok";
    		if(pilha.size() > 0){
    			negative = getResources().getString(R.string.labelVoltar) + " ("+pilha.size()+")";
    		}else{
    			negative = getResources().getString(R.string.labelVerJogo);
    		}
    		
    		new AlertDialog.Builder(this)
    		.setCancelable(Boolean.FALSE)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(getResources().getString(R.string.labelNenhumMovRestante))
            .setMessage(getResources().getString(R.string.labelPontuacao) + " " + pontuacao )
            .setPositiveButton(getResources().getString(R.string.labelRestart), new DialogInterface.OnClickListener()
		        {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	refreshChamado();
		            	if(adLoaded){
		            		displayInterstitial();
		            	}
		            }
		
		        })
		        .setNegativeButton(negative, new DialogInterface.OnClickListener()
		        {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	
		            	if(pilha.size() > 0){
		            		back(null);
		        		}else{
		        			addBanner();
		        		}
		            }
		        })
		        .show();
    		
    	}
    	
    }
    
    
    public void refresh(View v){
    	
    	new AlertDialog.Builder(this)
    	.setCancelable(Boolean.FALSE)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(getResources().getString(R.string.app_name))
        .setMessage(getResources().getString(R.string.labelReiniciarJogo))
        .setPositiveButton(getResources().getString(R.string.labelSim), new DialogInterface.OnClickListener()
	        {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            	refreshChamado();
	            	if(adLoaded){
	            		displayInterstitial();
	            	}
            	}
	        })
	        .setNegativeButton(getResources().getString(R.string.labelNao), null)
	        .show();
    }
    
    
    private void refreshChamado(){
    	
    	listClicked = new HashSet<Integer>();
    	pilha = new Stack<String>();
        menuText.findItem(R.id.menuBackMenu).setTitle(getResources().getString(R.string.labelVoltar) +  "(" + pilha.size() + ")");
    	numVoltar = 5;
    	if(contemBanner){
    		contemBanner = false;
    		LinearLayout banner = (LinearLayout) findViewById(R.id.ad_layout);
        	banner.removeView(adViewBanner);
    	}
    	
		for(Button b : listButtons){
			mudaButton(b);
		}
		
		for(TextView tv : listTxt){
			mudaTextView(tv);
		}
		
		if (mGoogleApiClient.isConnected()) {
			atualiza(loadAchievements(userData.getNumConquistas()));
        }else{
        	atualiza(0);
        }
		mudaEsquemaCores();
    }
    
    public void back(View v){
    	if(pilha.size() > 0){
    		decodeStatus(pilha.pop());
    		//btnBack.setText(getResources().getString(R.string.labelVoltar) + " (" + pilha.size() + ")");
    		menuText.findItem(R.id.menuBackMenu).setTitle(getResources().getString(R.string.labelVoltar) +  "(" + pilha.size() + ")");
    		numVoltar--;
    		saveStatus();
    		mudaEsquemaCores();
    	}
    	
    }
    
    private void addBanner(){
    	if(isNetworkAvailable() && Constants.ADS_SPLASH_BANNER){
    		
			LinearLayout banner = (LinearLayout) findViewById(R.id.ad_layout);
            
        	banner.addView(adViewBanner);
        	banner.bringToFront();
            
            final AdRequest.Builder adReq = new AdRequest.Builder();
            final AdRequest adRequestBanner = adReq.build();
            adViewBanner.loadAd(adRequestBanner);
            
            adViewBanner.setAdListener(new AdListener() {
	        	
	            
	            public void onAdLoaded(){
	            	super.onAdLoaded();
	            	Log.i("Banner", "loaded");
	            	contemBanner = true;
	            }
	            
	        });
		}
    }
    
    public int loadAchievements(int steps)  {

    	Random rd = new Random();
		int numero = rd.nextInt(listButtons.size());
		int pontos = 0;
		
		if(steps >= 1){
			listClicked.add(listButtons.get(numero).getId());
		}
		
    	switch (steps) {
		case 12:
			listButtons.get(numero).setText(10048576+"");
    		definirCor(listButtons.get(numero));
    		pontos = 1048576;
    		break;
		case 11:
    		listButtons.get(numero).setText(524288+"");
    		definirCor(listButtons.get(numero));
    		pontos = 524288;
    		break;
		case 10:
			listButtons.get(numero).setText(262144+"");
    		definirCor(listButtons.get(numero));
    		pontos = 262144;
    		break;
		case 9:
			listButtons.get(numero).setText(131072+"");
    		definirCor(listButtons.get(numero));
    		pontos = 131072;
    		break;
		case 8:
			listButtons.get(numero).setText(65536+"");
    		definirCor(listButtons.get(numero));
    		pontos = 65536;
    		break;
		case 7:
			listButtons.get(numero).setText(32768+"");
    		definirCor(listButtons.get(numero));
    		pontos = 32768;
    		break;
		case 6:
			listButtons.get(numero).setText(16384+"");
    		definirCor(listButtons.get(numero));
    		pontos = 16384;
    		break;
		case 5:
			listButtons.get(numero).setText(8192+"");
    		definirCor(listButtons.get(numero));
    		pontos = 8192;
    		break;
		case 4:
			listButtons.get(numero).setText(4096+"");
    		definirCor(listButtons.get(numero));
    		pontos = 4096;
    		break;
		case 3:
			listButtons.get(numero).setText(2048+"");
    		definirCor(listButtons.get(numero));
    		pontos = 2048;
    		break;
		case 2:
			listButtons.get(numero).setText(1024+"");
    		definirCor(listButtons.get(numero));
    		pontos = 1024;
    		break;
		case 1:
			listButtons.get(numero).setText(512+"");
    		definirCor(listButtons.get(numero));
    		pontos = 512;
    		break;

		default:
			break;
		}
    	
        return pontos;
     }
    
    private void saveStatus(){
    	
    	Editor editor = sharedpreferences.edit();
		editor.putString(Constants.MAX_PONTOS, txtMaxPontos.getText().toString());
		editor.putString(Constants.STATUS, getStatus());
		editor.putInt(Constants.NUM_VOLTAR, numVoltar);
		editor.putBoolean(Constants.LOGAR_PLAYGAMES, logarPlayGames);
        //editor.putInt(Constants.TICKTS, numTickts);
		
		userData.setScore(Long.parseLong(txtMaxPontos.getText().toString()));
		
		if(mGoogleApiClient.isConnected()){
			Games.Leaderboards.loadCurrentPlayerLeaderboardScore(mGoogleApiClient,
		            getString(R.string.pairsum_leaderboard_id),
		            LeaderboardVariant.TIME_SPAN_ALL_TIME,
		            LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(
		            new ResultCallback<LoadPlayerScoreResult>() {

		                @Override
		                public void onResult(LoadPlayerScoreResult arg0) {
		                	if(arg0 != null){
			                    LeaderboardScore c = arg0.getScore();
			                    if(c != null){
			                    	Long score = c.getRawScore();
				                    if(score > userData.getScore()){
				                    	userData.setScore(score);
				                    	txtMaxPontos.setText(userData.getScore()+"");
				                    }
			                    }
		                	}
		                }
		             });
		}
		
		editor.remove(Constants.PILHA1);
		editor.remove(Constants.PILHA2);
		editor.remove(Constants.PILHA3);
		editor.remove(Constants.PILHA4);
		editor.remove(Constants.PILHA5);
		
		switch (pilha.size()) {
		case 5:
			editor.putString(Constants.PILHA5, pilha.get(4));
		case 4:
			editor.putString(Constants.PILHA4, pilha.get(3));	
		case 3:
			editor.putString(Constants.PILHA3, pilha.get(2));
		case 2:
			editor.putString(Constants.PILHA2, pilha.get(1));
		case 1:
			editor.putString(Constants.PILHA1, pilha.get(0));
		default:
			break;
		}
		
		editor.commit();
		
    }
    
    private void addPilha(){
    	while(pilha.size() >= numVoltar){
    		pilha.remove(0);
    	}
    	pilha.push(getStatus());
    	//btnBack.setText(getResources().getString(R.string.labelVoltar) + " (" + pilha.size() + ")");
    	menuText.findItem(R.id.menuBackMenu).setTitle(getResources().getString(R.string.labelVoltar) +  "(" + pilha.size() + ")");
    }
    
    private void mudaTextView(TextView tv){
    	int exp = 2;
    	//int exp = 32;
    	Random rd = new Random();
		int numero = rd.nextInt(15) + 1;
		if      (numero == 1 || numero == 7 || numero == 12){
			exp = 2;
		}else if(numero == 2 || numero == 8 || numero == 13){
			exp = 4;
		}else if(numero == 3 || numero == 9 || numero == 14){
			exp = 8;
		}else if(numero == 4 || numero == 10 || numero == 15){
			exp = 16;
		}else if(numero == 5 || numero == 11){
			exp = 32;
		}else if(numero == 6){
			Random rdMaior = new Random();
			int maior = rdMaior.nextInt(8) + 1;
			if(maior == 1 || maior == 3 || maior == 5 || maior == 7 || maior == 8 ){
				exp = 64;
			}else if(maior == 2 || maior == 4){
				exp = 128;
			}else if(maior == 6){
				exp = 256;
			}
		}
		//listClicked.add(b.getId());
		tv.setText(exp + "");
		definirCorTv(tv);
    }
    
    private void decodeStatus(String status){
    	for(Button b : listButtons){
    		b.setText("2");
    	}
    	String[] fila = status.split("d");
    	
    	int pontuacao = 0;
    	//Log.i("status", status);
    	listClicked = new HashSet<Integer>();
    	String[] strButton = fila[0].split(" ");
		for(int j = 0 ; j < strButton.length; j++){
			String[] strStatus = strButton[j].split("-");
			if(strStatus.length > 1){
				listButtons.get(j).setText(strStatus[0]);
				listButtons.get(j).setHeight(tamButton);
				listButtons.get(j).setWidth(tamButton);
				listButtons.get(j).setTypeface(fonttype);
				if(strStatus[1].equals("t")){//foi clicado
					pontuacao += Integer.parseInt(strStatus[0]);
					listClicked.add(listButtons.get(j).getId());
				}
				definirCor(listButtons.get(j));
			}else{
				embaralha();
			}
			
		}
		if(fila[1] != null){
			String[] strDica = fila[1].split("-");
			for(int j = 0 ; j < strDica.length; j++){
				listTxt.get(j).setText(strDica[j]);
				definirCorTv(listTxt.get(j));
			}
		}else{
			for(TextView tv : listTxt){
				mudaTextView(tv);
			}
		}

		atualiza(pontuacao);
    }
    
    private void embaralha(){
    	int pontuacao = 0;
    	
    	for(Button b : listButtons){
    		if(listClicked.size() > 0){
    			if(!listClicked.contains(b.getId())){ // nao foi cliado
    				if(b.getId() != idFirstClick){
	    				mudaButton(b);
						b.setBackground(getResources().getDrawable(R.drawable.default_btninit));
    				}
    			}else{
    				if(b.getId() != idFirstClick){
	    				pontuacao += Integer.parseInt(b.getText().toString());
    				}
    			}
    		}else{
    			if(b.getId() != idFirstClick){
	    			mudaButton(b);
					b.setBackground(getResources().getDrawable(R.drawable.default_btninit));
    			}
    		}
    	}
    	
    	atualiza(pontuacao);
    }
    
  //int exp = 32;
    private int mudaButton(Button b){
    	int exp = 2;
    	Random rd = new Random();
		int numero = rd.nextInt(13)+1;
		if      (numero == 1 || numero == 6 || numero == 11){
			b.setText("2");
			exp = 2;
		}else if(numero == 2 || numero == 7 || numero == 12){
			b.setText("4");
			exp = 4;
		}else if(numero == 3 || numero == 8 || numero == 13){
			b.setText("8");
			exp = 8;
		}else if(numero == 4 || numero == 9){
			b.setText("16");
			exp = 16;
		}else if(numero == 5 || numero == 10){
			b.setText("32");
			exp = 32;
		}
		//listClicked.add(b.getId());
		//b.setText(exp + "");
		b.setHeight(tamButton);
		b.setWidth(tamButton);
		b.setTypeface(fonttype);
		definirCor(b);
	    //exp = exp * 2;
		
		return exp;
    }
    
    private String getStatus(){
    	String status = "";
    	
    	for(int i = 0 ; i < listButtons.size() ; i++){
			if(listClicked.contains(listButtons.get(i).getId())){
				status += listButtons.get(i).getText() + "-t ";
			}else{
				status += listButtons.get(i).getText() + "-f ";
			}
    	}
    	
    	status += "d";
    	for(int i = 0 ; i < listTxt.size() ; i++){
			status += listTxt.get(i).getText()+"-";
    	}

    	return status;
    }
    
    private void inverteCor(Button btn){
    	
    	dicas ++;
    	int id = R.drawable.default_btndica;
    	switch (Integer.parseInt(btn.getText().toString())) {
		case 2:
			id = R.drawable.default_btndica;
			break;
		case 4:
			id = R.drawable.default_btndica;
			break;
		case 8:
			id = R.drawable.default_btndica;
			break;
		case 16:
			id = R.drawable.default_btndica;
			break;
		case 32:
			id = R.drawable.default_btndica;
			break;
		case 64:
			id = R.drawable.default_btndica;
			break;
		case 128:
			id = R.drawable.default_btndica;
			break;
		case 256:
			id = R.drawable.default_btndica;
			break;
		case 512:
			id = R.drawable.default_btndc0000512;
			break;
		case 1024:
			id = R.drawable.default_btndc0001024;
			break;
		case 2048:
			id = R.drawable.default_btndc0002048;
			break;
		case 4096:
			id = R.drawable.default_btndc0004096;
			break;
		case 8192:
			id = R.drawable.default_btndc0008192;
			break;
		case 16384:
			id = R.drawable.default_btndc0016384;
			break;
		case 32768:
			id = R.drawable.default_btndc0032768;
			break;
		case 65536:
			id = R.drawable.default_btndc0065536;
			break;
		case 131072:
			id = R.drawable.default_btndc0131072;
			break;
		case 262144:
			id = R.drawable.default_btndc0262144;
			break;
		case 524288:
			id = R.drawable.default_btndc0524288;
			break;
		case 1048576:
			id = R.drawable.default_btndc1048576;
			break;
		default:
			id = R.drawable.default_btndc1048576;
			break;
		}
    	
    	btn.setBackground(getResources().getDrawable(id));
    	
    	
    }
    
    private void mostraDicas(){
    	
    	dicas = 0;
    	
    	Coord selecionado = mapaCoord.get(btnFirstClick.getId());
    	
    	String coord = selecionado.getLinha() + "" + selecionado.getColuna();
    	
    	switch (Integer.parseInt(coord)) {
			case 11:
				if(listButtons.get(0).getText().toString().equals(listButtons.get(1).getText().toString()))
					inverteCor(listButtons.get(1));
				if(listButtons.get(0).getText().toString().equals(listButtons.get(4).getText().toString()))
					inverteCor(listButtons.get(4));
				if(listButtons.get(0).getText().toString().equals(listButtons.get(5).getText().toString()))
					inverteCor(listButtons.get(5));
				break;
				
			case 12:
				if(listButtons.get(1).getText().toString().equals(listButtons.get(0).getText().toString()))
					inverteCor(listButtons.get(0));
				if(listButtons.get(1).getText().toString().equals(listButtons.get(2).getText().toString()))
					inverteCor(listButtons.get(2));
				if(listButtons.get(1).getText().toString().equals(listButtons.get(4).getText().toString()))
					inverteCor(listButtons.get(4));
				if(listButtons.get(1).getText().toString().equals(listButtons.get(5).getText().toString()))
					inverteCor(listButtons.get(5));
				if(listButtons.get(1).getText().toString().equals(listButtons.get(6).getText().toString()))
					inverteCor(listButtons.get(6));
				break;
				
			case 13:
				if(listButtons.get(2).getText().toString().equals(listButtons.get(1).getText().toString()))
					inverteCor(listButtons.get(1));
				if(listButtons.get(2).getText().toString().equals(listButtons.get(3).getText().toString()))
					inverteCor(listButtons.get(3));
				if(listButtons.get(2).getText().toString().equals(listButtons.get(5).getText().toString()))
					inverteCor(listButtons.get(5));
				if(listButtons.get(2).getText().toString().equals(listButtons.get(6).getText().toString()))
					inverteCor(listButtons.get(6));
				if(listButtons.get(2).getText().toString().equals(listButtons.get(7).getText().toString()))
					inverteCor(listButtons.get(7));
				break;
				
			case 14:
				if(listButtons.get(3).getText().toString().equals(listButtons.get(2).getText().toString()))
					inverteCor(listButtons.get(2));
				if(listButtons.get(3).getText().toString().equals(listButtons.get(6).getText().toString()))
					inverteCor(listButtons.get(6));
				if(listButtons.get(3).getText().toString().equals(listButtons.get(7).getText().toString()))
					inverteCor(listButtons.get(7));
				break;
				
			case 21:
				if(listButtons.get(4).getText().toString().equals(listButtons.get(0).getText().toString()))
					inverteCor(listButtons.get(0));
				if(listButtons.get(4).getText().toString().equals(listButtons.get(1).getText().toString()))
					inverteCor(listButtons.get(1));
				if(listButtons.get(4).getText().toString().equals(listButtons.get(5).getText().toString()))
					inverteCor(listButtons.get(5));
				if(listButtons.get(4).getText().toString().equals(listButtons.get(8).getText().toString()))
					inverteCor(listButtons.get(8));
				if(listButtons.get(4).getText().toString().equals(listButtons.get(9).getText().toString()))
					inverteCor(listButtons.get(9));
				break;
				
			case 22:
				if(listButtons.get(5).getText().toString().equals(listButtons.get(0).getText().toString()))
					inverteCor(listButtons.get(0));
				if(listButtons.get(5).getText().toString().equals(listButtons.get(1).getText().toString()))
					inverteCor(listButtons.get(1));
				if(listButtons.get(5).getText().toString().equals(listButtons.get(2).getText().toString()))
					inverteCor(listButtons.get(2));
				if(listButtons.get(5).getText().toString().equals(listButtons.get(4).getText().toString()))
					inverteCor(listButtons.get(4));
				if(listButtons.get(5).getText().toString().equals(listButtons.get(6).getText().toString()))
					inverteCor(listButtons.get(6));
				if(listButtons.get(5).getText().toString().equals(listButtons.get(8).getText().toString()))
					inverteCor(listButtons.get(8));
				if(listButtons.get(5).getText().toString().equals(listButtons.get(9).getText().toString()))
					inverteCor(listButtons.get(9));
				if(listButtons.get(5).getText().toString().equals(listButtons.get(10).getText().toString()))
					inverteCor(listButtons.get(10));
				break;
				
			case 23:
				if(listButtons.get(6).getText().toString().equals(listButtons.get(1).getText().toString()))
					inverteCor(listButtons.get(1));
				if(listButtons.get(6).getText().toString().equals(listButtons.get(2).getText().toString()))
					inverteCor(listButtons.get(2));
				if(listButtons.get(6).getText().toString().equals(listButtons.get(3).getText().toString()))
					inverteCor(listButtons.get(3));
				if(listButtons.get(6).getText().toString().equals(listButtons.get(5).getText().toString()))
					inverteCor(listButtons.get(5));
				if(listButtons.get(6).getText().toString().equals(listButtons.get(7).getText().toString()))
					inverteCor(listButtons.get(7));
				if(listButtons.get(6).getText().toString().equals(listButtons.get(9).getText().toString()))
					inverteCor(listButtons.get(9));
				if(listButtons.get(6).getText().toString().equals(listButtons.get(10).getText().toString()))
					inverteCor(listButtons.get(10));
				if(listButtons.get(6).getText().toString().equals(listButtons.get(11).getText().toString()))
					inverteCor(listButtons.get(11));
				break;
				
			case 24:
				if(listButtons.get(7).getText().toString().equals(listButtons.get(2).getText().toString()))
					inverteCor(listButtons.get(2));
				if(listButtons.get(7).getText().toString().equals(listButtons.get(3).getText().toString()))
					inverteCor(listButtons.get(3));
				if(listButtons.get(7).getText().toString().equals(listButtons.get(6).getText().toString()))
					inverteCor(listButtons.get(6));
				if(listButtons.get(7).getText().toString().equals(listButtons.get(10).getText().toString()))
					inverteCor(listButtons.get(10));
				if(listButtons.get(7).getText().toString().equals(listButtons.get(11).getText().toString()))
					inverteCor(listButtons.get(11));
				break;
				
			case 31:
				if(listButtons.get(8).getText().toString().equals(listButtons.get(4).getText().toString()))
					inverteCor(listButtons.get(4));
				if(listButtons.get(8).getText().toString().equals(listButtons.get(5).getText().toString()))
					inverteCor(listButtons.get(5));
				if(listButtons.get(8).getText().toString().equals(listButtons.get(9).getText().toString()))
					inverteCor(listButtons.get(9));
				if(listButtons.get(8).getText().toString().equals(listButtons.get(12).getText().toString()))
					inverteCor(listButtons.get(12));
				if(listButtons.get(8).getText().toString().equals(listButtons.get(13).getText().toString()))
					inverteCor(listButtons.get(13));
				break;
				
			case 32:
				if(listButtons.get(9).getText().toString().equals(listButtons.get(4).getText().toString()))
					inverteCor(listButtons.get(4));
				if(listButtons.get(9).getText().toString().equals(listButtons.get(5).getText().toString()))
					inverteCor(listButtons.get(5));
				if(listButtons.get(9).getText().toString().equals(listButtons.get(6).getText().toString()))
					inverteCor(listButtons.get(6));
				if(listButtons.get(9).getText().toString().equals(listButtons.get(8).getText().toString()))
					inverteCor(listButtons.get(8));
				if(listButtons.get(9).getText().toString().equals(listButtons.get(10).getText().toString()))
					inverteCor(listButtons.get(10));
				if(listButtons.get(9).getText().toString().equals(listButtons.get(12).getText().toString()))
					inverteCor(listButtons.get(12));
				if(listButtons.get(9).getText().toString().equals(listButtons.get(13).getText().toString()))
					inverteCor(listButtons.get(13));
				if(listButtons.get(9).getText().toString().equals(listButtons.get(14).getText().toString()))
					inverteCor(listButtons.get(14));
				break;
				
			case 33:
				if(listButtons.get(10).getText().toString().equals(listButtons.get(5).getText().toString()))
					inverteCor(listButtons.get(5));
				if(listButtons.get(10).getText().toString().equals(listButtons.get(6).getText().toString()))
					inverteCor(listButtons.get(6));
				if(listButtons.get(10).getText().toString().equals(listButtons.get(7).getText().toString()))
					inverteCor(listButtons.get(7));
				if(listButtons.get(10).getText().toString().equals(listButtons.get(9).getText().toString()))
					inverteCor(listButtons.get(9));
				if(listButtons.get(10).getText().toString().equals(listButtons.get(11).getText().toString()))
					inverteCor(listButtons.get(11));
				if(listButtons.get(10).getText().toString().equals(listButtons.get(13).getText().toString()))
					inverteCor(listButtons.get(13));
				if(listButtons.get(10).getText().toString().equals(listButtons.get(14).getText().toString()))
					inverteCor(listButtons.get(14));
				if(listButtons.get(10).getText().toString().equals(listButtons.get(15).getText().toString()))
					inverteCor(listButtons.get(15));
				break;
				
			case 34:
				if(listButtons.get(11).getText().toString().equals(listButtons.get(6).getText().toString()))
					inverteCor(listButtons.get(6));
				if(listButtons.get(11).getText().toString().equals(listButtons.get(7).getText().toString()))
					inverteCor(listButtons.get(7));
				if(listButtons.get(11).getText().toString().equals(listButtons.get(10).getText().toString()))
					inverteCor(listButtons.get(10));
				if(listButtons.get(11).getText().toString().equals(listButtons.get(14).getText().toString()))
					inverteCor(listButtons.get(14));
				if(listButtons.get(11).getText().toString().equals(listButtons.get(15).getText().toString()))
					inverteCor(listButtons.get(15));
				break;
				
			case 41:
				if(listButtons.get(12).getText().toString().equals(listButtons.get(8).getText().toString()))
					inverteCor(listButtons.get(8));
				if(listButtons.get(12).getText().toString().equals(listButtons.get(9).getText().toString()))
					inverteCor(listButtons.get(9));
				if(listButtons.get(12).getText().toString().equals(listButtons.get(13).getText().toString()))
					inverteCor(listButtons.get(13));
				break;
				
			case 42:
				if(listButtons.get(13).getText().toString().equals(listButtons.get(8).getText().toString()))
					inverteCor(listButtons.get(8));
				if(listButtons.get(13).getText().toString().equals(listButtons.get(9).getText().toString()))
					inverteCor(listButtons.get(9));
				if(listButtons.get(13).getText().toString().equals(listButtons.get(10).getText().toString()))
					inverteCor(listButtons.get(10));
				if(listButtons.get(13).getText().toString().equals(listButtons.get(12).getText().toString()))
					inverteCor(listButtons.get(12));
				if(listButtons.get(13).getText().toString().equals(listButtons.get(14).getText().toString()))
					inverteCor(listButtons.get(14));
				break;
				
			case 43:
				if(listButtons.get(14).getText().toString().equals(listButtons.get(9).getText().toString()))
					inverteCor(listButtons.get(9));
				if(listButtons.get(14).getText().toString().equals(listButtons.get(10).getText().toString()))
					inverteCor(listButtons.get(10));
				if(listButtons.get(14).getText().toString().equals(listButtons.get(11).getText().toString()))
					inverteCor(listButtons.get(11));
				if(listButtons.get(14).getText().toString().equals(listButtons.get(13).getText().toString()))
					inverteCor(listButtons.get(13));
				if(listButtons.get(14).getText().toString().equals(listButtons.get(15).getText().toString()))
					inverteCor(listButtons.get(15));
				break;
				
			case 44:
				if(listButtons.get(15).getText().toString().equals(listButtons.get(10).getText().toString()))
					inverteCor(listButtons.get(10));
				if(listButtons.get(15).getText().toString().equals(listButtons.get(11).getText().toString()))
					inverteCor(listButtons.get(11));
				if(listButtons.get(15).getText().toString().equals(listButtons.get(14).getText().toString()))
					inverteCor(listButtons.get(14));
				break;
	
			default:
				break;
		}
    	
    	if(dicas > 0){
    		btnFirstClick.setBackground(getResources().getDrawable(R.drawable.default_btn_trans));
    	}else{
    		fistClick = true;
    	}
    	
    }
    
    private Boolean clickValido(){
    	Boolean valido = true;
    	
    	int difColuna = mapaCoord.get(btnFirstClick.getId()).getColuna() - mapaCoord.get(btnSecondClick.getId()).getColuna();
		int difLinha = mapaCoord.get(btnFirstClick.getId()).getLinha() - mapaCoord.get(btnSecondClick.getId()).getLinha();
    	if(difLinha == 0 && difColuna == 0){
    		valido = false;
    	}else{
    		if(difLinha < -1 || difLinha > 1){
    			valido = false;
    		}
    		if(difColuna < -1 || difColuna > 1){
    			valido = false;
    		}
    	}
    	
    	return valido;
    }
    
    private void initTextViews(){
		txtTitPontos = (TextView) findViewById(R.id.txtTitPontos);
		txtTitPontos.setTypeface(fonttype);
		txtTitPontos.setTextSize(15);
		txtPontos = (TextView) findViewById(R.id.txtPontos);
		txtPontos.setTypeface(fonttype);
		txtPontos.setTextSize(20);
		txtTitMaxPontos = (TextView) findViewById(R.id.txtTitMaxPontos);
		txtTitMaxPontos.setTypeface(fonttype);
		txtTitMaxPontos.setTextSize(15);
		txtMaxPontos = (TextView) findViewById(R.id.txtMaxPontos);
		txtMaxPontos.setTypeface(fonttype);
		txtMaxPontos.setTextSize(20);
		
		txt1 = (TextView) findViewById(R.id.txt1);
		txt1.setTypeface(fonttype);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt2.setTypeface(fonttype);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt3.setTypeface(fonttype);
		txt4 = (TextView) findViewById(R.id.txt4);
		txt4.setTypeface(fonttype);
		txt5 = (TextView) findViewById(R.id.txt5);
		txt5.setTypeface(fonttype);
		
		listTxt.add(txt1);
		listTxt.add(txt2);
		listTxt.add(txt3);
		listTxt.add(txt4);
		listTxt.add(txt5);

		txtTitPontos.setTextColor(Color.BLACK);
		txtPontos.setTextColor(Color.BLACK);
		txtTitMaxPontos.setTextColor(Color.BLACK);
		txtMaxPontos.setTextColor(Color.BLACK);
		
    }
    
    private void initButtons(){
    	
    	//LINHA 1
    	btnL1C1 = (Button) findViewById(R.id.btnL1C1);
    	mapaCoord.put(btnL1C1.getId(), new Coord(1,1));
    	listButtons.add(btnL1C1);
    	
    	btnL1C2 = (Button) findViewById(R.id.btnL1C2);
    	listButtons.add(btnL1C2);
    	mapaCoord.put(btnL1C2.getId(), new Coord(1,2));
    	
    	btnL1C3 = (Button) findViewById(R.id.btnL1C3);
    	listButtons.add(btnL1C3);
    	mapaCoord.put(btnL1C3.getId(), new Coord(1,3));
    	
    	btnL1C4 = (Button) findViewById(R.id.btnL1C4);
    	listButtons.add(btnL1C4);
    	mapaCoord.put(btnL1C4.getId(), new Coord(1,4));
    	
    	//LINHA 2
    	btnL2C1 = (Button) findViewById(R.id.btnL2C1);
    	listButtons.add(btnL2C1);
    	mapaCoord.put(btnL2C1.getId(), new Coord(2,1));
    	
    	btnL2C2 = (Button) findViewById(R.id.btnL2C2);
    	listButtons.add(btnL2C2);
    	mapaCoord.put(btnL2C2.getId(), new Coord(2,2));
    	
    	btnL2C3 = (Button) findViewById(R.id.btnL2C3);
    	listButtons.add(btnL2C3);
    	mapaCoord.put(btnL2C3.getId(), new Coord(2,3));
    	
    	btnL2C4 = (Button) findViewById(R.id.btnL2C4);
    	listButtons.add(btnL2C4);
    	mapaCoord.put(btnL2C4.getId(), new Coord(2,4));
    	
    	//LINHA 3
    	btnL3C1 = (Button) findViewById(R.id.btnL3C1);
    	listButtons.add(btnL3C1);
    	mapaCoord.put(btnL3C1.getId(), new Coord(3,1));
    	
    	btnL3C2 = (Button) findViewById(R.id.btnL3C2);
    	listButtons.add(btnL3C2);
    	mapaCoord.put(btnL3C2.getId(), new Coord(3,2));
    	
    	btnL3C3 = (Button) findViewById(R.id.btnL3C3);
    	listButtons.add(btnL3C3);
    	mapaCoord.put(btnL3C3.getId(), new Coord(3,3));
    	
    	btnL3C4 = (Button) findViewById(R.id.btnL3C4);
    	listButtons.add(btnL3C4);
    	mapaCoord.put(btnL3C4.getId(), new Coord(3,4));
    	
    	//LINHA 4
    	btnL4C1 = (Button) findViewById(R.id.btnL4C1);
    	listButtons.add(btnL4C1);
    	mapaCoord.put(btnL4C1.getId(), new Coord(4,1));
    	
    	btnL4C2 = (Button) findViewById(R.id.btnL4C2);
    	listButtons.add(btnL4C2);
    	mapaCoord.put(btnL4C2.getId(), new Coord(4,2));
    	
    	btnL4C3 = (Button) findViewById(R.id.btnL4C3);
    	listButtons.add(btnL4C3);
    	mapaCoord.put(btnL4C3.getId(), new Coord(4,3));
    	
    	btnL4C4 = (Button) findViewById(R.id.btnL4C4);
    	listButtons.add(btnL4C4);
    	mapaCoord.put(btnL4C4.getId(), new Coord(4,4));
    	
    }
    
    private void definirCor(Button btn){
    	
    	int id = R.drawable.default_btn1048576;
    	int key = Integer.parseInt(btn.getText().toString());
    	
    	btn.setTextColor(Color.BLACK);
    	
    	switch (key) {
		case 2:
			id = R.drawable.default_btnlock;
			btn.setTextColor(Constants.COR_2);
			btn.setTextSize(sizeFont(btn.getText().toString()));
			break;
		case 4:
			id = R.drawable.default_btnlock;
			btn.setTextColor(Constants.COR_4);
			btn.setTextSize(sizeFont(btn.getText().toString()));
			break;
		case 8:
			id = R.drawable.default_btnlock;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			btn.setTextColor(Constants.COR_8);
			break;
		case 16:
			id = R.drawable.default_btnlock;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			btn.setTextColor(Constants.COR_16);
			break;
		case 32:
			id = R.drawable.default_btnlock;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			btn.setTextColor(Constants.COR_32);
			break;
		case 64:
			id = R.drawable.default_btnlock;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			btn.setTextColor(Constants.COR_64);
			break;
		case 128:
			id = R.drawable.default_btnlock;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			btn.setTextColor(Constants.COR_128);
			break;
		case 256:
			id = R.drawable.default_btnlock;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			btn.setTextColor(Constants.COR_256);
			break;
		case 512:
			id = R.drawable.default_btn0000512;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			mudaEsquemaCores();
			break;
		case 1024:
			id = R.drawable.default_btn0001024;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			mudaEsquemaCores();
			break;
		case 2048:
			id = R.drawable.default_btn0002048;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			mudaEsquemaCores();
			break;
		case 4096:
			id = R.drawable.default_btn0004096;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			mudaEsquemaCores();
			break;
		case 8192:
			id = R.drawable.default_btn0008192;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			mudaEsquemaCores();
			break;
		case 16384:
			id = R.drawable.default_btn0016384;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			mudaEsquemaCores();
			break;
		case 32768:
			id = R.drawable.default_btn0032768;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			mudaEsquemaCores();
			break;
		case 65536:
			id = R.drawable.default_btn0065536;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			mudaEsquemaCores();
			break;
		case 131072:
			id = R.drawable.default_btn0131072;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			mudaEsquemaCores();
			break;
		case 262144:
			id = R.drawable.default_btn0262144;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			mudaEsquemaCores();
			break;
		case 524288:
			id = R.drawable.default_btn0524288;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			mudaEsquemaCores();
			break;
		case 1048576:
			id = R.drawable.default_btn1048576;
			btn.setTextSize(sizeFont(btn.getText().toString()));
			mudaEsquemaCores();
			break;
		default:
			id = R.drawable.default_btn1048576;
			btn.setTextSize(10);
			break;
		}
    	
    	btn.setBackground(getResources().getDrawable(id));
    	
    	if(!listClicked.contains(btn.getId())){ // nao foi clicado
    		if(Integer.parseInt(btn.getText().toString()) <= 32){
    			btn.setTextColor(Color.GRAY);
    		}
    		btn.setBackground(getResources().getDrawable(R.drawable.default_btninit));
    	}
    }
    
    private int numMovimentos(){
    	movimentos = 0;
    	for(int i = 0; i < listButtons.size(); i++){
    		switch (i) {
			case 0:
				if(listButtons.get(0).getText().toString().equals(listButtons.get(1).getText().toString()))
					movimentos++;
				if(listButtons.get(0).getText().toString().equals(listButtons.get(4).getText().toString()))
					movimentos++;
				if(listButtons.get(0).getText().toString().equals(listButtons.get(5).getText().toString()))
					movimentos++;
				break;
				
			case 1:
				if(listButtons.get(1).getText().toString().equals(listButtons.get(0).getText().toString()))
					movimentos++;
				if(listButtons.get(1).getText().toString().equals(listButtons.get(2).getText().toString()))
					movimentos++;
				if(listButtons.get(1).getText().toString().equals(listButtons.get(4).getText().toString()))
					movimentos++;
				if(listButtons.get(1).getText().toString().equals(listButtons.get(5).getText().toString()))
					movimentos++;
				if(listButtons.get(1).getText().toString().equals(listButtons.get(6).getText().toString()))
					movimentos++;
				break;
				
			case 2:
				if(listButtons.get(2).getText().toString().equals(listButtons.get(1).getText().toString()))
					movimentos++;
				if(listButtons.get(2).getText().toString().equals(listButtons.get(3).getText().toString()))
					movimentos++;
				if(listButtons.get(2).getText().toString().equals(listButtons.get(5).getText().toString()))
					movimentos++;
				if(listButtons.get(2).getText().toString().equals(listButtons.get(6).getText().toString()))
					movimentos++;
				if(listButtons.get(2).getText().toString().equals(listButtons.get(7).getText().toString()))
					movimentos++;
				break;
				
			case 3:
				if(listButtons.get(3).getText().toString().equals(listButtons.get(2).getText().toString()))
					movimentos++;
				if(listButtons.get(3).getText().toString().equals(listButtons.get(6).getText().toString()))
					movimentos++;
				if(listButtons.get(3).getText().toString().equals(listButtons.get(7).getText().toString()))
					movimentos++;
				break;
				
			case 4:
				if(listButtons.get(4).getText().toString().equals(listButtons.get(0).getText().toString()))
					movimentos++;
				if(listButtons.get(4).getText().toString().equals(listButtons.get(1).getText().toString()))
					movimentos++;
				if(listButtons.get(4).getText().toString().equals(listButtons.get(5).getText().toString()))
					movimentos++;
				if(listButtons.get(4).getText().toString().equals(listButtons.get(8).getText().toString()))
					movimentos++;
				if(listButtons.get(4).getText().toString().equals(listButtons.get(9).getText().toString()))
					movimentos++;
				break;
				
			case 5:
				if(listButtons.get(5).getText().toString().equals(listButtons.get(0).getText().toString()))
					movimentos++;
				if(listButtons.get(5).getText().toString().equals(listButtons.get(1).getText().toString()))
					movimentos++;
				if(listButtons.get(5).getText().toString().equals(listButtons.get(2).getText().toString()))
					movimentos++;
				if(listButtons.get(5).getText().toString().equals(listButtons.get(4).getText().toString()))
					movimentos++;
				if(listButtons.get(5).getText().toString().equals(listButtons.get(6).getText().toString()))
					movimentos++;
				if(listButtons.get(5).getText().toString().equals(listButtons.get(8).getText().toString()))
					movimentos++;
				if(listButtons.get(5).getText().toString().equals(listButtons.get(9).getText().toString()))
					movimentos++;
				if(listButtons.get(5).getText().toString().equals(listButtons.get(10).getText().toString()))
					movimentos++;
				break;
				
			case 6:
				if(listButtons.get(6).getText().toString().equals(listButtons.get(1).getText().toString()))
					movimentos++;
				if(listButtons.get(6).getText().toString().equals(listButtons.get(2).getText().toString()))
					movimentos++;
				if(listButtons.get(6).getText().toString().equals(listButtons.get(3).getText().toString()))
					movimentos++;
				if(listButtons.get(6).getText().toString().equals(listButtons.get(5).getText().toString()))
					movimentos++;
				if(listButtons.get(6).getText().toString().equals(listButtons.get(7).getText().toString()))
					movimentos++;
				if(listButtons.get(6).getText().toString().equals(listButtons.get(9).getText().toString()))
					movimentos++;
				if(listButtons.get(6).getText().toString().equals(listButtons.get(10).getText().toString()))
					movimentos++;
				if(listButtons.get(6).getText().toString().equals(listButtons.get(11).getText().toString()))
					movimentos++;
				break;
				
			case 7:
				if(listButtons.get(7).getText().toString().equals(listButtons.get(2).getText().toString()))
					movimentos++;
				if(listButtons.get(7).getText().toString().equals(listButtons.get(3).getText().toString()))
					movimentos++;
				if(listButtons.get(7).getText().toString().equals(listButtons.get(6).getText().toString()))
					movimentos++;
				if(listButtons.get(7).getText().toString().equals(listButtons.get(10).getText().toString()))
					movimentos++;
				if(listButtons.get(7).getText().toString().equals(listButtons.get(11).getText().toString()))
					movimentos++;
				break;
				
			case 8:
				if(listButtons.get(8).getText().toString().equals(listButtons.get(4).getText().toString()))
					movimentos++;
				if(listButtons.get(8).getText().toString().equals(listButtons.get(5).getText().toString()))
					movimentos++;
				if(listButtons.get(8).getText().toString().equals(listButtons.get(9).getText().toString()))
					movimentos++;
				if(listButtons.get(8).getText().toString().equals(listButtons.get(12).getText().toString()))
					movimentos++;
				if(listButtons.get(8).getText().toString().equals(listButtons.get(13).getText().toString()))
					movimentos++;
				break;
				
			case 9:
				if(listButtons.get(9).getText().toString().equals(listButtons.get(4).getText().toString()))
					movimentos++;
				if(listButtons.get(9).getText().toString().equals(listButtons.get(5).getText().toString()))
					movimentos++;
				if(listButtons.get(9).getText().toString().equals(listButtons.get(6).getText().toString()))
					movimentos++;
				if(listButtons.get(9).getText().toString().equals(listButtons.get(8).getText().toString()))
					movimentos++;
				if(listButtons.get(9).getText().toString().equals(listButtons.get(10).getText().toString()))
					movimentos++;
				if(listButtons.get(9).getText().toString().equals(listButtons.get(12).getText().toString()))
					movimentos++;
				if(listButtons.get(9).getText().toString().equals(listButtons.get(13).getText().toString()))
					movimentos++;
				if(listButtons.get(9).getText().toString().equals(listButtons.get(14).getText().toString()))
					movimentos++;
				break;
				
			case 10:
				if(listButtons.get(10).getText().toString().equals(listButtons.get(5).getText().toString()))
					movimentos++;
				if(listButtons.get(10).getText().toString().equals(listButtons.get(6).getText().toString()))
					movimentos++;
				if(listButtons.get(10).getText().toString().equals(listButtons.get(7).getText().toString()))
					movimentos++;
				if(listButtons.get(10).getText().toString().equals(listButtons.get(9).getText().toString()))
					movimentos++;
				if(listButtons.get(10).getText().toString().equals(listButtons.get(11).getText().toString()))
					movimentos++;
				if(listButtons.get(10).getText().toString().equals(listButtons.get(13).getText().toString()))
					movimentos++;
				if(listButtons.get(10).getText().toString().equals(listButtons.get(14).getText().toString()))
					movimentos++;
				if(listButtons.get(10).getText().toString().equals(listButtons.get(15).getText().toString()))
					movimentos++;
				break;
				
			case 11:
				if(listButtons.get(11).getText().toString().equals(listButtons.get(6).getText().toString()))
					movimentos++;
				if(listButtons.get(11).getText().toString().equals(listButtons.get(7).getText().toString()))
					movimentos++;
				if(listButtons.get(11).getText().toString().equals(listButtons.get(10).getText().toString()))
					movimentos++;
				if(listButtons.get(11).getText().toString().equals(listButtons.get(14).getText().toString()))
					movimentos++;
				if(listButtons.get(11).getText().toString().equals(listButtons.get(15).getText().toString()))
					movimentos++;
				break;
				
			case 12:
				if(listButtons.get(12).getText().toString().equals(listButtons.get(8).getText().toString()))
					movimentos++;
				if(listButtons.get(12).getText().toString().equals(listButtons.get(9).getText().toString()))
					movimentos++;
				if(listButtons.get(12).getText().toString().equals(listButtons.get(13).getText().toString()))
					movimentos++;
				break;
				
			case 13:
				if(listButtons.get(13).getText().toString().equals(listButtons.get(8).getText().toString()))
					movimentos++;
				if(listButtons.get(13).getText().toString().equals(listButtons.get(9).getText().toString()))
					movimentos++;
				if(listButtons.get(13).getText().toString().equals(listButtons.get(10).getText().toString()))
					movimentos++;
				if(listButtons.get(13).getText().toString().equals(listButtons.get(12).getText().toString()))
					movimentos++;
				if(listButtons.get(13).getText().toString().equals(listButtons.get(14).getText().toString()))
					movimentos++;
				break;
				
			case 14:
				if(listButtons.get(14).getText().toString().equals(listButtons.get(9).getText().toString()))
					movimentos++;
				if(listButtons.get(14).getText().toString().equals(listButtons.get(10).getText().toString()))
					movimentos++;
				if(listButtons.get(14).getText().toString().equals(listButtons.get(11).getText().toString()))
					movimentos++;
				if(listButtons.get(14).getText().toString().equals(listButtons.get(13).getText().toString()))
					movimentos++;
				if(listButtons.get(14).getText().toString().equals(listButtons.get(15).getText().toString()))
					movimentos++;
				break;
				
			case 15:
				if(listButtons.get(15).getText().toString().equals(listButtons.get(10).getText().toString()))
					movimentos++;
				if(listButtons.get(15).getText().toString().equals(listButtons.get(11).getText().toString()))
					movimentos++;
				if(listButtons.get(15).getText().toString().equals(listButtons.get(14).getText().toString()))
					movimentos++;
				break;

			default:
				break;
			}
    	}
    	
    	return movimentos;
    }
    
    private int getScreenOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
            (rotation == Surface.ROTATION_90
                || rotation == Surface.ROTATION_270) && width > height) {
           /* switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;              
            }*/
        	orientation = 0;
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
           /* switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =  ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;              
            }*/
        	orientation = 1;
        }

        return orientation;
    }
    
    private int sizeFont(String text){
    	int size = 0;
    	DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Float dp = 0f ;
    	switch (text.length()) {
		case 1:
			dp = (tamButton / 4) / (displaymetrics.densityDpi / 160f);
	    	size = dp.intValue();
			break;
		case 2:
			dp = (tamButton / 4) / (displaymetrics.densityDpi / 160f);
	    	size = dp.intValue();	
			break;
		case 3:
			dp = (tamButton / 5) / (displaymetrics.densityDpi / 160f);
	    	size = dp.intValue();
			break;
		case 4:
			dp = (tamButton / 5) / (displaymetrics.densityDpi / 160f);
	    	size = dp.intValue();
			break;
		case 5:
			dp = (tamButton / 5.5f) / (displaymetrics.densityDpi / 160f);
	    	size = dp.intValue();
			break;
		case 6:
			dp = (tamButton / 6) / (displaymetrics.densityDpi / 160f);
	    	size = dp.intValue();
			break;
		case 7:
			dp = (tamButton / 7) / (displaymetrics.densityDpi / 160f);
	    	size = dp.intValue();
			break;

		default:
			break;
		}
    	
    	return size;
    }
    
    private void defineTamanho(){
    	DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics); 
        float pix = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, displaymetrics);
        int height = displaymetrics.heightPixels; // ALTURA
        int width = displaymetrics.widthPixels; // LARGURA
        
        LinearLayout layoutButtons = (LinearLayout)findViewById(R.id.layoutButtons);
        LayoutParams paramsLayoutButtons = layoutButtons.getLayoutParams();
        
        RelativeLayout rlImgUp = (RelativeLayout) findViewById(R.id.rlImgUp);
        RelativeLayout rlImgIn = (RelativeLayout) findViewById(R.id.rlImgIn);
        
        //if(getScreenOrientation() == 1){ // portrait
        	Double div = (double)(height-getStatusBarHeight()) / (double)width;
        	if(div < 1.3){
	        	Double d = width * (2 - div);
        		int tam = d.intValue();
        		paramsLayoutButtons.height = tam;
	    		paramsLayoutButtons.width = tam;
	        	tamButton = (tam - (int) pix)/4;
	        	
	        	// Gets the layout params that will allow you to resize the layout
	        	LayoutParams params1 = rl1.getLayoutParams();
	        	params1.height = (tam - (int) pix)/8;
	        	params1.width = (tam - (int) pix)/8;
	        	LayoutParams params2 = rl2.getLayoutParams();
	        	params2.height = (tam - (int) pix)/8;
	        	params2.width = (tam - (int) pix)/8;
	        	LayoutParams params3 = rl3.getLayoutParams();
	        	params3.height = (tam - (int) pix)/8;
	        	params3.width = (tam - (int) pix)/8;
	        	LayoutParams params4 = rl4.getLayoutParams();
	        	params4.height = (tam - (int) pix)/8;
	        	params4.width = (tam - (int) pix)/8;
	        	LayoutParams params5 = rl5.getLayoutParams();
	        	params5.height = (tam - (int) pix)/8;
	        	params5.width = (tam - (int) pix)/8;
	        	
	        	LayoutParams ParamsLinearLayoutDica = linearLayoutDica.getLayoutParams();
	        	ParamsLinearLayoutDica.height = (tam - (int) pix)/3;
	        	
	        	LayoutParams paramsImgUp = rlImgUp.getLayoutParams();
	        	paramsImgUp.height = (tam - (int) pix)/8;
	        	paramsImgUp.width = (tam - (int) pix)/8;
	        	
	        	LayoutParams paramsImgIn = rlImgIn.getLayoutParams();
	        	paramsImgIn.height = (tam - (int) pix)/8;
	        	paramsImgIn.width = (tam - (int) pix)/8;
	        	
        	}else{
        		paramsLayoutButtons.height = width;
	    		paramsLayoutButtons.width = width;
	        	tamButton = (width - (int) pix)/4;
	        	
	        	LayoutParams params1 = rl1.getLayoutParams();
	        	params1.height = (width - (int) pix)/8;
	        	params1.width = (width - (int) pix)/8;
	        	LayoutParams params2 = rl2.getLayoutParams();
	        	params2.height = (width - (int) pix)/8;
	        	params2.width = (width - (int) pix)/8;
	        	LayoutParams params3 = rl3.getLayoutParams();
	        	params3.height = (width - (int) pix)/8;
	        	params3.width = (width - (int) pix)/8;
	        	LayoutParams params4 = rl4.getLayoutParams();
	        	params4.height = (width - (int) pix)/8;
	        	params4.width = (width - (int) pix)/8;
	        	LayoutParams params5 = rl5.getLayoutParams();
	        	params5.height = (width - (int) pix)/8;
	        	params5.width = (width - (int) pix)/8;
	        	
	        	LayoutParams ParamsLinearLayoutDica = linearLayoutDica.getLayoutParams();
	        	ParamsLinearLayoutDica.height = (width - (int) pix)/3;
	        	
	        	LayoutParams paramsImgUp = rlImgUp.getLayoutParams();
	        	paramsImgUp.height = (width - (int) pix)/8;
	        	paramsImgUp.width = (width - (int) pix)/8;
	        	
	        	LayoutParams paramsImgIn = rlImgIn.getLayoutParams();
	        	paramsImgIn.height = (width - (int) pix)/8;
	        	paramsImgIn.width = (width - (int) pix)/8;
        	}
       // }else if(getScreenOrientation() == 0  ){ // landscape
        	
        	/*Double div = (double)(height-getStatusBarHeight()) / (double)width;
        	if(div < 1.3){
	        	Double d = width * (2 - div);
        		int tam = d.intValue();
        		paramsLayoutButtons.height = tam;
	    		paramsLayoutButtons.width = tam;
	        	tamButton = (tam - (int) pix)/4;
        	}else{
        		paramsLayoutButtons.height = width;
	    		paramsLayoutButtons.width = width;
	        	tamButton = (width - (int) pix)/4;*/
        	}
        	
        //}
    //}
    
    private Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
	
	 private Boolean isNetworkAvailable() {
	        ConnectivityManager connectivityManager 
	              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	    }
    
    private int getStatusBarHeight() { 
    	
    	int result = 0; 
    	int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android"); 
    	
		if(resourceId > 0) { 
			result = getResources().getDimensionPixelSize(resourceId); 
		} 
    		
    	return result; 
    }
    
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
        }
        return "";
    }
    
    protected void onPause(){
    	super.onPause();
    	if(mGoogleApiClient.isConnected()){
			salvaLeaderboards();
		}
    	//contClick.addStruct(new Struct(Constants.PAUSE, Constants.MAIN));
    	saveStatus();
    }

    protected void onStop(){
    	super.onStop();
    	//contClick.addStruct(new Struct(Constants.STOP, Constants.MAIN));
    	if (mGoogleApiClient.isConnected()) {
    			salvaLeaderboards();
    	      mGoogleApiClient.disconnect();
    	    }
    	saveStatus();
    }

    protected void onDestroy(){
    	super.onDestroy();
    	if(mGoogleApiClient.isConnected()){
			salvaLeaderboards();
			mGoogleApiClient.disconnect();
		}
    	//contClick.addStruct(new Struct(Constants.DESTROY, Constants.MAIN));
    	saveStatus();
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	


}
