package rocks.poopjournal.todont.Adapters;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rocks.poopjournal.todont.Alarm;
import rocks.poopjournal.todont.Db_Controller;
import rocks.poopjournal.todont.Fragments.AvoidedLogFragment;
import rocks.poopjournal.todont.Helper;
import rocks.poopjournal.todont.NotificationReceiver;
import rocks.poopjournal.todont.R;
import rocks.poopjournal.todont.databinding.UpdateLayoutBottomSheetTestBinding;


public class AvoidedLogAdapter extends RecyclerView.Adapter<AvoidedLogAdapter.RecyclerViewHolder> {
    private ArrayList<String> donotTask = new ArrayList<>();
    private ArrayList<String> donotCatagory = new ArrayList<>();
    private final ArrayList<String> updateddonotCatagory = new ArrayList<>();
    AvoidedLogFragment ft;
    Context con;
    Date c = Calendar.getInstance().getTime();
    String[] catagories;
    String catagoryselected = "";
    Db_Controller db;
    final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    String formattedDate = df.format(c);

    public AvoidedLogAdapter(Context con, AvoidedLogFragment ft, Db_Controller db, ArrayList<String> donotTask, ArrayList<String> donotCatagory) {
        this.donotTask = donotTask;
        this.donotCatagory = donotCatagory;
        this.con = con;
        this.ft = ft;
        this.db = db;

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recyclerview_layout, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        db.show_avoided_data();
        String dTask = donotTask.get(position);
        String date = Helper.avoidedData.get(position)[1];
        holder.task.setText(dTask);
        holder.dateoftask.setText(date);
        holder.times.setText(dTask);
        holder.task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(con, R.style.BottomSheetDialogTheme);
                final UpdateLayoutBottomSheetTestBinding bottomsheetview = UpdateLayoutBottomSheetTestBinding.inflate(LayoutInflater.from(con));

//                if (Helper.labels_array.size() == 0) {
//                    txt.setVisibility(View.VISIBLE);
//                } else {
//                    txt.setVisibility(View.INVISIBLE);
//                }

                bottomsheetview.titleText.setText(dTask);
                bottomsheetview.details.setText(Helper.habitsdata.get(position)[3]);
                bottomSheetDialog.setContentView(bottomsheetview.getRoot());
                bottomSheetDialog.show();
                db.show_avoided_data();
                db.show_done_data();
                if (Helper.avoidedData.size() == 0) {
                    bottomsheetview.avoidedCount.setText("0");
                } else {
                    bottomsheetview.avoidedCount.setText(getAvoidedCount(Helper.habitsdata.get(position)[2]));
                }
                if (Helper.donedata.size() == 0) {
                    bottomsheetview.doneCount.setText("0");
                } else {
                    bottomsheetview.doneCount.setText(getDoneCount(Helper.habitsdata.get(position)[2]));
                }
                bottomsheetview.avoidedPlusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.show_avoided_data();
                        db.show_habits_data();
                        Log.d("asize", "" + Helper.avoidedData.size());
                        int count = 0;
                        int countAll = 0;
                        int ids = Integer.parseInt(Helper.habitsdata.get(position)[0]);
                        if (Helper.avoidedData.size() != 0) {
                            for (int i = 0; i < Helper.avoidedData.size(); i++) {
                                String getdate = Helper.avoidedData.get(i)[1];
                                String gethabit = Helper.avoidedData.get(i)[2];
                       /*     if ((Helper.habitsdata.get(position)[2].equals(gethabit)) && (getdate.equals(df.format(c)))) {
//                                Toast.makeText(con, "    Already Added    ", Toast.LENGTH_SHORT).show();
                                count++;
                            }

*/
                                if ((Helper.habitsdata.get(position)[2].equals(gethabit)) && (getdate.equals(df.format(c)))) {
//                                Toast.makeText(con, "    Already Added    ", Toast.LENGTH_SHORT).show();
                                    count++;
                                    countAll = Integer.parseInt(Helper.avoidedData.get(i)[4]);
                                    Log.i("tariq", "onClick: " + Helper.avoidedData.get(i)[4]);
                                }
                            }
                            if (count == 0) {
                                int res = count + countAll;
                                db.insert_data(ids, df.format(c), Helper.habitsdata.get(position)[2], Helper.habitsdata.get(position)[3], res, catagoryselected);
                                Toast.makeText(con, "    Added To Avoided", Toast.LENGTH_SHORT).show();
                            } else {
                                int res = count + countAll;
                                db.update_data(ids, df.format(c), Helper.habitsdata.get(position)[2], Helper.habitsdata.get(position)[3], res, catagoryselected);
                                Toast.makeText(con, "    Added To Avoided", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            int res = 1;
                            db.insert_data(ids, df.format(c), Helper.habitsdata.get(position)[2], Helper.habitsdata.get(position)[3], res, catagoryselected);
                            Toast.makeText(con, "    Added To Avoided out", Toast.LENGTH_SHORT).show();
                        }

                        db.show_avoided_data();
                        db.show_done_data();
                        if (Helper.avoidedData.size() == 0) {
                            bottomsheetview.avoidedCount.setText("0");
                        } else {
                            bottomsheetview.avoidedCount.setText(getAvoidedCount(Helper.habitsdata.get(position)[2]));
                        }
                        //holder.catagoryoftask.setText(String.valueOf(getCount(position)));
                    }
                });

                bottomsheetview.avoidedMinusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        db.show_avoided_data();
                        db.show_avoided_data();
                        Log.d("asize", "" + Helper.avoidedData.size());
                        int count = 0;
                        int countAll = 0;
                        int ids = Integer.parseInt(Helper.habitsdata.get(position)[0]);
                        if (Helper.avoidedData.size() != 0) {
                            for (int i = 0; i < Helper.avoidedData.size(); i++) {
                                String getdate = Helper.avoidedData.get(i)[1];
                                String gethabit = Helper.avoidedData.get(i)[2];
                       /*     if ((Helper.habitsdata.get(position)[2].equals(gethabit)) && (getdate.equals(df.format(c)))) {
//                                Toast.makeText(con, "    Already Added    ", Toast.LENGTH_SHORT).show();
                                count++;
                            }

*/
                                if ((Helper.habitsdata.get(position)[2].equals(gethabit)) && (getdate.equals(df.format(c)))) {
//                                Toast.makeText(con, "    Already Added    ", Toast.LENGTH_SHORT).show();
                                    count--;
                                    countAll = Integer.parseInt(Helper.avoidedData.get(i)[4]);
                                    Log.i("tariq", "onClick: " + Helper.avoidedData.get(i)[4]);
                                }
                            }
                            if ((count + countAll) >= 0 && count == 0) {
                                int res = count + countAll;
                                db.insert_data(ids, df.format(c), Helper.habitsdata.get(position)[2], Helper.habitsdata.get(position)[3], res, catagoryselected);
                                Toast.makeText(con, "    Added To Avoided", Toast.LENGTH_SHORT).show();
                            } else if ((count + countAll) >= 0) {
                                int res = count + countAll;
                                db.update_data(ids, df.format(c), Helper.habitsdata.get(position)[2], Helper.habitsdata.get(position)[3], res, catagoryselected);
                                Toast.makeText(con, "    Added To Avoided", Toast.LENGTH_SHORT).show();
                            }
                        }

                        db.show_avoided_data();
                        db.show_done_data();
                        if (Helper.avoidedData.size() == 0) {
                            bottomsheetview.avoidedCount.setText("0");
                        } else {
                            bottomsheetview.avoidedCount.setText(getAvoidedCount(Helper.habitsdata.get(position)[2]));
                        }
                        //holder.catagoryoftask.setText(String.valueOf(getCount(position)));
                    }
                });
                bottomsheetview.donePlusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.show_done_data();
                        db.show_avoided_data();
                        int count = 0;
                        int countAll = 0;
                        int ids = Integer.parseInt(Helper.habitsdata.get(position)[0]);
                        if (Helper.donedata.size() != 0) {
                            for (int i = 0; i < Helper.donedata.size(); i++) {
                                String getdate = Helper.donedata.get(i)[1];
                                String gethabit = Helper.donedata.get(i)[2];
                                if ((Helper.habitsdata.get(position)[2].equals(gethabit)) && (getdate.equals(df.format(c)))) {
//                                Toast.makeText(con, "    Already Added    ", Toast.LENGTH_SHORT).show();
                                    count++;
                                    countAll = Integer.parseInt(Helper.donedata.get(i)[4]);
                                }
                            }
                            if (count == 0) {
                                int res = count + countAll;
                                db.insert_done_data(ids, df.format(c), Helper.habitsdata.get(position)[2], Helper.habitsdata.get(position)[3], res, catagoryselected);
                                Toast.makeText(con, "Added To Done", Toast.LENGTH_SHORT).show();
                            } else {
                                int res = count + countAll;
                                db.update_done_data(ids, df.format(c), Helper.habitsdata.get(position)[2], Helper.habitsdata.get(position)[3], res, catagoryselected);
                                Toast.makeText(con, "Added To Done ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            int res = 1;
                            db.insert_done_data(ids, df.format(c), Helper.habitsdata.get(position)[2], Helper.habitsdata.get(position)[3], res, catagoryselected);
                            Toast.makeText(con, "Added To Done out", Toast.LENGTH_SHORT).show();
                        }

                        db.show_avoided_data();
                        db.show_done_data();
                        if (Helper.donedata.size() == 0) {
                            bottomsheetview.doneCount.setText("0");
                        } else {
                            bottomsheetview.doneCount.setText(getDoneCount(Helper.habitsdata.get(position)[2]));
                        }
                        // holder.catagoryoftask.setText(String.valueOf(getCount(position)));

                    }

                });
                bottomsheetview.doneMinusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.show_done_data();
                        db.show_avoided_data();
                        int count = 0;
                        int countAll = 0;
                        int ids = Integer.parseInt(Helper.habitsdata.get(position)[0]);
                        if (Helper.donedata.size() != 0) {
                            for (int i = 0; i < Helper.donedata.size(); i++) {
                                String getdate = Helper.donedata.get(i)[1];
                                String gethabit = Helper.donedata.get(i)[2];
                                if ((Helper.habitsdata.get(position)[2].equals(gethabit)) && (getdate.equals(df.format(c)))) {
//                                Toast.makeText(con, "    Already Added    ", Toast.LENGTH_SHORT).show();
                                    count--;
                                    countAll = Integer.parseInt(Helper.donedata.get(i)[4]);
                                }
                            }
                            if ((count + countAll) >= 0) {
                                if (count == 0) {
                                    int res = count + countAll;
                                    db.insert_done_data(ids, df.format(c), Helper.habitsdata.get(position)[2], Helper.habitsdata.get(position)[3], res, catagoryselected);
                                    Toast.makeText(con, "Added To Done", Toast.LENGTH_SHORT).show();
                                } else {
                                    int res = count + countAll;
                                    db.update_done_data(ids, df.format(c), Helper.habitsdata.get(position)[2], Helper.habitsdata.get(position)[3], res, catagoryselected);
                                    Toast.makeText(con, "Added To Done ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        db.show_avoided_data();
                        db.show_done_data();
                        if (Helper.donedata.size() == 0) {
                            bottomsheetview.doneCount.setText("0");
                        } else {
                            bottomsheetview.doneCount.setText(getDoneCount(Helper.habitsdata.get(position)[2]));
                        }
                        // holder.catagoryoftask.setText(String.valueOf(getCount(position)));
                    }
                });
                bottomsheetview.tvNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // check if the user has the permission to post notificaiton
                        // else ask for the permission
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            NotificationManager notificationManager = ( NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
                            if (!notificationManager.areNotificationsEnabled()) {
                                // Request notification permission
                                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                        .putExtra(Settings.EXTRA_APP_PACKAGE, con.getPackageName());
                                con.startActivity(intent);
                            }else{
                                showDialog();
                            }
                        }else{
                            showDialog();
                        }
                    }

                    private void showDialog(){
                        // Get current time
                        final Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        // Create and show TimePickerDialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                view.getContext(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                        // Update the calendar with the selected time
                                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        calendar.set(Calendar.MINUTE, minute);

                                        // After selecting time, show dialog for repetition frequency
                                        showFrequencyDialog(calendar, hourOfDay, minute);
                                    }
                                },
                                hour,
                                minute,
                                true // Use 24-hour format
                        );
                        timePickerDialog.show();
                    }

                    private void showFrequencyDialog(Calendar calendar, int hourOfDay, int minute) {
                        // Options for repetition frequency
                        final String[] frequencies = {"Once", "Daily", "Weekly"};

                        // Show an AlertDialog to select frequency
                        AlertDialog.Builder builder = new AlertDialog.Builder(con);
                        builder.setTitle("Select Repetition Frequency");
                        builder.setItems(frequencies, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String frequency = frequencies[which];

                                // Update tvNotification with selected time and frequency
                                String notificationText = String.format("Time: %02d:%02d, Frequency: %s", hourOfDay, minute, frequency);
                                bottomsheetview.tvNotification.setText(notificationText);

                                // Schedule notification based on selected time and frequency
                                scheduleNotification(Integer.parseInt(Helper.habitsdata.get(position)[0]), calendar, frequency);
                            }
                        });
                        builder.show();
                    }

                    private void scheduleNotification(int taskId, Calendar calendar, String frequency) {
                        Context context = con;
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                        Intent intent = new Intent(context, NotificationReceiver.class);
                        intent.putExtra("task_id", taskId); // Pass task ID in intent
                        intent.putExtra("task",Helper.habitsdata.get(position)[2]);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, taskId, intent, PendingIntent.FLAG_IMMUTABLE);

                        long triggerTime = calendar.getTimeInMillis();
                        long interval = 0;

                        // Determine repetition interval based on frequency
                        if (frequency.equals("Daily")) {
                            interval = AlarmManager.INTERVAL_DAY;
                        } else if (frequency.equals("Weekly")) {
                            interval = AlarmManager.INTERVAL_DAY * 7;
                        }

                        // Store alarm details in database
                        db.insertOrUpdateAlarm(new Alarm(taskId, triggerTime, frequency));

                        // Set the alarm
                        if (interval > 0) {
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, interval, pendingIntent);
                        } else {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent); // For "Once" frequency
                        }

                        Toast.makeText(context, "Notification scheduled: " + frequency, Toast.LENGTH_SHORT).show();
                    }

                    private void deleteAlarm(int taskId) {
                        Context context = con;
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                        // Cancel alarm with taskId
                        Intent intent = new Intent(context, NotificationReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);

                        // Remove alarm from the database
                        db.deleteAlarm(taskId);

                        Toast.makeText(context, "Notification cancelled", Toast.LENGTH_SHORT).show();
                    }

                    private void updateAlarm(int taskId, Calendar newCalendar, String newFrequency) {
                        // First, cancel the existing alarm
                        deleteAlarm(taskId);

                        // Then, set a new alarm with updated time and frequency
                        scheduleNotification(taskId, newCalendar, newFrequency);
                    }
                });

            }
        });

    }

    private int getCount(int position) {
        int count = Integer.parseInt(getAvoidedCount(Helper.habitsdata.get(position)[2])) - Integer.parseInt(getDoneCount(Helper.habitsdata.get(position)[2]));
        db.update_habitsdata(Integer.parseInt(Helper.habitsdata.get(position)[0]),
                Helper.habitsdata.get(position)[1],
                Helper.habitsdata.get(position)[2],
                Helper.habitsdata.get(position)[3],
                count,
                Helper.habitsdata.get(position)[5]);
        return count;
    }

    private String getDoneCount(String s) {
        for (String x[] : Helper.donedata) {
            if (x[2].equalsIgnoreCase(s)) {
                return x[4];
            }
        }
        return "0";
    }

    private String getAvoidedCount(String s) {
        for (String x[] : Helper.avoidedData) {
            if (x[2].equalsIgnoreCase(s)) {
                return x[4];
            }
        }
        return "0";

    }

    @Override
    public int getItemCount() {
        return donotTask.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        Button rbtn;
        TextView task,times;
        TextView dateoftask;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            rbtn = itemView.findViewById(R.id.addToAvoided);
            task = itemView.findViewById(R.id.task);
            times = itemView.findViewById(R.id.times);
            dateoftask = itemView.findViewById(R.id.dateoftask);
            dateoftask.setVisibility(View.INVISIBLE);
            rbtn.setBackgroundResource(R.drawable.ic_avoided);

        }

    }
}
