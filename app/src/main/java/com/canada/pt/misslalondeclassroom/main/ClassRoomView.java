package com.canada.pt.misslalondeclassroom.main;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import components.LocationComponent;
import components.StudentComponent;

public class ClassRoomView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private List<LocationComponent> locationComponents = new ArrayList<>();
    private List<StudentComponent> studentComponents = new ArrayList<>();

    public ClassRoomView(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (StudentComponent sc : studentComponents
        ) {
            boolean done = sc.onTouchEvent(event);
            if (done) return done;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (LocationComponent locationComponent : locationComponents
        ) {
            locationComponent.draw(canvas);
        }
        for (StudentComponent studentComponent : studentComponents
        ) {
            studentComponent.draw(canvas);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        if (!thread.isAlive()) {
            thread.start();
//        TODO: Get stuff from db
            List<String> titles = new ArrayList<>(Arrays.asList("In Class", "Absent",
                    "Bathroom/Drink", "Office", "With Miss Catherine", "Miss Emilie's"));
            for (int i = 0; i < titles.size(); i++) {
                locationComponents.add(new LocationComponent(titles.get(i), titles.size(), i));
            }
            List<String> names = new ArrayList<>(Arrays.asList("Bobby", "Billy",
                    "Jean Coutu", "Mia K.", "Mia M.", "James"));
            for (int i = 0; i < names.size(); i++) {
                StudentComponent studentComponent = new StudentComponent(this.getContext(), names.get(i));
                studentComponents.add(studentComponent);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }


    public void update() {
        for (LocationComponent locationComponent : locationComponents
        ) {
            locationComponent.update();
        }
        for (StudentComponent studentComponent : studentComponents
        ) {
            studentComponent.update();
        }
    }


}
