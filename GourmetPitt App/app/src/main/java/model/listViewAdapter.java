package model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.ui.BusinessMyRestaurantActivity;
import com.example.user.gourmetpitt.ui.BusinessRestDetailActivity;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class listViewAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    private Context context;
//    public ImageLoader imageLoader;

    public listViewAdapter(Activity a, ArrayList<HashMap<String, String>> d, Context context) {
        activity = a;
        data=d;
        this.context=context;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
//        System.out.println("pos is"+position);
        if(convertView==null) {
//            System.out.println("null convert");
            vi = inflater.inflate(R.layout.list_row, null);
        }

        TextView title = (TextView)vi.findViewById(R.id.title); // title

        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image


        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        final String titleName=song.get(BusinessMyRestaurantActivity.KEY_TITLE);
        // Setting all values in listview
        title.setText(song.get(BusinessMyRestaurantActivity.KEY_TITLE));
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessRestDetailActivity.class);
                intent.putExtra("rest_name", titleName);
                intent.putExtra("position", position);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        artist.setText(song.get(BusinessMyRestaurantActivity.KEY_ARTIST));

        duration.setText(song.get(BusinessMyRestaurantActivity.KEY_DURATION));
        String url=song.get(BusinessMyRestaurantActivity.KEY_THUMB_URL);
        if (url!=null) {
            System.out.println("title name is "+titleName);
            String fileName="business/"+titleName;
            Drawable drawable = LoadImageFromWebOperations(url.toString(),fileName);
            thumb_image.setImageDrawable(drawable);
        }

//        thumb_image.setImageResource(Integer.valueOf(song.get(BusinessMyRestaurantActivity.KEY_THUMB_URL)));
        thumb_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessRestDetailActivity.class);
                intent.putExtra("rest_name", titleName);
                intent.putExtra("position", position);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        return vi;
    }

    private Drawable LoadImageFromWebOperations(String url,String restName)
    {
        try
        {

            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, restName);
            return d;

        }catch (Exception e) {
            Log.d("Excep", "Exc=" + e);
            return null;
        }
    }
}
