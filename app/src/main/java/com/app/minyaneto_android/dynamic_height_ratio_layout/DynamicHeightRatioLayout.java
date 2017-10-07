package com.app.minyaneto_android.dynamic_height_ratio_layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.minyaneto_android.R;

/**
 * Created by משה on 03/10/2017.
 */

public class DynamicHeightRatioLayout extends LinearLayout{

    private static final String DEBUG_TAG = "NavMotions";
    private final int DEFAULT_NAV_COLOR = Color.BLUE;
    private final float DEFAULT_NAV_DIMENSION = 80;

    private View m_UpperView;
    private View m_LowerView;
    private LinearLayout m_nav;

    private int m_NavColor;
    private float m_NavDimension;

    public DynamicHeightRatioLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode())
            return;

        setOrientation(LinearLayout.VERTICAL);

        TypedArray typedAttrs =
                context.getTheme().obtainStyledAttributes(attrs,
                        R.styleable.DynamicHeightRatioLayout,
                        0, 0);
        try{
            m_NavColor =
                    typedAttrs.getColor(R.styleable.DynamicHeightRatioLayout_navColor,
                            DEFAULT_NAV_COLOR);
            m_NavDimension =
                    typedAttrs.getDimension(R.styleable.DynamicHeightRatioLayout_navHeight,
                            DEFAULT_NAV_DIMENSION);
        }
        finally {
            typedAttrs.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        m_nav = createNavLinearLayout();
        initUpperAndLowerViews();
        addView(m_nav,1);
    }

    private void initUpperAndLowerViews() {
        if (getChildCount() != 2)
            return;
        m_UpperView = getChildAt(0);
        setViewWeight(m_UpperView);
        m_LowerView = getChildAt(1);
        setViewWeight(m_LowerView);
    }

    private void setViewWeight(View view){
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                              LayoutParams.WRAP_CONTENT,
                                              1.0f));
    }

    private LinearLayout createNavLinearLayout() {
        final LinearLayout nav = new LinearLayout(getContext());
        nav.setOrientation(LinearLayout.VERTICAL);
        LayoutParams navLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int)m_NavDimension);
        navLayoutParams.gravity = Gravity.CENTER;
        nav.setLayoutParams(navLayoutParams);
        nav.setBackgroundColor(m_NavColor);

        ImageView dragHandleImageView = new ImageView(getContext());
        dragHandleImageView.setImageResource(R.drawable.ic_drag_handle_black_24dp);
        LinearLayout.LayoutParams imageViewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        imageViewLayoutParams.gravity = Gravity.CENTER;
        dragHandleImageView.setLayoutParams(imageViewLayoutParams);

        nav.addView(dragHandleImageView);
        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(getContext(), new OnSwipeListener(){
            @Override
            public boolean onSwipe(Direction direction, float offset) {
                if (direction.equals(Direction.down))
                    onNavSwipeDown(offset);
                if (direction.equals(Direction.up))
                    onNavSwipeUp(offset);
                return true;
            }
        });
        nav.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        return nav;
    }

    private void onNavSwipeUp(float offset){
        if (!isNavInBounds(m_nav.getY() - offset)) {
            setViewHeightAddition(m_LowerView, m_LowerView.getHeight());
            setViewHeight(m_UpperView, 0);
            return;
        }

        setViewHeightAddition(m_LowerView, offset);
        setViewHeightAddition(m_UpperView, -offset);
    }

    private void onNavSwipeDown(float offset){
        if (!isNavInBounds(m_nav.getY() + offset)) {
            setViewHeightAddition(m_UpperView, m_LowerView.getHeight());
            setViewHeight(m_LowerView, 0);
            return;
        }

        setViewHeightAddition(m_UpperView, offset);
        setViewHeightAddition(m_LowerView, -offset);
    }

    private void setViewHeightAddition(View view, float additionHeight) {
        setViewHeight(view, view.getHeight() + additionHeight);
    }

    private void setViewHeight(View view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams(layoutParams);
    }

    private boolean isNavInBounds(float navNewY){
        return m_nav.getY() >= 0 && navNewY + m_nav.getHeight() <= getHeight();
    }
}