package patwa.aman.com.internalnotification;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    public RecyclerAdapter(Response.Listener<String> stringListener) {
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notificationlayout,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mobile,body;
        Button send,remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mobile = itemView.findViewById(R.id.mobile_no);
            body = itemView.findViewById(R.id.mess);
            send = itemView.findViewById(R.id.btn_send);
            remove = itemView.findViewById(R.id.btn_remove);

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
