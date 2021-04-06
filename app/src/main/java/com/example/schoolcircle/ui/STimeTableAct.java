package com.example.schoolcircle.ui;

import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;

public class STimeTableAct extends BaseActivity {


    //星期几
    private RelativeLayout day;
    int currentCoursesNumber = 0;
    int maxCoursesNumber = 0;

    @Override
    public int intiLayout() {
        return R.layout.act_timetable;
    }

    @Override
    public void initView() {
        //工具条
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }

    @Override
    public void initData() {

    }


//    //创建单个课程视图
//    private void createItemCourseView(final Course course) {
//        int getDay = course.getDay();
//        if ((getDay < 1 || getDay > 7) || course.getStart() > course.getEnd())
//            Toast.makeText(this, "星期几没写对,或课程结束时间比开始时间还早~~", Toast.LENGTH_LONG).show();
//        else {
//            int dayId = 0;
//            switch (getDay) {
//                case 1: dayId = R.id.monday; break;
//                case 2: dayId = R.id.tuesday; break;
//                case 3: dayId = R.id.wednesday; break;
//                case 4: dayId = R.id.thursday; break;
//                case 5: dayId = R.id.friday; break;
//                case 6: dayId = R.id.saturday; break;
//                case 7: dayId = R.id.weekday; break;
//            }
//            day = findViewById(dayId);
//
//            int height = 180;
//            final View v = LayoutInflater.from(this).inflate(R.layout.course_card, null); //加载单个课程布局
//            v.setY(height * (course.getStart()-1)); //设置开始高度,即第几节课开始
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
//                    (ViewGroup.LayoutParams.MATCH_PARENT,(course.getEnd()-course.getStart()+1)*height - 8); //设置布局高度,即跨多少节课
//            v.setLayoutParams(params);
//            TextView text = v.findViewById(R.id.text_view);
//            text.setText(course.getCourseName() + "\n" + course.getTeacher() + "\n" + course.getClassRoom()); //显示课程名
//            day.addView(v);
//            //长按删除课程
//            v.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    v.setVisibility(View.GONE);//先隐藏
//                    day.removeView(v);//再移除课程视图
//                    SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
//                    sqLiteDatabase.execSQL("delete from courses where course_name = ?", new String[] {course.getCourseName()});
//                    return true;
//                }
//            });
//        }
//    }


}
