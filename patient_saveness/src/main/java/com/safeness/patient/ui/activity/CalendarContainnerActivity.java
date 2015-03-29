package com.safeness.patient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.e_saveness_common.util.DateTimeUtil;
import com.safeness.patient.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by EISAVISA on 2015/1/26.
 */
public class CalendarContainnerActivity extends AppBaseActivity {

    private CaldroidFragment caldroidFragment;
    private  final int SET_DATE_RESULT = 12;

    private static final String TAG = "CalendarContainnerActivity";

    private final String SELECTED_MONTH ="SELECTED_MONTH";

    private final String SELECTED_YEAR ="SELECTED_YEAR";

    private final String SELECTED_DAY ="SELECTED_DAY";

    final SimpleDateFormat formatter = new SimpleDateFormat(DateTimeUtil.NORMAL_PATTERN);
    @Override
    protected int getLayoutId() {
        return R.layout.calendar_containner_activity;
    }


    private  Calendar saveCalendar = Calendar.getInstance();

    @Override
    protected void setupView() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
//		 caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
            saveCalendar.set(Calendar.YEAR, savedInstanceState.getInt(SELECTED_YEAR));
            saveCalendar.set(Calendar.MONTH,savedInstanceState.getInt(SELECTED_MONTH));
            saveCalendar.set(Calendar.DATE,savedInstanceState.getInt(SELECTED_DAY));
            setSelectDateBackground(saveCalendar.getTime());
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            caldroidFragment.setArguments(args);
        }
        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.fragment_container, caldroidFragment);

        t.commit();
        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
    }

    /**
     * 设置选中的日期的背景颜色
     * @param date
     */
    private void setSelectDateBackground(Date date){

        caldroidFragment.setBackgroundResourceForDate(R.color.blue,
                date);

        caldroidFragment.setTextColorForDate(R.color.white, date);

    }


    // Setup listener
    final CaldroidListener listener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {

            saveCalendar.setTime(date);
            Intent it = new Intent();
            it.putExtra("selectedDate",formatter.format(date));
            setSelectDateBackground(date);
            setResult(SET_DATE_RESULT,it);
            finish();
            overridePendingTransition(R.anim.in_from_up, R.anim.out_to_up);
            Toast.makeText(getApplicationContext(), formatter.format(date),
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onChangeMonth(int month, int year) {
            String text = "month: " + month + " year: " + year;
            Toast.makeText(getApplicationContext(), text,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLongClickDate(Date date, View view) {
            Toast.makeText(getApplicationContext(),
                    "Long click " + formatter.format(date),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCaldroidViewCreated() {

            if (caldroidFragment.getLeftArrowButton() != null) {
                Toast.makeText(getApplicationContext(),
                        "Caldroid view is created", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_up, R.anim.out_to_up);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void initializedData() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            outState.putInt(SELECTED_YEAR, saveCalendar.get(Calendar.YEAR));
            outState.putInt(SELECTED_MONTH,saveCalendar.get(Calendar.MONTH));
            outState.putInt(SELECTED_DAY,saveCalendar.get(Calendar.DATE));
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }


    }
}
