package me.yugy.github.reveallayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

/**
 * Created by yugy on 14/11/21.
 */
@SuppressWarnings("unused")
public class RevealLayout extends FrameLayout{

    private static final int DEFAULT_DURATION = 600;
    private Path mClipPath;
    private int mClipCenterX, mClipCenterY = 0;
    private Animation mAnimation;

    private float mClipRadius = 0;
    private boolean mIsContentShown = true;

    public RevealLayout(Context context) {
        this(context, null);
    }

    public RevealLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RevealLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mClipPath = new Path();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mClipCenterX = w / 2;
        mClipCenterY = h / 2;
        if (!mIsContentShown) {
            mClipRadius = 0;
        } else {
            mClipRadius = (float) (Math.sqrt(w * w + h * h) / 2);
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    public float getClipRadius() {
        return mClipRadius;
    }

    public void setClipRadius(float clipRadius) {
        mClipRadius = clipRadius;
        invalidate();
    }

    public boolean isContentShown() {
        return mIsContentShown;
    }

    public void setContentShown(boolean isContentShown) {
        mIsContentShown = isContentShown;
        if (mIsContentShown) {
            mClipRadius = 0;
        } else {
            mClipRadius = getMaxRadius(mClipCenterX, mClipCenterY);
        }
        invalidate();
    }

    public void show() {
        show(DEFAULT_DURATION);
    }

    public void show(int duration) {
        show(duration, null);
    }

    public void show(int x, int y) {
        show(x, y, DEFAULT_DURATION, null);
    }

    public void show(@Nullable Animation.AnimationListener listener) {
        show(DEFAULT_DURATION, listener);
    }

    public void show(int duration, @Nullable Animation.AnimationListener listener) {
        show(getWidth() / 2, getHeight() / 2, duration, listener);
    }

    public void show(int x, int y, @Nullable Animation.AnimationListener listener) {
        show(x, y, DEFAULT_DURATION, listener);
    }

    public void show(int x, int y, int duration) {
        show(x, y, duration, null);
    }

    public void show(int x, int y, int duration, @Nullable final Animation.AnimationListener listener) {
        if (x < 0 || x > getWidth() || y < 0 || y > getHeight()) {
            throw new RuntimeException("Center point out of range or call method when View is not initialed yet.");
        }

        mClipCenterX = x;
        mClipCenterY = y;
        final float maxRadius = getMaxRadius(x, y);

        clearAnimation();

        mAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                setClipRadius(interpolatedTime * maxRadius);
            }
        };
        mAnimation.setInterpolator(new BakedBezierInterpolator());
        mAnimation.setDuration(duration);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationRepeat(Animation animation) {
                if (listener != null) {
                    listener.onAnimationRepeat(animation);
                }
            }

            @Override
            public void onAnimationStart(Animation animation) {
                mIsContentShown = true;
                if (listener != null) {
                    listener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
            }
        });
        startAnimation(mAnimation);
    }

    public void hide() {
        hide(DEFAULT_DURATION);
    }

    public void hide(int duration) {
        hide(getWidth() / 2, getHeight() / 2, duration, null);
    }

    public void hide(int x, int y) {
        hide(x, y, DEFAULT_DURATION, null);
    }

    public void hide(@Nullable Animation.AnimationListener listener) {
        hide(DEFAULT_DURATION, listener);
    }

    public void hide(int duration, @Nullable Animation.AnimationListener listener) {
        hide(getWidth() / 2, getHeight() / 2, duration, listener);
    }

    public void hide(int x, int y, @Nullable Animation.AnimationListener listener) {
        hide(x, y, DEFAULT_DURATION, listener);
    }

    public void hide(int x, int y, int duration) {
        hide(x, y, duration, null);
    }

    public void hide(int x, int y, int duration, @Nullable final Animation.AnimationListener listener) {
        if (x < 0 || x > getWidth() || y < 0 || y > getHeight()) {
            throw new RuntimeException("Center point out of range or call method when View is not initialed yet.");
        }

        final float maxRadius = getMaxRadius(x, y);
        if (x != mClipCenterX || y != mClipCenterY) {
            mClipCenterX = x;
            mClipCenterY = y;
            mClipRadius = maxRadius;
        }

        clearAnimation();

        mAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                setClipRadius(maxRadius * (1 - interpolatedTime));
            }
        };
        mAnimation.setInterpolator(new BakedBezierInterpolator());
        mAnimation.setDuration(duration);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsContentShown = false;
                if (listener != null) {
                    listener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (listener != null) {
                    listener.onAnimationRepeat(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
            }
        });
        startAnimation(mAnimation);
    }

    public void next() {
        next(DEFAULT_DURATION);
    }

    public void next(int duration) {
        next(getWidth() / 2, getHeight() / 2, duration, null);
    }

    public void next(int x, int y) {
        next(x, y, DEFAULT_DURATION, null);
    }

    public void next(@Nullable Animation.AnimationListener listener) {
        next(DEFAULT_DURATION, listener);
    }

    public void next(int duration, @Nullable Animation.AnimationListener listener) {
        next(getWidth() / 2, getHeight() / 2, duration, listener);
    }

    public void next(int x, int y, @Nullable Animation.AnimationListener listener) {
        next(x, y, DEFAULT_DURATION, listener);
    }

    public void next(int x, int y, int duration) {
        next(x, y, duration, null);
    }

    public void next(int x, int y, int duration, @Nullable Animation.AnimationListener listener) {
        final int childCount = getChildCount();
        if (childCount > 1) {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (i == 0) {
                    bringChildToFront(child);
                }
            }
            show(x, y, duration, listener);
        }
    }

    private float getMaxRadius(int x, int y) {
        int h = Math.max(x, getWidth() - x);
        int v = Math.max(y, getHeight() - y);
        return (float) Math.sqrt(h * h + v * v);
    }

    @Override
    protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
        if (indexOfChild(child) == getChildCount() - 1) {
            boolean result;
            mClipPath.reset();
            mClipPath.addCircle(mClipCenterX, mClipCenterY, mClipRadius, Path.Direction.CW);

//            Log.d("RevealLayout", "ClipRadius: " + mClipRadius);
            canvas.save();
            canvas.clipPath(mClipPath);
            result = super.drawChild(canvas, child, drawingTime);
            canvas.restore();
            return result;
        } else {
            return super.drawChild(canvas, child, drawingTime);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.isContentShown = mIsContentShown;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());

        setContentShown(ss.isContentShown);
    }

    public static class SavedState extends BaseSavedState {

        boolean isContentShown;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isContentShown ? 1 : 0);
        }

        @SuppressWarnings("hiding")
        public static final Parcelable.Creator<RevealLayout.SavedState> CREATOR
                = new Parcelable.Creator<RevealLayout.SavedState>() {
            public RevealLayout.SavedState createFromParcel(Parcel in) {
                return new RevealLayout.SavedState(in);
            }

            public RevealLayout.SavedState[] newArray(int size) {
                return new RevealLayout.SavedState[size];
            }
        };

        private SavedState(Parcel in) {
            super(in);
            isContentShown = in.readInt() == 1;
        }
    }
}
