package com.zgb.student.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zgb.student.R;
import com.zgb.student.model.Student;
import java.util.List;

/**
 * Created by com.zgb on 2016/10/1.
 */
public class StudentAdapter extends ArrayAdapter<Student> {
    private int resourceId;

    public StudentAdapter(Context context, int resource, List<Student> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Student student = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.student_name = (TextView) view.findViewById(R.id.student_name);
            viewHolder.student_id = (TextView) view.findViewById(R.id.student_id);
            viewHolder.student_sex = (TextView) view.findViewById(R.id.student_sex);
            viewHolder.student_measureDate = (TextView) view.findViewById(R.id.student_measureDate);
            viewHolder.student_info = (TextView) view.findViewById(R.id.student_info);
//            viewHolder.student_image.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.student_name.setText(student.getName());
        viewHolder.student_id.setText(student.getId());
        viewHolder.student_sex.setText(student.getSex());
        viewHolder.student_measureDate.setText(student.getMeasureDate());
        viewHolder.student_info.setText(student.getInfo());

        return view;
    }

    class ViewHolder {
        TextView student_sex;
        TextView student_name;
        TextView student_id;
        TextView student_measureDate;
        TextView student_info;

    }

}
