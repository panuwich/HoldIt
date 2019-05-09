package project.senior.holdit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import project.senior.holdit.R;
import project.senior.holdit.model.Event;

public class EventListAdapter  extends BaseAdapter{
    Context mContext;
    private int mLayoutResId;
    private ArrayList<Event> resultList;
    public EventListAdapter(Context context, int mLayoutResId, ArrayList<Event> resultList){
        this.mContext = context;
        this.mLayoutResId = mLayoutResId;
        this.resultList = resultList;
    }
    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemLayout = inflater.inflate(mLayoutResId, null);
        ImageView imageViewCover = (ImageView)itemLayout.findViewById(R.id.imageView_event_image);
        CircleImageView imageViewLogo = (CircleImageView)itemLayout.findViewById(R.id.image_event_logo);
        TextView textLocation = (TextView)itemLayout.findViewById(R.id.textView_event_location);
        TextView textDate = (TextView)itemLayout.findViewById(R.id.textView_event_start_date);
        TextView textTitle = (TextView)itemLayout.findViewById(R.id.textView_event_title);
        TextView textDesc = (TextView)itemLayout.findViewById(R.id.textView_event_desc);

        String urlCover = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/event/"+resultList.get(i).getEventImgCover();
        String urlLogo = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/event/"+resultList.get(i).getEventImgLogo();
        Picasso.get().load(urlCover).into(imageViewCover);
        Picasso.get().load(urlLogo).into(imageViewLogo);

        String dateEnd = resultList.get(i).getDateEnd();
        String getOnlyDateEnd = dateEnd.substring(8);
        String getOnlyMonth = getMonth(dateEnd.substring(5,7));
        String getOnlyYear = ""+(Integer.parseInt(dateEnd.substring(0,4))+543);

        String date = "วันนี้ - " + getOnlyDateEnd + " " + getOnlyMonth + " " +getOnlyYear;
        textLocation.setText(resultList.get(i).getLocation());
        textDate.setText(date);
        textTitle.setText(resultList.get(i).getTitle());
        textDesc.setText(resultList.get(i).getDesc());
        return itemLayout;
    }
    String getMonth(String month){
        switch (month){
            case "01":
                return "มกราคม";
            case "02":
                return "กุมภาพันธ์";
            case "03":
                return "มีนาคม";
            case "04":
                return "เมษายม";
            case "05":
                return "พฤษภาคม";
            case "06":
                return "มิถุนายม";
            case "07":
                return "กรกฎาคม";
            case "08":
                return "สิงหาคม";
            case "09":
                return "กันยายน";
            case "10":
                return "ตุลาคม";
            case "11":
                return "พฤศจิกายน";
            case "12":
                return "ธันวาคม";
            default: break;
        }
        return "";
    }
}
