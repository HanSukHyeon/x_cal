package com.example.cal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    /**
     * 연/월 텍스트뷰
     */
    private TextView tvDate;
    /**
     * 그리드뷰 어댑터
     */
    private GridAdapter gridAdapter;

    /**
     * 일 저장 할 리스트
     */
    private ArrayList<String> dayList;

    /**
     * 그리드뷰
     */
    private GridView gridView;

    /**
     * 캘린더 변수
     */
    private Calendar mCal;
    private Button btn_move;

    //note list view 선언
   private ListView noteListView;

    //연,월,일을 따로 저장
    final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
    final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
    final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
    //앞뒤 버튼
    Button btn_next,btn_back;
    //
    int point_2_index,next_level=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initWidgets();
       // loadFromDBToMemory();
        setNoteAdapter();
        setOnClickListener();

        tvDate = (TextView) findViewById(R.id.tv_date);
        gridView = (GridView) findViewById(R.id.gridview);
        btn_move = findViewById(R.id.btn_move);
        btn_next=(Button)findViewById(R.id.btn_next);
        btn_back=(Button)findViewById(R.id.btn_back);
        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity. this, SubActivity.class);
                startActivity(intent);
            }
        });



        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);


        //현재 날짜 텍스트뷰에 뿌려줌
        tvDate.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));

        //gridview 요일 표시
        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        mCal = Calendar.getInstance();

        //날짜 배치
        dayList=setCalendar_day(dayList,mCal,date);

        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);

        btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                next_level++;
                dayList.clear();
                // year을 올려주기위한 if문
                if((Integer.parseInt(curMonthFormat.format(date))+next_level)%12==0)
                {
                    int year_num=(Integer.parseInt(curMonthFormat.format(date))+next_level)/12;
                    tvDate.setText((Integer.parseInt(curYearFormat.format(date))+year_num) + "/" + 12);
                }
                else if((Integer.parseInt(curMonthFormat.format(date))+next_level)>12){
                    int year_num=(Integer.parseInt(curMonthFormat.format(date))+next_level)/12;
                    tvDate.setText((Integer.parseInt(curYearFormat.format(date))+year_num) + "/" + (Integer.parseInt(curMonthFormat.format(date))+next_level)%12);
                }
                else
                {
                    tvDate.setText(curYearFormat.format(date) + "/" + (Integer.parseInt(curMonthFormat.format(date))+next_level));
                }

                dayList= setCalendar_yo(dayList);


                //날짜 배치
                dayList=setCalendar_day_i(dayList,mCal,date,next_level);
                setCalendarDate(mCal.get(Calendar.MONTH) + 1);


                gridAdapter = new GridAdapter(getApplicationContext(), dayList);
                gridView.setAdapter(gridAdapter);

            }
        });
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                next_level--;
                dayList.clear();
                // year을 올려주기위한 if문
                if((Integer.parseInt(curMonthFormat.format(date))+next_level)%12==0)
                {
                    int year_num=(Integer.parseInt(curMonthFormat.format(date))+next_level)/12;
                    tvDate.setText((Integer.parseInt(curYearFormat.format(date))+year_num) + "/" + 12);
                }
                else if((Integer.parseInt(curMonthFormat.format(date))+next_level)>12){
                    int year_num=(Integer.parseInt(curMonthFormat.format(date))+next_level)/12;
                    tvDate.setText((Integer.parseInt(curYearFormat.format(date))+year_num) + "/" + (Integer.parseInt(curMonthFormat.format(date))+next_level)%12);
                }
                else
                {
                    tvDate.setText(curYearFormat.format(date) + "/" + (Integer.parseInt(curMonthFormat.format(date))+next_level));
                }

                dayList= setCalendar_yo(dayList);


                //날짜 배치
                dayList=setCalendar_day_i(dayList,mCal,date,next_level);
                setCalendarDate(mCal.get(Calendar.MONTH) + 1);


                gridAdapter = new GridAdapter(getApplicationContext(), dayList);
                gridView.setAdapter(gridAdapter);

            }
        });

    }



    private void loadFromDBToMemory()
    {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOFDatabase(this);
        sqLiteManager.populateNoteListArray();
    }


    //note list view와 note adapter 설정
    private void initWidgets()
    {
        noteListView = findViewById(R.id.noteListView);
    }

    private void setNoteAdapter()
    {
        NoteAdapter noteAdapter = new NoteAdapter(getApplicationContext(),Note.noteArrayList);
        noteListView.setAdapter((noteAdapter));
    }

    private void setOnClickListener()
    {
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                Note selectedNote = (Note) noteListView.getItemAtPosition(position);
                Intent editNoteIntent = new Intent(getApplicationContext(), SubActivity.class);
                editNoteIntent.putExtra(Note.NOTE_EDIT_EXTRA, selectedNote.getId());
                startActivity(editNoteIntent);
            }
        });
    }


    public void newNote(View view)
    {
        Intent newNoteIntent = new Intent(this, SubActivity.class);
        startActivity(newNoteIntent);
    }

    @Override
    protected  void onResume()
    {
        super .onResume();
        setNoteAdapter();
    }
    /**
     * 해당 월에 표시할 일 수 구함
     *
     * @param month
     */
    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month - 1);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }

    }

    /**
     * 그리드뷰 어댑터
     *
     */
    private class GridAdapter extends BaseAdapter {

        private final List<String> list;

        private final LayoutInflater inflater;

        /**
         * 생성자
         *
         * @param context
         * @param list
         */
        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.tv_item_gridview);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));

            //해당 날짜 텍스트 컬러,배경 변경
            mCal = Calendar.getInstance();
            //오늘 day 가져옴
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            Integer today_month = mCal.get(Calendar.MONTH);
            String sToday = String.valueOf(today);
            if (sToday.equals(getItem(position))&&next_level==0) { //오늘 day 텍스트 컬러 변경
                holder.tvItemGridView.setTextColor(getResources().getColor(R.color.purple_200));
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvItemGridView;
    }

    //gridview 요일 표시
    public ArrayList<String> setCalendar_yo(ArrayList<String> dayList){


        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        return dayList;
    }

    public ArrayList<String> setCalendar_day(ArrayList<String> dayList,Calendar mCal,Date date) {

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date))-1 , 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }

        return dayList;
    }

    public ArrayList<String> setCalendar_day_i(ArrayList<String> dayList,Calendar mCal,Date date,int j) {

        //1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date))+j-1 , 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }

        return dayList;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //더블클릭 종료 설정
    private  long backBtnTime = 0;

    @Override
    public void onBackPressed(){
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        }
        else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }


}







