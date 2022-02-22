package org.servalproject.ui.scan;

import android.content.Context;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.servalproject.R;
import org.servalproject.ui.scan.model.Device;

import java.util.List;

/**
 * Created by dan on 10/22/14.
 */
public class NetDeviceAdapter extends RecyclerView.Adapter<NetDeviceAdapter.ViewHolder> {
    private List<Device> addresses;
    private final int rowLayout;

    private Context mContext;

    public NetDeviceAdapter(List<Device> addresses, int rowLayout, Context mContext) {
        this.addresses = addresses;
        this.rowLayout = rowLayout;
        this.mContext = mContext;
    }

    @Override
    public NetDeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NetDeviceAdapter.ViewHolder viewHolder, int i) {
        Device address = addresses.get(i);
        viewHolder.deviceName.setText(address.getDeviceName());
        viewHolder.deviceIp.setText("https://" + address.getIpAddress());
        viewHolder.macAdd.setText(address.getMacAddress());
    }

    @Override
    public int getItemCount() {
        return addresses == null ? 0 : addresses.size();
    }

    /**
     *
     * @return
     */
    public List<Device> getAddresses() {
        return addresses;
    }

    /**
     *
     * @param addresses
     */
    public void setAddresses(List<Device> addresses) {
        this.addresses = addresses;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView deviceName;
        public TextView deviceIp;
        public TextView macAdd;


        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = (TextView) itemView.findViewById(R.id.deviceName);
            deviceIp = (TextView) itemView.findViewById(R.id.deviceIp);
            macAdd = (TextView)itemView.findViewById(R.id.macAdd);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "position = " + getLayoutPosition(), Toast.LENGTH_SHORT).show();

            //go through each item if you have few items within recycler view
            /*if (getLayoutPosition() == 0) {


            } else if (getLayoutPosition() == 1) {
                //Do whatever you want here

            } else if (getLayoutPosition() == 2) {

            } else if (getLayoutPosition() == 3) {

            } else if (getLayoutPosition() == 4) {

            } else if (getLayoutPosition() == 5) {

            }*/
        }
    }
}
