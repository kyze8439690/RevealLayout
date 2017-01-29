package me.yugy.github.reveallayout.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import me.yugy.github.reveallayout.RevealLayout;


public class SingleChildActivity extends AppCompatActivity {

    private RevealLayout mRevealLayout;
    private boolean mIsAnimationSlowDown = false;
    private boolean mIsBaseOnTouchLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        mRevealLayout = (RevealLayout) findViewById(R.id.reveal_layout);

        initRevealLayout();
    }

    private void initRevealLayout() {
        if (mIsBaseOnTouchLocation) {
            mRevealLayout.setOnClickListener(null);
            mRevealLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        Log.d("SingleChildActivity", "x: " + event.getX() + ", y: " + event.getY());
                        if (mRevealLayout.isContentShown()) {
                            if (mIsAnimationSlowDown) {
                                mRevealLayout.hide((int) event.getX(), (int) event.getY(), 2000);
                            } else {
                                mRevealLayout.hide((int) event.getX(), (int) event.getY());
                            }
                        } else {
                            if (mIsAnimationSlowDown) {
                                mRevealLayout.show((int) event.getX(), (int) event.getY(), 2000);
                            } else {
                                mRevealLayout.show((int) event.getX(), (int) event.getY());
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
        } else {
            mRevealLayout.setOnTouchListener(null);
            mRevealLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRevealLayout.isContentShown()) {
                        if (mIsAnimationSlowDown) {
                            mRevealLayout.hide(2000);
                        } else {
                            mRevealLayout.hide();
                        }
                    } else {
                        if (mIsAnimationSlowDown) {
                            mRevealLayout.show(2000);
                        } else {
                            mRevealLayout.show();
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.slow) {
            item.setChecked(!item.isChecked());
            mIsAnimationSlowDown = item.isChecked();
            return true;
        } else if (item.getItemId() == R.id.touch) {
            item.setChecked(!item.isChecked());
            mIsBaseOnTouchLocation = item.isChecked();
            initRevealLayout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
