package in.wisemonkeys.android.wisemonkeys.HybridApp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import in.wisemonkeys.android.wisemonkeys.Communicator;
import in.wisemonkeys.android.wisemonkeys.ExitDialogFragment;
import in.wisemonkeys.android.wisemonkeys.R;

public class MainFragment extends AppCompatActivity implements Communicator {

    FrameLayout frameLayout_home;
    RelativeLayout relativeLayout_blog;
    RelativeLayout relativeLayout_video;

    BottomNavigationView navigation;
     boolean blog_clicked = false;
     boolean video_clicked = false;

    android.support.v4.app.FragmentManager manager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    visibleHome();
                    return true;
                case R.id.navigation_dashboard: {
                    visibleBlog();
                    if (!blog_clicked) {
                        BlogFragment blogFragment = (BlogFragment) manager.findFragmentByTag("frag_blog");
                        blogFragment.loadbtn.callOnClick();
                        blog_clicked = true;
                    }
                    return true;
                }
                case R.id.navigation_notifications: {
                    visibleVideo();
                    if (!video_clicked) {
                        videosFragment videoFragment = (videosFragment) manager.findFragmentByTag("frag_videos");
                        videoFragment.loadbtn.callOnClick();
                        video_clicked = true;
                    }
                    return true;
                }
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        frameLayout_home = (FrameLayout) findViewById(R.id.group);
        relativeLayout_blog = (RelativeLayout) findViewById(R.id.blog);
        relativeLayout_video = (RelativeLayout) findViewById(R.id.videos);
        manager = getSupportFragmentManager();
/*
        FragmentTransaction transaction = manager.beginTransaction();
        LoadingFragment loadingFragment = new LoadingFragment();
        transaction.add(R.id.videos,loadingFragment , null);
        transaction.commit();

        FragmentTransaction transaction1 = manager.beginTransaction();
        LoadingFragment loadingFragment1= new LoadingFragment();
        transaction1.add(R.id.blog,loadingFragment1 , null);
        transaction1.commit();
*/
        videos();
        blog();
        home();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void home() {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        HomeFragment homefrag = new HomeFragment();
        transaction.add(R.id.group, homefrag, "frag_home");
        transaction.commit();

    }

    public void blog() {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BlogFragment blogFragment = new BlogFragment();
        transaction.replace(R.id.blog, blogFragment, "frag_blog");
        transaction.commit();
    }

    private void videos() {


        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        videosFragment videofragment = new videosFragment();
        transaction.replace(R.id.videos, videofragment, "frag_videos");
        transaction.commit();

    }

    private void visibleHome() {
        relativeLayout_blog.setVisibility(View.INVISIBLE);
        relativeLayout_video.setVisibility(View.INVISIBLE);
        frameLayout_home.setVisibility(View.VISIBLE);
    }

    private void visibleBlog() {
        relativeLayout_video.setVisibility(View.INVISIBLE);
        frameLayout_home.setVisibility(View.INVISIBLE);
        relativeLayout_blog.setVisibility(View.VISIBLE);

    }

    private void visibleVideo() {
        frameLayout_home.setVisibility(View.INVISIBLE);
        relativeLayout_blog.setVisibility(View.INVISIBLE);
        relativeLayout_video.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {
        if (frameLayout_home.getVisibility() == View.VISIBLE) {
            HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("frag_home");
            if (fragment.webView.canGoBack()) {
                fragment.webView.goBack();
            } else {
                new ExitDialogFragment().show(getSupportFragmentManager(), null);
            }

        } else if (relativeLayout_blog.getVisibility() == View.VISIBLE) {
            BlogFragment fragment = (BlogFragment) getSupportFragmentManager().findFragmentByTag("frag_blog");
            if (fragment.webView.canGoBack()) {
                fragment.webView.goBack();
            } else {
                new ExitDialogFragment().show(getSupportFragmentManager(), null);
            }
        } else if (relativeLayout_video.getVisibility() == View.VISIBLE) {
            videosFragment fragment = (videosFragment) getSupportFragmentManager().findFragmentByTag("frag_videos");
            if (fragment.webView.canGoBack()) {
                fragment.webView.goBack();
            } else {
                new ExitDialogFragment().show(getSupportFragmentManager(), null);
            }

        } else new ExitDialogFragment().show(getSupportFragmentManager(), null);
    }


    @Override
    public void loadWebBlog() {
        if (!blog_clicked) {
            BlogFragment blogFragment = (BlogFragment) getSupportFragmentManager().findFragmentByTag("frag_blog");
            blog_clicked = true;
            blogFragment.loadWebView();
            blogFragment.loadbtn.setVisibility(View.INVISIBLE);
            return;
        }else if(!video_clicked){
            video_clicked=true;
            videosFragment videoFragment = (videosFragment) manager.findFragmentByTag("frag_videos");
            videoFragment.loadbtn.callOnClick();
        }
        else {
            //Toast.makeText(getApplicationContext(), "not null", Toast.LENGTH_LONG).show();
        }


    }
}
