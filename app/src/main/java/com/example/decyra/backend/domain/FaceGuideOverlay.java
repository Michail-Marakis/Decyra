package com.example.decyra.backend.domain;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * The type Face guide overlay.
 */
public class FaceGuideOverlay extends View {

    /**
     * The enum Face action.
     */
    public enum FaceAction {
        /**
         * Center face action.
         */
        CENTER,
        /**
         * Look left face action.
         */
        LOOK_LEFT,
        /**
         * Look right face action.
         */
        LOOK_RIGHT,
        /**
         * Look up face action.
         */
        LOOK_UP,
        /**
         * Look down face action.
         */
        LOOK_DOWN,
        /**
         * Blink face action.
         */
        BLINK,
        /**
         * Done face action.
         */
        DONE
    }

    private final Paint ovalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint guidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private boolean showLeft;
    private boolean showRight;
    private boolean showUp;
    private boolean showDown;
    private boolean completed;

    private FaceAction currentAction = FaceAction.CENTER;

    /**
     * Instantiates a new Face guide overlay.
     *
     * @param context the context
     */
    public FaceGuideOverlay(Context context) {
        super(context);
        init();
    }

    /**
     * Instantiates a new Face guide overlay.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public FaceGuideOverlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Instantiates a new Face guide overlay.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public FaceGuideOverlay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ovalPaint.setStyle(Paint.Style.STROKE);
        ovalPaint.setStrokeWidth(8f);
        ovalPaint.setColor(0xFFFF4D6D);

        arrowPaint.setStyle(Paint.Style.STROKE);
        arrowPaint.setStrokeWidth(8f);
        arrowPaint.setStrokeCap(Paint.Cap.ROUND);
        arrowPaint.setStrokeJoin(Paint.Join.ROUND);
        arrowPaint.setColor(0xFFFFFFFF);

        guidePaint.setStyle(Paint.Style.STROKE);
        guidePaint.setStrokeWidth(4f);
        guidePaint.setColor(0x66FFFFFF);

        updateStateForAction(currentAction);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();

        float left = width * 0.22f;
        float top = height * 0.18f;
        float right = width * 0.78f;
        float bottom = height * 0.82f;

        RectF oval = new RectF(left, top, right, bottom);

        ovalPaint.setColor(completed ? 0xFF22C55E : 0xFFD946EF);

        canvas.drawOval(oval, guidePaint);
        canvas.drawOval(oval, ovalPaint);

        if (showLeft) {
            drawArrow(canvas, width * 0.16f, height * 0.50f, width * 0.30f, height * 0.50f);
        }

        if (showRight) {
            drawArrow(canvas, width * 0.84f, height * 0.50f, width * 0.70f, height * 0.50f);
        }

        if (showUp) {
            drawArrow(canvas, width * 0.50f, height * 0.12f, width * 0.50f, height * 0.28f);
        }

        if (showDown) {
            drawArrow(canvas, width * 0.50f, height * 0.88f, width * 0.50f, height * 0.72f);
        }

        if (currentAction == FaceAction.BLINK) {
            drawBlinkGuide(canvas, width, height);
        }

        if (completed) {
            drawCheck(canvas, width, height);
        }
    }

    private void drawArrow(Canvas canvas, float startX, float startY, float endX, float endY) {
        canvas.drawLine(startX, startY, endX, endY, arrowPaint);

        float dx = endX - startX;
        float dy = endY - startY;
        double angle = Math.atan2(dy, dx);

        float arrowHeadLength = 24f;
        float arrowHeadAngle = (float) Math.toRadians(28);

        float x1 = (float) (endX - arrowHeadLength * Math.cos(angle - arrowHeadAngle));
        float y1 = (float) (endY - arrowHeadLength * Math.sin(angle - arrowHeadAngle));

        float x2 = (float) (endX - arrowHeadLength * Math.cos(angle + arrowHeadAngle));
        float y2 = (float) (endY - arrowHeadLength * Math.sin(angle + arrowHeadAngle));

        Path path = new Path();
        path.moveTo(endX, endY);
        path.lineTo(x1, y1);
        path.moveTo(endX, endY);
        path.lineTo(x2, y2);

        canvas.drawPath(path, arrowPaint);
    }

    private void drawBlinkGuide(Canvas canvas, float width, float height) {
        Paint blinkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blinkPaint.setStyle(Paint.Style.STROKE);
        blinkPaint.setStrokeWidth(6f);
        blinkPaint.setColor(0xFFFFFFFF);

        float eyeY = height * 0.42f;

        canvas.drawArc(
                width * 0.34f,
                eyeY,
                width * 0.44f,
                eyeY + 30f,
                0,
                180,
                false,
                blinkPaint
        );

        canvas.drawArc(
                width * 0.56f,
                eyeY,
                width * 0.66f,
                eyeY + 30f,
                0,
                180,
                false,
                blinkPaint
        );
    }

    private void drawCheck(Canvas canvas, float width, float height) {
        Paint checkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        checkPaint.setColor(0xFF22C55E);
        checkPaint.setStyle(Paint.Style.STROKE);
        checkPaint.setStrokeWidth(10f);
        checkPaint.setStrokeCap(Paint.Cap.ROUND);
        checkPaint.setStrokeJoin(Paint.Join.ROUND);

        Path check = new Path();
        check.moveTo(width * 0.42f, height * 0.54f);
        check.lineTo(width * 0.48f, height * 0.62f);
        check.lineTo(width * 0.61f, height * 0.44f);

        canvas.drawPath(check, checkPaint);
    }

    /**
     * Sets action.
     *
     * @param action the action
     */
    public void setAction(FaceAction action) {
        if (action == null) return;
        currentAction = action;
        updateStateForAction(action);
        invalidate();
    }

    private void updateStateForAction(FaceAction action) {
        showLeft = false;
        showRight = false;
        showUp = false;
        showDown = false;
        completed = false;

        switch (action) {
            case CENTER:
                break;

            case LOOK_LEFT:
                showLeft = true;
                break;

            case LOOK_RIGHT:
                showRight = true;
                break;

            case LOOK_UP:
                showUp = true;
                break;

            case LOOK_DOWN:
                showDown = true;
                break;

            case BLINK:
                break;

            case DONE:
                completed = true;
                break;
        }
    }

    /**
     * Gets current action.
     *
     * @return the current action
     */
    public FaceAction getCurrentAction() {
        return currentAction;
    }
}